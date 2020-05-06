package com.thingtek.socket;

import com.thingtek.beanServiceDao.data.entity.DisDataBean;
import com.thingtek.beanServiceDao.data.service.DisDataService;
import com.thingtek.beanServiceDao.point.entity.PointBean;
import com.thingtek.beanServiceDao.point.service.PointService;
import com.thingtek.beanServiceDao.unit.entity.DisUnitBean;
import com.thingtek.beanServiceDao.unit.service.UnitService;
import com.thingtek.beanServiceDao.warn.entity.WarnBean;
import com.thingtek.beanServiceDao.warn.service.WarnService;
import com.thingtek.view.logo.LogoInfo;
import com.thingtek.view.shell.dataCollect.DataCollectPanel;
import com.thingtek.view.shell.dataCollect.base.BaseCollectPanel;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class DataBuffer {
    private int clttype = 4;
    private Map<Short, Map<Integer, RawData[]>> buffer;//单元编号 , 大包编号  , 数据
    private List<RawData[]> wholebuffer;
    // 数据处理线程
    private Thread dataThread;

    private Lock lock;
    private Condition con;
    @Resource
    private DisDataService dataService;
    @Resource
    private UnitService unitService;
    @Resource
    private WarnService warnService;
    @Resource
    private PointService pointService;

    private Map<String, List<DisDataBean>> warnmap;

    public DataBuffer() {
        buffer = new HashMap<>();
        wholebuffer = new ArrayList<>();
        warnmap = new HashMap<>();
        lock = new ReentrantLock();
        con = lock.newCondition();
        start();
    }

    /**
     * 将有效长度为length的数据添加到数据缓冲区
     *
     * @param //数据
     */
    public boolean receDatas(RawData rawData) {
        synchronized (wholebuffer) {
            short unitnum = rawData.getData().getUnit_num();
            int bigseq = rawData.getBigseq();
            int smallseq = rawData.getSmallseq();
            int totalsmallseq = rawData.getTotalsmallseq();
            if (buffer.containsKey(unitnum)) {//找到单元
                Map<Integer, RawData[]> onebuf = buffer.get(unitnum);
                if (onebuf.containsKey(bigseq)) {//找到大包
                    RawData[] raws = onebuf.get(bigseq);
                    if (smallseq == 1 || raws[smallseq - 2] != null) {
                        raws[smallseq - 1] = rawData;
                        if (smallseq == raws.length) {//拼包成功移除缓存
                            wholebuffer.add(raws);
                            onebuf.remove(bigseq);
                        }
                    } else {//大包找到 小包未连续
                        System.out.println("单元:" + unitnum + ",smallseq:" + smallseq + ",remove" + new Date(System.currentTimeMillis()));
                        onebuf.remove(bigseq);
                        if (dataThread.getState() == Thread.State.WAITING) {
                            lock.lock();
                            con.signal();
                            lock.unlock();
                        }
                        return false;
                    }
                } else {//未找到大包
                    if (smallseq != 1) {//在未找到大包的情况下 小包的序列不是1 返回false
                        return false;
                    }
                    RawData[] raws = new RawData[totalsmallseq];
                    raws[smallseq - 1] = rawData;
                    onebuf.put(bigseq, raws);
                }
            } else {//未找到单元
                if (smallseq != 1) {//在未找到单元的情况下 小包的序列不是1 返回false
                    return false;
                }
                Map<Integer, RawData[]> onebuf = new HashMap<>();
                RawData[] raws = new RawData[totalsmallseq];
                raws[smallseq - 1] = rawData;
                onebuf.put(bigseq, raws);
                buffer.put(unitnum, onebuf);
            }
        }
        // 如果数据处理线程waiting中
        if (dataThread.getState() == Thread.State.WAITING) {
            lock.lock();
            con.signal();
            lock.unlock();
        }
        return true;
    }

    /**
     * 数据处理 ：另起一个线程对数据缓冲区的数据进行处理
     */
    private void start() {
        if (dataThread != null) {
            if (dataThread.isAlive()) {
                return;
            }
        }
        dataThread = new Thread(new DataRunnable());
        dataThread.start();
    }

    private boolean alive = true;

    class DataRunnable implements Runnable {
        @Override
        public void run() {// 如果flag标志为true，则继续循环
            lock.lock();
            try {
                while (alive) {
                    // 判断缓冲区数据是否达到最小数据长度
                    if (wholebuffer.size() == 0) {
                        Thread.sleep(50);// 等待50毫秒
                        // 判断缓冲区数据是否达到最小数据长度,如果没有，则保存数据
                        if (wholebuffer.size() == 0) {
                            Thread.sleep(50);// 等待50毫秒
                            // 判断缓冲区数据是否达到最小数据长度,如果没有则线程休眠
                            if (wholebuffer.size() == 0) {
//                                factory.saveData();// 数据存储
                                con.await();//很关键
                            }
                            continue;
                        }
                    }

                    try {
                        // 单条数据内容长度
                        RawData[] datas = wholebuffer.remove(0);
                        StringBuilder stringBuilder = new StringBuilder();
                        for (RawData data1 : datas) {
                            DisDataBean data = data1.getData();
                            for (int anInt : data.getData()) {
                                stringBuilder.append(Character.toChars(anInt));
                            }
                        }
                        RawData rawData = datas[datas.length - 1];
                        DisDataBean data = rawData.getData();
                        data.setDatastring(stringBuilder.toString());
                        resolveWarning(data);
                        int serverIndex = getServerIndex(data.getData());
                        data.setServerindex(serverIndex);
                        int minindex = data.getData().length - data.getGatewayfrontindex() - serverIndex;
                        data.setMinindex(minindex);
                        dataService.saveData(data);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                // 数据线程出错
            } finally {
                lock.unlock();
            }
        }
    }

    private void resolveWarning(DisDataBean data) {
        String phase = addData(data);
        if (phase == null) {
            return;
        }
        List<DisDataBean> datas = sort(phase);
        for (int i = 0; i < datas.size() - 1; i++) {
            DisDataBean data1 = datas.get(i);
            DisDataBean data2 = datas.get(i + 1);
            Calendar c1 = Calendar.getInstance();
            c1.setTime(data1.getInserttime());
            c1.add(Calendar.SECOND, 10);
            if (c1.getTime().before(data2.getInserttime())) {
                datas.remove(data1);
                i--;
                continue;
            }
            if (Objects.equals(data1.getUnit_num(), data2.getUnit_num())) {
                continue;
            }
            long sj1 = data1.getSj();
            long sj2 = data2.getSj();
            long mat = sj2 - sj1;
            if (mat < 0) {
                mat *= -1;
            }
            int place1 = data1.getUnit().getPoint().getPlace_value();
            int place2 = data2.getUnit().getPoint().getPlace_value();
            int juli = place1 - place2;
            if (juli < 0) {
                juli *= -1;
            }
            if (juli == 0) {
                continue;
            }
            double msec = mat / 1000.0;
            System.out.print("距离:" + juli + ",时间差:" + msec + "毫秒\n");
            //  这个5 是基数 每毫秒5米
            if (msec * 5 <= juli) {//两者之间 位置 = 距离 - 时间差 * 基数 最终位置 = 先出发位置起 + 位置;
                //最终位置
                double weizhi = (juli - msec * 5) / 2;
                String str = "异常位置:" + (sj1 < sj2 ?
                        data1.getUnit().getPoint().getPoint_name() :
                        data2.getUnit().getPoint().getPoint_name())
                        + "到"
                        + (sj1 >= sj2 ?
                        data1.getUnit().getPoint().getPoint_name() :
                        data2.getUnit().getPoint().getPoint_name()) + "( "
                        + weizhi + " )米附近,相位:" + phase
                        + ",时间差:" + msec;
                System.out.println(str);
                WarnBean warnBean = new WarnBean();
                warnBean.setClt_type(clttype);
                warnBean.setPhase(phase);
                warnBean.setInserttime(data.getInserttime());
                warnBean.setWarn_info(str);
                warnService.save(warnBean);
                addWarn(warnBean);
                datas.remove(data1);
                datas.remove(data2);
                i -= 2;
            } else {//先触发的另一边
                datas.remove(data2);
                i--;
                return;
            }
        }
    }

    private String addData(DisDataBean data) {
        DisUnitBean unit = (DisUnitBean) unitService.getUnitByNumber(clttype, data.getUnit_num());
        if (unit == null) {
            return null;
        }
        data.setUnit(unit);
        PointBean pointBean = pointService.getPointByNum(clttype, unit.getPoint_num());
        if (pointBean == null) {
            return null;
        }
        unit.setPoint(pointBean);
        List<DisDataBean> list;
        if (warnmap.containsKey(unit.getPhase())) {
            list = warnmap.get(unit.getPhase());
        } else {
            list = new ArrayList<>();
        }
        list.add(data);
        warnmap.put(unit.getPhase(), list);
        addDataWarn(data);
        return unit.getPhase();
    }

    private List<DisDataBean> sort(String phase) {
        List<DisDataBean> datas = warnmap.get(phase);
        return datas;
    }

    @Resource
    private LogoInfo logoInfo;
    @Resource
    private DataCollectPanel collectPanel;

    private void addWarn(WarnBean warn) {
        collectPanel.addtablewarn(warn);
    }

    private void addDataWarn(DisDataBean warn) {
        for (BaseCollectPanel collectPanel : logoInfo.getCollectPanelMap().values()) {
            if (clttype == collectPanel.getClttype() && collectPanel.isWarn()) {
                collectPanel.addWarn(warn);
            }
        }
    }

    public void close() {
        alive = false;
        // 如果数据处理线程waiting中
        if (dataThread.getState() == Thread.State.WAITING) {
            lock.lock();
            con.signal();
            lock.unlock();
        }
    }

    private int getServerIndex(int[] ints) {
        int index = -1;
        int avg = 0;//平均值
        int max = ints[0];//最大值
        for (int anInt : ints) {
            avg += anInt;
            max = Math.max(anInt, max);
        }
        avg /= ints.length;
//        System.out.println("平均值:" + avg);
        double[] vagdouble = new double[7];
        double percent = 0.2;
        for (int i = 0; i < vagdouble.length; i++) {
            vagdouble[i] = (max - avg) * percent + avg;
            percent += 0.1;
        }
        percent = 0.2;
        int[] indexs = new int[vagdouble.length];
        for (int j = 0; j < vagdouble.length; j++) {
            for (int i = 0; i < ints.length; i++) {
                if (ints[i] > vagdouble[j]) {
//                    System.out.println(Math.round(percent * 100) + "%:" + i);
                    indexs[j] = i;
                    break;
                }
            }
            percent += 0.1;
        }
        for (int i = 0; i < indexs.length - 1; i++) {
            if (indexs[i + 1] - indexs[i] < 100) {
                index = indexs[i];
                break;
            }
        }
        return index;
    }

}

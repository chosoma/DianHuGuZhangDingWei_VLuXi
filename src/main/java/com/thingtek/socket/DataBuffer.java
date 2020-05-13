package com.thingtek.socket;

import com.thingtek.beanServiceDao.data.entity.DisDataBean;
import com.thingtek.beanServiceDao.data.service.DisDataService;
import com.thingtek.beanServiceDao.pipe.entity.PipeBean;
import com.thingtek.beanServiceDao.pipe.service.PipeService;
import com.thingtek.beanServiceDao.unit.entity.LXUnitBean;
import com.thingtek.beanServiceDao.unit.service.LXUnitService;
import com.thingtek.beanServiceDao.warn.entity.WarnBean;
import com.thingtek.beanServiceDao.warn.service.WarnService;
import com.thingtek.view.shell.dataCollect.LXDataCollectPanel;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DataBuffer {
    private Map<Short, Map<Integer, RawData[]>> buffer;//单元编号 , 大包编号  , 数据
    private final List<RawData[]> wholebuffer;
    // 数据处理线程
    private Thread dataThread;

    private Lock lock;
    private Condition con;
    private boolean dev;

    public void setDev(boolean dev) {
        this.dev = dev;
    }

    @Resource
    private DisDataService dataService;
    @Resource
    private LXUnitService unitService;
    @Resource
    private PipeService pipeService;
    @Resource
    private WarnService warnService;

    private ConcurrentHashMap<PipeBean, List<DisDataBean>> warnmap; //根据管体 检查警报

    private ConcurrentHashMap<PipeBean, List<DisDataBean>> cacheData;

    private ConcurrentHashMap<PipeBean, Map<LXUnitBean, DisDataBean>> warningCacheData;

    public DataBuffer() {
        buffer = new HashMap<>();
        wholebuffer = new ArrayList<>();
        warnmap = new ConcurrentHashMap<>();
        cacheData = new ConcurrentHashMap<>();
        lock = new ReentrantLock();
        con = lock.newCondition();
        warningCacheData = new ConcurrentHashMap<>();
        start();
    }

    /**
     * 将有效长度为length的数据添加到数据缓冲区
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
//                                factory.saveDatas();// 数据存储
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
                        int serverIndex = getServerIndex(data.getData());
                        data.setServerindex(serverIndex);
                        int minindex = data.getData().length - data.getGatewayfrontindex() - serverIndex;
                        data.setMinindex(minindex);
                        int gatewayfrontsj = data.getGatewayfrontsj();
                        int serversj = gatewayfrontsj - minindex * 10;
                        data.setServersj(serversj);
                        if (dev){
                            resolveData(data);
                        }else {
                            resolveWarning(data);
                        }
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
        PipeBean pipe = addData(data);
        if (pipe == null) {
            return;
        }
        List<DisDataBean> datas = sort(pipe);
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
            long sj1 = data1.getGatewayfrontsj();
            long sj2 = data2.getGatewayfrontsj();
            long mat = sj2 - sj1;
            if (mat < 0) {
                mat *= -1;
            }
            int place1 = data1.getUnit().getPlace_value();
            int place2 = data2.getUnit().getPlace_value();
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
                        data1.getUnit().getPlace_name() :
                        data2.getUnit().getPlace_name())
                        + "到"
                        + (sj1 >= sj2 ?
                        data1.getUnit().getPlace_name() :
                        data2.getUnit().getPlace_name()) + "( "
                        + weizhi + " )米附近,相位:" + pipe
                        + ",时间差:" + msec;
                System.out.println(str);
                WarnBean warnBean = new WarnBean();
                warnBean.setPipe(pipe);
                warnBean.setInserttime(data.getInserttime());
                warnBean.setWarn_info(str);
                warnService.save(warnBean);
                addWarn(warnBean);
                datas.remove(data1);
                datas.remove(data2);
                i -= 2;
            } else {//先触发的另一边
                datas.remove(data2);
                --i;
                return;
            }
        }
    }


    private void resolveData(DisDataBean data) {
        LXUnitBean unit = unitService.getUnitByNumber(data.getUnit_num());
        PipeBean pipe = pipeService.getPipeById(unit.getPipe_id());
        //检测正在报警的
        Map<LXUnitBean, DisDataBean> warningData = warningCacheData.get(pipe);
        if (warningData.containsKey(unit)) {//单元相同 直接存储
            dataService.saveDatas(data);
            return;
        }
        for (Map.Entry<LXUnitBean, DisDataBean> entry : warningData.entrySet()) {
            DisDataBean disDataBean = entry.getValue();
            LXUnitBean lxUnitBean = entry.getKey();
            LXUnitBean resolveunit = resolveNearestUnit(pipe, disDataBean, data);
            if (resolveunit == null) {
                continue;
            }
            if (lxUnitBean.getUnit_num().equals(resolveunit.getUnit_num())) {
                dataService.saveDatas(data);
                return;
            }
        }
        //找到 返回 没找到 存储缓存数据
        addCacheData(data);

    }

    private void addCacheData(DisDataBean data) {//存储缓存数据
        LXUnitBean unit = unitService.getUnitByNumber(data.getUnit_num());
        PipeBean pipe = pipeService.getPipeById(unit.getPipe_id());
        if (cacheData.containsKey(pipe)) {
            cacheData.get(pipe).add(data);
        } else {
            List<DisDataBean> datas = new ArrayList<>();
            datas.add(data);
            cacheData.put(pipe, datas);
        }
        resolvecachedata();//解析缓存
    }

    private void resolvecachedata() {
        Set<Map.Entry<PipeBean, List<DisDataBean>>> entries = cacheData.entrySet();
        a:
        for (Map.Entry<PipeBean, List<DisDataBean>> entry : entries) {
            PipeBean pipe = entry.getKey();
            List<DisDataBean> datas = entry.getValue();
            b:
            for (int i = 0; i < datas.size(); i++) {
                for (int j = i + 1; j < datas.size(); j++) {
                    DisDataBean data1 = datas.get(i);
                    DisDataBean data2 = datas.get(j);
                    LXUnitBean unit1 = data1.getUnit();
                    LXUnitBean unit2 = data2.getUnit();
                    int jian_ge_unit_point = unit1.getPoint() - unit2.getPoint();
                    if (jian_ge_unit_point <= 0) {
                        jian_ge_unit_point *= -1;
                    }
                    if (unit1.getUnit_num().equals(unit2.getUnit_num()) || jian_ge_unit_point >= 3) {
                        continue;
                    }
                    LXUnitBean unit = resolveNearestUnit(pipe, data1, data2);
                    if (unit == null) {
                        continue;
                    }
                    double place = resolvePlace(data1, data2);
                    addWarningCahceData(unit, data1);
                    WarnBean warnBean = new WarnBean();
                    warnBean.setPipe(pipe);
                    warnBean.setNearunit(unit);
                    warnBean.setNear_unit_num(unit.getUnit_num());
                    LXUnitBean toUnit = unitService.getToUnit(unit, place);
                    if (toUnit != null) {
                        warnBean.setTounit(toUnit);
                        warnBean.setTo_unit_num(toUnit.getUnit_num());
                    }
                    warnBean.setPalce_value(place);
                    warnBean.setInserttime(data1.getInserttime());
                    dataService.saveDatas(data1, data2);
                    warnService.save(warnBean);
                    addWarn(warnBean);
                    addDataWarn(warnBean);
                    datas.remove(data1);
                    datas.remove(data2);
                    break b;
                }
            }
            removeAndSaveCache(pipe, datas);
        }
    }

    private void removeAndSaveCache(PipeBean pipe, List<DisDataBean> datas) {
        Map<LXUnitBean, DisDataBean> warningData = warningCacheData.get(pipe);
        for (Map.Entry<LXUnitBean, DisDataBean> entry : warningData.entrySet()) {
            LXUnitBean unit = entry.getKey();
            DisDataBean data = entry.getValue();
            Iterator<DisDataBean> iterator = datas.iterator();
            while (iterator.hasNext()) {
                DisDataBean next = iterator.next();
                LXUnitBean nearunit = resolveNearestUnit(pipe, next, data);
                if (nearunit != null && unit.getUnit_num().equals(nearunit.getUnit_num())) {
                    dataService.saveDatas(next);
                    iterator.remove();
                }
            }
        }

    }

    private double resolvePlace(DisDataBean data1, DisDataBean data2) {
        LXUnitBean unit1 = data1.getUnit();
        LXUnitBean unit2 = data2.getUnit();
        long sj1 = data1.getServersj();
        long sj2 = data2.getServersj();
        long mat = sj2 - sj1;
        if (mat < 0) {
            mat *= -1;
        }
        int place1 = unit1.getPlace_value();
        int place2 = unit2.getPlace_value();
        int juli = place1 - place2;
        if (juli < 0) {
            juli *= -1;
        }
        double msec = (mat / 1000.0) * 5;
        //  这个5 是基数 每毫秒5米
        if (msec <= juli) {//两者之间
            // 位置 = 距离 - 时间差 * 基数 最终位置 = 先出发位置起 + 位置;
            //最终位置

            double weizhi = (juli - msec * 5) / 2;
            return unit1.getPoint() < unit2.getPoint() ?
                    unit1.getPlace_value() + weizhi :
                    unit1.getPlace_value() - weizhi;
        }
        return -1;
    }

    private LXUnitBean resolveNearestUnit(PipeBean pipe, DisDataBean data1, DisDataBean data2) {
        double place = resolvePlace(data1, data2);
        if (place < 0) {
            return null;
        }
        return unitService.getNearestUnit(pipe, place);
    }

    private void addWarningCahceData(LXUnitBean unit, DisDataBean data) {
        PipeBean pipe = pipeService.getPipeById(unit.getPipe_id());
        Map<LXUnitBean, DisDataBean> disDataBeanMap;
        if (warningCacheData.containsKey(pipe)) {
            disDataBeanMap = warningCacheData.get(pipe);
        } else {
            disDataBeanMap = new HashMap<>();
            warningCacheData.put(pipe, disDataBeanMap);
        }
        if (disDataBeanMap.containsKey(unit)) {
            return;
        }
        disDataBeanMap.put(unit, data);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                warningCacheData.get(pipe).remove(unit);
            }
        }, 10000);
    }

    private PipeBean addData(DisDataBean data) {
        LXUnitBean unit = unitService.getUnitByNumber(data.getUnit_num());
        if (unit == null) {
            return null;
        }
        data.setUnit(unit);
        PipeBean pipeBean = pipeService.getPipeById(unit.getPipe_id());
        if (pipeBean == null) {
            return null;
        }
        unit.setPipe(pipeBean);
        List<DisDataBean> list;
        if (warnmap.containsKey(pipeBean)) {
            list = warnmap.get(pipeBean);
        } else {
            list = new ArrayList<>();
        }
        list.add(data);
        warnmap.put(pipeBean, list);
//        addDataWarn(data);
        return pipeBean;
    }

    private List<DisDataBean> sort(PipeBean pipeBean) {
        List<DisDataBean> datas = warnmap.get(pipeBean);
        Collections.sort(datas);
        return datas;
    }

    @Resource
    private LXDataCollectPanel collectPanel;

    private void addWarn(WarnBean warn) {
        collectPanel.addtablewarn(warn);
    }

    private void addDataWarn(WarnBean warn) {

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

    public void close() {
        alive = false;
        // 如果数据处理线程waiting中
        if (dataThread.getState() == Thread.State.WAITING) {
            lock.lock();
            con.signal();
            lock.unlock();
        }
    }

}

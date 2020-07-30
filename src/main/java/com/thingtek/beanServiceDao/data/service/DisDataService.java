package com.thingtek.beanServiceDao.data.service;

import com.thingtek.beanServiceDao.base.BaseService;
import com.thingtek.beanServiceDao.data.dao.DisDataDao;
import com.thingtek.beanServiceDao.data.entity.DisDataBean;
import com.thingtek.beanServiceDao.unit.entity.LXUnitBean;
import com.thingtek.beanServiceDao.unit.service.LXUnitService;
import com.thingtek.socket.DataBuffer;
import com.thingtek.socket.data.entity.DataSearchPara;
import com.thingtek.socketank.AnkServer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class DisDataService extends BaseService {

    @Resource
    private DisDataDao dao;

    @Resource
    private LXUnitService unitService;

    @Resource
    private DataBuffer dataBuffer;

    private Map<Short, List<DisDataBean>> findbuffer = new Hashtable<>();

    public List<DisDataBean> getDataInfo(DataSearchPara para) {
        findbuffer.clear();
        List<DisDataBean> baseDataBeanList = new ArrayList<>();
        try {
            List<LXUnitBean> units = unitService.getAll();
            for (LXUnitBean unit : units) {
                para.setUnit(unit);
                List<DisDataBean> bufferlist = dao.findDatas(para);
                for (DisDataBean one : bufferlist) {
                    one.setUnit(unitService.getUnitByNumber(one.getUnit_num()));
                }
                baseDataBeanList.addAll(bufferlist);
                findbuffer.put(unit.getUnit_num(), bufferlist);
            }
        } catch (Exception e) {
            log(e);
        }
        return baseDataBeanList;
    }

    public DisDataBean getData(DisDataBean datapara) {
        DisDataBean datareturn = null;
        try {
            LXUnitBean unit = unitService.getUnitByNumber(datapara.getUnit_num());
            for (DisDataBean dataBean : findbuffer.get(unit.getUnit_num())) {
                if (dataBean.getInserttime().getTime() == datapara.getInserttime().getTime()) {
                    datareturn = dataBean;
                    break;
                }
            }
        } catch (Exception e) {
            log(e);
        }
        check(datareturn);
        if (dataBuffer.isClick()) {
            int min = check2(datareturn);
            short addr = datareturn.getUnit().getAddr();
            if (min > 0) {
                ankServer.setValue(addr, (short) (min / 1500 + 10));
            } else {
                ankServer.setValue(addr, (short) 10);
            }
        }
        return datareturn;
    }

    @Resource
    AnkServer ankServer;

    private boolean check(DisDataBean data) {
        int[] datas = data.getData();
        int count = 0;
        for (int value : datas) {
            if (value > 4000 || value < 100) {
                count++;
            }
        }
        return count < 100;
    }

    private boolean check1(DisDataBean data) {
        int[] datas = data.getData();
        int first;
        int firstend;
        List<Boolean> flags = new ArrayList<>();
        List<Integer> indexs = new ArrayList<>();
        for (int i = 0; i < datas.length; i++) {
            if (datas[i] > 3800) {
                indexs.add(i);
            }
        }
        if (indexs.size() > 2) {
            Integer[] ints = indexs.toArray(new Integer[1]);
            Arrays.sort(ints);
            first = ints[0];
            for (int i = 1; i < ints.length - 1; i++) {
                if (ints[i] - ints[i - 1] <= 5000) {//未结束
                    firstend = ints[i - 1];
                    if (firstend - first > 20000) {
                        flags.add(true);
                    }
                } else {
                    first = ints[i];
                }
            }
        } else return false;
        return flags.contains(true);
    }

    private int check2(DisDataBean data) {
        int[] datas = data.getData();
        int first = -1;
        int firstend = -1;
        List<Integer> indexs = new ArrayList<>();
        for (int i = 0; i < datas.length; i++) {
            if (datas[i] > 3800) {
                indexs.add(i);
            }
        }
        boolean flag = false;
        List<Integer> indexmin = new ArrayList<>();
        if (indexs.size() > 2) {
            Integer[] ints = indexs.toArray(new Integer[1]);
            Arrays.sort(ints);
            first = ints[0];
            for (int i = 1; i < ints.length - 1; i++) {
                if (ints[i] - ints[i - 1] <= 5000) {//未结束
                    firstend = ints[i - 1];
                    if (firstend - first > 20000) {
                        flag = true;
                        continue;
                    }
                } else {
                    if (first >= 0 || firstend >= 0) {
                        indexmin.add(firstend - first);
                    }
                    first = ints[i];
                    firstend = ints[i];
                }
            }
        }
        if (flag) {
            indexmin.add(firstend - first);
        }
        int MAX = -1;
        for (Integer integer : indexmin) {
            if (integer > MAX) {
                MAX = integer;
            }
        }
//        System.out.println(MAX);
        return MAX;
    }

    public void deleteData(Map<LXUnitBean, List<Date>> datas) {
        Set<Map.Entry<LXUnitBean, List<Date>>> entries = datas.entrySet();
        for (Map.Entry<LXUnitBean, List<Date>> entry : entries) {
            try {
                dao.deleteDatas(entry.getKey(), entry.getValue());
            } catch (Exception e) {
                log(e);
            }
        }
    }

    public void saveDatas(DisDataBean... datas) {
        for (DisDataBean data : datas) {
            try {
                data.setUnit(unitService.getUnitByNumber(data.getUnit_num()));
                dao.saveDatas(data);
            } catch (Exception e) {
                log(e);
            }
        }
    }

    public void saveNoWaningData(DisDataBean... datas) {
        for (DisDataBean data : datas) {
            try {
                data.setUnit(unitService.getUnitByNumber(data.getUnit_num()));
                dao.saveNoWarningDatas(data);
            } catch (Exception e) {
                log(e);
            }
        }
    }


/*
    private int checkdata(int[] ints) {
        int index = -1;
        int avg = 0;//平均值
        int max = ints[0];//最大值
        for (int anInt : ints) {
            avg += anInt;
            max = Math.max(anInt, max);
        }
        avg /= ints.length;
        System.out.println("平均值:" + avg);
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
                    System.out.println(Math.round(percent * 100) + "%:" + i);
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

        */
/*int dian = 10;
        int[] dians = new int[100];
        for (int i = 0; i < ints.length / dian - dians.length; i++) {
            System.arraycopy(ints, i * dian, dians, 0, dians.length);
            double dianzong = 0;
            for (int value : dians) {
                dianzong += value > avg ? value : avg;
            }
//            System.out.println(dianzong);
            dianzong /= dians.length;
//            System.out.print("," + dianzong);
            double beishu = 1.125;
            if (dianzong > avg * beishu) {
                System.out.println(":" + (i * dian));
                break;
            }
        }*//*

    }
*/
}

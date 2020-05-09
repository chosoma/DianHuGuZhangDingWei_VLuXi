package com.thingtek.beanServiceDao.data.service;

import com.thingtek.beanServiceDao.base.BaseService;
import com.thingtek.beanServiceDao.data.dao.DisDataDao;
import com.thingtek.beanServiceDao.data.entity.DisDataBean;
import com.thingtek.beanServiceDao.unit.entity.LXUnitBean;
import com.thingtek.beanServiceDao.unit.service.LXUnitService;
import com.thingtek.socket.data.entity.DataSearchPara;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class DisDataService extends BaseService {

    @Resource
    private DisDataDao dao;

    @Resource
    private LXUnitService unitService;

    private Map<LXUnitBean, List<DisDataBean>> databuffer = new Hashtable<>();

    public int count(DataSearchPara para) {
        int count = 0;
        try {
            long start = System.currentTimeMillis();
            count = dao.count(para);
            System.out.println("count时间:" + count + ":" + (System.currentTimeMillis() - start) + "ms");
        } catch (Exception e) {
            log(e);
        }
        return count;
    }

    public List<DisDataBean> getDataInfo(DataSearchPara para) {
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
                databuffer.put(unit, bufferlist);
            }
        } catch (Exception e) {
            log(e);
        }
        return baseDataBeanList;
    }

    public DisDataBean getData(DisDataBean datapara) {
        DisDataBean datareturn = new DisDataBean();
        try {
            LXUnitBean unit = unitService.getUnitByNumber(datapara.getUnit_num());
            for (DisDataBean dataBean : databuffer.get(unit)) {
                if (dataBean.getInserttime().getTime() == datapara.getInserttime().getTime()) {
                    datareturn = dataBean;
                    break;
                }
            }
        } catch (Exception e) {
            log(e);
        }
//        checkdata(datareturn.getData());
        return datareturn;
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

    public boolean saveDatas(DisDataBean... datas) {
        List<Boolean> flags = new ArrayList<>();
        for (DisDataBean data : datas) {
            try {
                data.setUnit(unitService.getUnitByNumber(data.getUnit_num()));
                flags.add(dao.saveDatas(data));
            } catch (Exception e) {
                log(e);
                flags.add(false);
            }
        }
        return !flags.contains(false);
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

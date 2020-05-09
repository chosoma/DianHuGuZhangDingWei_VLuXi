package com.thingtek.beanServiceDao.unit.service;


import com.thingtek.beanServiceDao.base.BaseService;
import com.thingtek.beanServiceDao.pipe.entity.PipeBean;
import com.thingtek.beanServiceDao.pipe.service.PipeService;
import com.thingtek.beanServiceDao.unit.entity.LXUnitBean;
import com.thingtek.beanServiceDao.unit.dao.LXUnitDao;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class LXUnitService extends BaseService {

    @Resource
    private LXUnitDao dao;

    @Resource
    private PipeService pipeService;


    private List<LXUnitBean> units;

    public LXUnitService() {
        units = new ArrayList<>();
    }

    public void saveLXUnit(LXUnitBean unitBean) {
        try {
            String unit_data_name = "unit_data_" + unitBean.getUnit_num();
            unitBean.setData_table_name(unit_data_name);
            dao.createDataTable(unit_data_name);
            dao.saveLXUnit(unitBean);
        } catch (Exception e) {
            log(e);
        }
            units.clear();
            cache();
    }

    /*public List<LXUnitBean> getAll() {
        cache();
        return units;
    }*/

    public List<LXUnitBean> getAll() {
        cache();
        return units;
    }

    public Vector<Short> getUnHasUnitNum() {
        cache();
        Vector<Short> vector = new Vector<>();
        for (short i = 1; i < Short.MAX_VALUE; i++) {
            vector.add(i);
        }
        Vector<Short> removes = new Vector<>();
        for (LXUnitBean unit : units) {
            removes.add((short) (unit.getUnit_num() & 0xff));
        }
        vector.removeAll(removes);
        return vector;
    }

    public List<LXUnitBean> getUnitsByPipe(PipeBean pipeBean) {
        cache();
        List<LXUnitBean> units = new ArrayList<>();
        for (LXUnitBean unit : this.units) {
            if (pipeBean.getPipe_id() == unit.getPipe_id()) {
                units.add(unit);
            }
        }
        return units;
    }

    public LXUnitBean getUnitByNumber(short unit_num) {
        cache();
        for (LXUnitBean unit : units) {
            if (Objects.equals(unit_num, unit.getUnit_num())) {
                return unit;
            }
        }
        return null;
    }

    public boolean updateLXUnit(LXUnitBean... units) {
        List<Boolean> flags = new ArrayList<>();
        for (LXUnitBean unit : units) {
            try {
                flags.add(dao.updateLXUnit(unit));
            } catch (Exception e) {
                flags.add(false);
                log(e);
            }
        }
        return !flags.contains(false);
    }

    public void deleteUnitByNum(short... unit_nums) {
        try {
            for (short unit_num : unit_nums) {
                LXUnitBean unit = getUnitByNumber(unit_num);
                dao.dropDataTable(unit.getData_table_name());
            }
            if (dao.deleteUnitByNum(unit_nums)) {
                units.clear();
                cache();
            }
        } catch (Exception e) {
            log(e);
        }
    }

    /*public boolean deleteUnitByNum(byte unit_num) {
        boolean flag;
        try {
            flag = dao.deleteUnitByNum(unit_num);
            if (flag) {
                units.clear();
                cache();
            }
        } catch (Exception e) {
            flag = false;
            log(e);
        }
        return flag;
    }*/

    private void cache() {
        if (units.size() == 0) {
            try {
                units = dao.findAll();
                for (LXUnitBean one : units) {
                    one.setPipe(pipeService.getPipeById(one.getPipe_id()));
                }
            } catch (Exception e) {
                log(e);
            }
        }
    }


}

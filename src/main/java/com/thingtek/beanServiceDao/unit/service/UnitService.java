package com.thingtek.beanServiceDao.unit.service;


import com.thingtek.beanServiceDao.base.service.BaseService;
import com.thingtek.beanServiceDao.clt.entity.CltBean;
import com.thingtek.beanServiceDao.clt.service.CltService;
import com.thingtek.beanServiceDao.point.entity.PointBean;
import com.thingtek.beanServiceDao.point.service.PointService;
import com.thingtek.beanServiceDao.unit.base.BaseUnitBean;
import com.thingtek.beanServiceDao.unit.dao.UnitDao;

import com.thingtek.config.clazz.ClazzConfig;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class UnitService extends BaseService {

    @Resource
    private UnitDao dao;

    @Resource
    private PointService pointService;

    @Resource
    private CltService cltService;

//    private List<DisUnitBean> units;

    private Map<Integer, List<BaseUnitBean>> unitList;

    public UnitService() {
        unitList = new HashMap<>();
    }

    /*public boolean saveUnit(DisUnitBean unit) {
        boolean flag = false;
        try {
            unit.resolve2map();
            flag = dao.saveUnit(unit);
        } catch (Exception e) {
            log(e);
        }
        if (flag) {
            units.clear();
            cache();
        }
        return flag;
    }*/

    public boolean saveUnit(int clt_type, BaseUnitBean unitBean) {
        boolean flag = false;
        try {
            unitBean.resolve2map();
            String unit_data_name = "unit_data_" + unitBean.getUnit_num();
            unitBean.setData_table_name(unit_data_name);
            dao.createDataTable(unit_data_name);
            flag = dao.saveUnit(clt_type, unitBean);
        } catch (Exception e) {
            log(e);
        }
        if (flag) {
            unitList.clear();
            cache();
        }
        return flag;
    }

    /*public List<DisUnitBean> getAll() {
        cache();
        return units;
    }*/

    public List<BaseUnitBean> getAll(int clttype) {
        cache();
        return unitList.get(clttype);
    }

    public Vector<Integer> getUnHasUnitNum(int clttype) {
        cache();
        Vector<Integer> vector = new Vector<>();
        for (int i = 1; i <= Short.MAX_VALUE; i++) {
            vector.add(i);
        }
        Vector<Integer> removes = new Vector<>();
        for (BaseUnitBean unit : unitList.get(clttype)) {
            removes.add(unit.getUnit_num() & 0xff);
        }
        vector.removeAll(removes);
        return vector;
    }

    /*public Vector<Integer> getUnHasUnitNum(int clttype) {
        cache();
        Vector<Integer> vector = new Vector<>();
        for (int i = 1; i <= 255; i++) {
            if (clttype == 1) {
                if (i == 173 || i > 247) {
                    continue;
                }
            }
            vector.add(i);
        }
        Vector<Integer> removes = new Vector<>();
        for (BaseUnitBean unit : unitList.get(clttype)) {
            removes.add(unit.getUnit_num() & 0xff);
        }
        vector.removeAll(removes);
        return vector;
    }*/

    /*public DisUnitBean getUnitByNumber(Byte unit_num) {
        cache();
        for (DisUnitBean unit : units) {
            if (Objects.equals(unit_num, unit.getUnit_num())) {
//                System.out.println(unit);
                return unit;
            }
        }
        return null;
    }*/
    public List<BaseUnitBean> getUnitsByPoint(int clttype, PointBean point) {
        List<BaseUnitBean> units = new ArrayList<>();
        for (BaseUnitBean unit : unitList.get(clttype)) {
            if (Objects.equals(point.getPoint_num(), unit.getPoint_num())) {
                units.add(unit);
            }
        }
        return units;
    }

    public BaseUnitBean getUnitByPointAndPhase(int clttype, PointBean point, String phase) {
        List<BaseUnitBean> units = getUnitsByPoint(clttype, point);
        for (BaseUnitBean unit : units) {
            if (Objects.equals(phase, unit.getPhase())) {
                return unit;
            }
        }
        return null;
    }

    public BaseUnitBean getUnitByPointAndPhase(int clttype, int point, String phase) {
        PointBean pointBean = pointService.getPointByNum(clttype, point);
        List<BaseUnitBean> units = getUnitsByPoint(clttype, pointBean);
        for (BaseUnitBean unit : units) {
            if (Objects.equals(phase, unit.getPhase())) {
                return unit;
            }
        }
        return null;
    }

    public BaseUnitBean getUnitByNumber(int clttype, short unit_num) {
        cache();
        for (BaseUnitBean unit : unitList.get(clttype)) {
            if (Objects.equals(unit_num, unit.getUnit_num())) {
                return unit;
            }
        }
        return null;
    }

    public boolean updateUnit(int clttype, BaseUnitBean... units) {
        List<Boolean> flags = new ArrayList<>();
        for (BaseUnitBean unit : units) {
            unit.resolve2map();
            try {
                flags.add(dao.update(clttype, unit.getOne()));
            } catch (Exception e) {
                flags.add(false);
                log(e);
            }
        }
        return !flags.contains(false);
    }

    public boolean deleteUnitByNum(int clttype, short... unit_nums) {
        boolean flag;
        try {
            for (short unit_num : unit_nums) {
                BaseUnitBean unit = getUnitByNumber(clttype, unit_num);
//                System.out.println(unit.getData_table_name());
                dao.dropDataTable(unit.getData_table_name());
            }
            flag = dao.deleteUnitByNum(clttype, unit_nums);
            if (flag) {
                unitList.clear();
                cache();
            }
        } catch (Exception e) {
            flag = false;
            log(e);
        }
        return flag;
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
        if (unitList.size() == 0) {
            List<CltBean> clts = cltService.getAll();
//            System.out.println(clts);
            for (CltBean clt : clts) {
                try {
                    List<Map<String, Object>> unitmap = dao.findByCltType(clt.getType_num());
                    List<BaseUnitBean> oneunits = new ArrayList<>();
                    unitList.put(clt.getType_num(), oneunits);
                    if (unitmap.size() == 0) {
                        continue;
                    }
                    for (Map<String, Object> one : unitmap) {
                        BaseUnitBean unit = (BaseUnitBean) this.getClass()
                                .getClassLoader()
//                            .loadClass(clazzConfig.getUnitClazzName((Integer) one.get("CLT_TYPE")))
                                .loadClass(clazzConfig.getUnitClazzName(clt.getType_num()))
                                .newInstance();
                        unit.resolve(one);
                        unit.setClt(clt);
                        unit.setPoint(pointService.getPointByNum(clt.getType_num(), unit.getPoint_num()));
                        oneunits.add(unit);
                    }
                } catch (Exception e) {
                    log(e);
                }
            }
        }

        /*if (units == null || units.size() == 0) {
            units = new ArrayList<>();
            List<Map<String, Object>> units;
            try {
                units = dao.findAll();
            } catch (Exception e) {
                units = new ArrayList<>();
                log(e);
            }
            for (Map<String, Object> one : units) {
                DisUnitBean unit = new DisUnitBean();
                unit.resolve(one);
                this.units.add(unit);
            }
        }*/
    }

    @Resource
    private ClazzConfig clazzConfig;
}

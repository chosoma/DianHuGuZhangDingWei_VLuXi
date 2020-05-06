package com.thingtek.beanServiceDao.warn.service;

import com.thingtek.beanServiceDao.base.service.BaseService;
import com.thingtek.beanServiceDao.point.entity.PointBean;
import com.thingtek.beanServiceDao.point.service.PointService;
import com.thingtek.beanServiceDao.unit.base.BaseUnitBean;
import com.thingtek.beanServiceDao.unit.service.UnitService;
import com.thingtek.beanServiceDao.warn.dao.WarnDao;
import com.thingtek.beanServiceDao.warn.entity.WarnBean;
import com.thingtek.socket.data.entity.DataSearchPara;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class WarnService extends BaseService {

    private @Resource
    WarnDao dao;

    private @Resource
    UnitService unitService;

    private @Resource
    PointService pointService;


    public List<WarnBean> getWarnByPara(DataSearchPara para) {
        List<WarnBean> warns;
        try {
            warns = dao.getByPara(para);
        } catch (Exception e) {
            warns = new ArrayList<>();
            log(e);
        }
        return warns;
    }


    public boolean deleteWarn(Map<WarnBean, List<Date>> warnDateMapList) {
        List<Boolean> flags = new ArrayList<>();
        for (Map.Entry<WarnBean, List<Date>> entry : warnDateMapList.entrySet()) {
            try {
                flags.add(dao.delete(entry.getKey(), entry.getValue()));
            } catch (Exception e) {
                log(e);
                flags.add(false);
            }
        }
        return !flags.contains(false);
    }

    public boolean save(WarnBean... warns) {
        try {
            return dao.save(warns);
        } catch (Exception e) {
            log(e);
            return false;
        }
    }
}

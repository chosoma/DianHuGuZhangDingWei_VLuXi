package com.thingtek.beanServiceDao.warn.service;

import com.thingtek.beanServiceDao.base.BaseService;
import com.thingtek.beanServiceDao.pipe.entity.PipeBean;
import com.thingtek.beanServiceDao.pipe.service.PipeService;
import com.thingtek.beanServiceDao.unit.entity.LXUnitBean;
import com.thingtek.beanServiceDao.unit.service.LXUnitService;
import com.thingtek.beanServiceDao.warn.dao.WarnDao;
import com.thingtek.beanServiceDao.warn.entity.WarnBean;
import com.thingtek.socket.data.entity.DataSearchPara;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class WarnService extends BaseService {

    private @Resource
    WarnDao dao;

    private @Resource
    LXUnitService unitService;

    @Resource
    private PipeService pipeService;

    public List<WarnBean> getWarnByPara(DataSearchPara para) {
        List<WarnBean> warns;
        try {
            warns = dao.getByPara(para);
            for (WarnBean warn : warns) {
                PipeBean pipeBean = pipeService.getPipeById(warn.getPipe_id());
                warn.setPipe(pipeBean);
                LXUnitBean nearunit = unitService.getUnitByNumber(warn.getNear_unit_num());
                warn.setNearunit(nearunit);
                if (warn.getTo_unit_num() != null) {
                    LXUnitBean tounit = unitService.getUnitByNumber(warn.getTo_unit_num());
                    warn.setTounit(tounit);
                }
            }
        } catch (Exception e) {
            warns = new ArrayList<>();
            log(e);
        }
        return warns;
    }


    public boolean deleteWarn(WarnBean... warns) {
        List<Boolean> flags = new ArrayList<>();
        for (WarnBean entry : warns) {
            try {
                flags.add(dao.delete(entry));
            } catch (Exception e) {
                log(e);
                flags.add(false);
            }
        }
        return !flags.contains(false);
    }

    public void save(WarnBean... warns) {
        try {
            dao.save(warns);
        } catch (Exception e) {
            log(e);
        }
    }
}

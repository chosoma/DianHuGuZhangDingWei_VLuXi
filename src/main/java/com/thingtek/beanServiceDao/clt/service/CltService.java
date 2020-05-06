package com.thingtek.beanServiceDao.clt.service;

import com.thingtek.beanServiceDao.base.service.BaseService;
import com.thingtek.beanServiceDao.clt.dao.CltDao;
import com.thingtek.beanServiceDao.clt.entity.CltBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Vector;

@Service
public class CltService extends BaseService {
    @Resource
    private CltDao dao;

    private List<CltBean> collects;

    public List<CltBean> getAll() {
        cache();
        return collects;
    }

    public CltBean getCltByNum(int cltnum) {
        cache();
        for (CltBean clt : collects) {
            if (clt.getType_num() == cltnum) {
                return clt;
            }
        }
        return null;
    }

    public CltBean getCltByName(String cltname) {
        cache();
        for (CltBean clt : collects) {
            if (Objects.equals(cltname, clt.getType_name())) {
                return clt;
            }
        }
        return null;
    }

    public Vector<String> getCltNames() {
        cache();
        Vector<String> cltNames = new Vector<>();
        for (CltBean clt : collects) {
            cltNames.add(clt.getType_name());
        }
        return cltNames;
    }

    private void cache() {
        if (collects == null || collects.size() != 0) {
            try {
                collects = dao.findAll();
            } catch (Exception e) {
                log(e);
                collects = new ArrayList<>();
            }
        }
    }


}

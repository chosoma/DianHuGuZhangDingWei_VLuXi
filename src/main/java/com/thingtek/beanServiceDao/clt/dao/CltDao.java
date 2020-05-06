package com.thingtek.beanServiceDao.clt.dao;

import com.thingtek.beanServiceDao.clt.entity.CltBean;

import java.util.List;

public interface CltDao {
    List<CltBean> findAll() throws Exception;
}

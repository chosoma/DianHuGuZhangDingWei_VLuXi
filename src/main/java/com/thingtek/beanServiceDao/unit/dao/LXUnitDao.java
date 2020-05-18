package com.thingtek.beanServiceDao.unit.dao;

import com.thingtek.beanServiceDao.unit.entity.LXUnitBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LXUnitDao {


    List<LXUnitBean> findAll() throws Exception;

    void saveLXUnit(@Param("units") LXUnitBean... unitBean) throws Exception;

    boolean updateLXUnit(@Param("unit") LXUnitBean unitBean) throws Exception;

    boolean deleteUnitByNum( @Param("unit_nums") short... unit_num) throws Exception;

    void createDataTable(LXUnitBean unitBean) throws Exception;

    void dropDataTable(String data_table_name) throws Exception;
}

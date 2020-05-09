package com.thingtek.beanServiceDao.data.dao;

import com.thingtek.beanServiceDao.data.entity.DisDataBean;
import com.thingtek.beanServiceDao.unit.entity.LXUnitBean;
import com.thingtek.socket.data.entity.DataSearchPara;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface DisDataDao {

    List<DisDataBean> findDatas(DataSearchPara para) throws Exception;

    int count(DataSearchPara para) throws Exception;

    boolean deleteDatas(@Param("unit") LXUnitBean unit, @Param("dates") List<Date> dates) throws Exception;

    boolean saveDatas(DisDataBean dataBean) throws Exception;
}

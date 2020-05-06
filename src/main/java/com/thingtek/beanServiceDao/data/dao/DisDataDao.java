package com.thingtek.beanServiceDao.data.dao;

import com.thingtek.beanServiceDao.data.entity.DisDataBean;
import com.thingtek.beanServiceDao.unit.entity.DisUnitBean;
import com.thingtek.socket.data.entity.DataSearchPara;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface DisDataDao {

    String findData(DisDataBean datapara) throws Exception;

    List<Map<String, Object>> findDataInfo(DataSearchPara para) throws Exception;

    int count(DataSearchPara para) throws Exception;

    boolean deleteDatas(@Param("unit") DisUnitBean unit, @Param("dates") List<Date> dates) throws Exception;

    boolean saveData(DisDataBean dataBean) throws Exception;
}

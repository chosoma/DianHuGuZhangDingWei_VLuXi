package com.thingtek.beanServiceDao.warn.dao;

import com.thingtek.beanServiceDao.warn.entity.WarnBean;
import com.thingtek.socket.data.entity.DataSearchPara;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface WarnDao {

    List<WarnBean> getByPara(DataSearchPara para) throws Exception;

    List<WarnBean> getLast() throws Exception;

    boolean delete(WarnBean warn) throws Exception;

    boolean save(WarnBean... warns) throws Exception;

    WarnBean getLastByUnit(@Param("clt_type") int clttype, @Param("unit_num") byte unit_num) throws Exception;

}

package com.thingtek.beanServiceDao.point.dao;

import com.thingtek.beanServiceDao.point.entity.PointBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PointDao {

    List<PointBean> findAll() throws Exception;

    boolean save(PointBean... points) throws Exception;

    boolean delete(@Param("clt_type") int clt_type, @Param("point_nums") int... point_nums) throws Exception;

    boolean update(PointBean point) throws Exception;
}

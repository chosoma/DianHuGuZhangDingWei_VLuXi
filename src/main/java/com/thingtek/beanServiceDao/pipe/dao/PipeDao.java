package com.thingtek.beanServiceDao.pipe.dao;

import com.thingtek.beanServiceDao.pipe.entity.PipeBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PipeDao {

    boolean save(PipeBean... pipeBean) throws Exception;

    boolean update(PipeBean pipeBean) throws Exception;

    boolean delete(@Param("pipe_ids") int... pipe_id_s) throws Exception;

    List<PipeBean> findAll() throws Exception;

}

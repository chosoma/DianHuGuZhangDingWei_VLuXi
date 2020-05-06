package com.thingtek.beanServiceDao.pipe.service;

import com.thingtek.beanServiceDao.base.service.BaseService;
import com.thingtek.beanServiceDao.pipe.dao.PipeDao;
import com.thingtek.beanServiceDao.pipe.entity.PipeBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class PipeService extends BaseService {
    @Resource
    private PipeDao dao;

    private List<PipeBean> pipes;

    public boolean save(PipeBean... pipeBean) {
        boolean flag = false;
        try {
            flag = dao.save(pipeBean);
        } catch (Exception e) {
            log(e);
        }
        if (flag) {
            pipes.clear();
        }
        return flag;
    }

    public boolean delete(int... pipe_ids) {
        boolean flag = false;
        try {
            flag = dao.delete(pipe_ids);
        } catch (Exception e) {
            log(e);
        }
        if (flag) {
            pipes.clear();
            cache();
        }
        return flag;
    }

    public boolean update() {
        List<Boolean> flags = new ArrayList<>();
        try {
            for (PipeBean pipe : pipes) {
                flags.add(dao.update(pipe));
            }
        } catch (Exception e) {
            log(e);
            flags.add(false);
        }
        return !flags.contains(false);
    }

    public PipeBean getPipeById(int pipe_id) {
        cache();
        for (PipeBean pipe : pipes) {
            if (pipe_id == pipe.getPipe_id()) {
                return pipe;
            }
        }
        return null;
    }

    public List<PipeBean> findAll() {
        List<PipeBean> pipes = new ArrayList<>();
        try {
            pipes = dao.findAll();
        } catch (Exception e) {
            log(e);
        }
        return pipes;
    }

    public int getUnHasPointNum() {
        cache();
        List<Integer> nums = new ArrayList<>();
        for (PipeBean pipe : pipes) {
            nums.add(pipe.getPipe_id());
        }
        for (int i = 1; i <= 1000; i++) {
            if (!nums.contains(i)) {
                PipeBean pipe = new PipeBean();
                pipe.setPipe_id(i);
                pipe.setPipe_name(i + "#");
                save(pipe);
                return i;
            }
        }
        return -1;
    }

    private void cache() {
        if (pipes == null || pipes.size() == 0) {
            try {
                pipes = dao.findAll();
            } catch (Exception e) {
                pipes = new ArrayList<>();
                log(e);
            }
        }
    }
}

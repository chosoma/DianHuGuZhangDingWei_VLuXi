package com.thingtek.beanServiceDao.pipe.service;

import com.thingtek.beanServiceDao.base.BaseService;
import com.thingtek.beanServiceDao.pipe.dao.PipeDao;
import com.thingtek.beanServiceDao.pipe.entity.PipeBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Vector;

@Service
public class PipeService extends BaseService {
    @Resource
    private PipeDao dao;

    private List<PipeBean> pipes;

    public void savePipe(PipeBean... pipeBean) {
        try {
            if (dao.save(pipeBean)) {
                pipes.clear();
            }
        } catch (Exception e) {
            log(e);
        }

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

    public PipeBean getPipeByName(String pipe_name) {
        cache();
        for (PipeBean pipe : pipes) {
            if (Objects.equals(pipe_name, pipe.getPipe_name())) {
                return pipe;
            }
        }
        return null;
    }

    public Vector<String> getPipeNames() {
        cache();
        Vector<String> names = new Vector<>();
        for (PipeBean pipe : pipes) {
            names.add(pipe.getPipe_name());
        }
        return names;
    }

    public List<PipeBean> findAll() {
        cache();
        return pipes;
    }

    /*public int getPipePages() {
        int pages = 0;
        for (PipeBean pipe : pipes) {
            pages += pipe.getPipe_page();
        }
        return pages;
    }*/

    public int getUnHasPipeId() {
        cache();
        List<Integer> nums = new ArrayList<>();
        for (PipeBean pipe : pipes) {
            nums.add(pipe.getPipe_id());
        }
        for (int i = 1; i <= 1000; i++) {
            if (!nums.contains(i)) {
                PipeBean pipe = new PipeBean();
                pipe.setPipe_id(i);
                pipe.setPipe_name(i + "@");
                savePipe(pipe);
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

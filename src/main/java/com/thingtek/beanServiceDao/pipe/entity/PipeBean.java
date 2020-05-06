package com.thingtek.beanServiceDao.pipe.entity;

import lombok.Data;

import java.util.Vector;

@Data
public class PipeBean {
    private int pipe_id;
    private String pipe_name;
    private int pipe_page;

    public Vector<Object> getDataTotalCol() {
        Vector<Object> vector = new Vector<>();
        vector.add(pipe_id);
        vector.add(pipe_name);
        vector.add(pipe_page);
        return vector;
    }

}

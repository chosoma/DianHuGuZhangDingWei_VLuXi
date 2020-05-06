package com.thingtek.beanServiceDao.point.service;


import com.thingtek.beanServiceDao.base.service.BaseService;
import com.thingtek.beanServiceDao.clt.entity.CltBean;
import com.thingtek.beanServiceDao.clt.service.CltService;
import com.thingtek.beanServiceDao.point.dao.PointDao;
import com.thingtek.beanServiceDao.point.entity.PointBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Vector;

@Service
public class PointService extends BaseService {

    @Resource
    private PointDao dao;

    @Resource
    private CltService cltService;

    private List<PointBean> points;

    public int getUnHasPointNum(int clttype) {
        cache();
        List<Integer> nums = new ArrayList<>();
        for (PointBean point : points) {
            if (clttype == point.getClt_type()) {
                nums.add(point.getPoint_num());
            }
        }
        for (int i = 1; i <= 1000; i++) {
            if (!nums.contains(i)) {
                PointBean pointBean = new PointBean();
                pointBean.setPoint_num(i);
                pointBean.setClt_type(clttype);
                pointBean.setPoint_name(i+"#");
                savePoints(pointBean);
                return i;
            }
        }
        return -1;
    }

    public boolean savePoints(PointBean... points) {
        boolean flag = false;
        try {
            flag = dao.save(points);
        } catch (Exception e) {
            log(e);
        }
        if (flag) {
            this.points.clear();
        }
        return flag;
    }

    public PointBean getPointByNum(int clttype, int point_num) {
        cache();
        for (PointBean point : points) {
            if (clttype == point.getClt_type() && point.getPoint_num() == point_num) {
                return point;
            }
        }
        return null;
    }

    public PointBean getPointByName(int clttype, String point_name) {
        cache();
        for (PointBean point : points) {
            if (clttype == point.getClt_type() && Objects.equals(point.getPoint_name(), point_name)) {
                return point;
            }
        }
        return null;
    }

    public List<PointBean> getPoints(int clttype) {
        cache();
        List<PointBean> points = new ArrayList<>();
        for (PointBean point : this.points) {
            if (clttype == point.getClt_type()) {
                points.add(point);
            }
        }
        return points;
    }

    public Integer getPointNumByName(int clttype, String point_name) {
        for (PointBean point : points) {
            if (Objects.equals(point.getPoint_name(), point_name)) {
                return point.getPoint_num();
            }
        }
        return null;
    }


    public Vector<String> getPointNames(int clttype) {
        cache();
        Vector<String> vector = new Vector<>();
        for (PointBean point : points) {
            if (point.getClt_type() == clttype && !vector.contains(point.getPoint_name())) {
                vector.add(point.getPoint_name());
            }
        }
        return vector;
    }

    public boolean updatePoint(int clttype) {
        List<Boolean> flags = new ArrayList<>();
        try {
            for (PointBean point : points) {
                if (clttype == point.getClt_type()) {
                    flags.add(dao.update(point));
                }
            }
        } catch (Exception e) {
            log(e);
            flags.add(false);
        }
        return !flags.contains(false);
    }

    public void updatePointXY(PointBean pointBean){
        try {
            dao.update(pointBean);
        } catch (Exception e) {
            log(e);
        }
    }

    public boolean deleteByNums(int clttype, int... pointnums) {
        boolean flag;
        try {
            flag = dao.delete(clttype, pointnums);
        } catch (Exception e) {
            log(e);
            flag = false;
        }
        if (flag) {
            points.clear();
            cache();
        }
        return flag;
    }


    private void cache() {
        if (points == null || points.size() == 0) {
            try {
                points = dao.findAll();
                for (PointBean pointBean : points) {
                    pointBean.setClt(cltService.getCltByNum(pointBean.getClt_type()));
                }
            } catch (Exception e) {
                points = new ArrayList<>();
                log(e);
            }
        }
    }

}

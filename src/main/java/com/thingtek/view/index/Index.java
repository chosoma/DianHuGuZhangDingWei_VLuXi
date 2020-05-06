package com.thingtek.view.index;


import com.thingtek.socket.CollectServer;
import lombok.Data;
//import com.thingtek.util.JfreeChartUtil;
import com.thingtek.view.login.Loading;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public @Data
class Index {

    @Resource
    private Loading loading;

    @Resource
    private CollectServer server;

    public void init() {
//        JfreeChartUtil.setChartTheme();
        loading.init();
        new Thread(new Runnable() {
            @Override
            public void run() {
                server.openConnection();
            }
        }).start();
    }

}

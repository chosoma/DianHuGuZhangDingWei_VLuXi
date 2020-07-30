package com.thingtek.view.index;


import com.thingtek.socket.CollectServer;
import com.thingtek.socketank.AnkServer;
import com.thingtek.socketank.AnkSocket;
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
    @Resource
    private AnkServer ankServer;

    public void init() {
//        JfreeChartUtil.setChartTheme();
        loading.init();
        new Thread(() -> server.openConnection()).start();
        new Thread(() -> ankServer.openConnection()).start();
    }

}

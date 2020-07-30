package com.thingtek.socketank;

import com.thingtek.beanServiceDao.unit.service.LXUnitService;
import com.thingtek.config.PortConfig;

import com.thingtek.view.shell.debugs.Debugs;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.swing.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

@Component
public class AnkServer {
    private ServerSocket ss;
    private List<AnkSocket> listST;
    @Resource
    private LXUnitService unitService;

    private void createbuffer() {
        databuffer = new Hashtable<>();
    }

    private Map<String, Short> databuffer;

    public void setValue(short addr, short value) {
        int addrs = addr;
        databuffer.put(String.valueOf(addrs), value);
        addrs += 40000;
        databuffer.put(String.valueOf(addrs), value);
    }

    public short getValue(short addr) {
        int addrs = (addr & 0xffff) + 1;
        if (!databuffer.containsKey(String.valueOf(addrs)) && unitService.hasAddr((short) addrs)) {
            return 10;
        }
        String key = String.valueOf(addrs);
        return databuffer.containsKey(key) ? databuffer.get(String.valueOf(addrs)) : 0;
    }

    @Resource
    private PortConfig portConfig;
    @Resource
    private Debugs debugs;

    private AnkServer() {
        listST = new ArrayList<>();
    }

    public void openConnection() {
        int localPort = portConfig.getAnkport();
        try {
            ss = new ServerSocket(localPort);
        } catch (IOException e1) {
            JOptionPane.showMessageDialog(null, "程序已经启动");
            System.exit(0);
            e1.printStackTrace();
            return;
        }
        listST.clear();
        new Thread(() -> {
            try {
                while (true) {
                    Socket s = ss.accept();
                    AnkSocket socket = new AnkSocket(s);
                    System.out.println(s.getInetAddress().getHostAddress() + ":" + s.getPort());
                    socket.setDebugShow(debugs);
                    socket.setAnkServer(this);
                    Thread thread = new Thread(socket);
                    thread.start();
                }
            } catch (IOException e) {
                // e.printStackTrace();
            } finally {
                // 关闭serverSocket
                if (ss != null && !ss.isClosed()) {
                    try {
                        ss.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        createbuffer();
    }


}

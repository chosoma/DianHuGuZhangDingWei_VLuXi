package com.thingtek.socket;

import com.thingtek.beanServiceDao.unit.service.LXUnitService;
import com.thingtek.socket.agreement.SocketAgreement;
import com.thingtek.config.PortConfig;
import com.thingtek.view.shell.debugs.Debugs;
import com.thingtek.view.shell.systemSetup.systemSetupComptents.LXUnitSetPanel;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.swing.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


@Component
public class CollectServer {

    private ServerSocket ss;
    private List<CollectSocket> listST = new ArrayList<>();

    @Resource
    private PortConfig portConfig;
    @Resource
    private LXUnitService unitService;
    @Resource
    private LXUnitSetPanel unitSetPanel;

    private CollectServer() {

    }

    @Resource
    private Debugs debugs;
    @Resource
    private SocketAgreement agreement;

    /**
     * 启动服务
     */
    public void openConnection() {
        int localPort = portConfig.getPort();
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
                    CollectSocket socket = new CollectSocket(s);
                    System.out.println(s.getInetAddress().getHostAddress() + ":" + s.getPort());
                    socket.setAgreement(agreement);
                    socket.setDebugShow(debugs);
                    socket.setServer(CollectServer.this);
                    socket.setUnitService(unitService);
                    socket.setUnitSetPanel(unitSetPanel);
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
    }

    //
//    /**
//     * 关闭服务
//     */
    public void closeConnection() {
        try {
            if (ss != null) {
                ss.close();
            }
        } catch (IOException e1) {
            System.exit(0);
            e1.printStackTrace();
        }
        // 关闭socket
        for (int i = listST.size() - 1; i >= 0; i--) {
            CollectSocket st = listST.get(i);
            try {
                st.close();
            } catch (IOException e) {

                e.printStackTrace();
            }
        }
        listST.clear();
    }

    // 移除Socket
    synchronized void removeSocket(CollectSocket st) {
        listST.remove(st);
        checkUseful();
    }

    // 添加Socket，并移除关闭和未连接的
    synchronized void addSocket(CollectSocket st) {
        listST.add(st);
        checkUseful();
    }

    public CollectSocket getSocket(String ip, int port) {
        for (CollectSocket socket : listST) {
            if (socket.getIp().equals(ip) && socket.getPort() == port) {
                return socket;
            }
        }
        return null;
    }

    private void checkUseful() {
        for (int i = 0; i < listST.size(); i++) {
            CollectSocket s = listST.get(i);
            if (s.isClosed() || !s.isConnected()) {
                listST.remove(i);
                i--;
            }
        }
    }

    public boolean isNullLinked() {
        return listST.size() == 0;
    }

}

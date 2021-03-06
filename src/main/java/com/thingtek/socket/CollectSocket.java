package com.thingtek.socket;

import com.thingtek.beanServiceDao.base.BaseService;
import com.thingtek.beanServiceDao.unit.entity.LXUnitBean;
import com.thingtek.beanServiceDao.unit.service.LXUnitService;
import com.thingtek.socket.entity.BaseG2S;
import com.thingtek.socket.agreement.SocketAgreement;
import com.thingtek.socket.entity.G2SUploadData;
import com.thingtek.socket.entity.UnUnitNum;
import com.thingtek.view.shell.debugs.Debugs;
import com.thingtek.view.shell.systemSetup.LXUnitAdminSetPanel;
import com.thingtek.view.shell.systemSetup.LXUnitSetPanel;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.*;

public class CollectSocket extends BaseService implements Runnable {
    private Socket socket;
    private OutputStream out;// socket输出流
    private InputStream in;
    private SocketAgreement agreement;
    private String ip;
    private int port;

    private short unitnum;

    private LXUnitBean unit;
    private byte[] readcaches;
    private Debugs debugShow;
    private LXUnitService unitService;
    private LXUnitSetPanel unitSetPanel;
    private LXUnitAdminSetPanel unitSetAdminPanel;

    void setUnitSetAdminPanel(LXUnitAdminSetPanel unitSetAdminPanel) {
        this.unitSetAdminPanel = unitSetAdminPanel;
    }

    public Socket getSocket() {
        return socket;
    }

    void setAgreement(SocketAgreement agreement) {
        this.agreement = agreement;
    }

    void setUnitSetPanel(LXUnitSetPanel unitSetPanel) {
        this.unitSetPanel = unitSetPanel;
    }

    public void setUnitService(LXUnitService unitService) {
        this.unitService = unitService;
    }

    void setDebugShow(Debugs debugShow) {
        this.debugShow = debugShow;
    }

    private DataBuffer dataFactory;// 数据工厂
    private CollectServer server;

    void setServer(CollectServer server) {
        this.server = server;
        server.addSocket(this);
    }

    CollectSocket(Socket socket) {
        this.socket = socket;
        dataFactory = new DataBuffer();
        ip = socket.getInetAddress().getHostAddress();
        port = socket.getPort();
    }


    @Override
    public void run() {
        String offlineMSG = "";
        try {
            offlineMSG = "连接成功:";
            if (debugShow.isShow()) {
                debugShow.showMsg(offlineMSG + ip + ":" + port);
            }
            in = socket.getInputStream();
            out = socket.getOutputStream();

            unit = unitService.getUnitByIp(socket.getInetAddress().getHostAddress());
            byte[] b = new byte[1024 * 1024];
            int num;
            while ((num = in.read(b)) != -1) {
                Date time = Calendar.getInstance().getTime();
                if (debugShow.isShow()) {
                    debugShow.rec(b, num, time, " " + socket.getPort());
                }
                byte[] bytes = new byte[num];
                System.arraycopy(b, 0, bytes, 0, num);
                //如果没有缓存 缓存等于当前接收
                if (readcaches == null || readcaches.length == 0) {
                    readcaches = bytes;
                } else {
                    int length = readcaches.length;
                    readcaches = Arrays.copyOf(readcaches, length + num);
                    System.arraycopy(bytes, 0, readcaches, length, bytes.length);
                }
                switch (agreement.contains(readcaches)) {
                    case SocketAgreement.HasHeadHasTail:
                        //数据有头有尾 解析
                        resolveCache();
                        break;
                    case SocketAgreement.HasHeadNoTail:
                        //数据有头无尾 不解析
                        continue;
                    case SocketAgreement.NoHeadHasTail:
                        //数据无头有尾 丢弃
                    case SocketAgreement.NoHeadNoTail:
                        //数据无头无尾 丢弃
                        readcaches = null;
                        break;
                }
            }
            offlineMSG = "已下线,port: ";
        } catch (SocketException e) {
            log(e);
            offlineMSG = "连接已被关闭,port: ";
        } catch (IOException e) {
            log(e);
            offlineMSG = "接受数据超时,port: ";
        } finally {
            try {
                this.close();
            } catch (IOException e) {
                log(e);
            }
            if (unit != null) {
                unit.setConnect(false);
            }
            server.removeSocket(this);
            dataFactory.close();
            if (debugShow.isShow()) {
                debugShow.showMsg(offlineMSG + ip + ":" + port);
            }
        }
    }

    // 如果socket没有关闭
    public void close() throws IOException {
        if (!socket.isClosed()) {
            socket.close();
        }
    }

    private void resolveCache() {
        int startoff = agreement.getstartoff(readcaches);
        int endoff = agreement.getendoff(readcaches);
        byte[] bytes;
        if (endoff == -1 || startoff == -1) {
            return;
        }
        if (endoff > startoff) {
            //看似是正常的消息
            bytes = new byte[endoff - startoff + 1];
            System.arraycopy(readcaches, startoff, bytes, 0, bytes.length);
            byte[] other = new byte[readcaches.length - bytes.length];
            System.arraycopy(readcaches, bytes.length, other, 0, other.length);
            readcaches = other;
        } else {
            // 第一个头在第一个尾后
            if (readcaches.length - startoff == 0) {
                readcaches = null;
                return;
            }
            bytes = new byte[readcaches.length - startoff];
            System.arraycopy(readcaches, startoff, bytes, 0, bytes.length);
            readcaches = bytes;
        }
        bytes = Decrypt(bytes);
        if (!agreement.checkCRC16_X(bytes)) {
            return;
        }
        BaseG2S g2s = agreement.getG2S(bytes);
        g2s.resolve();
        if (!(g2s instanceof UnUnitNum)) {
            unitnum = g2s.getUnitnum();
            String ip = getIp();
            int port = getPort();
            unit = unitService.getUnitByNumber(unitnum);
            if (unit == null) {
                unit = new LXUnitBean();
                unit.setUnit_num(unitnum);
                unit.setPipe_id(1);
                unit.setPipe_page(1);
                unit.setIp(ip);
                unit.setPort(port);
                unitService.saveLXUnit(unit);
                unitSetPanel.refreshUnit();
            }
            if (!ip.equals(unit.getIp()) || port != unit.getPort()) {
                unit.setIp(ip);
                unit.setPort(port);
                unitService.updateLXUnit(unit);
            }
        }
        unit = unitService.getUnitByNumber(unitnum);
        reconnect();
        byte[] result = g2s.getResult();
        if (result != null && g2s.isCansend()) {
            try {
                sendMSG(result);
                if (g2s instanceof G2SUploadData) {
                    for (int i = 0; i < 4; i++) {
                        sendMSG(result);
                    }
                }
            } catch (IOException e) {
                readcaches = null;
                return;
            }
        }
        if (readcaches != null && readcaches.length >= agreement.getTotallength()) {
            resolveCache();
        }
    }

    private Timer timer;

    private void reconnect() {
        unit.setConnect(true);
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                unit.setConnect(false);
            }
        }, 120000);
    }

    boolean isNoUseful() {
        return socket.isClosed() && !socket.isConnected();
    }

    // 发送指令
    public void sendMSG(byte[] msg) throws IOException {
        /*if (readflag) {
            return;
        }*/
        try {
            msg = Encryption(msg);
            out.write(msg);
            out.flush();
            if (debugShow.isShow()) {
                debugShow.send(msg, msg.length, "");
            }
        } catch (IOException e) {
            try {
                this.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            throw new IOException(" 发送命令时连接中断");
        }
    }

    String getIp() {
        return ip;
    }

    int getPort() {
        return port;
    }

    /**
     * 逆转义
     */
    private byte[] Decrypt(byte[] source) {
        List<Byte> btlist = new ArrayList<>();
        for (int i = 1; i < source.length - 1; i++) {
            if (source[i] == Protocol.TURN) {
                switch (source[i + 1]) {
                    case (Protocol.HEADT): {
                        btlist.add(Protocol.HEAD);
                        i++;
                        break;
                    }
                    case (Protocol.TAILT): {
                        btlist.add(Protocol.TAIL);
                        i++;
                        break;
                    }
                    case (Protocol.TURNT): {
                        btlist.add(Protocol.TURN);
                        i++;
                        break;
                    }
                    default: {
                        btlist.add(Protocol.TURN);
                    }

                }

            } else {
                btlist.add(source[i]);
            }
        }
        byte[] ret = new byte[btlist.size()];
        int i = 0;
        for (byte data : btlist) {
            ret[i] = data;
            i++;
        }
        return ret;
    }

    /**
     * 转义
     */
    private byte[] Encryption(byte[] source) {
        List<Byte> btlist = new ArrayList<>();
//        btlist.add(Protocol.TAIL);// 头
        btlist.add(Protocol.HEAD);// 头
        for (byte aSource : source) {
            switch (aSource) {
                case (Protocol.HEAD): {
                    btlist.add(Protocol.TURN);
                    btlist.add(Protocol.HEADT);
                    break;
                }
                case (Protocol.TAIL): {
                    btlist.add(Protocol.TURN);
                    btlist.add(Protocol.TAILT);
                    break;
                }
                case (Protocol.TURN): {
                    btlist.add(Protocol.TURN);
                    btlist.add(Protocol.TURNT);
                    break;
                }
               /* case (Protocol.IST1): {
                    btlist.add(Protocol.TURN);
                    btlist.add(Protocol.IST1T);
                    break;
                }
                case (Protocol.IST2): {
                    btlist.add(Protocol.TURN);
                    btlist.add(Protocol.IST2T);
                    break;
                }*/
                default: {
                    btlist.add(aSource);
                }
            }
        }
//        btlist.add(Protocol.HEAD);// 尾
        btlist.add(Protocol.TAIL);
        byte[] ret = new byte[btlist.size()];
        int i = 0;
        for (byte data : btlist) {
            ret[i] = data;
            i++;
        }
        return ret;
    }
}
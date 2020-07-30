package com.thingtek.socketank;

import com.thingtek.beanServiceDao.base.BaseService;
import com.thingtek.view.shell.debugs.Debugs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Calendar;
import java.util.Date;

public class AnkSocket extends BaseService implements Runnable {
    private Socket socket;
    private InputStream in;
    private OutputStream out;
    private Debugs debugShow;
    private AnkServer ankServer;

    private String ip;
    private int port;

    public void setAnkServer(AnkServer ankServer) {
        this.ankServer = ankServer;
    }

    public void setDebugShow(Debugs debugShow) {
        this.debugShow = debugShow;
    }

    public AnkSocket(Socket s) {
        this.socket = s;
        try {
            in = s.getInputStream();
            out = s.getOutputStream();
            ip = socket.getInetAddress().getHostAddress();
            port = socket.getPort();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            byte[] b = new byte[1024 * 1024];
            int num;
            byte c = (byte) 256;
            while ((num = in.read(b)) != -1) {
//                c++;
                try {
                    Date time = Calendar.getInstance().getTime();
                    if (debugShow.isShow()) {
                        debugShow.rec(b, num, time, " " + socket.getPort());
                    }
                    byte[] bytes = new byte[num];
                    System.arraycopy(b, 0, bytes, 0, num);
                    switch (bytes[5]) {
                        case 6:
                            int count = bytes2int(10, 2, bytes);
                            byte[] back = new byte[9 + count * 2];
                            System.arraycopy(bytes, 0, back, 0, 8);
                            back[5] = 7;//order
                            back[8] = (byte) (count * 2);
                            short addr = (short) ((bytes[8] & 0xff) << 8 | bytes[9] & 0xff);//sddr
                            for (int i = 9; i < 9 + count * 2; i += 2, addr++) {
                                short value = ankServer.getValue(addr);
                                back[i + 1] = (byte) value;
                                back[i] = (byte) (value >> 8);
                            }
                        /*for (int i = 9; i < back.length; i += 2, addr++) {
                            back[i] = 1;
                            back[i + 1] = 0;
                        }*/
//                        System.out.println(Arrays.toString(back));
                            if (debugShow.isShow()) {
                                debugShow.send(back, back.length, " " + socket.getPort());
                            }
                            out.write(back);
                            break;
                    }
                } catch (Exception e) {
                    log(e);
                }
            }
            offlineMSG = "已下线,port: ";
        } catch (
                SocketException e) {
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
            if (debugShow.isShow()) {
                debugShow.showMsg(offlineMSG + ip + ":" + port);
            }
        }
    }

    public void close() throws IOException {
        if (!socket.isClosed()) {
            socket.close();
        }
    }

    protected int bytes2int(int off, int length, byte[] bytes) {
        int i = 0;
        for (int j = off + length, k = 0; j > off; j--, k++) {
            i |= (bytes[j - 1] & 0xff) << (k * 8);
        }
        return i;
    }
}

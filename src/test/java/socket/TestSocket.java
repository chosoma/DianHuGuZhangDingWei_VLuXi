package socket;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class TestSocket {

    public static void main(String[] args) {
        for (int i = 0; i < 2000; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Socket socket = new Socket("192.168.1.250", 1024);
                        OutputStream out = socket.getOutputStream();
//                        Thread.sleep(10000);
                        out.write(new byte[1000]);
                        Thread.sleep(100000);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }


    }

}

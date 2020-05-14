package socket;

public class TestUploadData {

    private int[] data;

    public TestUploadData() {
        this.data = new int[60000];
    }

    private int cal_serv_crc(byte[] message, int len) {
        int crc = 0x00;
        int polynomial = 0x1021;
        for (int index = 0; index < len; index++) {
            byte b = message[index];
            for (int i = 0; i < 8; i++) {
                boolean bit = ((b >> (7 - i) & 1) == 1);
                boolean c15 = ((crc >> 15 & 1) == 1);
                crc <<= 1;
                if (c15 ^ bit)
                    crc ^= polynomial;
            }
        }
        crc &= 0xffff;
        return crc;
    }


    public boolean checkCRC16_X(byte[] bytes) {
        int crc16 = cal_serv_crc(bytes, bytes.length - 2);
//        System.out.println("校验：" + Integer.toHexString(crc16));
        return (bytes[bytes.length - 1] == (byte) (crc16 & 0xFF))
                && (bytes[bytes.length - 2] == (byte) (crc16 >> 8 & 0xFF));
    }
}

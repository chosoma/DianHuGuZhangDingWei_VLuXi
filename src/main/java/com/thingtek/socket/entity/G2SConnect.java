package com.thingtek.socket.entity;

public class G2SConnect extends BaseG2S {
    @Override
    public void resolve() {
        super.resolve();
        BaseS2G s2g = agreementConfig.getS2G("connect");
        s2g.setUnitnum(unitnum);
        result = s2g.getResult();
        cansend = true;
    }

    private byte[] result;

    @Override
    public byte[] getResult() {
        return result;
    }
}

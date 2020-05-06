package com.thingtek.socket.data.entity;

import java.util.Date;

public class RawData {
	private byte[] data;
	private Date time;

	public RawData(byte[] data, Date time) {
		this.data = data;
		this.time = time;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

}

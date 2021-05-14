package com.example.demo.VO;
import java.sql.Timestamp;


public class MsgVO {
	private Integer msg_ID;
	private String from;
	private String to;
	private String message;
	private Integer status;
	private Timestamp msg_time;
		
	
	
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public Integer getMsg_ID() {
		return msg_ID;
	}
	public void setMsg_ID(Integer msg_ID) {
		this.msg_ID = msg_ID;
	}
	
	public Timestamp getMsg_time() {
		return msg_time;
	}
	
	public void setMsg_time(Timestamp msg_time) {
		this.msg_time = msg_time;
	}
	
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	
	
}

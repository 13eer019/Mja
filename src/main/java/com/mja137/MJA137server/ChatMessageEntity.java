package com.mja137.MJA137server;

import javax.persistence.*;

@Entity
public class ChatMessageEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;
	private String dateTime;
	private String empId;
	private String name;
	@Column(length = 100000)
	private String message;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	public String getEmpId() {
		return empId;
	}
	public void setEmpId(String empId) {
		this.empId = empId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public ChatMessageEntity(String id, String dateTime, String empId, String name, String message) {
		super();
		this.id = id;
		this.dateTime = dateTime;
		this.empId = empId;
		this.name = name;
		this.message = message;
	}
	public ChatMessageEntity() {}
}

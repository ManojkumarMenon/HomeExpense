package com.manoj.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="userpaymentdetails")
public class UserPaymentDetails {

	@Id
	@Column(name="id")
	private int id;
	@Column(name="userreceiver")
	private int userreceiver;
	@Column(name="userpayer")
	private int userpayer;
	@Column(name="amount")
	private float amount;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserreceiver() {
		return userreceiver;
	}
	public void setUserreceiver(int userreceiver) {
		this.userreceiver = userreceiver;
	}
	public int getUserpayer() {
		return userpayer;
	}
	public void setUserpayer(int userpayer) {
		this.userpayer = userpayer;
	}
	public float getAmount() {
		return amount;
	}
	public void setAmount(float amount) {
		this.amount = amount;
	}
	
	
	
}

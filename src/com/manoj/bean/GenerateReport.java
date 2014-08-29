package com.manoj.bean;

import java.util.Iterator;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

import com.manoj.service.BusinessService;

@ManagedBean(name="generateReport")
@SessionScoped
public class GenerateReport {
	
	private String username;
	private String itemname;
	private int price;
	
	private String userReceiver;
	private String userPayer;
	private float amount;
	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getItemname() {
		return itemname;
	}


	public void setItemname(String itemname) {
		this.itemname = itemname;
	}


	public int getPrice() {
		return price;
	}


	public void setPrice(int price) {
		this.price = price;
	}


	public String getUserReceiver() {
		return userReceiver;
	}


	public void setUserReceiver(String userReceiver) {
		this.userReceiver = userReceiver;
	}


	public String getUserPayer() {
		return userPayer;
	}


	public void setUserPayer(String userPayer) {
		this.userPayer = userPayer;
	}


	public float getAmount() {
		return amount;
	}


	public void setAmount(float amount) {
		this.amount = amount;
	}

	
}

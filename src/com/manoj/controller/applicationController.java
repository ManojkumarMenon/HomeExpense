package com.manoj.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

import com.manoj.bean.AddItems;
import com.manoj.bean.GenerateReport;
import com.manoj.bean.LoginBean;
import com.manoj.entity.Username;
import com.manoj.service.BusinessService;



@ManagedBean(name="applicationController")
@SessionScoped
public class applicationController {
	private DataModel<SelectItem> dataModel;
	private int totalAmt;
	private float individualAmt;
	
	public DataModel<SelectItem> getDataModel() {
		return dataModel;
	}

	public void setDataModel(DataModel<SelectItem> dataModel) {
		this.dataModel = dataModel;
	}

	public int getTotalAmt() {
		return totalAmt;
	}

	public void setTotalAmt(int totalAmt) {
		this.totalAmt = totalAmt;
	}

	public float getIndividualAmt() {
		return individualAmt;
	}

	public void setIndividualAmt(float individualAmt) {
		this.individualAmt = individualAmt;
	}

	public void submit(){
		try{
		
		LoginBean loginBean=(LoginBean)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("loginBean");
		BusinessService busService =new BusinessService();
		if(busService.validateUser(loginBean)){
			new applicationController().homepage();
		}
		else{
			FacesContext.getCurrentInstance().getExternalContext().redirect("LoginError.xhtml");
		}
		
		}
		catch(Exception e){
			
		}
	}
	
	public void homepage(){
		try{
			FacesContext.getCurrentInstance().getExternalContext().redirect("Home.xhtml");
		}
		catch(Exception e){
			
		}
	}
	public void addItemsPage(){
		try{
			System.out.println("in add item function");
		FacesContext.getCurrentInstance().getExternalContext().redirect("AddItem.xhtml");
		}
		catch(Exception e ){
			
		}
	}
	
	public List<SelectItem> users(){
		
		
	      List<Username> lst =  new BusinessService().getUsers();
	          
	      List<SelectItem> itens = new ArrayList<SelectItem>(lst.size());
	      
	      for (Username user : lst) {  
	         itens.add(new SelectItem(user.getUserId(), user.getUsername() ));  
	      }  
	      
	      return itens;  
	}
	
	
	public void addItems(){
		System.out.println("in additems function");
		AddItems addItems=(AddItems)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("addItems");
		System.out.println(addItems.getUserid()+"   "+ addItems.getItemName()+"  "+ addItems.getItemPrice());
		new BusinessService().addItems(addItems);
		new applicationController().addItemsPage();
	}
	
	public void viewBalancePage(){
		try{
		FacesContext.getCurrentInstance().getExternalContext().redirect("ViewBalance.xhtml");
		}
		catch(Exception e ){
			
		}
	}
	
	public DataModel<SelectItem> generateTable(){
		totalAmt=0;
		dataModel=null;
		LoginBean loginBean =(LoginBean)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("loginBean");
		List lst=new BusinessService().generateReport(loginBean);
		Iterator iterator = lst.iterator();
		dataModel =new ListDataModel<SelectItem>(lst);
		//System.out.println("hello"+dataModel.getRowCount());
		while(iterator.hasNext()){
			GenerateReport report=new GenerateReport();
			report =(GenerateReport) iterator.next();
			totalAmt=totalAmt+report.getPrice();
		}
		
		individualAmt =totalAmt/(new applicationController().users().size());
//		new BusinessService().userPaymentDetailsTable(individualAmt);
		return dataModel;
	}

	
	public DataModel<SelectItem> userPaymentDetailsTable(){
		dataModel=null;
		List lst=new BusinessService().userPaymentDetailsTable(individualAmt);
		dataModel=new ListDataModel<SelectItem>(lst);
		return dataModel;
	}
}

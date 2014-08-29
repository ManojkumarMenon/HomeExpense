package com.manoj.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.manoj.bean.AddItems;
import com.manoj.bean.GenerateReport;
import com.manoj.bean.LoginBean;
import com.manoj.dao.DAOService;
import com.manoj.entity.ItemTable;
import com.manoj.entity.UserPaymentDetails;
import com.manoj.entity.Username;

public class BusinessService {

	public boolean validateUser(LoginBean loginBean) {
		System.out.println("in business service class");
		boolean flag = false;
		Username username = new Username();
		username.setUsername(loginBean.getUsername());
		username.setPassword(loginBean.getPassword());
		DAOService daoService = new DAOService();
		return daoService.validateUser(username);

	}

	public List<Username> getUsers() {
		DAOService daoService = new DAOService();
		List<Username> userLst = daoService.getUsers();
		return userLst;
	}

	public void addItems(AddItems addItems) {
		ItemTable itemTable = new ItemTable();
		itemTable.setUserid(addItems.getUserid());
		itemTable.setItemname(addItems.getItemName());
		itemTable.setPrice(addItems.getItemPrice());
		itemTable.setEntrydate(addItems.getEntryDate());
		DAOService daoService = new DAOService();
		daoService.addItems(itemTable);

	}

	public List generateReport(LoginBean loginBean) {
		DAOService daoService = new DAOService();
		return daoService.generateReport();
	}

	

	public  List userPaymentDetailsTable(float individualAmt) {
		List userpaymentdetailslst=new ArrayList();
		List templst1 = new ArrayList();
		List templst2 = new ArrayList();
		
		DAOService daoService = new DAOService();
		List userPaymentLst = daoService.userPaymentAmount();
		List userLst = daoService.getUsers();
		Iterator userPaymentIterator1 = userPaymentLst.iterator();
		Iterator userIterator = userLst.iterator();

		while (userPaymentIterator1.hasNext()) {
			Object[] userPaymentObj = (Object[]) userPaymentIterator1.next();
			int user = (Integer) userPaymentObj[0];
			int sumPrice = (Integer) userPaymentObj[1];

			if (sumPrice > individualAmt) {
				Object[] userObjects = { user, sumPrice, true };
				templst1.add(userObjects);
			} else {
				Object[] userObjects = { user, sumPrice, false };
				templst2.add(userObjects);
			}
		}
		
		while(userIterator.hasNext()){
			boolean flag=true;
			 userPaymentIterator1 = userPaymentLst.iterator();
			Username userObj =(Username)userIterator.next();
			int user = (Integer) userObj.getUserId();
			while (userPaymentIterator1.hasNext()) {
				Object[] userPaymentObj = (Object[]) userPaymentIterator1.next();
				int userPayment = (Integer) userPaymentObj[0];
				if(userPayment==user){
					flag=false;
					break;
				}
			}
			if(flag){
				Object[] userObjects = { user, 0, false };
				templst2.add(userObjects);
			}
		}
		
		
		userPaymentIterator1=templst1.iterator();
		
		while(userPaymentIterator1.hasNext()){
			Object[] receiverobj= (Object[])userPaymentIterator1.next();
			List temporary=new ArrayList();temporary.addAll(templst2);
			Iterator userPaymentIterator2 = temporary.iterator();
			//int amtvault=(int)individualAmt;
			int reveiverobjamt=(Integer)receiverobj[1]-(int)individualAmt;
			while(userPaymentIterator2.hasNext()){
				UserPaymentDetails userpaymentdetails =new UserPaymentDetails();
				Object[] payerobj =(Object[])userPaymentIterator2.next();
				int payerobjamt=(int)individualAmt-(Integer)payerobj[1];
				 //reveiverobjamt=(Integer)receiverobj[1]-amtvault;
				if(reveiverobjamt>=payerobjamt){
					if(payerobjamt>0){
						userpaymentdetails.setUserreceiver((Integer)receiverobj[0]);
						userpaymentdetails.setUserpayer((Integer)payerobj[0]);
						userpaymentdetails.setAmount(payerobjamt);
						receiverobj[1]=reveiverobjamt-payerobjamt;
						int index=templst2.indexOf(payerobj);
					templst2.remove(index);
					Object[] payerobj1={payerobj[0], 0,payerobj[2]};
					templst2.add(index, payerobj);
					userpaymentdetailslst.add(userpaymentdetails);
					reveiverobjamt=reveiverobjamt-payerobjamt;
					}
				}
				else {
					
					int index=templst2.indexOf(payerobj);
					templst2.remove(index);
					Object[] payerobj1={payerobj[0],reveiverobjamt - payerobjamt ,payerobj[2]};
					templst2.add(index, payerobj);
					userpaymentdetails.setUserreceiver((Integer)receiverobj[0]);
					userpaymentdetails.setUserpayer((Integer)payerobj[0]);
					userpaymentdetails.setAmount(reveiverobjamt);
					userpaymentdetailslst.add(userpaymentdetails);
					reveiverobjamt=0;
					break;
				}
				
			}
		}
		
		daoService.userPaymentDetailsTable(userpaymentdetailslst);
		
		List userPaymentDataTable = new ArrayList();
		userPaymentIterator1 = userpaymentdetailslst.iterator();
		
		while(userPaymentIterator1.hasNext()){
			userIterator = userLst.iterator();
			UserPaymentDetails user=(UserPaymentDetails)userPaymentIterator1.next();
			GenerateReport generateReport=new GenerateReport();
			generateReport.setAmount(user.getAmount());
			while(userIterator.hasNext()){
				Username username =(Username)userIterator.next();
				if(user.getUserpayer()==username.getUserId()){
					generateReport.setUserPayer(username.getUsername());
					}
			}
			userIterator = userLst.iterator();
			while(userIterator.hasNext()){
				Username username =(Username)userIterator.next();
				if(user.getUserreceiver()==username.getUserId()){
					generateReport.setUserReceiver(username.getUsername());
					}
			}
			userPaymentDataTable.add(generateReport);
		}
		
		
		return userPaymentDataTable;

	}

}

package com.manoj.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

//import javassist.bytecode.Descriptor.Iterator;

import javax.faces.model.SelectItem;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.manoj.bean.GenerateReport;
import com.manoj.bean.LoginBean;
import com.manoj.entity.HibernateUtil;
import com.manoj.entity.ItemTable;
import com.manoj.entity.UserPaymentDetails;
import com.manoj.entity.Username;

public class DAOService {
	public boolean validateUser(Username username){
		System.out.println("in dao class");
		boolean flag =false;
		Session session =HibernateUtil.getSessionFactory().openSession();
		String qr="select * from usertable where username ='"+username.getUsername()+"' and password='"+username.getPassword()+"'";
		SQLQuery query =session.createSQLQuery(qr);
		query.addEntity(Username.class);
		if(query.list().size()!=0){
			session.close();
			return flag=true;
		}
		else{
			session.close();	
			return flag;
		}

	}
	
	public List<Username> getUsers(){
		//List<Username> userLst = new ArrayList<Username>();
		Session session =HibernateUtil.getSessionFactory().openSession();
		String query ="Select * from usertable";
		SQLQuery sqlQuery = session.createSQLQuery(query);
		sqlQuery.addEntity(Username.class);
		return sqlQuery.list();
		//return userMap;
	}
	
	public void addItems(ItemTable itemTable){
		Session session =HibernateUtil.getSessionFactory().openSession();
		Transaction transaction =session.getTransaction();
		transaction.begin();
		session.persist(itemTable);
		transaction.commit();
		session.close();
	}
	
	public List<SelectItem> generateReport(){
		List retLst =new ArrayList();
		Session session = HibernateUtil.getSessionFactory().openSession();
		String query =" select u.username as username, i.itemname as itemname , i.price as price from itemtable i , usertable u " +
				      " where u.userid = i.userid order by u.username ";
		SQLQuery sqlQuery =session.createSQLQuery(query);
		sqlQuery.addScalar("username", Hibernate.STRING);
		sqlQuery.addScalar("itemname", Hibernate.STRING);
		sqlQuery.addScalar("price", Hibernate.INTEGER);
		List lst = sqlQuery.list();
		java.util.Iterator iterator =lst.iterator();
		while(iterator.hasNext()){
			Object[] obj =(Object[])iterator.next();
			GenerateReport report =new GenerateReport();
			report.setUsername((String)obj[0]);
			report.setItemname((String)obj[1]);
			report.setPrice(((Integer)obj[2]));
			retLst.add(report);
		}
		session.close();
		return retLst;
	}
	
	public List userPaymentAmount(){
		Session session= HibernateUtil.getSessionFactory().openSession();
		String query="select u.userid as userid, sum( i.price) as total from usertable u, itemtable i where i.userid= u.userid group by u.userid";
		SQLQuery sqlQuery =session.createSQLQuery(query);
		sqlQuery.addScalar("userid", Hibernate.INTEGER);
		sqlQuery.addScalar("total", Hibernate.INTEGER);
		return sqlQuery.list();
		//session.close();
	}
	
	public List userPaymentDetailsTable(List userpaymentdetailslst){
		insertUserPaymentDetailsTable(userpaymentdetailslst);
		/*Session session= HibernateUtil.getSessionFactory().openSession();
		String query="Select u.usernme as receiver , username as payer , amount as amount " +
					 " from usertable , userpaymentdetails where ";
		SQLQuery sqlQuery =session.createSQLQuery(query);
		sqlQuery.addScalar("userid", Hibernate.INTEGER);
		sqlQuery.addScalar("total", Hibernate.INTEGER);
		return sqlQuery.list();*/
		//List lst=new DAOService().getUsers();
		
		return userpaymentdetailslst;
	}
	public void insertUserPaymentDetailsTable(List userpaymentdetailslst){
		
		Iterator iterator=userpaymentdetailslst.iterator();
		while(iterator.hasNext()){
			Session session= HibernateUtil.getSessionFactory().openSession();
			Transaction transaction=session.beginTransaction();
			
			transaction.begin();
			UserPaymentDetails userPaymentDetails =(UserPaymentDetails)iterator.next();
			
			session.save(userPaymentDetails);
			transaction.commit();session.flush();
			//userPaymentDetails=null;
			session.close();
		}
		
		


	}
}

package com.syml.hibernate.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Test;

import com.syml.hibernate.dao.IPostGresDaoService;
import com.syml.hibernate.dao.factory.DAOFactory;
import com.syml.hibernate.utils.HibernateUtils;

import controllers.Applicant;
import controllers.Opportunity;

public class PostgresDAOTestCase {
	IPostGresDaoService dao = DAOFactory.getInstance();

	@Test
public void createApplicant(){
	
	Opportunity opportunity=new Opportunity();
	List<Applicant> applicants=new ArrayList<Applicant>();
	Applicant applicant = new Applicant();
	applicant.setApplicant_name("xyz4");
	applicant.setApplicant_last_name("x4");
	applicant.setEmail_personal("akhandalmani.malik@bizruntime.com");
	applicant.setCell("987654321");
	applicant.setRelationship_status("Divorced");
	applicants.add(applicant);
	applicant = new Applicant();
	applicant.setApplicant_name("xyz44");
	applicant.setApplicant_last_name("x44");
	applicant.setEmail_personal("akhandalmani.malik@bizruntime.com");
	applicant.setCell("123456789");
	applicant.setRelationship_status("Single");
	applicants.add(applicant);
	opportunity.setApplicants(applicants);
	
	opportunity.setId(2);
	opportunity.setWhat_is_your_lending_goal("1");
	//opportunity = dao.createApplicant(opportunity);
	Assert.assertNotNull("Expected not null value.",findOpportunityById(opportunity.getId()));
	/*try{
		List<Applicant> appll=opportunity.getApplicants();
		for(Applicant ap:appll){
			System.out.println("applicants created all ids"+ap.getId());
		}
	}catch(Exception e){
		Logger.error("Error from main in test ",e);
	}*/
	
}
	private Opportunity findOpportunityById(int id){
		SessionFactory factory = HibernateUtils.getSessionFactory();
		Session session = factory.openSession();
		Object o = session.get(Opportunity.class, id);
		Opportunity proxyOpportunity = (Opportunity) o;
	return proxyOpportunity;
}
}

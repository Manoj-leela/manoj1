package com.syml.purchaseProposal.stagelead;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.LoggerFactory;

import com.syml.purchaseProposal.stagelead.PostgressConnection;

public class StageLead {
	private static org.slf4j.Logger logger = LoggerFactory.getLogger(StageLead.class);
	private Connection conn;

	String result = null;
	PostgressConnection postgres = null;

	public String getcrmLeadFrompostgress(String opportunityId) throws SQLException, IOException {
		logger.info("getcrmLeadFrompostgress" + opportunityId);
		postgres= new PostgressConnection();
		conn = postgres.getPostGressConnection();

		try {
			Statement stmt11 = conn.createStatement();
			ResultSet rs11 = stmt11
					.executeQuery("select row_to_json(crm_lead) as crm_lead from crm_lead where id =" + opportunityId);
			// log.debug("********hr_applicant_backup_tbl Data
			// IS*****"+rs11.getString("hr_applicant_backup_tbl").toString());
			while (rs11.next()) {
				result = rs11.getString("crm_lead");
				logger.info(rs11.getString("crm_lead"));
			}
			rs11.close();
		} catch (SQLException e) {

			closeDbConnection();
		}

		return result;
	}

	public void closeDbConnection() {
		try {
			conn.close();
		} catch (SQLException e) {
logger.error("sql Exception : "+e.getMessage());
		}
	}

	public static void main(String[] args) {/*
	StageLead lead =new StageLead();
	String  leadValue="";
	try {
		leadValue =lead.getcrmLeadFrompostgress("3584");
		System.out.println("sdsd"+leadValue);
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		
		
	*/}
}

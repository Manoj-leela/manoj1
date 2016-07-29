package com.syml.proposalRefinance.stagelead;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.LoggerFactory;

import com.syml.proposalRefinance.stagelead.PostgressConnection;

public class StageLead {
	private static org.slf4j.Logger logger = LoggerFactory.getLogger(StageLead.class);
	private Connection conn;

	String result = null;
	PostgressConnection postgres = null;

	public String getcrmLeadFrompostgress(String opportunityId) throws SQLException, IOException {
		logger.info("getcrmLeadFrompostgress" + opportunityId);
		postgres = new PostgressConnection();
		conn = postgres.getPostGressConnection();

		try {
			Statement stmt11 = conn.createStatement();
			ResultSet rs11 = stmt11
					.executeQuery("select row_to_json(crm_lead) as crm_lead from crm_lead where id =" + opportunityId);
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
			logger.error("sql Exception : " + e.getMessage());
		}
	}

}

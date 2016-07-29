package com.syml.proposalRefinance.stagelead;

import java.io.IOException;
import java.io.InputStream;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.util.Properties;

import org.slf4j.LoggerFactory;

public class PostgressConnection {
	private static org.slf4j.Logger logger = LoggerFactory.getLogger(StageLead.class);

	public Connection getPostGressConnection() throws SQLException, IOException {
		logger.debug("inside the getPostGressConnection() : ");

		Connection con1 = null;

		try {
			Class.forName("org.postgresql.Driver");

			Properties prop = new Properties();

			InputStream inputStream = PostgressConnection.class.getClassLoader()
					.getResourceAsStream("underwrite.properties");

			prop.load(inputStream);

			String url = prop.getProperty("postgresURL");
			String userName = prop.getProperty("postgresUserName");
			String userPassword = prop.getProperty("postgresPassword");

			try {

				con1 = DriverManager.getConnection(url, userName, userPassword);

				logger.debug("connection done " + con1);

			} catch (Exception e) {
				try {
					url = prop.getProperty("postgresURL1");
					userName = prop.getProperty("postgresUserName1");
					userPassword = prop.getProperty("postgresPassword1");
					con1 = DriverManager.getConnection(url, userName, userPassword);
				} catch (SQLException e1) {
					logger.error("" + e1.getMessage());

				}
			}

		} catch (ClassNotFoundException e) {
			logger.error("Sqle " + e.getMessage());
		}
		logger.info("Connenction Done: " + con1);
		return con1;
	}

}

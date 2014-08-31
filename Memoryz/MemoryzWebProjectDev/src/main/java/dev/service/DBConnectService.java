package dev.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.stereotype.Service;

@Service("DBConnectService")
public class DBConnectService {

	private Connection conn = null;
	private Statement statement = null;

	public void connectDB() {
		String url ="jdbc:mysql://localhost:3306/memoryzdev";
		String user = "root";
		String password = "Syakure1192";
		try{
			conn = DriverManager.getConnection(url, user, password);
			statement = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean registerData(String userName, String password) throws Exception {
		if (statement == null) {
			connectDB();
		}
		String sql = "INSERT INTO `memoryzdev`.`user_data` (`user_name`, `password`) VALUES ('" + userName + "', '" + password + "')";
		boolean registFlag = false;
		try {
			statement.execute(sql);
			registFlag = true;
		} catch (SQLException e) {
			e.printStackTrace();
			registFlag = false;
		} finally {
			close();
		}
		return registFlag;
	}

	public void close() {

		try {
			statement.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}

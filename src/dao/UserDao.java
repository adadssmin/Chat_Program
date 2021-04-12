package dao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dbutil.MyConnectionProvider;

public class UserDao {
	
	public int addUser(String id, String password) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String query = "INSERT INTO user (id, password) VALUE (?, ?);";
		try {
			conn = MyConnectionProvider.getConnection();
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, id);
			pstmt.setString(2, password);
			int result = pstmt.executeUpdate();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			MyConnectionProvider.closeConnection(conn);
			MyConnectionProvider.closeStmt(pstmt);
		}
		return -1;
	}
	
	public int updateUser(String id, String phone, String address) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String query = "UPDATE customer" 
				+ " SET phone = ?, address = ?" 
				+ " WHERE id = ?;";
		try {
			conn = MyConnectionProvider.getConnection();
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, id);
			pstmt.setString(2, phone);
			pstmt.setString(3, address);
			int result = pstmt.executeUpdate();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			MyConnectionProvider.closeConnection(conn);
			MyConnectionProvider.closeStmt(pstmt);
		}
		return -1;
	}
}

package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import data.UserData;
import dbutil.MyConnectionProvider;

public class UserDao {
	Connection conn;
	PreparedStatement pstmt;

	public void addUser(String id, String password, String email) {
		conn = null;
		pstmt = null;
		String query = "INSERT INTO user (id, password, email)" 
					+ " VALUE (?,?,?)";
		try {
			conn = MyConnectionProvider.getConnection();
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, id);
			pstmt.setString(2, password);
			pstmt.setString(3, email);
			int result = pstmt.executeUpdate();
			System.out.println("추가된 행" + result);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			MyConnectionProvider.closeStmt(pstmt);
			MyConnectionProvider.closeConnection(conn);
		}
	}

	public static List<UserData> getAllUser() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<UserData> list = new ArrayList<>();
		String query = "SELECT * FROM user";
		try {
			conn = MyConnectionProvider.getConnection();
			pstmt = conn.prepareStatement(query);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				String id = rs.getString("id");
				String password = rs.getString("password");
				String email = rs.getString("email");
				list.add(new UserData(id, password, email));
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			MyConnectionProvider.closeRS(rs);
			MyConnectionProvider.closeStmt(pstmt);
			MyConnectionProvider.closeConnection(conn);
		}
		return list;
	}

	public UserData getUserById(String id) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String query = "SELECT * FROM user WHERE id = ?";
		try {
			conn = MyConnectionProvider.getConnection();
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				String bid = rs.getString("id");
				String password = rs.getString("password");
				String email = rs.getString("email");

//				System.out.printf("%d: %s, %d\n", bid, title, price);
				return new UserData(bid, password, email);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			MyConnectionProvider.closeRS(rs);
			MyConnectionProvider.closeStmt(pstmt);
			MyConnectionProvider.closeConnection(conn);
		}
		return null;
	}

	public static boolean findExistId(String id) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String query = "SELECT count(*) cnt FROM user WHERE id = ?";
		try {
			conn = MyConnectionProvider.getConnection();
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				int cnt = rs.getInt("cnt");
				if (cnt > 0) {
					return true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			MyConnectionProvider.closeRS(rs);
			MyConnectionProvider.closeStmt(pstmt);
			MyConnectionProvider.closeConnection(conn);
		}
		return false;
	}

	public static boolean loginUser(String id, String pw) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String query = "SELECT COUNT(*) AS 'CNT' FROM user" 
					+ " WHERE id = ? AND password = ?;";
		try {
			con = MyConnectionProvider.getConnection();
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, id);
			pstmt.setString(2, pw);
			rs = pstmt.executeQuery();
			rs.next();
			int count = rs.getInt("CNT");
			return count == 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean findPassword(String id, String email) {
		String query = "SELECT COUNT(*) AS CNT FROM user" + " WHERE id = '" + id + "' AND email = '" + email + "';";
		try (Connection con = MyConnectionProvider.getConnection();
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(query)) {
			rs.next();
			int count = rs.getInt("CNT");
			return count == 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void updatePW(String password, String id) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String query = "UPDATE user SET password = ? WHERE id = ?";
		try {
			conn = MyConnectionProvider.getConnection();
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, password);
			pstmt.setString(2, id);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			MyConnectionProvider.closeStmt(pstmt);
			MyConnectionProvider.closeConnection(conn);
		}
	}

	public static UserData getUserByPW(String password) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String query = "SELECT * FROM user WHERE password = ?";
		try {
			conn = MyConnectionProvider.getConnection();
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, password);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				String id = rs.getString("id");
				String bpassword = rs.getString("password");
				String email = rs.getString("email");

				return new UserData(id, bpassword, email);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			MyConnectionProvider.closeRS(rs);
			MyConnectionProvider.closeStmt(pstmt);
			MyConnectionProvider.closeConnection(conn);
		}
		return null;
	}

	public static List<String> getUserByEmail(String email) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String query = "SELECT * FROM user WHERE email = ?";
		try {
			conn = MyConnectionProvider.getConnection();
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, email);
			rs = pstmt.executeQuery();
			
			List<String> ids = new ArrayList<>();
			while (rs.next()) {
				String id = rs.getString("id");
				ids.add(id);
			}
			return ids;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			MyConnectionProvider.closeRS(rs);
			MyConnectionProvider.closeStmt(pstmt);
			MyConnectionProvider.closeConnection(conn);
		}
		return null;
	}
}

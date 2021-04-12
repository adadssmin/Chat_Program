package dbutil;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class MyConnectionProvider {//설정에 build path확인, 추가먼저하기
	// 키=벨류 형태의 properties 파일을 객체 형태로 사용할 수 있는 클래스
	private final static Properties props = new Properties();
	static {// 설정을 미리 적어두면 시작되기전 불러와짐
		// 파일 인풋 스트림 생성
		try (FileInputStream input 
				= new FileInputStream("chat_db.properties")) {
			// 파일 로드하여 Properties 객체에 파일 내용을 읽어 올림.
			props.load(input);
			Class.forName(props.getProperty("DRIVER"));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	// + "?characterEncoding=UTF-8"; //한글안될때

	public static Connection getConnection() {
		String url = props.getProperty("URL");
		String id = props.getProperty("ID");
		String password = props.getProperty("PASSWORD");

		Connection con = null;
		try {
			con = DriverManager.getConnection(url, id, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return con;
	}
	
	public static void closeRS(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void closeStmt(Statement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void closeConnection(Connection con) {
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}

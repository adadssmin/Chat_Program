package lunch;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

import data.Room;
import dbutil.MyConnectionProvider;

public class Server {
	private static final int PORT = 9999;
	private ArrayList<ServerHandler> allUserList;
	private ArrayList<ServerHandler> standbyUserList;
	private ArrayList<Room> totalRoomList;
	
	public Server() {
		Connection conn = null;
		try {
			ServerSocket server = new ServerSocket(PORT);
			
			conn = MyConnectionProvider.getConnection();
			allUserList = new ArrayList<>(); //사용자 전체
			standbyUserList = new ArrayList<>(); //대기중인 사용자
			totalRoomList = new ArrayList<>(); //전체 방 리스트
			
			timeWatcher("[서버 준비 완료]");
			while (true) {
				Socket socket = server.accept();
				ServerHandler handler = new ServerHandler(socket, allUserList
							, standbyUserList, totalRoomList, conn);
				handler.start();
				allUserList.add(handler);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void timeWatcher(String str) {
		SimpleDateFormat fm = null;
		Date today = new Date();
		fm = new SimpleDateFormat("yy.MM.dd a h시 mm분");
		System.out.println(str + " - " + fm.format(today));
	}
	
	public static int getPort() {
		return PORT;
	}

	public static void main(String[] args) {
		new Server();
	}
}

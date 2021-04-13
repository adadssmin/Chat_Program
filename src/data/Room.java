package data;

import java.util.ArrayList;

import lunch.ServerHandler;

public class Room {
	private int room_Num;
	private String title;
	private String room_Password;
	private String user_Count;
	private String admin;
	private int priv;//
	public ArrayList<ServerHandler> userListInRoom;
	
	public Room() {
		this.room_Num = 0;
		this.title = "";
		this.room_Password = "";
		this.user_Count = "";
		this.admin = "";
		this.priv = 0;
		userListInRoom = new ArrayList<>();
	}
	
	public Room(int room_Num, String title, String room_Password, String user_Count, String admin, int condtionP,
			ArrayList<ServerHandler> userListInRoom) {
		super();
		this.room_Num = room_Num;
		this.title = title;
		this.room_Password = room_Password;
		this.user_Count = user_Count;
		this.admin = admin;
		this.priv = condtionP;
		this.userListInRoom = userListInRoom;
	}
	
	public Room(String title, String room_Password, String user_Count, String admin, int priv,
			ArrayList<ServerHandler> userListInRoom) {
		super();
		this.title = title;
		this.room_Password = room_Password;
		this.user_Count = user_Count;
		this.admin = admin;
		this.priv = priv;
		this.userListInRoom = userListInRoom;
	}

	public int getRoom_Num() {
		return room_Num;
	}

	public void setRoom_Num(int room_Num) {
		this.room_Num = room_Num;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getRoom_Password() {
		return room_Password;
	}

	public void setRoom_Password(String room_Password) {
		this.room_Password = room_Password;
	}

	public String getUser_Count() {
		return user_Count;
	}

	public void setUser_Count(String user_Count) {
		this.user_Count = user_Count;
	}

	public String getAdmin() {
		return admin;
	}

	public void setAdmin(String admin) {
		this.admin = admin;
	}

	public int getPriv() {
		return priv;
	}

	public void setPriv(int priv) {
		this.priv = priv;
	}

	public ArrayList<ServerHandler> getUserListInRoom() {
		return userListInRoom;
	}

	public void setUserListInRoom(ArrayList<ServerHandler> userListInRoom) {
		this.userListInRoom = userListInRoom;
	}

	@Override
	public String toString() {
		return "room_Num: " + room_Num + ", title: " + title + ", room_Password: " + room_Password + ", user_Count: "
				+ user_Count + ", admin: " + admin + ", priv: " + priv + ", userListInRoom: " + userListInRoom;
	}
}

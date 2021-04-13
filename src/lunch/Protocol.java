package lunch;

public class Protocol {
	public static final String REGISTER = "100";//회원가입
	public static final String LOGIN = "110";//로그인
	public static final String LOGIN_OK = "111";//로그인 성공
	public static final String LOGIN_NO = "112";//로그인 실패
	public static final String LOGOUT = "120";//로그아웃
	public static final String MAKEROOM = "200";//방 만들기
	public static final String MAKEROOM_OK = "201";//방 만들기 성공
	public static final String MAKEROOM_ADMIN_OK = "202";//방 만든 유저 성공
	public static final String ENTER = "300";//방 입장
	public static final String ENTER_User = "301";//방 입장
	public static final String EXITROOM = "310";//대화방 퇴장
}

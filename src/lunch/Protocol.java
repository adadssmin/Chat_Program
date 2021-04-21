package lunch;

public class Protocol {
	public static final String REGISTER = "100";//회원가입
	public static final String REGISTER_OK = "101";//회원가입
	public static final String REGISTER_NO = "102";//회원가입
	
	public static final String IDSEARCHCHECK = "110"; // ID중복확인
	public static final String IDSEARCHCHECK_OK = "111"; // ID중복확인(중복)
	public static final String IDSEARCHCHECK_NO = "112"; // ID중복확인(중복아님)
	
	public static final String PWCHECK = "120"; // PW확인
	public static final String PWCHECK_OK = "121"; // PW확인
	public static final String PWCHECK_NO = "122"; // PW확인
	
	public static final String LOGIN = "200";//로그인
	public static final String LOGIN_OK = "201";//로그인 성공
	public static final String LOGIN_NO = "202";//로그인 실패
	
	public static final String SEARCHID = "210"; // ID찾기
	public static final String SEARCHID_OK = "211"; // ID찾기 (있음)
	public static final String SEARCHID_NO = "212"; // ID찾기 (없음)
	
	public static final String SEARCHPASSWORD = "220"; // Password찾기
	public static final String SEARCHPASSWORD_OK = "221"; // Password찾기 (있음)
	public static final String SEARCHPASSWORD_NO = "222"; // Password찾기 (없음)

	public static final String CHANGEPASSWORD = "230"; // 잊어버린 Password변경
	public static final String CHANGEPASSWORD_OK = "231"; // 잊어버린 Password변경 성공
	public static final String CHANGEPASSWORD_NO = "232"; // 잊어버린 Password변경 실패
	
	public static final String LOGOUT = "300";//로그아웃
	
	public static final String MAKEROOM = "400";//방 만들기
	public static final String MAKEROOM_OK = "401";//방 만들기 성공
	public static final String MAKEROOM_ADMIN_OK = "402";//방 만든 유저 성공
	public static final String MAKEROOM_SHOW = "403";//모든 방 보기
	
	public static final String ENTER = "500";//공개방 입장
	public static final String ENTER_PASSWORD = "501";//비밀방 입장
	public static final String ENTER_OK = "502";//방 입장 성공
	public static final String ENTER_NO = "503";//방 입장 실패
	
	public static final String EXITROOM = "600";//대화방 퇴장
	public static final String EXITROOM_OK = "601";//대화방 퇴장 성공
	public static final String EXITROOM_OK_ALL = "602";//대화방 전체 퇴장 성공
	
	public static final String CHATROOMSUBINFO = "700";//채팅방 제목, 인원수, 접속자 목록 새로고침
	
	public static final String CHATMESSAGE = "800";//채팅 메세
	public static final String CHATMESSAGE_OK = "801";//채팅 메세
}

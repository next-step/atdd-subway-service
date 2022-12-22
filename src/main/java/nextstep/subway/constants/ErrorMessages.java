package nextstep.subway.constants;

public class ErrorMessages {
    public static final String STATION_DOES_NOT_EXIST = "존재하지 않는 역입니다.";
    public static final String UNAUTHORIZED_MEMBER_REQUESTED_FAVORITE_CREATION = "사용자 인증 실패로 즐겨찾기 등록 불가합니다.";
    public static final String LAST_LINE_STATION_CANNOT_BE_DELETED = "구간이 2개 이상 등록되어 있을 때에만 제거할 수 있습니다.";
    public static final String STATION_POSITION_NEEDED = "판단 기준 StationPosition 누락";
    public static final String LINE_STATION_ALREADY_EXIST = "이미 등록된 구간 입니다.";
    public static final String NO_STATION_MATCH = "등록할 수 없는 구간 입니다.";
    public static final String LINE_STATION_DISTANCE_TOO_LONG = "역과 역 사이의 거리보다 좁은 거리를 입력해주세요";
    public static final String SOURCE_TARGET_CANNOT_BE_SAME = "출발역과 도착역이 같은 경로는 조회할 수 없습니다.";
    public static final String CANNOT_FIND_ANY_PATH = "출발역과 도착역이 연결되어있지 않습니다.";
    public static final String AUTH_PRINCIPAL_MISSING = "인증정보가 누락 되어있습니다.";
    public static final String LOGIN_MEMBER_FIELD_MISSING = "로그인 사용자 정보가 불완전합니다.";
    public static final String CANNOT_CHECK_OVER_DISTANCE = "초과 요금 기준 확인에 실패하였습니다.";
}

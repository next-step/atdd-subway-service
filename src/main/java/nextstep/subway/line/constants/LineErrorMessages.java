package nextstep.subway.line.constants;

public class LineErrorMessages {
    public static final String LAST_LINE_STATION_CANNOT_BE_DELETED = "구간이 2개 이상 등록되어 있을 때에만 제거할 수 있습니다.";
    public static final String CANNOT_ADD_LINE_STATION_IF_BOTH_DOES_NOT_EXIST_IN_LINE = "상행역과 하행역 모두 노선에 등록되지 않은 경우 구간 등록 불가합니다.";
    public static final String STATION_POSITION_NEEDED = "판단 기준 StationPosition 누락";
    public static final String LINE_STATION_ALREADY_EXIST = "이미 등록된 구간 입니다.";
    public static final String NO_STATION_MATCH = "등록할 수 없는 구간 입니다.";
    public static final String LINE_STATION_DISTANCE_TOO_LONG = "역과 역 사이의 거리보다 좁은 거리를 입력해주세요";
}

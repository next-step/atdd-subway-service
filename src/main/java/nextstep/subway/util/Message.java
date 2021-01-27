package nextstep.subway.util;

public class Message {
    public static final String ALREADY_EXIST_STATION_MESSAGE = "이미 존재하는 상하행역입니다.";
    public static final String NOT_EXIST_STATION_MESSAGE = "존재하지 않는 지하철역입니다.";
    public static final String DISTANCE_EXCESS_MESSAGE = "기존 상하행역 거리보다 길 수 없습니다.";
    public static final String NOT_FOUND_LINE_MESSAGE = "지하철 노선이 존재하지 않습니다.";
    public static final String ONE_SECTION_CANNOT_BE_UNDESSED = "구간이 하나인 지하철 노선은 구간을 제거할 수 없습니다.";
    public static final String NOT_REGISTER_STATION_CANNOT_REMOVE = "노선에 등록되지 않은 역은 제거할 수 없습니다.";
    public static final String NOT_REGISTER_STATION = "경로에 포함되어 있지 않은 역입니다.";
    public static final String EQUALS_START_STATION_AND_ARRIVAL_STATION = "출발지와 도착지가 같습니다.";
}

package nextstep.subway.path.message;

public enum LinePathMessage {
    GRAPH_ERROR_SOURCE_AND_TARGET_STATION_IS_EQUALS("출발역과 도착역이 동일합니다."),
    GRAPH_ERROR_NOT_FOUND_SOURCE_STATION("노선에 출발역이 등록되어 있지 않습니다."),
    GRAPH_ERROR_NOT_FOUND_TARGET_STATION("노선에 도착역이 등록되어 있지 않습니다."),
    GRAPH_ERROR_NOT_CONNECTED_STATIONS("출발역과 도착역이 연결되어 있지 않습니다."),
    GRAPH_ERROR_IS_ALREADY_ENROLLED_EDGE("그래프 간선에 이미 추가되어 있습니다.");

    private final String message;

    LinePathMessage(String message) {
        this.message = message;
    }

    public String message() {
        return message;
    }
}

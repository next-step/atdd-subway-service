package nextstep.subway.common;

public enum Message {
    MESSAGE_NO_RESULT_DATA ("데이터가 존재하지 않습니다."),
    MESSAGE_STATION_NOT_FOUND("지하철역이 존재하지 않습니다."),
    MESSAGE_EQUALS_START_STATION_END_STATION("출발역과 도착역이 같습니다."),
    MESSAGE_NOT_EXISTS_START_STATION_OR_END_STATION("존재하지 않은 출발역이나 도착역을 조회 하였습니다."),
    MESSAGE_NOT_CONNECTED_START_STATION_AND_END_STATION("출발역과 도착역이 연결되어있지 않습니다."),
    MESSAGE_LINE_NOT_FOUND("라인이 존재하지 않습니다."),
    MESSAGE_CAN_NOT_DELETE("삭제할 수 없습니다.")
    ;

    private String message;

    Message(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public static String format(Message message, Object... arg) {
        return String.format(message.getMessage(), arg);
    }


}

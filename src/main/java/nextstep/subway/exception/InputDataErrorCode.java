package nextstep.subway.exception;

public enum InputDataErrorCode {
    THERE_IS_NOT_SEARCHED_STATION("[ERROR] 검색된 역이 없습니다."),
    THERE_IS_NOT_SEARCHED_SECTION("[ERROR] 검색된 구간이 없습니다."),
    DISTANCE_IS_NOT_LESS_THEN_ZERO("[ERROR] 거리는 0이하가 될수 없습니다."),
    THERE_IS_SAME_STATIONS("[ERROR] 서로 같은 역입니다."),
    IT_CAN_NOT_SEARCH_SOURCE_AND_TARGET_ON_LINE("[ERROR] 라인에 역이 없습니다."),
    THERE_IS_NOT_SEARCHED_LINE("[ERROR] 검색 된 라인이 없습니다."),
    IT_DO_NOT_CONNECT_STATIONS_EACH_OTHER("[ERROR]출발역과 도착역이 서로 연결되어있지 않습니다.");
    private String errorMessage;

    InputDataErrorCode(String errorMessage){
        this.errorMessage = errorMessage;
    }

    public String errorMessage(){
        return this.errorMessage;
    }
}

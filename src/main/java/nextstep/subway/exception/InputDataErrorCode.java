package nextstep.subway.exception;

public enum InputDataErrorCode {
    THERE_IS_NOT_SEARCHED_STATION("[ERROR] 검색된 역이 없습니다."),
    THERE_IS_NOT_SEARCHED_SECTION("[ERROR] 검색된 구간이 없습니다."),
    DISTANCE_IS_NOT_LESS_THEN_ZERO("[ERROR] 거리는 0이하가 될수 없습니다.");
    private String errorMessage;

    InputDataErrorCode(String errorMessage){
        this.errorMessage = errorMessage;
    }

    public String errorMessage(){
        return this.errorMessage;
    }
}

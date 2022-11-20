package nextstep.subway.common.constant;

public enum ErrorCode {

    상행종착역은_비어있을_수_없음("[ERROR] 상행종착역은 비어있을 수 없습니다."),
    하행종착역은_비어있을_수_없음("[ERROR] 하행종착역은 비어있을 수 없습니다."),
    노선_정보가_없음("[ERROR] 노선 정보는 비어있을 수 없습니다."),
    구간의_상행역과_하행역이_동일할_수_없음("[ERROR] 구간의 상행역과 하행역은 동일할 수 없습니다."),
    ;

    private final String errorMessage;

    ErrorCode(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}

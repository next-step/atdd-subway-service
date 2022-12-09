package nextstep.subway.common;

public enum ErrorCode {

    NO_SAME_SECTION_EXCEPTION("[ERROR] 같은 Section을 추가할 수 없습니다.");

    private final String errorMessage;
    ErrorCode(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}

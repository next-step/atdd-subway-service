package nextstep.subway.line.exception;

public class InvalidAddSectionException extends IllegalArgumentException {

    public static final String EXISTS_SECTION = "이미 등록된 구간 입니다.";
    public static final String NOT_EXIST_STATIONS = "등록할 수 없는 구간 입니다.";

    public InvalidAddSectionException(String message) {
        super(message);
    }
}

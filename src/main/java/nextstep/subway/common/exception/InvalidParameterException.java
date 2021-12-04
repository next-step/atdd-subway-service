package nextstep.subway.common.exception;

/**
 * 유효하지 않은 매개 변수가 메소드에 전달 될 때 발생합니다.
 */
public class InvalidParameterException extends RuntimeException {

    public static final InvalidParameterException SECTION_EXIST_EXCEPTION = new InvalidParameterException(
        "이미 등록된 구간 입니다.");
    public static final InvalidParameterException SECTION_ADD_NO_POSITION_EXCEPTION = new InvalidParameterException(
        "등록할 수 없는 구간 입니다.");
    public static final InvalidParameterException DISTANCE_RANGE_EXCEPTION = new InvalidParameterException(
        "역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
    public static final InvalidParameterException SECTION_ONE_COUNT_CAN_NOT_REMOVE_EXCEPTION = new InvalidParameterException(
        "구간이 하나 일 경우 제거 할 수 없습니다.");
    public static final InvalidParameterException NOT_EMPTY_EXCEPTION = new InvalidParameterException(
        "빈 값을 입력 할 수 없습니다.");
    public static final InvalidParameterException STATION_NAME_DUPLICATE_DATA_EXCEPTION = new InvalidParameterException(
        "지하철 역 이름이 이미 존재합니다");
    public static final InvalidParameterException LINE_NAME_DUPLICATE_DATA_EXCEPTION = new InvalidParameterException(
        "노선 이름이 이미 존재합니다");

    public InvalidParameterException() {
    }

    public InvalidParameterException(String message) {
        super(message);
    }

    public InvalidParameterException(String message, Throwable cause, boolean enableSuppression,
        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public InvalidParameterException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidParameterException(Throwable cause) {
        super(cause);
    }
}

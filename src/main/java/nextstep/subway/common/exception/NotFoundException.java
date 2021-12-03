package nextstep.subway.common.exception;

/**
 * 데이터베이스 조회시 정보를 찾지 못할 때 throw 되는 예외입니다.
 */
public class NotFoundException extends DatabaseException {

    public static final NotFoundException SECTION_NOT_FOUND_EXCEPTION = new NotFoundException(
        "지하철 역을 찾을 수 없습니다.");
    public static final NotFoundException LINE_NOT_FOUND_EXCEPTION = new NotFoundException(
        "노선을 찾을 수 없습니다.");

    public NotFoundException() {
        super("Not Found Exception");
    }

    public NotFoundException(String message) {
        super(message);
    }

}

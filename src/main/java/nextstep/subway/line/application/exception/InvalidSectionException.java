package nextstep.subway.line.application.exception;

public class InvalidSectionException extends RuntimeException {

    public static final InvalidSectionException SECTION_DUPLICATION = new InvalidSectionException("같은 상행역과 하행역으로 등록된 구간이 이미 존재합니다.");
    public static final InvalidSectionException NOT_CONNECTABLE = new InvalidSectionException("구간을 연결할 상행역 또는 하행역이 존재해야 합니다.");
    public static final InvalidSectionException CAN_NOT_DELETE = new InvalidSectionException("구간을 더 이상 삭제할 수 없습니다.");
    public static final InvalidSectionException LONGER_THAN_BETWEEN_SECTIONS = new InvalidSectionException("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");

    public InvalidSectionException(String message) {
        super(message);
    }

    public static InvalidSectionException shorterThanMinDistance(int minValue) {
        return new InvalidSectionException("지하철 구간 사이의 거리는 최소 " + minValue + " 이상이어야 합니다.");
    }
}

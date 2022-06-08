package nextstep.subway.line.domain;

public class InvalidSectionSizeException extends RuntimeException {

    public InvalidSectionSizeException() {
        super("더 이상 역을 제거할 수 없습니다.");
    }
}

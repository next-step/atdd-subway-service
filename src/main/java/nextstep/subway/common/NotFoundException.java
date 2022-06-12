package nextstep.subway.common;

public class NotFoundException extends RuntimeException {
    public NotFoundException() {
        super("[ERROR] 데이터를 찾을 수 없습니다.");
    }

    public NotFoundException(Long data) {
        super("[ERROR] 데이터를 찾을 수 없습니다. [data = " + data + "]");
    }
}

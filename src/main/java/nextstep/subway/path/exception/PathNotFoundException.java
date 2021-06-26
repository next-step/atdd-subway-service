package nextstep.subway.path.exception;

public class PathNotFoundException extends RuntimeException {

    public PathNotFoundException() {
        super("경로를 찾을 수 없습니다.");
    }
}

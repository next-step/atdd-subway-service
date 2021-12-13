package nextstep.subway.path.application.exception;

import nextstep.subway.common.InvalidException;

public class InvalidPathException extends InvalidException {
    public static final InvalidPathException SAME_DEPARTURE_ARRIVAL = new InvalidPathException("출발역과 도착역이 같습니다.");
    public static final InvalidPathException NOT_EXIST_STATION = new InvalidPathException("지하철 노선에 존재하지 않는 역입니다.");
    public static final InvalidPathException NOT_CONNECTABLE = new InvalidPathException("출발역과 도착역은 연결되어 있어야 합니다.");

    public InvalidPathException() {
    }

    public InvalidPathException(String message) {
        super(message);
    }
}

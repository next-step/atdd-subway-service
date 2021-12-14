package nextstep.subway.common.exception;

import nextstep.subway.common.CustomException;

public class PathDisconnectedException extends CustomException {
    private static final String NOT_LINK_REQUEST_PATH = "요청한 경로는 연결되어 있지 않습니다.";

    public PathDisconnectedException() {
        super(NOT_LINK_REQUEST_PATH);
    }
}

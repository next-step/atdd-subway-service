package nextstep.subway.exception.favorite;

public class SameStationException extends RuntimeException {

    private static final long serialVersionUID = 6187640947702177868L;
    private static final String SAME_STATION = "동일 역으로 즐겨찾기 등록은 불가능합니다.";

    public SameStationException() {
        super(SAME_STATION);
    }

    public SameStationException(String msg) {
        super(msg);
    }
}

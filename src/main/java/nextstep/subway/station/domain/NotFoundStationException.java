package nextstep.subway.station.domain;

public class NotFoundStationException extends RuntimeException {

    private static final long serialVersionUID = 2470091811082457602L;

    public NotFoundStationException(Long id) {
        super(id + "에 해당하는 정거장을 찾을 수 없습니다.");
    }
}

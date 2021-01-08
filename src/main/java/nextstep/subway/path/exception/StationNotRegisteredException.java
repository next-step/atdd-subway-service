package nextstep.subway.path.exception;

/**
 * @author : leesangbae
 * @project : subway
 * @since : 2021-01-07
 */
public class StationNotRegisteredException extends RuntimeException {

    public StationNotRegisteredException(long stationId) {
        super("등록되지 않은 지하철 역입니다. id : " + stationId);
    }
}

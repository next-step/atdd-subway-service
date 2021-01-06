package nextstep.subway.path.exception;

/**
 * @author : leesangbae
 * @project : subway
 * @since : 2021-01-06
 */
public class StationNotFoundException extends RuntimeException {

    public StationNotFoundException(long stationId) {
        super("존재하지 않는 지하철 역입니다. id : " + stationId);
    }
}

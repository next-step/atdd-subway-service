package nextstep.subway.path.ui.application;

import nextstep.subway.path.ui.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * packageName : nextstep.subway.path.ui.application
 * fileName : PathFinder
 * author : haedoang
 * date : 2021/12/04
 * description :
 */
@Component
public class PathFinder {
    public PathResponse getPath() {
        return PathResponse.of(Arrays.asList(StationResponse.of(new Station("강남역")), StationResponse.of(new Station("역삼역"))));
    }
}

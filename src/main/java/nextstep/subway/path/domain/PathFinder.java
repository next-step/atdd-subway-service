package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;

import java.util.List;

/**
 * packageName : nextstep.subway.path.application
 * fileName : ThirdPartyService
 * author : haedoang
 * date : 2021/12/05
 * description :
 */
public interface PathFinder {
    PathResponse getShortestPath(List<Line> lineList, List<Station> stationList, Long srcStationId, Long destStationId);
}

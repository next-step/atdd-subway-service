package nextstep.subway.path.finder;

import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;

public interface PathFinder {
    String NOT_GENERATED_GRAPH = "거리를 찾기 위한 그래프 구성이 되어있지 않습니다.";
    PathResponse findShortPath(Station sourceStation, Station targetStation);
}

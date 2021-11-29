package nextstep.subway.path.application;

import nextstep.subway.path.dto.PathResult;
import nextstep.subway.station.domain.Station;

public interface Path {

    PathResult find(Station source, Station target);
}

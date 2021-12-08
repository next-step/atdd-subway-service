package nextstep.subway.path.application;

import nextstep.subway.path.domain.Path;
import nextstep.subway.path.dto.PathResult;

public interface PathSearch {

    PathResult findShortestPath(Path path);
}


package nextstep.subway.line.application;

import nextstep.subway.line.domain.Path;
import nextstep.subway.line.dto.path.PathResult;

public interface PathSearch {

    PathResult findShortestPath(Path path);
}


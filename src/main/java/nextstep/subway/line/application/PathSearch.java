package nextstep.subway.line.application;

import nextstep.subway.line.domain.Path;
import nextstep.subway.line.domain.PathResult;

public interface PathSearch {

    PathResult findShortestPath(Path path);
}


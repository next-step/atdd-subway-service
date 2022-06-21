package nextstep.subway.path.domain;

import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.ShortestPath;

public interface PathFinder {
    ShortestPath getShortestPath(PathRequest request);
}

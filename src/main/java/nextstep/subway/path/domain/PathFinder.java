package nextstep.subway.path.domain;

import nextstep.subway.path.PathResponse;
import nextstep.subway.path.dto.PathRequest;

public interface PathFinder {
    PathResponse getShortestPath(PathRequest request);
}

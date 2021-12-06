package nextstep.subway.path.application;

import java.util.List;
import nextstep.subway.path.dto.PathEdge;
import nextstep.subway.path.dto.PathResult;

public interface PathFactory {

    PathResult findShortestPath(List<PathEdge> pathEdges, Long source, Long target);

}

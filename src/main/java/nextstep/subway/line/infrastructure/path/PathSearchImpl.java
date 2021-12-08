package nextstep.subway.line.infrastructure.path;


import nextstep.subway.line.application.PathSearch;
import nextstep.subway.line.domain.Path;
import nextstep.subway.line.dto.path.PathResult;
import org.springframework.stereotype.Component;

@Component
public class PathSearchImpl implements PathSearch {

    @Override
    public PathResult findShortestPath(Path path) {
        SubwayGraph subwayGraph = SubwayGraph.of(path.getSections());
        SubwayPath subwayPath = SubwayPath.of(subwayGraph);
        return subwayPath.getShortestPath(path.getSource(), path.getTarget());
    }
}

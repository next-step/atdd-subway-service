package nextstep.subway.path.infrastructure;


import nextstep.subway.path.application.PathSearch;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.dto.PathResult;
import org.springframework.stereotype.Component;

@Component
public class PathSearchImpl implements PathSearch {

    @Override
    public PathResult findShortestPath(Path path) {
        SubwayGraph subwayGraph = new SubwayGraph(SectionEdge.toList(path.getSections()));
        SubwayPath subwayPath = new SubwayPath(subwayGraph, path.getSource(), path.getTarget());
        return subwayPath.getShortestPath();
    }
}

package nextstep.subway.line.infrastructure.path;


import nextstep.subway.line.application.PathSearch;
import nextstep.subway.line.domain.Path;
import nextstep.subway.line.dto.path.PathResult;
import org.springframework.stereotype.Component;

@Component
public class PathSearchImpl implements PathSearch {

    @Override
    public PathResult findShortestPath(Path path) {
//        SubwayGraph subwayGraph = new SubwayGraph(SectionEdge.toList(path.getSections()));
        SubwayGraph subwayGraph = new SubwayGraph(SectionEdge.toList(path.getSections()));
        SubwayPath subwayPath = new SubwayPath(subwayGraph, path.getSource(), path.getTarget());
        return subwayPath.getShortestPath();
    }
}

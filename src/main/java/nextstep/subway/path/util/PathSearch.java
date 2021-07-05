package nextstep.subway.path.util;

import nextstep.subway.auth.domain.AuthMember;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.path.domain.PathDestination;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.springframework.stereotype.Component;

@Component
public class PathSearch {

    public PathResponse findPaths(AuthMember loginMember, Lines lines, PathDestination pathDestination) {
        final GraphGenerator graphGenerator = new GraphGenerator(lines);

        final DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graphGenerator.getGraph());
        final GraphPath<Station, SectionEdge> graphPath = dijkstraShortestPath.getPath(pathDestination.getSource(), pathDestination.getTarget());

        Path path = new Path(graphPath);
        return new PathResponse(path);
    }


}

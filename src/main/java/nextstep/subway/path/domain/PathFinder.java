package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Sections;
import nextstep.subway.member.domain.MemberAge;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import java.util.List;
import java.util.Objects;

public class PathFinder {
    private static DijkstraShortestPath path;

    public PathFinder(DijkstraShortestPath path) {
        this.path = path;
    }

    public Path findPath(Station startStation, Station endStation, MemberAge age) {
        GraphPath path = this.path.getPath(startStation, endStation);
        validatePath(path);

        List<Station> stations = path.getVertexList();
        int distance = (int) this.path.getPathWeight(startStation, endStation);
        Fare fare = Fare.of(Sections.of(path.getEdgeList()), new TotalDistance(distance), age);
        return new Path(stations, distance, fare.getFare());
    }

    private void validatePath(GraphPath path) {
        if (isNull(path)) {
            throw new IllegalArgumentException("경로가 존재하지 않습니다.");
        }
    }

    private boolean isNull(GraphPath path) {
        return Objects.isNull(path);
    }


}

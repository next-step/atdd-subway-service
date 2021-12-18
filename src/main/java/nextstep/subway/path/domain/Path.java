package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.Optional;

public class Path {

    private List<Station> stations;
    private List<SectionWeightedEdge> sectionEdges;

    public Path(List<Station> stations, List<SectionWeightedEdge> sectionEdges) {
        this.stations = stations;
        this.sectionEdges = sectionEdges;
    }

    public static Path of(GraphPath graphPath) {
        return new Path(graphPath.getVertexList(), graphPath.getEdgeList());
    }


    public List<Station> getStations() {
        return this.stations;
    }

    public int sumPathDistance() {
        return sectionEdges.stream()
                .mapToInt(SectionWeightedEdge::getDistance)
                .sum();
    }

}

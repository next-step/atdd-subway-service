package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.List;

public class SectionGraph {
    private GraphPath<Station, DefaultWeightedEdge> paths;

    public SectionGraph(GraphPath<Station, DefaultWeightedEdge> paths) {
        this.paths = paths;
    }

    public List<Station> getStations() {
        return paths.getVertexList();
    }

    public int getTotalDistance() {
        return (int) paths.getWeight();
    }

    public boolean containsSection(Section section) {
        return paths.getGraph()
                .containsEdge(section.getUpStation(), section.getDownStation());
    }
}

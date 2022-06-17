package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;

public class SubwayGraph extends WeightedMultigraph<Station, DefaultWeightedEdge> {

    public SubwayGraph(Class<? extends DefaultWeightedEdge> edgeClass) {
        super(edgeClass);
    }

    public void addVertex(List<Station> stations) {
        stations.forEach(this::addVertex);
    }

    public void setEdgeWeight(Sections sections) {
        sections.getSections()
                .forEach(section -> this.setEdgeWeight(
                        this.addEdge(
                                section.getUpStation(),
                                section.getDownStation()),
                        section.getDistance().toInt()));
    }
}

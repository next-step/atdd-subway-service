package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.stream.Collectors;

public class SubwayGraph extends WeightedMultigraph<Station, DefaultWeightedEdge> {

    public SubwayGraph(Class<DefaultWeightedEdge> defaultWeightedEdgeClass) {
        super(defaultWeightedEdgeClass);
    }

    public void setEdgeWeightWithSections(List<Line> lines) {
        lines.stream()
                .flatMap(it -> it.getSections().stream())
                .collect(Collectors.toList())
                .forEach(section -> this.setEdgeWeight(this.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance()));
    }

    public void addVertexWithStations(List<Line> lines) {
        lines.stream()
                .flatMap(it -> it.getStationsInOrder().stream())
                .distinct()
                .collect(Collectors.toList())
                .forEach(station -> this.addVertex(station));
    }
}

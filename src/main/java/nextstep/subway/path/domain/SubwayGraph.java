package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.stream.Collectors;

public class SubwayGraph extends WeightedMultigraph<Station, SubwayEdge> {

    public SubwayGraph(Class<SubwayEdge> subwayEdge) {
        super(subwayEdge);
    }

    public void setEdgeWeightWithSections(List<Line> lines) {
        lines.stream()
                .flatMap(it -> it.getSections().stream())
                .collect(Collectors.toList())
                .forEach(section -> {
                    SubwayEdge subwayEdge = SubwayEdge.of(section);
                    this.addEdge(subwayEdge.getUpStation(), subwayEdge.getDownStation(), subwayEdge);
                    this.setEdgeWeight(subwayEdge, subwayEdge.getDistance());
                });
    }

    public void addVertexWithStations(List<Line> lines) {
        lines.stream()
                .flatMap(it -> it.getStationsInOrder().stream())
                .distinct()
                .collect(Collectors.toList())
                .forEach(station -> this.addVertex(station));
    }
}

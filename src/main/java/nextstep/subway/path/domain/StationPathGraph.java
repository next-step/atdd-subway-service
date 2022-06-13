package nextstep.subway.path.domain;

import java.util.List;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

public class StationPathGraph extends WeightedMultigraph<Station, DefaultWeightedEdge> {

    public StationPathGraph(List<Line> lines) {
        super(DefaultWeightedEdge.class);
        lines.forEach(line -> {
            addStation(line.getStations());
            addSection(line.getSections());
        });
    }

    private void addSection(Sections sections) {
        sections.getSections().forEach(this::addEdgeWeight);
    }

    private void addStation(List<Station> stations) {
        stations.forEach(this::addVertex);
    }

    private void addEdgeWeight(Section section) {
        setEdgeWeight(addEdge(section.getUpStation(), section.getDownStation()), section.getDistance().getValue());
    }

}

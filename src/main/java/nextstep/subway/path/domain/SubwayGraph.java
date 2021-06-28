package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.stream.Collectors;

public class SubwayGraph extends WeightedMultigraph<Station, SubwayWeightedEdge> {
    public SubwayGraph(Class edgeClass) {
        super(edgeClass);
    }

    public void addVertexWith(List<Line> lines) {
        lines.stream()
                .flatMap(line -> line.getStations().stream())
                .distinct()
                .collect(Collectors.toList())
                .forEach(this::addVertex);
    }

    public void addEdge(List<Line> lines) {
        for (Line line : lines) {
            line.getSections()
                    .forEach(section -> addEdge(section, line));
        }
    }

    private void addEdge(Section section, Line line) {
        SubwayWeightedEdge sectionEdge = new SubwayWeightedEdge(section, line.getId());
        addEdge(section.getUpStation(), section.getDownStation(), sectionEdge);
        setEdgeWeight(sectionEdge, section.getDistance());
    }
}

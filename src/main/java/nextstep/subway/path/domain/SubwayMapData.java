package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.jgrapht.WeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SubwayMapData extends WeightedMultigraph<Station, DefaultWeightedEdge> {
    private final List<Line> lines;

    public SubwayMapData(List<Line> lines, Class<DefaultWeightedEdge> edgeClass) {
        super(edgeClass);
        this.lines = Collections.unmodifiableList(lines);
    }

    public WeightedGraph initData() {
        initVertex();
        initEdgeWeight();
        return this;
    }

    private void initVertex() {
        lines.stream()
                .flatMap(line -> line.getStations().stream())
                .collect(Collectors.toSet())
                .forEach(this::addVertex);
    }

    private void initEdgeWeight() {
        lines.stream()
             .flatMap(line -> line.getSections().stream())
             .forEach(section -> {
                 SectionEdge sectionEdge = new SectionEdge(section);
                 addEdge(section.getUpStation(), section.getDownStation(), sectionEdge);
                 setEdgeWeight(sectionEdge, section.getDistance());
             });
    }
}

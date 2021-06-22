package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.jgrapht.WeightedGraph;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SubwayMapData extends WeightedMultigraph<Station, SectionEdge> {
    private static final String NO_LINES_EXCEPTION = "경로 조회에 필요한 노선도 값이 조회되지 않습니다.";

    private final List<Line> lines;

    public SubwayMapData(List<Line> lines, Class<SectionEdge> edgeClass) {
        super(edgeClass);
        validateLines(lines);
        this.lines = Collections.unmodifiableList(lines);
    }

    private void validateLines(List<Line> lines) {
        if (lines.isEmpty()) {
            throw new IllegalArgumentException(NO_LINES_EXCEPTION);
        }
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

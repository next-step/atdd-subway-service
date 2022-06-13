package nextstep.subway.path.application;

import java.util.List;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class SubwayGraphProvider {
    @Cacheable("graph")
    public WeightedMultigraph<Station, SectionEdge> getSubwayGraph(List<Line> lines) {
        return toGraph(lines);
    }

    private WeightedMultigraph<Station, SectionEdge> toGraph(List<Line> lines) {
        WeightedMultigraph<Station, SectionEdge> graph = new WeightedMultigraph<>(SectionEdge.class);
        lines.stream().forEach(line -> addLineToGraph(graph, line));
        return graph;
    }

    private void addLineToGraph(WeightedMultigraph<Station, SectionEdge> graph, Line line) {
        List<Station> stations = line.getStations();
        stations.stream().forEach(graph::addVertex);
        Sections sections = line.getSections();
        List<SectionEdge> edges = sections.toSectionEdge();
        edges.stream().forEach(edge -> graph.addEdge(edge.getSource(), edge.getTarget(), edge));
    }
}

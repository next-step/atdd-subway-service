package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.stream.Collectors;

public class Path {
    private WeightedMultigraph<String, DefaultWeightedEdge> graph = new WeightedMultigraph(DefaultWeightedEdge.class);
    private Station source;
    private Station target;

    public Path(Station source, Station target) {
        if (source.equals(target)){
            throw new RuntimeException("출발지와 도착지가 같은 경로는 검색할수 없습니다.");
        }
        this.source = source;
        this.target = target;
    }

    public List<Station> assembleStations(List<Line> lines) {
        return lines.stream()
                .flatMap(it -> it.assembleStations().stream())
                .distinct()
                .collect(Collectors.toList());
    }

    public void addVertex(List<Line> lines) {
        lines.stream()
                .flatMap(it -> it.assembleStations().stream())
                .distinct()
                .collect(Collectors.toList())
                .forEach(station -> graph.addVertex(station.getName()));
    }

    public void addEdge(List<Line> lines) {
        for (Line line : lines) {
            line.getSections().stream()
                    .forEach(it -> addEdgeWeight(it));
        }
    }

    private void addEdgeWeight(Section section) {
        DefaultWeightedEdge test = graph.addEdge(section.upStation().getName(), section.downStation().getName());
        graph.setEdgeWeight(test, section.distance());
    }

    public void validStations(List<Line> lines) {
        List<Station> stations = assembleStations(lines);
        if (!hasStations(stations)) {
            throw new RuntimeException("역이 연결되지 않았습니다.");
        }
    }

    private boolean hasStations(List<Station> stations) {
        return stations.stream()
                .anyMatch(station -> station.equals(source) || station.equals(target));
    }
}

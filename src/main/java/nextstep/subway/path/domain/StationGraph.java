package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.List;
import java.util.stream.Collectors;

public class StationGraph {
    private final WeightedMultigraph<Station, DefaultWeightedEdge> stationGraph;
    private final List<Line> lines;

    public StationGraph(List<Line> lines) {
        this.stationGraph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        this.lines = lines;
    }

    public Graph<Station, DefaultWeightedEdge> generateGraph() {
        addVertex();
        setEdgeWeight();
        return stationGraph;
    }

    private void addVertex() {
        getAllStations().forEach(stationGraph::addVertex);
    }

    private void setEdgeWeight() {
        getAllSections().forEach(section -> {
            Station upStation = section.getUpStation();
            Station downStation = section.getDownStation();
            int distance = section.getDistance().getDistance();

            stationGraph.setEdgeWeight(stationGraph.addEdge(upStation, downStation), distance);
        });
    }

    private List<Station> getAllStations() {
        return lines.stream()
                .flatMap(line -> line.getStations().stream())
                .collect(Collectors.toList());
    }

    private List<Section> getAllSections() {
        return lines.stream()
                .map(Line::getSections)
                .flatMap(it -> it.getSections().stream())
                .collect(Collectors.toList());
    }
}

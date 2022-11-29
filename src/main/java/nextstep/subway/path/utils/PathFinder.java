package nextstep.subway.path.utils;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.Path;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

@Component
public class PathFinder {
    public Path findPath(List<Line> lines, Long source, Long target) {
        DijkstraShortestPath dijkstraShortestPath = getDijkstraShortestPath(lines);
        List<StationResponse> stationResponses
                = getStationResponses(lines, getShortestPath(dijkstraShortestPath, source, target));
        return new Path(stationResponses, getPathWeight(dijkstraShortestPath, source, target));
    }

    private List<String> getShortestPath(DijkstraShortestPath dijkstraShortestPath, Long source, Long target) {
        return dijkstraShortestPath.getPath(String.valueOf(source), String.valueOf(target)).getVertexList();
    }

    private int getPathWeight(DijkstraShortestPath dijkstraShortestPath, Long source, Long target){
        return (int) dijkstraShortestPath.getPathWeight(String.valueOf(source), String.valueOf(target));
    }

    private List<StationResponse> getStationResponses(List<Line> lines, List<String> shortestPath){
        List<Station> stations = getStations(lines);
        return shortestPath.stream()
                .map(stationId -> findStation(stations, stationId))
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    private List<Station> getStations(List<Line> lines){
        return lines.stream()
                .flatMap(line -> line.getStations().stream())
                .distinct()
                .collect(Collectors.toList());
    }

    private Station findStation(List<Station> stations, String stationId){
        return stations.stream()
                .filter(station -> station.isEqualToStationId(stationId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("station id와 일치하는 역을 찾을 수 없습니다."));
    }

    private DijkstraShortestPath getDijkstraShortestPath (List<Line> lines) {
        return new DijkstraShortestPath(getLineGraph(lines));
    }

    private WeightedMultigraph<String, DefaultWeightedEdge> getLineGraph(List<Line> lines){
        WeightedMultigraph<String, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        addVertex(lines, graph);
        setEdgeWeight(lines, graph);
        return graph;
    }

    private void addVertex(List<Line> lines, WeightedMultigraph<String, DefaultWeightedEdge> graph){
        lines.stream()
                .flatMap(line -> line.getStations().stream())
                .forEach(station -> graph.addVertex(String.valueOf(station.getId())));
    }

    private void setEdgeWeight(List<Line> lines, WeightedMultigraph<String, DefaultWeightedEdge> graph){
        lines.stream()
                .flatMap(line -> line.getSections().getSections().stream())
                .forEach(section -> graph.setEdgeWeight(
                        graph.addEdge(String.valueOf(section.getUpStation().getId()),
                                      String.valueOf(section.getDownStation().getId())),
                        section.getDistance().getDistance()));
    }
}

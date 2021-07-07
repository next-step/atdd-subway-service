package nextstep.subway.path.domain;

import nextstep.subway.exception.CanNotFoundShortestPathException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.PathsResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;

import java.util.ArrayList;
import java.util.List;

public class Path {
    private List<Station> shortestPathStationList;
    private WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    public Path() {
        this.shortestPathStationList = new ArrayList<>();
        graph = new WeightedMultigraph(DefaultWeightedEdge.class);
    }

    public List<Station> getShortestPathStationList() {
        return shortestPathStationList;
    }

    public List<Station> shortestPathByDijkstra(List<Line> lineList, Station sourceStation, Station targetStation) {
        List<Station> shortestPathStations = null;
        for(Line line : lineList) {
            setVertex(line);
            setEdgeWeight(line);
        }

        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph);
        try {
            shortestPathStations = dijkstraShortestPath.getPath(sourceStation, targetStation).getVertexList();
        }
        catch (NullPointerException e) {
            throw new CanNotFoundShortestPathException("최단 경로를 찾을 수 없습니다.");
        }

        return shortestPathStations;
    }

    private void setVertex(Line line) {
        for(Station station : line.getStations())
            graph.addVertex(station);
    }

    private void setEdgeWeight(Line line) {
        for(Section section: line.getSections()) {
            graph.setEdgeWeight(graph.addEdge(section.getUpStation(),
                    section.getDownStation()), section.getDistance());
        }
    }
}

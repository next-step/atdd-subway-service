package nextstep.subway.path.application;

import nextstep.subway.common.domain.SubwayFare;
import nextstep.subway.exception.NoPathException;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.dto.StationGraphPath;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PathService {
    private StationService stationService;
    private LineService lineService;

    public PathService(StationService stationService, LineService lineService) {
        this.stationService = stationService;
        this.lineService = lineService;
    }
    public PathResponse findShortestPath(Long source, Long target) {
        checkSameStation(source, target);

        Station sourceStation = stationService.findStationById(source);
        Station targetStation = stationService.findStationById(target);
        List<Section> allSectionList = lineService.findAllLineSectionList();
        StationGraphPath stationGraphPath = new StationGraphPath(sourceStation, targetStation, allSectionList);
        SubwayFare chargedFare = SubwayFare.chargeByDistance(stationGraphPath.getDistance());

        return new PathResponse(stationGraphPath.getPathStations(), stationGraphPath.getDistance(), chargedFare.charged());
    }

    public GraphPath<Station, DefaultWeightedEdge> generateShortestPath(Station source, Station target, List<Line> lines) {
        WeightedMultigraph graph = new WeightedMultigraph(DefaultWeightedEdge.class);
        setUpGraphByLines(graph, lines);
        DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<Station, DefaultWeightedEdge> stationPath = dijkstraShortestPath.getPath(source, target);
        checkExistPath(stationPath);
        return stationPath;
    }

    private void setUpGraphByLines(WeightedMultigraph<Station, DefaultWeightedEdge> graph, List<Line> lines) {
        lines.stream().flatMap(line -> line.getSections().values().stream())
                .forEach(
                        section -> {
                            graph.addVertex(section.getUpStation());
                            graph.addVertex(section.getDownStation());
                            graph.setEdgeWeight(graph.addEdge(section.getUpStation(), section.getDownStation()), section.getDistance());
                        }
        );
    }

    private void checkSameStation(Long source, Long target) {
        if (source.equals(target)) {
            throw new NoPathException("출발역과 도착역이 같습니다.");
        }
    }

    private void checkExistPath(GraphPath<Station, DefaultWeightedEdge> stationPath) {
        if (stationPath == null) {
            throw new NoPathException("연결된 경로가 없습니다.");
        }
    }

}

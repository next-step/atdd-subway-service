package nextstep.subway.path.application;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.domain.SectionGraph;
import nextstep.subway.line.domain.SectionGraphEdge;
import nextstep.subway.member.domain.MemberAge;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class PathService {
    private final LineService lineService;
    private final StationService stationService;

    public PathService(LineService lineService, StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    @Transactional(readOnly = true)
    public PathResponse findPath(Long startStationId, Long endStationId, int age) {
        validateStations(startStationId, endStationId);

        SectionGraph graph = new SectionGraph();
        Lines lines = new Lines(lineService.findLines());
        DijkstraShortestPath path = lines.createPath(graph);
        PathFinder pathFinder = new PathFinder(path);

        Station startStation = stationService.findById(startStationId);
        Station endStation = stationService.findById(endStationId);

        Path findPath = pathFinder.findPath(startStation, endStation, new MemberAge(age));

        return PathResponse.of(findPath);
    }

    private void validateStations(Long startStationId, Long endStationId) {
        if(isSameStationId(startStationId, endStationId)) {
            throw new IllegalArgumentException("출발역과 도착역이 같습니다.");
        }
    }

    private boolean isSameStationId(Long startStationId, Long endStationId) {
        return Objects.equals(startStationId, endStationId);
    }
}

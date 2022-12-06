package nextstep.subway.path.application;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.Graph;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PathService {

    StationService stationService;
    LineService lineService;

    public PathService(StationService stationService, LineService lineService) {
        this.stationService = stationService;
        this.lineService = lineService;
    }

    public PathResponse findBestPath(Long sourceId, Long targetId) {
        Station source = findStationById(sourceId);
        Station target = findStationById(targetId);
        Graph graph = new Graph();

        findLinesContainStation(source).stream()
                .forEach(line -> line.setGraph(graph));
        findLinesContainStation(target).stream()
                .forEach(line -> line.setGraph(graph));

        Path path = new Path(graph, source, target);

        return new PathResponse(path.getBestPath(), path.getBestPathDistance());
    }

    private Station findStationById(Long stationId) {
        return stationService.findById(stationId);
    }

    private List<Line> findLinesContainStation(Station station) {
        return lineService.findLinesContainStation(station);
    }
}
package nextstep.subway.path.application;

import java.util.List;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.domain.PathFindResult;
import nextstep.subway.path.domain.PathFindService;
import nextstep.subway.path.domain.exception.NotExistPathException;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PathService {

    private final StationService stationService;
    private final LineService lineService;
    private final PathFindService pathFindService;

    public PathService(StationService stationService, LineService lineService,
                       PathFindService pathFindService) {
        this.stationService = stationService;
        this.lineService = lineService;
        this.pathFindService = pathFindService;
    }

    public PathResponse findShortestPath(Long startStationId, Long endStationId) {
        Station startStation = stationService.findStationById(startStationId);
        Station endStation = stationService.findStationById(endStationId);
        PathFindResult findResult = null;
        WeightedMultigraph<Station, SectionEdge> graph = getSubwayGraph();
        try {
            findResult = pathFindService.findShortestPath(graph, startStation, endStation);
        } catch (NotExistPathException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
        return PathResponse.of(findResult);
    }


    private WeightedMultigraph<Station, SectionEdge> getSubwayGraph(){
        List<Line> lines = lineService.findAll();
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

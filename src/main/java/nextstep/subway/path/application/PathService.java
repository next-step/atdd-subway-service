package nextstep.subway.path.application;

import java.util.List;
import java.util.NoSuchElementException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.domain.PathFindResult;
import nextstep.subway.path.domain.PathFindService;
import nextstep.subway.path.domain.exception.NotExistPathException;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PathService {

    private final StationRepository stationRepository;
    private final LineRepository lineRepository;
    private final PathFindService pathFindService;

    public PathService(StationRepository stationRepository, LineRepository lineRepository,
                       PathFindService pathFindService) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
        this.pathFindService = pathFindService;
    }

    public PathResponse findShortestPath(Long startStationId, Long endStationId) {
        Station startStation = findStationById(startStationId);
        Station endStation = findStationById(endStationId);
        PathFindResult findResult = null;
        WeightedMultigraph<Station, SectionEdge> graph = getSubwayGraph();
        try {
            findResult = pathFindService.findShortestPath(graph, startStation, endStation);
        } catch (NotExistPathException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
        return PathResponse.of(findResult);
    }

    private Station findStationById(Long startStationId){
        return stationRepository.findById(startStationId).orElseThrow(NoSuchElementException::new);
    }

    private WeightedMultigraph<Station, SectionEdge> getSubwayGraph(){
        List<Line> lines = lineRepository.findAll();
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

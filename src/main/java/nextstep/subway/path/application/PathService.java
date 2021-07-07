package nextstep.subway.path.application;

import nextstep.subway.exception.CanNotFoundStationException;
import nextstep.subway.exception.SameSourceTargetStationException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.dto.PathsRequest;
import nextstep.subway.path.dto.PathsResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class PathService {

    private LineRepository lineRepository;
    private StationRepository stationRepository;
    private WeightedMultigraph<Station, DefaultWeightedEdge> graph;

    public PathService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;

        graph = new WeightedMultigraph(DefaultWeightedEdge.class);
    }

    public PathsResponse findShortestPath(Long sourceStationId, Long targetStationId) {
        if (sourceStationId == targetStationId)
            throw new SameSourceTargetStationException("출발역과 도착역이 같습니다.");

        Station sourceStation = stationRepository.findById(sourceStationId).orElseThrow(() -> new CanNotFoundStationException("일치하는 출발역이 없습니다."));
        Station targetStation = stationRepository.findById(targetStationId).orElseThrow(() -> new CanNotFoundStationException("일치하는 도착역이 없습니다."));
        PathsResponse pathsResponse = getDijkstraShortestPath(sourceStation, targetStation);
        return pathsResponse;
    }

    public PathsResponse getDijkstraShortestPath(Station sourceStation, Station targetStation) {
        List<Line> lineList = lineRepository.findAll();

        Path shortestPath = new Path();
        List<Station> shortestPathStations = shortestPath.shortestPathByDijkstra(lineList, sourceStation, targetStation);

        List<StationResponse>  shortestPathStationsResponse = new ArrayList<>();
        for(Station station : shortestPathStations) {
            shortestPathStationsResponse.add(StationResponse.of(station));
        }

        return PathsResponse.of(shortestPathStationsResponse);
    }

}

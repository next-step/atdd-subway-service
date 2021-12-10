package nextstep.subway.path.application;

import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.List;
import nextstep.graph.Graph;
import nextstep.graph.GraphEdge;
import nextstep.subway.fare.FarePolicy;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.stereotype.Service;

@Service
public class PathService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public PathService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public PathResponse findShortestPath(PathRequest request) {
        List<Line> lines = lineRepository.findAll();
        Station source = findStationById(request.getSource());
        Station target = findStationById(request.getTarget());
        validateIfExistStations(source, target);

        Graph<Station> graph = toGraphFrom(lines);

        int distance = (int) graph.getShortestPathWeight(source, target);
        return new PathResponse(StationResponse.of(graph.getShortestPathList(source, target)),
            distance, FarePolicy.fareByDistance(distance));
    }

    private Graph<Station> toGraphFrom(List<Line> lines) {
        List<Station> vertexes = getAllStationsIn(lines);
        List<GraphEdge<Station>> edges = getAllSectionsToEdges(lines);

        return new Graph<>(vertexes, edges);
    }

    private List<GraphEdge<Station>> getAllSectionsToEdges(List<Line> lines) {
        return lines.stream()
            .flatMap(line -> line.getSections().getSections().stream())
            .map(section -> new GraphEdge<>(section.getUpStation(), section.getDownStation(),
                section.getDistance()))
            .collect(toList());
    }

    private List<Station> getAllStationsIn(List<Line> lines) {
        return lines.stream()
            .map(Line::getStations)
            .flatMap(Collection::stream)
            .collect(toList());
    }

    private void validateIfExistStations(Station sourceStation, Station targetStation) {
        if (sourceStation == null || targetStation == null) {
            throw new IllegalArgumentException("출발역 또는 도착역이 존재하지 않습니다.");
        }
    }

    public Station findStationById(Long id) {
        return stationRepository.findById(id).orElseThrow(
            () -> new IllegalArgumentException("id에 해당하는 역이 없습니다. id=" + id));
    }

}

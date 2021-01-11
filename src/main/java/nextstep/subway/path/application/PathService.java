package nextstep.subway.path.application;

import lombok.RequiredArgsConstructor;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PathService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public PathResponse findShortestPath(PathRequest pathRequest) {
        List<Station> findResult = findAllByIdIn(pathRequest);

        Station source = findResult.stream()
                .filter(station -> station.isSameStation(pathRequest.getSourceStationId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 출발역입니다."));

        Station target = findResult.stream()
                .filter(station -> station.getId().equals(pathRequest.getTargetStationId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 도착역입니다."));

        List<Line> lines = lineRepository.findAll();

        Path path = Path.of(lines);
        GraphPath<Station, DefaultWeightedEdge> shortestPath = path.findShortestPath(source, target);
        return PathResponse.of(shortestPath.getVertexList(), (int) shortestPath.getWeight());
    }

    private List<Station> findAllByIdIn(PathRequest pathRequest) {
        return stationRepository.findAllByIdIn(
                Arrays.asList(pathRequest.getSourceStationId(), pathRequest.getTargetStationId()));
    }
}

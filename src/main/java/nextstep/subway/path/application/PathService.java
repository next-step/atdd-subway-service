package nextstep.subway.path.application;

import lombok.RequiredArgsConstructor;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.ShortestPath;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
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

        Station source = getStation(findResult, pathRequest.getSourceStationId());
        Station target = getStation(findResult, pathRequest.getTargetStationId());

        List<Line> lines = lineRepository.findAll();

        Path path = Path.of(lines);
        ShortestPath shortestPath = path.findShortestPath(source, target);
        return PathResponse.of(shortestPath);
    }

    private Station getStation(List<Station> findResult, Long stationId) {
        return findResult.stream()
                .filter(station -> station.isSameStation(stationId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 출발역입니다."));
    }

    private List<Station> findAllByIdIn(PathRequest pathRequest) {
        return stationRepository.findAllByIdIn(
                Arrays.asList(pathRequest.getSourceStationId(), pathRequest.getTargetStationId()));
    }
}

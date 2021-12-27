package nextstep.subway.path.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.Route;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PathService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public PathService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public PathResponse findPath(PathRequest pathRequest) {
        List<Line> lines = lineRepository.findAll();

        Station sourceStation = findStation(pathRequest.getSource());
        Station targetStation = findStation(pathRequest.getTarget());

        Route route = Route.of(lines);
        Path path = route.findPath(sourceStation, targetStation);

        return PathResponse.of(path);
    }

    public Station findStation(long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 역입니다."));
    }
}

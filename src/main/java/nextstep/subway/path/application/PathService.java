package nextstep.subway.path.application;

import java.util.Optional;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.exception.StationNotFoundException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author : leesangbae
 * @project : subway
 * @since : 2021-01-02
 */
@Service
@Transactional
public class PathService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final PathFinder pathFinder;

    public PathService(LineRepository lineRepository,
            StationRepository stationRepository,
            PathFinder pathFinder) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.pathFinder = pathFinder;
    }

    @Transactional(readOnly = true)
    public PathResponse getShortestPath(PathRequest request) {

        validateRequest(request);

        Lines lines = new Lines(lineRepository.findAll());
        Optional<Station> sourceStation = stationRepository.findById(request.getSource());
        Optional<Station> targetStation = stationRepository.findById(request.getTarget());

        validateContains(lines, sourceStation, targetStation);

        Path path = pathFinder.findPath(lines, sourceStation.get(), targetStation.get());

        return PathResponse.of(path);
    }

    private void validateRequest(PathRequest request) {
        if (request.getSource().equals(request.getTarget())) {
            throw new IllegalArgumentException("같은 역은 조회할 수 없습니다.");
        }
    }

    private void validateContains(Lines lines, Optional<Station> sourceStation, Optional<Station> targetStation) {
        if (!sourceStation.isPresent() || !targetStation.isPresent()) {
            throw new StationNotFoundException("존재하지 않는 지하철 역입니다.");
        }

        if (!lines.contains(sourceStation.get()) || !lines.contains(targetStation.get())) {
            throw new StationNotFoundException("등록되지 않은 지하철 역입니다.");
        }
    }

}

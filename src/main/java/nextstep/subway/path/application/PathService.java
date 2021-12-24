package nextstep.subway.path.application;

import nextstep.subway.common.exception.NoResultException;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PathService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public PathService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional(readOnly = true)
    public PathResponse findPath(final Long sourceId, final Long targetId) {
        Station sourceStation = findStationById(sourceId);
        Station targetStation = findStationById(targetId);
        Path path = new Path(sourceStation, targetStation);
        Lines lines = findAllLines();
        List<Station> shortestPaths = path.findShortestPath(lines);
        return PathResponse.of(shortestPaths, path.findShortestDistance(lines, shortestPaths));
    }

    private Station findStationById(final Long id) {
        return stationRepository.findById(id)
                .orElseThrow(() -> new NoResultException("찾을 수 없는 역입니다."));
    }

    private Lines findAllLines() {
        return new Lines(lineRepository.findAll());
    }

}

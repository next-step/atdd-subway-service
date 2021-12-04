package nextstep.subway.path.application;

import lombok.RequiredArgsConstructor;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * packageName : nextstep.subway.path.application
 * fileName : PathService
 * author : haedoang
 * date : 2021/12/04
 * description :
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PathService {
    private final PathFinder pathFinder;
    private final StationRepository stationRepository;
    private final LineRepository lineRepository;

    public PathResponse getShortestPath(Long srcStationId, Long destStationId) {
        List<Station> stations = stationRepository.findAll();
        List<Line> lines = lineRepository.findAll();
        return pathFinder.getShortestPath(lines, stations, srcStationId, destStationId);
    }
}

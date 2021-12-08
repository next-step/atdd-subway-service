package nextstep.subway.path.application;

import lombok.RequiredArgsConstructor;
import nextstep.subway.member.dto.FavoriteRequest;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
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
    private final PathFinder pathHandler;
    private final StationRepository stationRepository;
    private final LineRepository lineRepository;

    public Path getShortestPath(Long srcStationId, Long destStationId) {
        List<Station> stations = stationRepository.findAll();
        List<Line> lines = lineRepository.findAll();
        return pathHandler.getShortestPath(lines, stations, srcStationId, destStationId);
    }

    public Path getShortestPath(FavoriteRequest request) {
        List<Station> stations = stationRepository.findAll();
        List<Line> lines = lineRepository.findAll();
        return pathHandler.getShortestPath(lines, stations, request.getSource(), request.getTarget());
    }
}

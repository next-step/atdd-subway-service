package nextstep.subway.path.application;

import lombok.RequiredArgsConstructor;
import nextstep.subway.auth.domain.User;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.line.domain.Money;
import nextstep.subway.line.domain.SubwayFare;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
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
    private final SubwayFare subwayFare;
    private final StationRepository stationRepository;
    private final LineRepository lineRepository;

    public Path getShortestPath(Long srcStationId, Long destStationId) {
        List<Station> stations = stationRepository.findAll();
        List<Line> lines = lineRepository.findAll();
        return pathFinder.getShortestPath(lines, stations, srcStationId, destStationId);
    }

    public PathResponse getShortestPath(Long srcStationId, Long destStationId, User user) {
        Path path = getShortestPath(srcStationId, destStationId);
        Money money = subwayFare.rateInquiry(path, user);
        return PathResponse.of(path, money);
    }

    public Path getShortestPath(FavoriteRequest request) {
        final List<Station> stations = stationRepository.findAll();
        final List<Line> lines = lineRepository.findAll();
        return pathFinder.getShortestPath(lines, stations, request.getSource(), request.getTarget());
    }

}

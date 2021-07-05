package nextstep.subway.path.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.exception.StationNotExistException;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.path.domain.ShortestPathFinder;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;

@Service
public class PathService {
    private StationRepository stationRepository;
    private LineRepository lineRepository;

    public PathService(StationRepository stationRepository, LineRepository lineRepository) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
    }

    public PathResponse findShortestPath(final PathRequest pathRequest) {
        ShortestPathFinder pathFinder = ShortestPathFinder.getDefault(new Lines(lineRepository.findAll()));

        return PathResponse.from(pathFinder.findShortestPath(findStation(pathRequest.getSource()), findStation(pathRequest.getTarget())));
    }

    public PathResponse findShortestPathForLoginMember(final LoginMember loginMember, final PathRequest pathRequest) {
        ShortestPathFinder pathFinder = ShortestPathFinder.getDefault(new Lines(lineRepository.findAll()));
        return PathResponse.of(loginMember.buildAgeDiscount(),
                pathFinder.findShortestPath(findStation(pathRequest.getSource()), findStation(pathRequest.getTarget())));
    }

    private Station findStation(Long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(StationNotExistException::new);
    }
}

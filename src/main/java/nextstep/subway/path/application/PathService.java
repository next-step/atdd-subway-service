package nextstep.subway.path.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.ui.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;

@Service
public class PathService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public PathService(final LineRepository lineRepository, final StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public PathResponse findShortestPath(final Long sourceId, final Long targetId, final int age) {
        Station sourceStation = findStationById(sourceId);
        Station targetStation = findStationById(targetId);
        PathFinder pathFinder = PathFinder.from(lineRepository.findAll());
        PathResponse pathResponse = pathFinder.getShortestPath(sourceStation, targetStation);
        pathResponse.applyDiscountFare(age);
        return pathResponse;
    }

    private Station findStationById(final Long id) {
        return stationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(id + "에 해당하는 Station을 찾을 수 없습니다."));
    }
}

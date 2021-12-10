package nextstep.subway.path.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.member.domain.Age;
import nextstep.subway.path.domain.DistanceFareCalculator;
import nextstep.subway.path.domain.Fare;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.domain.Paths;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class PathService {
    private static final String NOT_FOUND_SOURCE_ERROR_MESSAGE = "경로의 출발역을 찾지 못하였습니다.";
    private static final String NOT_FOUND_TARGET_ERROR_MESSAGE = "경로의 도착역을 찾지 못하였습니다.";

    private final LineRepository lineRepository;
    private final StationService stationService;

    public PathService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    @Transactional(readOnly = true)
    public PathResponse findPathBetween(LoginMember loginMember, PathRequest pathRequest) {
        final Paths paths = findPathBetween(pathRequest);
        return PathResponse.of(paths.getStations(), paths.getDistance(), paths.calculateFare(loginMember));
    }

    private Paths findPathBetween(PathRequest pathRequest) {
        final Station sourceStation = getSourceStation(pathRequest.getSource());
        final Station targetStation = getTargetStation(pathRequest.getTarget());
        final List<Line> allLines = lineRepository.findAll();
        return PathFinder.of(allLines)
                .findPathBetween(sourceStation, targetStation);
    }

    private Station getTargetStation(Long target) {
        return getStationWithErrorMessage(target, NOT_FOUND_TARGET_ERROR_MESSAGE);
    }

    private Station getSourceStation(Long source) {
        return getStationWithErrorMessage(source, NOT_FOUND_SOURCE_ERROR_MESSAGE);
    }

    private Station getStationWithErrorMessage(Long target, String errorMessage) {
        try {
            return stationService.findById(target);
        } catch (RuntimeException exception) {
            throw new IllegalArgumentException(errorMessage);
        }
    }
}

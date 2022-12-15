package nextstep.subway.path.application;

import nextstep.subway.line.domain.*;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class PathService {
    private final LineRepository lineRepository;
    private final StationService stationService;

    public PathService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    @Transactional(readOnly = true)
    public PathResponse findShortestPath(Long source, Long target, Integer age) {
        Lines lines = new Lines(lineRepository.findAll());
        Station sourceStation = stationService.findStationById(source);
        Station targetStation = stationService.findStationById(target);

        Path path = PathFinder.findShortestPath(lines, sourceStation, targetStation);
        return PathResponse.of(path, calculate(age, lines, path));
    }

    private Fare calculate(Integer age, Lines lines, Path path) {
        Fare fare = AddedFarePolicyByDistance.calculate(path.getDistance().value());
        if (Objects.nonNull(age)) {
            fare = DiscountPolicyByAge.calculate(fare, age);
        }
        fare = fare.plus(lines.getMaxAddedFare());
        return fare;
    }
}

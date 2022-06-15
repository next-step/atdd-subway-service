package nextstep.subway.path.application;

import java.util.List;
import nextstep.subway.auth.domain.Customer;
import nextstep.subway.fare.domain.Fare;
import nextstep.subway.fare.domain.FareCalculator;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PathService {
    private final StationService stationService;
    private final LineService lineService;

    public PathService(StationService stationService, LineService lineService) {
        this.stationService = stationService;
        this.lineService = lineService;
    }

    @Transactional(readOnly = true)
    public PathResponse findShortestPath(Customer customer, Long departureStationId, Long arrivalStationId) {
        Station departureStation = stationService.findById(departureStationId);
        Station arrivalStation = stationService.findById(arrivalStationId);
        List<Line> lines = lineService.findAll();
        PathFinder pathFinder = new PathFinder(lines);

        Path path = pathFinder.findPath(departureStation, arrivalStation);
        Fare fare = FareCalculator.calculate(path, customer);
        List<StationResponse> stations = stationService.findAllStationsByIds(path.getStations());
        return PathResponse.of(stations, path.getDistance(), fare.getValue());
    }
}

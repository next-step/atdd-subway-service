package nextstep.subway.path.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.amount.domain.Amount;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.domain.Stations;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

@Transactional(readOnly = true)
@Service
public class PathService {
    private final LineService lineService;
    private final StationService stationService;

    public PathService(LineService lineService, StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    public PathResponse findPath(LoginMember loginMember, Long source, Long target) {
        Lines lines = lineService.findAllLines();
        Station sourceStation = stationService.findStationById(source);
        Station targetStation = stationService.findStationById(target);

        Path path = new PathFinder(lines.getStations(), lines.getSections()).findPath(sourceStation, targetStation);

        Stations stations = path.getStations();
        Distance distance = path.getDistance();
        Amount amount = path.getAmount(lines, loginMember);

        return PathResponse.of(stations, distance, amount);
    }
}

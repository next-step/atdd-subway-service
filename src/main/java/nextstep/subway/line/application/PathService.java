package nextstep.subway.line.application;

import java.util.List;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Fare;
import nextstep.subway.line.domain.FareCalculate;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Navigation;
import nextstep.subway.line.dto.PathRequest;
import nextstep.subway.line.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.exception.StationNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class PathService {

    private final StationRepository stationRepository;
    private final LineRepository lineRepository;

    public PathService(StationRepository stationRepository, LineRepository lineRepository) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
    }

    public PathResponse findStationPath(final LoginMember loginMember, final PathRequest pathRequest) {
        final Station sourceStation = stationRepository.findById(pathRequest.getSource())
            .orElseThrow(StationNotFoundException::new);
        final Station targetStation = stationRepository.findById(pathRequest.getTarget())
            .orElseThrow(StationNotFoundException::new);

        final List<Line> lines = lineRepository.findAll();

        final Navigation navigation = Navigation.of(lines);
        final List<Station> fastPath = navigation.findFastPath(sourceStation, targetStation);
        final Distance totalDistance = navigation.getDistance();

        final Fare fare = FareCalculate.of(totalDistance)
                                       .calculate(fastPath, lines, loginMember);
        return PathResponse.ofList(fastPath, totalDistance, fare.longValue());
    }
}

package nextstep.subway.path.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.exception.EntityNotExistException;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.DijkstraShortestDistance;
import nextstep.subway.path.domain.FareCalculatorFacade;
import nextstep.subway.path.domain.ShortestDistance;
import nextstep.subway.path.dto.LinePathRequest;
import nextstep.subway.path.dto.LinePathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@Transactional(readOnly = true)
public class LinePathQueryService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LinePathQueryService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public LinePathResponse findShortDistance(LoginMember loginMember, LinePathRequest linePathRequest) {
        Station source = findStationById(linePathRequest.getSource());
        Station target = findStationById(linePathRequest.getTarget());

        ShortestDistance shortestDistance = new DijkstraShortestDistance(lineRepository.findAll(), source, target);

        return new LinePathResponse(
                shortestDistance.shortestRoute(),
                shortestDistance.shortestDistance(),
                FareCalculatorFacade.calcFare(loginMember, shortestDistance)
        );
    }

    private Station findStationById(Long id) {
        return stationRepository.findById(id)
                .orElseThrow(EntityNotExistException::new);
    }
}

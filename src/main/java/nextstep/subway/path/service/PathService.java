package nextstep.subway.path.service;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.fare.domain.Fare;
import nextstep.subway.fare.dto.FareRequest;
import nextstep.subway.fare.dto.FareResponse;
import nextstep.subway.fare.dto.PathWithFareResponse;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PathService {

    private final LineRepository lineRepository;
    private final PathFinder pathFinder;

    public PathService(LineRepository lineRepository, PathFinder pathFinder) {
        this.lineRepository = lineRepository;
        this.pathFinder = pathFinder;
    }

    public PathWithFareResponse findPath(LoginMember member, Long departureStationId, Long arrivalStationId) {
        checkEqualsDepartureArrival(departureStationId, arrivalStationId);
        Lines lines = findAllLine();
        Station departureStation = lines.searchStationById(departureStationId);
        Station arrivalStation = lines.searchStationById(arrivalStationId);
        PathResponse pathResponse = getPathResponse(lines, departureStation, arrivalStation);
        FareResponse fareResponse = getFareResponse(member, lines, pathResponse);
        return PathWithFareResponse.ofResponse(pathResponse.getStations(), pathResponse.getDistance(), fareResponse.getFare());
    }

    private PathResponse getPathResponse(Lines lines, Station departureStation, Station arrivalStation) {
        return pathFinder.ofPathResponse(new PathRequest(lines.allSection(), departureStation, arrivalStation));
    }

    private FareResponse getFareResponse(LoginMember member, Lines lines, PathResponse pathResponse) {
        return Fare.ofResponse(new FareRequest(pathResponse.getStations(), lines.getLines(), pathResponse.getDistance(), member.getAge()));
    }

    @Transactional(readOnly = true)
    public Lines findAllLine() {
        return new Lines(lineRepository.findAllJoinFetch());
    }

    private void checkEqualsDepartureArrival(Long source, Long target) {
        if (source.equals(target)) {
            throw new IllegalArgumentException("출발역과 도착역이 같습니다.");
        }
    }
}

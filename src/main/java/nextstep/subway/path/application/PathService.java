package nextstep.subway.path.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.common.ErrorCode;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.member.domain.Member;
import nextstep.subway.path.domain.Fare;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.domain.ShortestPathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.From;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class PathService {

    private final StationRepository stationRepository;
    private final LineRepository lineRepository;

    public PathService(StationRepository stationRepository, LineRepository lineRepository) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
    }

    public PathResponse findTheShortestPath(long sourceStationId, long targetStationId, LoginMember member) {
        Station sourceStation = findStationById(sourceStationId);
        Station targetStation = findStationById(targetStationId);
        Sections allSections = findAllSections();

        PathFinder pathFinder = ShortestPathFinder.from(allSections);
        List<Station> findStations = pathFinder.findAllStationsInTheShortestPath(sourceStation, targetStation);
        int distance = pathFinder.findTheShortestPathDistance(sourceStation, targetStation);

        return PathResponse.from(findStations, distance, calculateFare(allSections, findStations, distance, member));
    }

    private Station findStationById(long id) {
        return stationRepository.findById(id).orElseThrow((() -> new IllegalArgumentException(ErrorCode.NO_MATCH_STATION_EXCEPTION.getErrorMessage())));
    }

    private Sections findAllSections() {
        return new Sections(lineRepository.findAll()
                .stream()
                .flatMap(line -> line.getSections().stream())
                .collect(Collectors.toList()));
    }

    private long calculateFare(Sections sections, List<Station> stations, int distance, LoginMember member) {
        List<Line> lines = sections.findLines(stations);
        Fare fare = Fare.fromBaseFare(
                lines.stream()
                .map(Line::getAddFare)
                .max(Comparator.comparing(eachLineFare -> eachLineFare))
                .orElse(Fare.from().findFare()));
        return fare.currentFare(distance, member);
    }

}

package nextstep.subway.path.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.path.domain.FareCalculator;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class PathService {

    private final StationRepository stationRepository;
    private final LineRepository lineRepository;

    public PathService(StationRepository stationRepository, LineRepository lineRepository) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
    }

    public PathResponse findShortestPath(LoginMember loginMember, Long source, Long target) {
        List<Station> allStations = stationRepository.findAll();
        Lines lines = new Lines(lineRepository.findAll());

        PathFinder pathFinder = new PathFinder(allStations, lines);

        Station sourceStation = stationRepository.findById(source).orElseThrow(NoSuchElementException::new);
        Station targetStation = stationRepository.findById(target).orElseThrow(NoSuchElementException::new);

        List<Station> shortestPath = pathFinder.shortestPath(sourceStation, targetStation);
        int distance = pathFinder.shortestWeight(sourceStation, targetStation);
        Set<Long> lineIds = pathFinder.goThroughLinesId(sourceStation, targetStation);

        FareCalculator fareCalculator = new FareCalculator(distance, loginMember.getAge());

        int fare = fareCalculator.calculate(lineIds);

        return new PathResponse(
                shortestPath.stream().map(StationResponse::of).collect(Collectors.toList()),
                distance,
                fare);
    }
}

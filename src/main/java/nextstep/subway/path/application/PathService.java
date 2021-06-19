package nextstep.subway.path.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.ui.NoStationInListException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
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

    public PathResponse findShortestPath(Long source, Long target) {
        List<Station> allStations = stationRepository.findAll();
        List<Line> lines = lineRepository.findAll();

        PathFinder pathFinder = new PathFinder(allStations, lines);

        Station sourceStation = stationRepository.findById(source).orElseThrow(NoSuchElementException::new);
        Station targetStation = stationRepository.findById(target).orElseThrow(NoSuchElementException::new);

        validateValidStation(allStations, sourceStation, targetStation);

        List<Station> shortestPath = pathFinder.shortestPath(sourceStation, targetStation);
        int distance = pathFinder.shortestWeight(sourceStation, targetStation);

        return new PathResponse(
                shortestPath.stream().map(StationResponse::of).collect(Collectors.toList()),
                distance
        );
    }

    private void validateValidStation(List<Station> allStations, Station source, Station target) {
        if (!allStations.contains(source) || !allStations.contains(target)) {
            throw new NoStationInListException();
        }
    }
}

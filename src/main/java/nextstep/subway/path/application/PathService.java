package nextstep.subway.path.application;

import nextstep.subway.exception.EntityNotFoundException;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.message.ExceptionMessage;
import nextstep.subway.path.domain.DijkstraShortestPathFinder;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PathService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public PathService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public PathResponse findShortestPath(Long source, Long target) {
        Station sourceStation = findStationById(source);
        Station targetStation = findStationById(target);
        List<Section> sections = findAllSections();

        PathFinder pathFinder = DijkstraShortestPathFinder.from(sections);
        List<Station> stations = pathFinder.findAllStationsInShortestPath(sourceStation, targetStation);
        int distance = pathFinder.findShortestDistance(sourceStation, targetStation);

        return PathResponse.of(stations, distance);
    }

    private List<Section> findAllSections() {
        return lineRepository.findAll()
                .stream()
                .flatMap(line -> line.getSections().stream())
                .collect(Collectors.toList());
    }

    private Station findStationById(Long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.STATION_NOT_EXIST));
    }
}

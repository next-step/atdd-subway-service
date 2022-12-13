package nextstep.subway.path.application;

import nextstep.subway.common.ErrorCode;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.domain.ShortestPathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public PathResponse findTheShortestPath(long sourceStationId, long targetStationId) {
        Station sourceStation = findStationById(sourceStationId);
        Station targetStation = findStationById(targetStationId);
        List<Section> allSections = findAllSections();

        PathFinder pathFinder = ShortestPathFinder.from(allSections);
        List<Station> findStations = pathFinder.findAllStationsInTheShortestPath(sourceStation, targetStation);
        int distance = pathFinder.findTheShortestPathDistance(sourceStation, targetStation);

        return PathResponse.from(findStations, distance);
    }

    private Station findStationById(long id) {
        return stationRepository.findById(id).orElseThrow((() -> new IllegalArgumentException(ErrorCode.NO_MATCH_STATION_EXCEPTION.getErrorMessage())));
    }

    private List<Section> findAllSections() {
        return lineRepository.findAll()
                .stream()
                .flatMap(line -> line.getSections().stream())
                .collect(Collectors.toList());
    }
}

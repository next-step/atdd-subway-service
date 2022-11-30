package nextstep.subway.path.application;

import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PathService {
    private static final String NONE_EXISTS_STATION = "역이 존재하지 않습니다";
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public PathService(final LineRepository lineRepository, final StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public PathResponse findShortestPath(Long sourceId, Long targetId) {
        Station sourceStation = findStationById(sourceId);
        Station targetStation = findStationById(targetId);
        List<Station> stations = new PathFinder(lineRepository.findAll())
                .getShortestPath(sourceStation, targetStation);
        return new PathResponse(stations.stream().map(StationResponse::of).collect(Collectors.toList()));
    }

    private Station findStationById(Long id) {
        return stationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(NONE_EXISTS_STATION));
    }
}

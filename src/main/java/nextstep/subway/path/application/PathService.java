package nextstep.subway.path.application;

import nextstep.subway.exception.BadRequestException;
import nextstep.subway.exception.NotFoundStationException;
import nextstep.subway.path.domain.PathResult;
import nextstep.subway.path.domain.PathSelector;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class PathService {
    private final StationRepository stationRepository;

    public PathService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public PathResponse findShortestPath(Long source, Long target) {
        if (source.equals(target)) {
            throw new BadRequestException("출발역과 도착역은 달라야 합니다");
        }

        Station sourceStation = stationRepository.findById(source)
                .orElseThrow(NotFoundStationException::new);
        Station targetStation = stationRepository.findById(target)
                .orElseThrow(NotFoundStationException::new);
        PathResult result = PathSelector.select(sourceStation, targetStation);

        final Map<Long,Station> stations = stationRepository.findAll().stream()
                .collect(Collectors.toMap(Station::getId, it -> it));

        List<Long> shortestStationIds = result.getStationIds();
        int totalDistance = result.getTotalDistance();

        List<StationResponse> shortestStations = shortestStationIds.stream()
                .map(id -> StationResponse.of(stations.get(id)))
                .collect(Collectors.toList());
        return new PathResponse(shortestStations, totalDistance);
    }
}

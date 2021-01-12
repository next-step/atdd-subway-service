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
        PathResult result = selectPath(source, target);

        List<StationResponse> shortestStations = toStationResponses(result.getStationIds());
        int totalDistance = result.getTotalDistance();

        return new PathResponse(shortestStations, totalDistance);
    }

    private List<StationResponse> toStationResponses(List<Long> stationIds) {
        final Map<Long,Station> stations = stationRepository.findAll().stream()
                .collect(Collectors.toMap(Station::getId, it -> it));

        return stationIds.stream()
                .map(stations::get)
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    private PathResult selectPath(Long source, Long target) {
        if (source.equals(target)) {
            throw new BadRequestException("출발역과 도착역은 달라야 합니다");
        }

        Station sourceStation = stationRepository.getOne(source);
        Station targetStation = stationRepository.getOne(target);
        return PathSelector.select(sourceStation, targetStation);
    }
}

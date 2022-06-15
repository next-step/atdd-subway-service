package nextstep.subway.path.application;

import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.utils.Route;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class PathService {
    private final StationRepository stationRepository;

    public PathService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public PathResponse findShortestRoute(long sourceId, long targetId) {
        final Station startStation = stationRepository.findById(sourceId).orElseThrow(EntityNotFoundException::new);
        final Station endStation = stationRepository.findById(targetId).orElseThrow(EntityNotFoundException::new);
        return new Route().getShortestRoute(startStation, endStation);
    }
}

package nextstep.subway.path.application;

import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;

@Service
public class PathService {
    private StationRepository stationRepository;
    private LineRepository lineRepository;

    public PathService(StationRepository stationRepository, LineRepository lineRepository) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
    }

    public PathResponse findRouteSearch(Long source, Long target) {
        Station station1 = stationRepository.findById(source).orElseThrow(IllegalArgumentException::new);
        Station station2 = stationRepository.findById(target).orElseThrow(IllegalArgumentException::new);
        PathFinder path = new PathFinder(lineRepository);
        path.findRouteSearch(station1, station2);
        PathResponse response = new PathResponse(path.getStation(), path.getDistance());
        return response;
    }

}

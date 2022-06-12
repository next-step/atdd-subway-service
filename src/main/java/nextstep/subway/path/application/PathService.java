package nextstep.subway.path.application;

import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class PathService {

    private LineRepository lineRepository;
    private StationRepository stationRepository;

    public PathService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional(readOnly = true)
    public PathResponse findPath(PathRequest request) {
        request.checkValid();
        PathFinder pathFinder = new PathFinder(lineRepository.findAll());

        Station sourceStation = getStation(request.getSource());
        Station targetStation = getStation(request.getTarget());

        List<Station> pathStations = pathFinder.getPathStations(sourceStation, targetStation);
        int distance = pathFinder.getDistance(sourceStation, targetStation);

        return new PathResponse(pathStations, distance);
    }

    private Station getStation(Long source) {
        return stationRepository.findById(source)
                .orElseThrow(EntityNotFoundException::new);
    }

}

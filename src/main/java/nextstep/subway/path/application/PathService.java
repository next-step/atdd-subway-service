package nextstep.subway.path.application;

import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.exception.StationNotFoundException;
import nextstep.subway.path.exception.StationsSameException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public PathResponse findPath(Long source, Long target) {
        checkValid(source, target);
        PathFinder pathFinder = new PathFinder(lineRepository.findAll());

        Station sourceStation = getStation(source);
        Station targetStation = getStation(target);

        List<Station> pathStations = pathFinder.getPathStations(sourceStation, targetStation);
        int distance = pathFinder.getDistance(sourceStation, targetStation);

        return new PathResponse(pathStations, distance);
    }

    private void checkValid(Long source, Long target) {
        if (source == target) {
            throw new StationsSameException();
        }
    }

    private Station getStation(Long source) {
        return stationRepository.findById(source)
                .orElseThrow(StationNotFoundException::new);
    }

}

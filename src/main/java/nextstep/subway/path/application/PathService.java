package nextstep.subway.path.application;

import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathNavigation;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;

@Service
public class PathService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public PathService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public Path findShortestPath(long source, long target) {
        PathNavigation navigation = PathNavigation.by(lineRepository.findAll());
        Station sourceStation = stationRepository.findById(source).orElseThrow(RuntimeException::new);
        Station targetStation = stationRepository.findById(target).orElseThrow(RuntimeException::new);
        return navigation.findShortestPath(sourceStation, targetStation);
    }
}

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

    public Path findShortestPath(long source, long target, int age) {
        PathNavigation navigation = PathNavigation.by(lineRepository.findAll());
        Station sourceStation = getSourceStation(source);
        Station targetStation = getSourceStation(target);

        return navigation.findShortestPath(sourceStation, targetStation, age);
    }

    private Station getSourceStation(long source) {
        return stationRepository.findById(source).orElseThrow(RuntimeException::new);
    }
}

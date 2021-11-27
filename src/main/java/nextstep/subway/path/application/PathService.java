package nextstep.subway.path.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PathService {

    private final StationRepository stationRepository;
    private final LineRepository lineRepository;

    public PathService(StationRepository stationRepository, LineRepository lineRepository) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
    }

    public Path getShortestPath(int source, int target){
        Station sourceStation = stationRepository.findById((long) source).orElseThrow(() ->new IllegalArgumentException("존재하지 않는 역입니다."));
        Station targetStation = stationRepository.findById((long) target).orElseThrow(() ->new IllegalArgumentException("존재하지 않는 역입니다."));

        List<Line> lines = lineRepository.findAll();
        PathFinder pathFinder = PathFinder.from(lines);
        return pathFinder.getShortestPath(sourceStation, targetStation);
    }
}

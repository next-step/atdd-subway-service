package nextstep.subway.path.application;

import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.PathFindResult;
import nextstep.subway.path.domain.PathFindService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Component;

@Component
public class JGraphPathFindService implements PathFindService {

    private final LineRepository lineRepository;

    public JGraphPathFindService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    @Override
    public PathFindResult findShortestPath(Station startStation, Station endStation) {
        return null;
    }
}

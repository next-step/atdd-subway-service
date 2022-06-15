package nextstep.subway.line.application;

import nextstep.subway.exception.SameOriginDestinationException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.PathFinder;
import nextstep.subway.line.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PathService {
    private final LineService lineService;
    private final StationService stationService;

    public PathService(LineService lineService, StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    public PathResponse findShortestPath(Long source, Long target) {
        if (source.equals(target)) {
            throw new SameOriginDestinationException("출발지와 도착지가 같습니다.");
        }
        List<Line> lines = lineService.findAll();

        Station sourceStation = stationService.findById(source);
        Station targetStation = stationService.findById(target);

        return PathFinder.of().findShortest(lines, sourceStation, targetStation);
    }
}

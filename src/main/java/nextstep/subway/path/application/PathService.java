package nextstep.subway.path.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.dto.PathResponse;

import java.util.List;

@Service
@Transactional
public class PathService {

    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private StationService stationService;

    public PathResponse findShortestPath(Long source, Long target) {
        List<Line> lines = lineRepository.findAll();
        Station sourceStation = stationService.findById(source);
        Station targetStation = stationService.findById(target);
        return PathResponse.of(PathFinder.of(lines)
                .findPath(sourceStation, targetStation));
    }
}

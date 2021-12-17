package nextstep.subway.path.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

@Service
@Transactional
public class PathService {
    private LineRepository lineRepository;
    private StationService stationService;

    public PathService(final LineRepository lineRepository, final StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    public PathResponse computeShortestDistance(
        final Long sourceStationId, final Long targetStationId, final int memberAge
    ) {
        final List<Line> lines = lineRepository.findAll();
        final Station source = stationService.findById(sourceStationId);
        final Station target = stationService.findById(targetStationId);

        return PathFinder.computePath(lines, source, target, memberAge);
    }
}

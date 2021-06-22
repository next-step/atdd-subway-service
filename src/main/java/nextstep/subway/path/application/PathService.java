package nextstep.subway.path.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.exception.station.NoStation;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.LinePathSearch;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;

@Service
@Transactional
public class PathService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public PathService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional(readOnly = true)
    public PathResponse findPathBySourceAndTarget(Long sourceId, Long targetId) {
        List<Line> lines = lineRepository.findAll();

        Station source = stationRepository.findById(sourceId)
            .orElseThrow(() -> new NoStation(NoStation.NO_UPSTAION));

        Station target = stationRepository.findById(targetId)
            .orElseThrow(() -> new NoStation(NoStation.NO_DOWNSTATION));

        Path path = LinePathSearch.of(lines).searchPath(source, target);
        return PathResponse.of(path.getStations(), path.getMinDistance());
    }

}

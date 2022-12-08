package nextstep.subway.path.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class PathService {

    private final StationRepository stationRepository;
    private final LineRepository lineRepository;

    public PathService(StationRepository stationRepository, LineRepository lineRepository) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
    }

    public PathResponse getShortestDistance(long sourceStationId, long targetStationId) {
        Station sourceStation = stationRepository.findById(sourceStationId)
                .orElseThrow(EntityNotFoundException::new);
        Station targetStation = stationRepository.findById(targetStationId)
                .orElseThrow(EntityNotFoundException::new);

        List<Line> lines = lineRepository.findAllWithSections();
        LinePathGraph linePathGraph = new LinePathGraph(lines);
        LinePath path = linePathGraph.getShortestPath(sourceStation, targetStation);
        return PathResponse.of(path);
    }
}

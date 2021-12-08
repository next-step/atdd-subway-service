package nextstep.subway.path.application;

import nextstep.subway.exception.InputDataErrorCode;
import nextstep.subway.exception.InputDataErrorException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PathService {

    private LineRepository lineRepository;
    private StationRepository stationRepository;

    public PathService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional(readOnly = true)
    public PathResponse findPath(Long sourceId, Long targetId) {
        Station sourceStation = stationRepository.findById(sourceId)
                .orElseThrow(() -> new InputDataErrorException(InputDataErrorCode.THERE_IS_NOT_SEARCHED_STATION));

        Station targetStation = stationRepository.findById(targetId)
                .orElseThrow(() -> new InputDataErrorException(InputDataErrorCode.THERE_IS_NOT_SEARCHED_STATION));

        List<Line> allLines = lineRepository.findAll();
        Path path = new Path(sourceStation, targetStation);

        return PathResponse.of(path.findShortestPath(allLines), path.findShortestPathDistance(allLines));
    }
}

package nextstep.subway.path.application;

import java.util.List;
import nextstep.subway.constants.ErrorMessages;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.domain.Stations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PathService {
    private LineRepository lineRepository;
    private StationRepository stationRepository;

    public PathService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public PathResponse findPath(Long source, Long target) {
        List<Line> lines = lineRepository.findAll();
        Station sourceStation = stationRepository.findById(source)
                .orElseThrow(() -> new RuntimeException(ErrorMessages.STATION_DOES_NOT_EXIST));
        Station targetStation = stationRepository.findById(target)
                .orElseThrow(() -> new RuntimeException(ErrorMessages.STATION_DOES_NOT_EXIST));

        Stations pathStations = PathFinder.findPath(lines, sourceStation, targetStation);
        return PathResponse.from(pathStations);
    }
}

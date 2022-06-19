package nextstep.subway.path.apllication;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PathService {
    private StationService stationService;
    private LineService lineService;
    private Path path;

    public PathService(StationService stationService, LineService lineService, Path path) {
        this.stationService = stationService;
        this.lineService = lineService;
        this.path = path;
    }

    public PathResponse findPath(Long source, Long target) {
        Station sourceStation = stationService.findStationById(source);
        Station targetStation = stationService.findStationById(target);
        List<Line> lines = lineService.findAll();

        return path.find(lines, sourceStation, targetStation);
    }
}

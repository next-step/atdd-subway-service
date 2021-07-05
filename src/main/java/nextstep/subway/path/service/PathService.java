package nextstep.subway.path.service;

import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.path.domain.PathDestination;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.util.PathSearch;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional(readOnly = true)
public class PathService {
    private static final String STATIONS_NULL_NOT_ALLOWED = "역을 입력해야합니다.";
    private static final String STATIONS_DUPLICATED = "동일역을 검색할 수 없습니다";

    private final LineRepository lineRepository;
    private final StationService stationService;
    private final PathSearch pathSearch;

    public PathService(LineRepository lineRepository, StationService stationService, PathSearch pathSearch) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
        this.pathSearch = pathSearch;
    }

    public PathResponse findPaths(Long source, Long target) {
        validateStations(source, target);
        final Lines lines = new Lines(lineRepository.findAll());
        final Station sourceStation = stationService.findStation(source);
        final Station targetStation = stationService.findStation(target);
        final PathDestination pathDestination = new PathDestination(sourceStation, targetStation);
        return pathSearch.findPaths(lines, pathDestination);
    }

    private void validateStations(Long source, Long target) {
        if (Objects.isNull(source) || Objects.isNull(target)) {
            throw new IllegalArgumentException(STATIONS_NULL_NOT_ALLOWED);
        }
        if (Objects.equals(source, target)) {
            throw new IllegalArgumentException(STATIONS_DUPLICATED);
        }
    }
}

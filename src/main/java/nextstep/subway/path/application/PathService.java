package nextstep.subway.path.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.exception.station.NoStationException;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.domain.LinePathSearch;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

@Service
@Transactional
public class PathService {

    private final LineService lineService;
    private final StationService stationService;

    public PathService(LineService lineService, StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    @Transactional(readOnly = true)
    public PathResponse findPathBySourceAndTarget(Long sourceId, Long targetId) {
        List<Section> allSections = lineService.getAllSections();
        Station source = stationService.findByIdWithError(sourceId,
            new NoStationException(NoStationException.NO_UPSTAION));
        Station target = stationService.findByIdWithError(targetId,
            new NoStationException(NoStationException.NO_DOWNSTATION));
        Path path = LinePathSearch.of(allSections).searchPath(source, target);
        return PathResponse.of(path.getStations(), path.getMinDistance());
    }

}

package nextstep.subway.path.application;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.navigation.application.NavigationService;
import nextstep.subway.navigation.dto.NavigationResponse;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

import java.util.List;

import static nextstep.subway.common.Messages.SAME_SOURCE_TARGET_STATION;

@Service
public class PathService {

    private final StationService stationService;
    private final LineService lineService;
    private final NavigationService navigationService;

    public PathService(StationService stationService, LineService lineService, NavigationService navigationService) {
        this.stationService = stationService;
        this.lineService = lineService;
        this.navigationService = navigationService;
    }

    public PathResponse findShortestDistance(PathRequest pathRequest) {
        validateFindShortest(pathRequest);

        Station sourceStation = stationService.findStationById(pathRequest.getSource());
        Station targetStation = stationService.findStationById(pathRequest.getTarget());
        List<Line> persistLines = lineService.findPersistLines();

        NavigationResponse navigationResponse = navigationService.findShortest(persistLines, sourceStation, targetStation);
        return PathResponse.of(navigationResponse.getStations(), navigationResponse.getDistance());
    }

    private void validateFindShortest(PathRequest pathRequest) {
        if (pathRequest.getSource().equals(pathRequest.getTarget())) {
            throw new IllegalArgumentException(SAME_SOURCE_TARGET_STATION);
        }
    }
}

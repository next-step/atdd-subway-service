package nextstep.subway.path.application;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.PathFinderResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.dto.PathStationResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

@Service
public class PathService {

    private final LineService lineService;
    private final StationService stationService;
    private final PathFinder pathFinder;

    public PathService(LineService lineService, StationService stationService,
        PathFinder pathFinder) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.pathFinder = pathFinder;
    }

    public PathResponse getShortestPaths(Long source, Long target, LoginMember loginMember) {
        Station sourceStation = stationService.findStationById(source);
        Station targetStation = stationService.findStationById(target);
        Set<Section> allSection = lineService.findAllSection();
        PathFinderResponse pathFinderResponse =
            pathFinder.getShortestPaths(allSection, sourceStation, targetStation);
        return convertPathResponse(pathFinderResponse.getStations(), pathFinderResponse.getDistance());
    }

    private PathResponse convertPathResponse(List<Station> stations, double weight) {
        return new PathResponse(convertPathStationResponses(stations), (int)weight);
    }

    private List<PathStationResponse> convertPathStationResponses(List<Station> stations) {
        return stations.stream()
            .map(station -> PathStationResponse.of(station.getId(), station.getName(), station.getCreatedDate()))
            .collect(Collectors.toList());
    }
}

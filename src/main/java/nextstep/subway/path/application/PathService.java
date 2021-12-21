package nextstep.subway.path.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.map.application.SubwayMapService;
import nextstep.subway.map.domain.SubwayMap;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

@Service
public class PathService {

    private final StationService stationService;
    private final SubwayMapService subwayMapService;

    public PathService(StationService stationService, SubwayMapService subwayMapService) {
        this.stationService = stationService;
        this.subwayMapService = subwayMapService;
    }

    public PathResponse findShortestPath(PathRequest pathRequest, LoginMember loginMember) {
        Station source = stationService.findStationById(pathRequest.getSource());
        Station target = stationService.findStationById(pathRequest.getTarget());

        SubwayMap subwayMap = subwayMapService.getMap();
        return subwayMap.findShortestPath(source, target, loginMember);
    }
}

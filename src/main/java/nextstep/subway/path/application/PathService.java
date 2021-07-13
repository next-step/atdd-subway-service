package nextstep.subway.path.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.SubwayCharge;
import nextstep.subway.path.domain.SubwayPath;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.module.JgraphtModule;
import nextstep.subway.path.module.PathModule;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PathService {

    private static final String FIND_SAME_STATION_ERROR = "두 역은 같은 역일 수 없습니다.";

    private final LineService lineService;
    private final StationService stationService;
    private final PathModule pathModule;

    public PathService(LineService lineService, StationService stationService, JgraphtModule jgraphtModule) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.pathModule = jgraphtModule;
    }

    public PathResponse findPath(LoginMember loginMember, Long sourceId, Long targetId) {
        validateFindStations(sourceId, targetId);

        List<Line> lines = lineService.fineAllLines();
        Station sourceStation = stationService.findStationById(sourceId);
        Station targetStation = stationService.findStationById(targetId);
        SubwayPath subwayPath = pathModule.findPath(lines, sourceStation, targetStation);
        SubwayCharge subwayCharge = new SubwayCharge(subwayPath, loginMember.getAge());
        return new PathResponse(StationResponse.ofList(subwayPath.stations()), subwayPath.distance(), subwayCharge.charge());
    }

    private void validateFindStations(Long sourceId, Long targetId) {
        if (sourceId.equals(targetId)) {
            throw new IllegalArgumentException(FIND_SAME_STATION_ERROR);
        }
    }
}

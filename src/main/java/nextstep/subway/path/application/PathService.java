package nextstep.subway.path.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.application.LineService;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.domain.PathResult;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PathService {

    private final StationService stationService;
    private final LineService lineService;
    private final ExtraChargeService extraChargeService;

    public PathService(StationService stationService, LineService lineService, ExtraChargeService extraChargeService) {
        this.stationService = stationService;
        this.lineService = lineService;
        this.extraChargeService = extraChargeService;
    }

    @Transactional(readOnly = true)
    public PathResponse getShortestPath(LoginMember loginMember, long sourceId, long targetId) {
        final Station sourceStation = stationService.findById(sourceId);
        final Station targetStation = stationService.findById(targetId);

        validSameStation(sourceStation, targetStation);

        final PathFinder pathFinder = new PathFinder();
        final PathResult result = pathFinder.findShortestPath(lineService.findPathBag(), sourceStation, targetStation);

        return PathResponse.from(result,
                loginMember.getCharge()
                        .plus(result.getMaxLineCharge())
                        .plus(extraChargeService.getExtraChargeFromDistance(result.getDistance())));
    }

    private void validSameStation(Station sourceStation, Station targetStation) {
        if (sourceStation.equals(targetStation)) {
            throw new IllegalArgumentException("출발역과 도착역은 서로 다른역이어야 합니다");
        }
    }
}

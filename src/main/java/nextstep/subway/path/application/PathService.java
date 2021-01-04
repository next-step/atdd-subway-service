package nextstep.subway.path.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.path.domain.*;
import nextstep.subway.path.domain.adapters.SafeLineAdapter;
import nextstep.subway.path.domain.adapters.SafeStationAdapter;
import nextstep.subway.path.domain.fee.distanceFee.DistanceFee;
import nextstep.subway.path.domain.fee.distanceFee.DistanceFeeSelector;
import nextstep.subway.path.domain.fee.transferFee.LineOfStationInPaths;
import nextstep.subway.path.domain.fee.transferFee.TransferLines;
import nextstep.subway.path.ui.dto.PathResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class PathService {
    private final SafeLineAdapter safeLineAdapter;
    private final SafeStationAdapter safeStationAdapter;

    public PathService(SafeLineAdapter safeLineAdapter, SafeStationAdapter safeStationAdapter) {
        this.safeLineAdapter = safeLineAdapter;
        this.safeStationAdapter = safeStationAdapter;
    }

    @Transactional(readOnly = true)
    public PathResponse findShortestPath(Long sourceId, Long destinationId, LoginMember loginMember) {
        List<Long> allStationIds = safeLineAdapter.getAllStationIds();
        List<SafeSectionInfo> allSafeSectionInfos = safeLineAdapter.getAllSafeSectionInfos();

        PathFinder pathFinder = PathFinder.of(allStationIds, allSafeSectionInfos);
        ShortestPath shortestPath = pathFinder.findShortestPath(sourceId, destinationId);

        List<Long> pathStations = shortestPath.getPathStations();
        List<SafeStationInfo> safeStationInfos = safeStationAdapter.findStationsById(pathStations);

        LineOfStationInPaths lineOfStationInPaths = safeLineAdapter.getLineOfStationInPaths(pathStations);
        Fee fee = shortestPath.calculateFee(lineOfStationInPaths, loginMember);

        return PathResponse.of(safeStationInfos, shortestPath.calculateTotalDistance(), fee.calculate());
    }
}

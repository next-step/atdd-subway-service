package nextstep.subway.path.application;

import java.util.List;
import nextstep.subway.auth.domain.discount.DiscountPolicy;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.ShortestPathFinder;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PathService {

    private final LineService lineService;
    private final StationService stationService;

    public PathService(LineService lineService, StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    public PathResponse findShortestPath(PathRequest request,
                                         DiscountPolicy discountPolicy) {
        List<Line> lines = lineService.findAll();
        Station source = stationService.findStationById(request.getSource());
        Station target = stationService.findStationById(request.getTarget());

        ShortestPathFinder shortestPathFinder = new ShortestPathFinder();
        Path shortestPath = shortestPathFinder.findShortestPath(lines, source, target);
        shortestPath.addMaxExtraCostInLines();
        shortestPath.applyDiscountPolicy(discountPolicy);

        return new PathResponse(shortestPath);
    }
}

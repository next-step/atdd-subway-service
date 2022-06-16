package nextstep.subway.line.application;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.line.utils.PathFinder.DijkstraWeightedEdgeWithLine;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.utils.PathFinder.PathFinder;
import nextstep.subway.line.dto.PathResponse;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.springframework.stereotype.Service;

@Service
public class PathServiceFacade {

    private final StationService stationService;
    private final LineService lineService;
    private final PathPriceCalculator pathPriceCalculator;
    private final MemberService memberService;

    public PathServiceFacade(StationService stationService, LineService lineService,
        PathPriceCalculator pathPriceCalculator, MemberService memberService) {
        this.stationService = stationService;
        this.lineService = lineService;
        this.pathPriceCalculator = pathPriceCalculator;
        this.memberService = memberService;
    }

    public PathResponse findPath(long srcStationId, long dstStationId, long memberId) {
        Station srcStation = stationService.findById(srcStationId);
        Station dstStation = stationService.findById(dstStationId);
        Member member = memberService.findById(memberId);
        List<Line> lines = lineService.findAllLines();

        PathFinder pathFinder = new PathFinder(lines);

        GraphPath path = pathFinder.findPath(srcStation, dstStation);

        List<Line> passLines = (List<Line>) path.getEdgeList().stream()
            .map(o -> ((DijkstraWeightedEdgeWithLine) o).getLine())
            .collect(Collectors.toList());

        int price = pathPriceCalculator.getPrice((int) path.getWeight(), passLines, member);
        return PathResponse.of(path.getVertexList(), path.getWeight(), price);
    }
}

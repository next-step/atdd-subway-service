package nextstep.subway.path.domain;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.jgrapht.GraphPath;

public class Path {
    private final List<Station> stations;
    private final int distance;
    private final Fee fee;

    public Path(List<Station> stations, int distance, Fee fee) {
        this.stations = stations;
        this.distance = distance;
        this.fee = fee;
    }

    public static Path findShortestPath(List<Line> lines, Station source, Station target) {
        PathFinder pathFinder = pathFinderInit(lines);
        GraphPath<Station, SectionWeightedEdge> graphPath = pathFinder.getDijkstraPath(source, target);

        Fee fee = Fee.defaultFee();
        int distance = (int) graphPath.getWeight();
        calculateFee(graphPath, fee, distance);

        return new Path(graphPath.getVertexList(), distance, fee);
    }

    private static PathFinder pathFinderInit(List<Line> lines) {
        PathFinder pathFinder = PathFinder.create();
        pathFinder.init(lines);
        return pathFinder;
    }

    private static Set<Line> getPathLines(GraphPath<Station, SectionWeightedEdge> graphPath) {
        return graphPath.getEdgeList()
                .stream()
                .map(SectionWeightedEdge::getLine)
                .collect(Collectors.toSet());
    }

    private static void calculateFee(GraphPath<Station, SectionWeightedEdge> graphPath, Fee fee, int distance) {
        final FeeHandler lineExtraFeeHandler = new LineExtraFeeHandler(null, getPathLines(graphPath));
        FeeHandler feeHandler = new DistanceFeeHandler(lineExtraFeeHandler, distance);
        feeHandler.calculate(fee);
    }

    public void discountFee(LoginMember loginMember) {
        final FeeHandler feeHandler = new DiscountFeeHandler(null, loginMember.getAge());
        feeHandler.calculate(this.fee);
    }

    public PathResponse toPathResponse() {
        List<StationResponse> stationsResponse = this.stations.stream()
                .map(StationResponse::of).collect(Collectors.toList());
        return new PathResponse(stationsResponse, this.distance, this.fee);
    }
}

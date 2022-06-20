package nextstep.subway.path.domain;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

public class Path {
    private List<Station> stations;
    private int distance;
    private Set<Line> lines;
    private Fee fee;

    public Path(List<Station> stations, int distance, Set<Line> lines) {
        this.stations = stations;
        this.distance = distance;
        this.lines = lines;
    }

    public void calculateFee(LoginMember member) {
        this.fee = new Fee();
        final FeeHandler discountFeeHandler = new DiscountFeeHandler(null, member.getAge());
        final FeeHandler lineExtraFeeHandler = new LineExtraFeeHandler(discountFeeHandler, lines);
        FeeHandler feeHandler = new DistanceFeeHandler(lineExtraFeeHandler, distance);
        feeHandler.calculate(this.fee);
    }

    public PathResponse toPathResponse() {
        List<StationResponse> stationsResponse = this.stations.stream()
                .map(StationResponse::of).collect(Collectors.toList());
        return new PathResponse(stationsResponse, this.distance, this.fee);
    }

    public List<Station> throughStations() {
        return stations;
    }

    public int totalDistance() {
        return distance;
    }

    public Set<Line> throughLines() {
        return lines;
    }
}

package nextstep.subway.path.domain;

import java.util.List;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Stations;
import nextstep.subway.path.domain.fare.Fare;
import nextstep.subway.path.domain.fare.FarePolicy;
import nextstep.subway.path.domain.fare.FarePolicyFactory;
import nextstep.subway.path.domain.fare.TotalFarePolicy;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;
import nextstep.subway.station.dto.StationResponses;

public class PathAssembler {
    private PathAssembler() {
    }

    public static PathResponse writeResponse(LoginMember loginMember, PathFinder pathFinder) {
        Stations stations = pathFinder.getStations();

        List<StationResponse> responses = StationResponses.from(stations.getStations())
            .getResponses();

        int distance = pathFinder.getDistance().getValue();

        FarePolicy<Integer> distanceFairPolicy = FarePolicyFactory.createDistanceFairPolicy();
        FarePolicy<Fare> ageFairPolicy = FarePolicyFactory.createAgeFairPolicy(loginMember);
        TotalFarePolicy totalFairPolicy = FarePolicyFactory.createTotalFairPolicy(distanceFairPolicy, ageFairPolicy);
        Fare fare = totalFairPolicy.calculateFare(distance);
        return new PathResponse(responses, distance, fare.getFare());
    }

}

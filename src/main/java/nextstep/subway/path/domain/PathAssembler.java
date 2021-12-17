package nextstep.subway.path.domain;

import java.util.List;

import nextstep.subway.line.domain.Stations;
import nextstep.subway.path.application.FarePolicy;
import nextstep.subway.path.domain.fare.Fare;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;
import nextstep.subway.station.dto.StationResponses;

public class PathAssembler {
    private PathAssembler() {
    }

    public static PathResponse writeResponse(PathFinder pathFinder, FarePolicy farePolicy) {
        Stations stations = pathFinder.getStations();

        List<StationResponse> responses = StationResponses.from(stations.getStations())
            .getResponses();

        int distance = pathFinder.getDistance().getValue();

        Fare fare = farePolicy.calculateFare(distance);

        return new PathResponse(responses, distance, fare.getFare());
    }

}

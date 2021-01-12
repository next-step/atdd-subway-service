package nextstep.subway.path.application.fare;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.OptionalInt;

@Service
public class FareCalculator {

    private static final int BASIC_FARE = 1250;

    public int calculateFare(int distance) {
        return BASIC_FARE
                + DistanceFarePolicy.valueOf(distance).calculateOverFare(distance);
    }

    public int calculateFare(int distance, List<Line> lines, List<Station> stations) {
        return BASIC_FARE
                + DistanceFarePolicy.valueOf(distance).calculateOverFare(distance)
                + getMaxLineOverFare(lines, stations);
    }

    private int getMaxLineOverFare(List<Line> lines, List<Station> stations) {
        return lines.stream()
                .filter(line -> line.containsAnyStation(stations))
                .mapToInt(Line::getOverFare)
                .max().orElse(0);
    }
}

package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class PathFare {
    private static final int BASIC_FARE = 1250;
    private static final int ADDED_FARE = 100;
    private static final int TEN_KILOMETER = 10;
    private static final int FIFTY_KILOMETER = 50;
    private static final double FIVE = 5.0;
    private static final double EIGHT = 8.0;

    public int calculateFare(List<DefaultWeightedEdge> edges, List<Section> sections) {
        AtomicReference<Line> line = new AtomicReference<>();
        AtomicInteger surcharge = new AtomicInteger(0);
        AtomicInteger distance = new AtomicInteger(0);

        edges.forEach(
                edge -> {
                    String[] stations = edge.toString().split(" : ");
                    Long upStation = parseUpStation(stations[0]);
                    Long downStation = parseDownStation(stations[1]);

                    Optional<Section> section = sections.stream()
                                                        .filter(sec -> sec.containsUpDownStations(upStation, downStation))
                                                        .findFirst();
                    if (!section.isPresent()) {
                        return;
                    }
                    line.set(section.get().getLine());
                    surcharge.set(findMaxSurcharge(surcharge.intValue(), line.get().getSurcharge()));
                    distance.addAndGet(section.get().getDistance());
                });

        return sum(distance.intValue(), surcharge.intValue());
    }

    private Long parseUpStation(String station) {
        return Long.parseLong(station.substring(station.indexOf("=") + 1, station.indexOf(",")));
    }

    private Long parseDownStation(String station) {
        return Long.parseLong(station.substring(station.indexOf("=") + 1, station.indexOf(",")));
    }

    private int findMaxSurcharge(int surcharge, int lineSurcharge) {
        return Math.max(surcharge, lineSurcharge);
    }

    private int sum(int distance, int surcharge) {
        if (distance > TEN_KILOMETER) {
            return BASIC_FARE + surcharge + calculateOverFare(distance);
        }
        return BASIC_FARE + surcharge;
    }

    private int calculateOverFare(int distance) {
        if (distance > FIFTY_KILOMETER) {
            int over50km = distance - FIFTY_KILOMETER;
            int over10To50km = distance - over50km - TEN_KILOMETER;
            return addFarePerFiveKilometer(over10To50km) + addFarePerEightKilometer(over50km);
        }
        return addFarePerFiveKilometer(distance - TEN_KILOMETER);
    }

    private int addFarePerFiveKilometer(int distance) {
        return (int) (Math.ceil(distance / FIVE) * ADDED_FARE);
    }

    private int addFarePerEightKilometer(int distance) {
        return (int) (Math.ceil(distance / EIGHT) * ADDED_FARE);
    }
}

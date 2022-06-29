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
        if (distance > 10) {
            return 1250 + surcharge + calculateOverFare(distance);
        }
        return 1250 + surcharge;
    }

    private int calculateOverFare(int distance) {
        if (distance > 50) {
            int over10km = distance - (distance - 50) - 10;
            int over50km = distance - 50;
            return (int) (Math.ceil(over10km / 5.0) * 100) + (int) (Math.ceil(over50km / 8.0) * 100);
        }
        return (int) (Math.ceil((distance - 10.0) / 5)) * 100;
    }
}

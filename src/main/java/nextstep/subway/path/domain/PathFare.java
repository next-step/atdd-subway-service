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
    // 기본운임(10㎞ 이내) : 기본운임 1,250원
    // 10km초과∼50km까지(5km마다 100원)
    // 50km초과 시 (8km마다 100원)

    // 추가 요금이 있는 노선을 이용 할 경우 측정된 요금에 추가
    // 900원 추가 요금이 있는 노선 8km 이용 시 1,250원 -> 2,150원
    // 900원 추가 요금이 있는 노선 12km 이용 시 1,350원 -> 2,250원

    // 경로 중 추가요금이 있는 노선을 환승 하여 이용 할 경우 가장 높은 금액의 추가 요금만 적용
    // 0원, 500원, 900원의 추가 요금이 있는 노선들을 경유하여 8km 이용 시 1,250원 -> 2,150원

    // 청소년: 운임에서 350원을 공제한 금액의 20%할인
    // 청소년: 13세 이상~19세 미만

    // 어린이: 운임에서 350원을 공제한 금액의 50%할인
    // 어린이: 6세 이상~ 13세 미만

    public PathFare() {
    }

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
                    if (surcharge.intValue() < line.get().getSurcharge()) {
                        surcharge.set(line.get().getSurcharge());
                    }
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

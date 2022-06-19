package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;

import java.util.List;
import java.util.stream.Collectors;

public class Fare {
    private int value;

    private Fare(Path path) {
        this.value = calculateOverFare(path.getDistance()) + calculateLineFare(path.getSections());
    }

    private int calculateOverFare(int distance) {
        FareDistanceType distanceType = FareDistanceType.typeOf(distance);
        return distanceType.calculateFare(distance);
    }

    private int calculateLineFare(List<Section> sections) {
        List<Line> lines = sections.stream()
                .map(section -> section.getLine())
                .distinct()
                .collect(Collectors.toList());
        return lines.stream()
                .map(line -> line.getAdditionalFare())
                .max(Integer::compareTo)
                .orElseThrow(IllegalArgumentException::new);
    }

    public static Fare from(Path path) {
        return new Fare(path);
    }

    public int getValue() {
        return value;
    }
}

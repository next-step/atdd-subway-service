package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;

import java.util.List;
import java.util.stream.Collectors;

public class Fare {
    private int value;

    private Fare(Path path, Integer age) {
        value = calculateDistanceFare(path.getDistance()) + calculateLineFare(path.getSections());
        if (age != null) {
            discountFareWithAge(age);
        }
    }

    public static Fare of(Path path, Integer age) {
        return new Fare(path, age);
    }

    private int calculateDistanceFare(int distance) {
        FareDistancePolicyType policy = FareDistancePolicyType.of(distance);
        return policy.calculateFare(distance);
    }

    private int calculateLineFare(List<Section> sections) {
        List<Line> lines = findDistinctLines(sections);
        return lines.stream()
                .map(line -> line.getAdditionalFare().getValue())
                .max(Integer::compareTo)
                .orElseThrow(IllegalArgumentException::new);
    }

    private List<Line> findDistinctLines(List<Section> sections) {
        return sections.stream()
                .map(section -> section.getLine())
                .distinct()
                .collect(Collectors.toList());
    }

    private void discountFareWithAge(int age) {
        FareAgePolicyType policy = FareAgePolicyType.of(age);
        value = policy.discountFare(value);
    }

    public int getValue() {
        return value;
    }
}

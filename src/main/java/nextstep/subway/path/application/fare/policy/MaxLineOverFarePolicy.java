package nextstep.subway.path.application.fare.policy;

import nextstep.subway.line.domain.Section;

import java.util.List;

public class MaxLineOverFarePolicy {

    private final List<Section> sections;

    public MaxLineOverFarePolicy(List<Section> sections) {
        this.sections = sections;
    }

    public int getMaxLineOverFare() {
        return sections.stream()
                .mapToInt(section -> section.getLine().getOverFare())
                .max().orElse(0);
    }

    public int calculateFare(int fare) {
        return fare + getMaxLineOverFare();
    }
}

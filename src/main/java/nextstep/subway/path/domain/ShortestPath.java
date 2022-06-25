package nextstep.subway.path.domain;

import nextstep.subway.line.domain.*;
import nextstep.subway.station.domain.Station;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ShortestPath {
    private static final int FIRST_INDEX = 0;
    private static final int ZERO = 0;
    private final List<Section> sections;

    private final OperationCostPolicy costPolicy = new DistanceCostPolicy();

    public ShortestPath(List<Section> sections) {
        if (Objects.isNull(sections) || sections.isEmpty()) {
            throw new IllegalArgumentException("빈값이나 null 를 입력 받을수 없다");
        }
        this.sections = sections;
    }


    public List<Station> stations() {
        List<Station> result = new ArrayList<>();
        result.add(sections.get(FIRST_INDEX).getUpStation());
        sections.forEach(section -> result.add(section.getDownStation()));
        return result;
    }

    public Distance totalDistance() {
        return sections.stream().map(Section::getDistance).reduce(new Distance(ZERO), Distance::plus);
    }

    public Charge totalCharge() {
        Charge extraCharge = sections.stream()
                .map(Section::getLine)
                .distinct()
                .map(Line::getExtraCharge)
                .sorted()
                .findFirst()
                .orElseThrow(IllegalStateException::new);

        return this.totalDistance().calculate(costPolicy).plus(extraCharge);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShortestPath that = (ShortestPath) o;
        return Objects.equals(sections, that.sections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sections);
    }
}

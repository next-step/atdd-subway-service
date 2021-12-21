package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;

import java.util.List;

public class LineFareCalculator {

    public static Fare calculateByLine(List<SectionWeightedEdge> edgeList) {
        int sum = edgeList.stream()
                .map(SectionWeightedEdge::getLine)
                .distinct()
                .mapToInt(Line::getAdditionalFare)
                .sum();

        return new Fare(sum);
    }

}

package nextstep.subway.fare.policy;

import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.SectionEdge;

import java.util.Comparator;
import java.util.NoSuchElementException;

public class LineSurchargeCalculator implements SurchargeCalculator {

    @Override
    public int calculate(Path shortestPath) {
        return shortestPath.getSectionEdges().stream()
                .max(Comparator.comparing(SectionEdge::getLineSurcharge))
                .orElseThrow(NoSuchElementException::new)
                .getLineSurcharge();
    }
}

package nextstep.subway.fare.policy.path;

import static java.util.Comparator.*;

import nextstep.subway.fare.domain.Fare;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.Path;

public class LineAdditionalPolicy implements PathAdditionalPolicy {

    LineAdditionalPolicy() {
    }

    @Override
    public Fare apply(Fare fare, Path path) {
        return fare.add(path.getPassingLines()
            .stream()
            .map(Line::additionalFare)
            .max(naturalOrder())
            .orElse(Line.NO_ADDITIONAL_FARE));
    }
}

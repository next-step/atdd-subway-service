package nextstep.subway.fare.policy.path;

import static java.util.Comparator.*;

import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.Path;

public class LineFarePolicy implements AdditionalFarePolicy {

    @Override
    public int applyPolicy(Path path) {
        return path.getPassingLines()
            .stream()
            .map(Line::additionalFare)
            .max(naturalOrder())
            .orElse(Line.NO_ADDITIONAL_FARE);
    }
}

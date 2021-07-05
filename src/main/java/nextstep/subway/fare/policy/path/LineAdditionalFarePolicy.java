package nextstep.subway.fare.policy.path;

import static java.util.Comparator.*;

import nextstep.subway.fare.policy.FarePolicy;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.Path;

public class LineAdditionalFarePolicy implements PathAdditionalFarePolicy {
    @Override
    public FarePolicy getPolicy(Path path) {
        return fare -> fare.add(path.getPassingLines()
            .stream()
            .map(Line::additionalFare)
            .max(naturalOrder())
            .orElse(Line.NO_ADDITIONAL_FARE));
    }
}

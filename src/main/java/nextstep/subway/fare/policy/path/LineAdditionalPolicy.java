package nextstep.subway.fare.policy.path;

import static java.util.Comparator.*;

import nextstep.subway.fare.policy.FarePolicy;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.Path;

public class LineAdditionalPolicy extends PathAdditionalPolicy {

    LineAdditionalPolicy() {
    }

    @Override
    public FarePolicy of(Object object) {
        checkPathObject(object);
        return fare -> fare.add(((Path)object)
            .getPassingLines()
            .stream()
            .map(Line::additionalFare)
            .max(naturalOrder())
            .orElse(Line.NO_ADDITIONAL_FARE));
    }
}

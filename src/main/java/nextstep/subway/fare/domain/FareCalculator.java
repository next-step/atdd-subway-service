package nextstep.subway.fare.domain;

import java.util.Set;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Path;

public class FareCalculator {
    public static Fare caculateFare(Set<Line> lines, Path path) {
        return Fare.valueOf(100);
    }

}

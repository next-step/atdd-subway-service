package nextstep.subway.path.fare.extra;

import nextstep.subway.path.domain.Path;
import nextstep.subway.path.fare.Fare;

public interface ExtraFareStrategy {
    Fare calculate(Path path);
}

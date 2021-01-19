package nextstep.subway.path.domain.fare.distance;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.path.domain.fare.FareUserAge;
import nextstep.subway.path.domain.fare.Money;

public interface DistanceFare {
	Money calculate(final Distance distance, final FareUserAge ageFare);
}

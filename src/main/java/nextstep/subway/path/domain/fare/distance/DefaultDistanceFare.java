package nextstep.subway.path.domain.fare.distance;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.path.domain.fare.FareUserAge;
import nextstep.subway.path.domain.fare.Money;

public class DefaultDistanceFare implements DistanceFare {

	@Override
	public Money calculate(final Distance distance, FareUserAge ageFare) {
		return ageFare.getBasicFare();
	}
}

package nextstep.subway.fare.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import nextstep.subway.fare.exception.FareException;
import nextstep.subway.line.domain.Distance;

public class DistanceFareCalculator {

	private List<DistanceFarePolicy> distanceFarePolicies;
	private static DistanceFareCalculator instance;

	private DistanceFareCalculator() {
		distanceFarePolicies = new ArrayList<>();
		distanceFarePolicies.add(new DefaultDistanceFarePolicy());
		distanceFarePolicies.add(new NearDistanceFarePolicy());
		distanceFarePolicies.add(new FarDistanceFarePolicy());
	}

	public static DistanceFareCalculator getInstance() {
		if (Objects.isNull(instance)) {
			instance = new DistanceFareCalculator();
		}
		return instance;
	}

	public Fare calculate(Distance distance) {
		return getDistanceFarePolicy(distance).calculate(distance);
	}

	private DistanceFarePolicy getDistanceFarePolicy(Distance distance) {
		return distanceFarePolicies.stream()
			.filter(policy -> policy.isAccepted(distance))
			.findFirst()
			.orElseThrow(FareException::new);
	}
}

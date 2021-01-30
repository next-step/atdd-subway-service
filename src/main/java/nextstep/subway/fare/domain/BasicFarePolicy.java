package nextstep.subway.fare.domain;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.fare.dto.Fare;
import nextstep.subway.path.dto.Path;

@Component
@Order(1)
public class BasicFarePolicy implements FarePolicy {
	public static int BASIC_FARE = 1_250;

	@Override
	public Fare calculate(LoginMember member, Path path) {
		return Fare.from(BASIC_FARE);
	}

}

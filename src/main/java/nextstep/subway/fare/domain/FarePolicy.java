package nextstep.subway.fare.domain;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.fare.dto.Fare;
import nextstep.subway.path.dto.Path;

public interface FarePolicy {
	Fare calculate(LoginMember member, Path path);
}

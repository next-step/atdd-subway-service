package nextstep.subway.fare.domain;

import nextstep.subway.fare.dto.Fare;
import nextstep.subway.path.dto.Path;

public interface FarePolicy {
	Fare calculate(Path path);
}

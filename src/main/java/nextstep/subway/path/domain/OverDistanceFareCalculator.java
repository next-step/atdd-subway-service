package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Fare;

interface OverDistanceFareCalculator {
	Fare calculateOverDistanceFare(int totalDistance);
}

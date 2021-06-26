package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Fare;

interface DistanceFareStrategy {
	Fare calculateDistanceFare(int totalDistance);
}

package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;

public class FareCalculator {

	public void calculateFare(Path path, LoginMember loginMember) {
		int distance = (int)path.getDistance();
		int fare = DistanceFare.findByDistance(distance).calculateFare(distance);
	}
}

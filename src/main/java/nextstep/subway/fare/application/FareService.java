package nextstep.subway.fare.application;

import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.fare.domain.AgeFare;
import nextstep.subway.fare.domain.DefaultFare;
import nextstep.subway.fare.domain.DistanceFare;
import nextstep.subway.fare.domain.Fare;
import nextstep.subway.fare.domain.LineFare;
import nextstep.subway.path.domain.shortest.ShortestPath;

@Service
public class FareService {

	public Fare calculate(LoginMember loginMember, ShortestPath shortestPath) {
		final Fare generalFare = Fare.of(
			Stream.of(
				DefaultFare.calculate(),
				LineFare.calculate(shortestPath.getLines()),
				DistanceFare.calculate(shortestPath.getDistance()))
			.mapToInt(Fare::getFare)
			.sum()
		);
		return AgeFare.calculate(loginMember.getAge(), generalFare);
	}
}

package nextstep.subway.fare.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.fare.dto.Fare;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.Path;
import nextstep.subway.station.domain.Station;

class FareCalculaterTest {

	FareCalculater fareCalculater = new FareCalculater(
		Arrays.asList(new DistanceFarePolicy(), new LineExtraFarePolicy(), new BasicFarePolicy()),
		Arrays.asList(new AgeDiscountPolicy())
	);

	private final Station 교대역 = new Station("교대역");
	private final Station 강남역 = new Station("강남역");
	private final Station 남부터미널역 = new Station("남부터미널역");
	private final Station 양재역 = new Station("양재역");
	private final Station 석촌역 = new Station("석촌역");
	private final Station 송파역 = new Station("송파역");
	private final Station 잠실역 = new Station("잠실역");

	private final Line 신분당선 = new Line("신분당선", "bg-red-600", 10);
	private final Line 이호선 = new Line("이호선", "bg-red-600", 20);
	private final Line 삼호선 = new Line("삼호선", "bg-red-600", 30);
	private final Line 팔호선 = new Line("팔호선", "bg-red-600");

	private Sections sections;
	private Path path;

	private LoginMember 어린이;
	private LoginMember 청소년;
	private LoginMember 성인;

	@BeforeEach
	void setUp() {
		sections = new Sections();

		sections.addSection(신분당선, 강남역, 양재역, 10);
		sections.addSection(이호선, 교대역, 강남역, 10);
		sections.addSection(삼호선, 교대역, 양재역, 4);
		sections.addSection(삼호선, 교대역, 남부터미널역, 3);
		sections.addSection(팔호선, 석촌역, 송파역, 5);

		path = PathFinder.findPath(sections, 강남역, 남부터미널역).get();
		어린이 = new LoginMember(1L, "child@emil.com", 7);
		청소년 = new LoginMember(2L, "teenager@emil.com", 17);
		성인 = new LoginMember(3L, "adult@emil.com", 27);
	}

	@Test
	void calculateFare() {
		Fare fare = fareCalculater.calculateFare(성인, path);
		assertThat(fare.getFare()).isEqualTo(1380);
	}
}

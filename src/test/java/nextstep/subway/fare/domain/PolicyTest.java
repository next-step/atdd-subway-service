package nextstep.subway.fare.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.fare.dto.Fare;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.path.dto.Path;
import nextstep.subway.station.domain.Station;

@DisplayName("Discount 및 Fare Policy들의 단위테스트")
public class PolicyTest {

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

		// 강남역 --2호선(10km)-- 교대역 --3호선(3km)-- 남부터미널역
		path = PathFinder.findPath(sections, 강남역, 남부터미널역).get();

		어린이 = new LoginMember(1L, "child@emil.com", 7);
		청소년 = new LoginMember(2L, "teenager@emil.com", 17);
		성인 = new LoginMember(3L, "adult@emil.com", 27);
	}

	@DisplayName("LineExtraFarePolicy의 calculate 기능 테스트")
	@Test
	void lineExtraFarePolicy() {
		Fare fare = new LineExtraFarePolicy().calculate(어린이, path);
		assertThat(fare.getFare()).isEqualTo(30);
	}

	@DisplayName("DistanceFarePolicy의 calculate 기능 테스트")
	@ParameterizedTest
	@CsvSource(value = {"10, 0", "20, 200", "50, 800", "74, 1100"})
	void distanceFarePolicy(long distance, long expected) {
		Fare fare = new DistanceFarePolicy().calculate(어린이, new Path(null, null, distance));
		assertThat(fare.getFare()).isEqualTo(expected);
	}

	@DisplayName("BasicFarePolicy의 calculate 기능 테스트")
	@Test
	void basicFarePolicy() {
		Fare fare = new BasicFarePolicy().calculate(어린이, path);
		assertThat(fare.getFare()).isEqualTo(1_250);
	}

	@DisplayName("AgeDiscountPolicy의 calculate 기능 테스트")
	@ParameterizedTest
	@CsvSource(value = {"1000, 20, 1000", "1000, 18, 520", "921, 18, 456", "1000, 12, 325"})
	void ageDiscountPolicy(long fare, int age, long expected) {
		Fare result = new AgeDiscountPolicy().discount(Fare.from(fare), new LoginMember(null, null, age), path);
		assertThat(result.getFare()).isEqualTo(expected);
	}
}

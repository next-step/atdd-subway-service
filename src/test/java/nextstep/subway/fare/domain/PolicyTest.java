package nextstep.subway.fare.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Sections;
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

	@BeforeEach
	void setUp() {
		sections = new Sections();
		sections.addSection(이호선, 교대역, 강남역, 10);
		sections.addSection(삼호선, 교대역, 양재역, 4);
		sections.addSection(삼호선, 교대역, 남부터미널역, 3);
		sections.addSection(신분당선, 강남역, 양재역, 10);
	}

	@Test
	void sumExtraFareByLine() {
		long extraFare = LineExtraFarePolicy.maxExtraFareByLine(sections);
		assertThat(extraFare).isEqualTo(삼호선.getExtraFare());

	}

	@ParameterizedTest
	@CsvSource(value = {"10, 0", "20, 200", "50, 800", "74, 1100"})
	void calculateDistanceFare(int distance, int expected) {
		assertThat(DistanceFarePolicy.calculateDistanceFare(distance)).isEqualTo(expected);

	}

	@ParameterizedTest
	@CsvSource(value = {"10, 5, 200", "11, 5, 300", "19, 8, 300"})
	void calculateOverFare(int distance, int interval, int expected) {
		assertThat(DistanceFarePolicy.calculateOverFare(distance, interval)).isEqualTo(expected);
	}

	@ParameterizedTest
	@CsvSource(value = {"1000, 20, 1000", "1000, 18, 520", "921, 18, 456", "1000, 12, 325"})
	void discountFareByAge(int fare, int age, int expected) {
		assertThat(AgeDiscountPolicy.discountFareByAge(fare, age)).isEqualTo(expected);
	}
}

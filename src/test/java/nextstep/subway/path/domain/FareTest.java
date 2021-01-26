package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.dto.Fare;
import nextstep.subway.station.domain.Station;

@DisplayName("Path Domain 단위테스트")
public class FareTest {

	private final Station 교대역 = new Station("교대역");
	private final Station 강남역 = new Station("강남역");
	private final Station 남부터미널역 = new Station("남부터미널역");
	private final Station 양재역 = new Station("양재역");
	private final Station 석촌역 = new Station("석촌역");
	private final Station 송파역 = new Station("송파역");
	private final Station 잠실역 = new Station("잠실역");

	private final Line 신분당선 = new Line("신분당선", "bg-red-600");
	private final Line 이호선 = new Line("이호선", "bg-red-600");
	private final Line 삼호선 = new Line("삼호선", "bg-red-600");
	private final Line 팔호선 = new Line("팔호선", "bg-red-600");

	private Sections sections;

	@BeforeEach
	void setUp() {
		sections = new Sections();
		sections.addSection(신분당선, 강남역, 양재역, 10);
		sections.addSection(이호선, 교대역, 강남역, 10);
		sections.addSection(삼호선, 교대역, 양재역, 4);
		sections.addSection(삼호선, 교대역, 남부터미널역, 3);
		sections.addSection(팔호선, 석촌역, 송파역, 5);
	}

	@Test
	void calculateFare() {
		long fare = Fare.calculateFare(sections, Arrays.asList(강남역, 교대역, 남부터미널역, 양재역, 강남역), 24L);
		assertThat(fare).isEqualTo(300);
	}

	@Test
	void sumExtraFareByLine() {
		long extraFare = Fare.sumExtraFareByLine(sections, Arrays.asList(강남역, 교대역, 남부터미널역, 양재역, 강남역));
		assertThat(extraFare).isEqualTo(신분당선.getExtraFare() + 이호선.getExtraFare() + 삼호선.getExtraFare());

	}

	@Test
	void getLinesByStationsInPath() {
		Set<Line> lines = Fare.getLinesByStationsInPath(sections, Arrays.asList(강남역, 교대역, 남부터미널역, 양재역, 강남역));
		assertThat(lines).contains(이호선, 삼호선, 신분당선);
	}

	@ParameterizedTest
	@CsvSource(value = {"10, 0", "20, 200", "50, 800", "74, 1100"})
	void calculateDistanceFare(int distance, int expected) {
		assertThat(Fare.calculateDistanceFare(distance)).isEqualTo(expected);

	}

	@ParameterizedTest
	@CsvSource(value = {"10, 5, 200", "11, 5, 300", "19, 8, 300"})
	void calculateOverFare(int distance, int interval, int expected) {
		assertThat(Fare.calculateOverFare(distance, interval)).isEqualTo(expected);
	}
}

package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

class SubwayFareTest {
	private static Station 교대역 = new Station(1L, "교대역");
	private static Station 선릉역 = new Station(2L, "선릉역");

	private static Line 이호선 = new Line("이호선", "green", 교대역, 선릉역, Distance.of(20), SubwayFare.of(new BigDecimal(700)));
	private static Section 이호선_구간 = new Section(null, 이호선, 교대역, 선릉역, Distance.of(20));

	private static SubwayFare subwayFare;

	@BeforeEach
	public void setUp() {
		subwayFare = SubwayFare.of(SubwayFare.SUBWAY_BASE_FARE);
	}

	@Test
	@DisplayName("10km 이하 기본요금 테스트")
	public void createBaseFareTest() {
		//when
		SubwayFare calculateFare = subwayFare.calculateDistanceOverFare(Distance.of(10));
		//then
		assertThat(calculateFare.value()).isEqualTo(1250);
	}

	@Test
	@DisplayName("10km 초과 ~ 50km 이하 요금 테스트")
	public void crateFareTenToFiftyTest() {
		//when
		SubwayFare calculateFare = subwayFare.calculateDistanceOverFare(Distance.of(11));
		//then
		assertThat(calculateFare.value()).isEqualTo(1350);
	}

	@Test
	@DisplayName("50km 초과 요금 테스트")
	public void crateFareMoreThenFiftyTest() {
		//when
		SubwayFare calculateFare = subwayFare.calculateDistanceOverFare(Distance.of(59));
		//then
		assertThat(calculateFare.value()).isEqualTo(2250);
	}

	@Test
	@DisplayName("라인 추가 요금 테스트")
	public void createCalculateLineOverFare() {
		//given
		//when
		SubwayFare calculateFare = subwayFare.calculateLineOverFare(Lists.newArrayList(이호선_구간), Lists.newArrayList(new SectionEdge(이호선_구간)));
		//then
		assertThat(calculateFare.value()).isEqualTo(1950);
	}

}

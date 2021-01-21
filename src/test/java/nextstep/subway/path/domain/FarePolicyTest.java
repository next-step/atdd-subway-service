package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

/**
 * @author : byungkyu
 * @date : 2021/01/21
 * @description :
 **/
class FarePolicyTest {

	Station 강남역 = new Station(1L, "강남역");
	Station 양재역 = new Station(2L, "양재역");


	@DisplayName("10km 이하")
	@Test
	void calculateLowerThenTenKilometer(){

		// given
		List<Line> lines = Arrays.asList(new Line(11L, "신분당선", "bg-red-600", 강남역, 양재역, 10, 0));
		FarePolicy farePolicy = new FarePolicy(lines, 5);

		// when
		int amount = farePolicy.calculate();

		// then
		assertThat(amount).isEqualTo(1250);
	}

	@DisplayName("10km 초과 50km까지")
	@Test
	void calculateBetweenTenKilometerAndFiftyKilometer(){
		// given
		List<Line> lines = Arrays.asList(new Line(11L, "신분당선", "bg-red-600", 강남역, 양재역, 10, 0));
		FarePolicy farePolicy = new FarePolicy(lines, 30);

		// when
		int amount = farePolicy.calculate();

		// then
		assertThat(amount).isEqualTo(1850);

	}

	@DisplayName("50km 초과")
	@Test
	void calculateOverFiftyKilometer(){
		// given
		List<Line> lines = Arrays.asList(new Line(11L, "신분당선", "bg-red-600", 강남역, 양재역, 10, 0));
		FarePolicy farePolicy = new FarePolicy(lines, 80);

		// when
		int amount = farePolicy.calculate();

		// then
		assertThat(amount).isEqualTo(2850);

	}

	@DisplayName("10km 이하 추가요금노선 이용")
	@Test
	void calculateLowerThenTenKilometerWithAdditionalFare(){
		// given
		List<Line> lines = Arrays.asList(new Line(11L, "신분당선", "bg-red-600", 강남역, 양재역, 10, 900));
		FarePolicy farePolicy = new FarePolicy(lines, 5);

		// when
		int amount = farePolicy.calculate();

		// then
		assertThat(amount).isEqualTo(1250 + 900);
	}

	@DisplayName("10km 초과 50km까지 추가요금노선 이용")
	@Test
	void calculateBetweenTenKilometerAndFiftyKilometerWithAdditionalFare(){
		// given
		List<Line> lines = Arrays.asList(new Line(11L, "신분당선", "bg-red-600", 강남역, 양재역, 10, 900));
		FarePolicy farePolicy = new FarePolicy(lines, 30);

		// when
		int amount = farePolicy.calculate();

		// then
		assertThat(amount).isEqualTo(1850 + 900);

	}

	@DisplayName("50km 초과 추가요금노선 이용")
	@Test
	void calculateOverFiftyKilometerWithAdditionalFare(){
		// given
		List<Line> lines = Arrays.asList(new Line(11L, "신분당선", "bg-red-600", 강남역, 양재역, 10, 900));
		FarePolicy farePolicy = new FarePolicy(lines, 80);

		// when
		int amount = farePolicy.calculate();

		// then
		assertThat(amount).isEqualTo(2850 + 900);

	}

	@DisplayName("10km 이하 추가요금노선 이용 + 노선경유")
	@Test
	void calculateBetweenTenKilometerAndFiftyKilometerWithAdditionalFareAndThroughAnotherLine(){
		/**
		 * 교대역
		 * |
		 * *3호선(2)*
		 * |
		 * 남부터미널역  --- *3호선(3)* ---   양재       --- *GTX-C(3)* --- 과천
		 *
		 *
		 * 3호선 추가요금 - 900
		 * GTX-C 추가요금 - 1100
		 */

		// given
		Station 교대역 = new Station(3L, "교대역");
		Station 남부터미널역 = new Station(4L, "남부터미널역");
		Station 과천역 = new Station(5L, "과천역");

		Line 삼호선 = new Line(13L, "삼호선", "bg-red-600", 교대역, 양재역, 5, 900);
		Line GTX_C = new Line(14L, "GTX-C", "bg-red-600", 양재역, 과천역, 3, 1100);

		삼호선.addSection(교대역, 남부터미널역, 3);


		List<Line> lines = Arrays.asList(삼호선, GTX_C);
		FarePolicy farePolicy = new FarePolicy(lines, 8);

		// when
		int amount = farePolicy.calculate();

		// then
		assertThat(amount).isEqualTo(1250 + 1100);
	}

}
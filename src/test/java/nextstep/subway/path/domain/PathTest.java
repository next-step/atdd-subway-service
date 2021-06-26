package nextstep.subway.path.domain;

import static java.util.Arrays.*;
import static nextstep.subway.station.domain.StationTest.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.line.domain.Fare;
import nextstep.subway.line.domain.Line;

class PathTest {

	private PathFinder pathFinder;

	/**
	 *				     	   (신분당선)
	 * (2호선) 서초역 -2- 교대역 -1- 강남역
	 * 					          |
	 * 			    		      4
	 * 			           		  |
	 * 					 		 양재역
	 */
	@BeforeEach
	void setup() {
		Line 이호선 = new Line("2호선", "green", 서초역, 교대역, 2, 500);
		이호선.addSection(교대역, 강남역, 1);

		Line 신분당선 = new Line("신분당선", "red", 강남역, 양재역, 4, 1000);
		pathFinder = PathFinder.of(asList(이호선, 신분당선));
	}

	@DisplayName("경로 중 추가 요금이 있는 노선을 이용 할 경우 노선의 추가요금을 적용한다.")
	@Test
	void extraFareTest() {
		Path path = pathFinder.findPath(서초역, 강남역);

		Fare fare = path.calculateFare();

		assertThat(fare).isEqualTo(Fare.wonOf(1250 + 500));
	}

	@DisplayName("경로 중 추가 요금이 있는 노선이 여러 개 일때 가장 높은 높은 금액의 추가 요금만 적용한다.")
	@Test
	void maxExtraFareTest() {
		Path path = pathFinder.findPath(서초역, 양재역);

		Fare fare = path.calculateFare();

		assertThat(fare).isEqualTo(Fare.wonOf(1250 + 1000));
	}

}
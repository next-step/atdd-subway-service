package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

class FarePolicyTest {

	Line 신분당선;
	Station 강남역;
	Station 잠실역;
	Station 양재역;

	@BeforeEach
	void setup() {
		강남역 = new Station("강남역");
		양재역 = new Station("양재역");
		잠실역 = new Station("잠실역");

		신분당선 = new Line("신분당선", "red lighten-1", 강남역, 양재역, 10, 0);
	}

	@DisplayName("성인이 추가요금이 없는 노선의 이동 거리가 10km이내인 경우, 요금은 1250원이다.")
	@Test
	void 성인_추가요금_없는_노선_이동_거리_10키로_이내() {
		FarePolicy farePolicy = new FarePolicy(5, 30, Arrays.asList(신분당선));
		assertThat(farePolicy.getFare()).isEqualTo(1250);
	}

	@DisplayName("성인이 추가요금이 없는 노선의 이동 거리가 10km초과 50km 이내인 경우, 5km마다 100원 추가 요금이 붙는다")
	@Test
	void 성인_추가요금_없는_노선_이동_거리_10키로_초과_50키로_이내() {
		FarePolicy farePolicy = new FarePolicy(12, 30, Arrays.asList(신분당선));
		int expectFare = 1250 + 100;
		assertThat(farePolicy.getFare()).isEqualTo(expectFare);
	}

	@DisplayName("성인이 추가요금이 없는 노선의 이동 거리가 50km초과인 경우, 8km마다 100원 추가 요금이 붙는다")
	@Test
	void 성인_추가요금_없는_노선_이동_거리_50키로_초과() {
		FarePolicy farePolicy = new FarePolicy(66, 30, Arrays.asList(신분당선));
		int expectFare = 1250 + 700;
		assertThat(farePolicy.getFare()).isEqualTo(expectFare);
	}

	@DisplayName("성인이 추가요금이 있는 노선인 경우, 기본 요금에 추가요금이 붙는다.")
	@Test
	void 성인_추가요금_있는_노선_이용시() {
		int additionalFare = 300;
		신분당선 = new Line("신분당선", "red lighten-1", 강남역, 양재역, 10, additionalFare);
		FarePolicy farePolicy = new FarePolicy(5, 30, Arrays.asList(신분당선));
		int expectFare = 1250 + additionalFare;
		assertThat(farePolicy.getFare()).isEqualTo(expectFare);
	}

	@DisplayName("추가요금은 이용한 노선중 가장 높은 금액이 붙는다.")
	@Test
	void 추가요금은_이용한_노선중_가장_높은_금액() {
		신분당선 = new Line("신분당선", "red lighten-1", 강남역, 양재역, 10, 300);
		Line 이호선 = new Line("이호선", "red lighten-1", 강남역, 잠실역, 10, 500);

		FarePolicy farePolicy = new FarePolicy(5, 30, Arrays.asList(신분당선, 이호선));
		int expectFare = 1250 + 500;
		assertThat(farePolicy.getFare()).isEqualTo(expectFare);
	}

	@DisplayName("어린이인 경우, 운임에서 350원을 공제한 금액의 50%할인해준다.")
	@Test
	void 어린이_이용시() {
		FarePolicy farePolicy = new FarePolicy(5, 7, Arrays.asList(신분당선));
		int expectFare = (int)((1250 - 350) * 0.5);
		assertThat(farePolicy.getFare()).isEqualTo(expectFare);
	}

	@DisplayName("청소년인 경우, 운임에서 350원을 공제한 금액의 20%할인해준다.")
	@Test
	void 청소년_이용시() {
		FarePolicy farePolicy = new FarePolicy(5, 15, Arrays.asList(신분당선));
		int expectFare = (int)((1250 - 350) * 0.2);
		assertThat(farePolicy.getFare()).isEqualTo(expectFare);
	}

	@DisplayName("성인인 경우, 할인이 없다.")
	@Test
	void 성인_이용시() {
		FarePolicy farePolicy = new FarePolicy(5, 21, Arrays.asList(신분당선));
		int expectFare = 1250;
		assertThat(farePolicy.getFare()).isEqualTo(expectFare);
	}
}

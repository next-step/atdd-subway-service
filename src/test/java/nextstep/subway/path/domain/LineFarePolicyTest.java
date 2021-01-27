package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

public class LineFarePolicyTest {
	private Line 신분당선;
	private Line 이호선;
	private Line 삼호선;
	private Line 구호선;
	private Station 강남역;
	private Station 양재역;
	private Station 교대역;
	private Station 남부터미널;
	private Station 당산역;
	private Station 여의도역;

	@BeforeEach
	void setUp() {
		강남역 = new Station("강남역");
		양재역 = new Station("양재역");
		교대역 = new Station("교대역");
		남부터미널 = new Station("남부터미널");
		당산역 = new Station("당산역");
		여의도역 = new Station("여의도역");

		신분당선 = new Line("신분당선", "bg-red-600", 강남역, 양재역, 10, 900);
		이호선 = new Line("이호선", "bg-red-600", 강남역, 교대역, 10, 1300);
		삼호선 = new Line("삼호선", "bg-red-600", 양재역, 남부터미널, 10, 700);
		구호선 = new Line("구호선", "bg-red-600", 당산역, 여의도역, 10, 100);
	}

	@DisplayName("노선별 추가 요금")
	@Test
	void dd() {
		assertThat(LineFarePolicy.calculateOverFare(Arrays.asList(신분당선, 이호선, 삼호선, 구호선))).isEqualTo(1300);
	}
}

package nextstep.subway.fare.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

class LineFareTest {

	@Test
	void calculate_1_line() {
		final Station 강남역 = new Station("강남역");
		final Station 양재역 = new Station("양재역");
		final Line 신분당선 = new Line("신분당선", "red", 강남역, 양재역, 5, 800);

		assertThat(LineFare.calculate(Arrays.asList(신분당선)).getFare()).isEqualTo(800);
	}

	@Test
	void calculate_many_lines() {
		final Station 강남역 = new Station("강남역");
		final Station 양재역 = new Station("양재역");
		final Station 선릉역 = new Station("선릉역");
		final Station 강남구청역 = new Station("강남구청역");

		final Line 신분당선 = new Line("신분당선", "red", 강남역, 양재역, 5, 1000);
		final Line 이호선 = new Line("이호선", "green", 강남역, 선릉역, 3, 100);
		final Line 분당선 = new Line("분당선", "yellow", 선릉역, 강남구청역, 10, 500);

		assertThat(LineFare.calculate(Arrays.asList(신분당선, 이호선, 분당선)).getFare()).isEqualTo(1000);
	}
}

package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.station.domain.Station;

/**
 * @author : byungkyu
 * @date : 2021/01/11
 * @description :
 **/
@DisplayName("지하철노선 관련 기능")
class LineTest {
	Station 강남역;
	Station 광교역;
	Station 판교역;

	@BeforeEach
	void setUp() {
		강남역 = new Station("강남역");
		광교역 = new Station("광교역");
		판교역 = new Station("판교역");
	}

	@DisplayName("노선을_생성한다.")
	@Test
	void createLine(){
		// when
		Line actual = new Line("신분당선", "red");

		// then
		assertThat(actual.getName()).isEqualTo("신분당선");
		assertThat(actual.getColor()).isEqualTo("red");
	}


}
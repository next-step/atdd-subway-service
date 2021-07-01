package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.station.domain.Station;

@DisplayName("노선 엔티티 테스트")
public class LineTest {

	@Test
	void 노선요청을_노선엔티티로_변경() {
		// given
		Station 종로3가역 = new Station("종로3가역");
		Station 신길역 = new Station("신길역");
		LineRequest 노선요청 = new LineRequest("1호선", "blue", 종로3가역.getId(), 신길역.getId(), 10);

		// when
		Line 노선엔티티 = Line.of(노선요청, 종로3가역, 신길역);

		// then
		Line 비교할_노선엔티티 = new Line("1호선", "blue", 종로3가역, 신길역, 10);
		assertThat(노선엔티티).isEqualTo(비교할_노선엔티티);
	}

	@Test
	void 노선_내_역_목록_조회() {
		// given
		Station 종로3가역 = new Station("종로3가역");
		Station 신길역 = new Station("신길역");
		Line 일호선 = new Line("1호선", "blue", 종로3가역, 신길역, 10);

		// when
		List<Station> 노선_내_역들 = 일호선.stations();

		// then
		assertThat(노선_내_역들).containsSequence(Arrays.asList(종로3가역, 신길역));
	}
}

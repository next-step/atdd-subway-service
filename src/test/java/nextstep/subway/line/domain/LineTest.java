package nextstep.subway.line.domain;

import static nextstep.subway.line.domain.SectionTest.*;
import static nextstep.subway.station.domain.StationTest.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.station.domain.Station;

class LineTest {

	static Line 신분당선 = new Line("신분당선","red");

	@Test
	@DisplayName("상행 -> 하행 순서로 정렬된 노선의 역들을 반환한다.")
	void getStationsTest() {
		// given
		Line 신분당선 = new Line("신분당선","red");
		신분당선.setSections(양재_양재시민의숲_구간, 강남_양재_구간, 양재시민의숲_광교_구간);

		// when
		List<Station> stationsInOrder = 신분당선.getStationsInOrder();

		// then
		assertThat(stationsInOrder).containsExactly(강남역, 양재역, 양재시민의숲역, 광교역);
	}

}

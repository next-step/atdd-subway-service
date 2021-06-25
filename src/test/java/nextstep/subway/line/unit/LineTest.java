package nextstep.subway.line.unit;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

@DisplayName("Line 단위테스트")
public class LineTest {

	@DisplayName("역 목록 반환")
	@Test
	public void getStations() {
		Station 운정역 = new Station("운정역");
		Station 일산역 = new Station("일산역");
		Line line = new Line("경의선", "BLUE",운정역, 일산역, 15);

		List<Station> stations = line.getStations();
		assertThat(stations).containsExactly(운정역, 일산역);
	}

	@DisplayName("구간 추가 - 예외")
	@Test
	void addLineStatio2() {
		Station 운정역 = new Station("운정역");
		Station 야당역 = new Station("야당역");
		Station 탄현역 = new Station("탄현역");
		Station 일산역 = new Station("일산역");
		Line line = new Line("경의선", "BLUE",운정역, 일산역, 15);

		assertThatThrownBy(() -> line.addSection(new Section(line, 운정역, 일산역, 15))).isInstanceOf(RuntimeException.class).hasMessage("이미 등록된 구간 입니다.");
		assertThatThrownBy(() -> line.addSection(new Section(line, 야당역, 탄현역, 15))).isInstanceOf(RuntimeException.class).hasMessage("등록할 수 없는 구간 입니다.");
	}

}

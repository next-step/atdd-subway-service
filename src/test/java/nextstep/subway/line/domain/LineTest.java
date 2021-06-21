package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.station.domain.Station;

public class LineTest {

	Station 성수역;
	Station 뚝섬역;

	@BeforeEach
	void setUp() {
		성수역 = new Station(1L, "성수역");
		뚝섬역 = new Station(2L, "뚝섬역");
	}

	@DisplayName("LineRequest Line으로 변환")
	@Test
	void LineRequest_Line_으로_변환() {
		LineRequest lineRequest = new LineRequest("2호선", "초록색", 성수역.getId(), 뚝섬역.getId(), 10);
		Line line = lineRequest.toLine(성수역, 뚝섬역);
		assertThat(line.getName()).isEqualTo("2호선");
		assertThat(line.getColor()).isEqualTo("초록색");
		assertThat(line.getSections().get(0).getUpStation().getId()).isEqualTo(1L);
		assertThat(line.getSections().get(0).getDownStation().getId()).isEqualTo(2L);
		assertThat(line.getSections().get(0).getDistance()).isEqualTo(10);

	}
}

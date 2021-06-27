package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.station.domain.Station;

public class LinesTest {

	Station 성수역;
	Station 뚝섬역;
	Station 건대입구역;
	Station 강남구청역;
	Station 왕십리역;
	Line 이호선;
	Line 칠호선;
	Line 수인분당선;
	Section 뚝섬성수구간;
	Section 성수건대구간;
	Section 건대강남구청역구간;
	Section 강남구청왕십리구간;
	Section 왕십리뚝섬구간;

	/*
	왕십리 -4- 뚝섬 -2- 성수 -2- 건대
	  |						 |
	 10						 6
	  |						 |
	강남구청 ----------------강남구청
	 */

	@BeforeEach
	void setUp() {
		성수역 = new Station(1L, "성수역");
		뚝섬역 = new Station(2L, "뚝섬역");
		건대입구역 = new Station(3L, "건대입구역");
		강남구청역 = new Station(4L, "강남구청역");
		왕십리역 = new Station(5L, "왕십리역");

		이호선 = new Line("이호선", "초록색");
		칠호선 = new Line("칠호선", "칠호선색");
		수인분당선 = new Line("수인분당선", "노랑색");

		왕십리뚝섬구간 = new Section(이호선, 왕십리역, 뚝섬역, new Distance(4));
		뚝섬성수구간 = new Section(이호선, 뚝섬역, 성수역, new Distance(2));
		성수건대구간 = new Section(이호선, 성수역, 건대입구역, new Distance(2));

		건대강남구청역구간 = new Section(칠호선, 건대입구역, 강남구청역, new Distance(6));
		강남구청왕십리구간 = new Section(수인분당선, 강남구청역, 왕십리역, new Distance(10));

		이호선.addLineStation(뚝섬성수구간);
		이호선.addLineStation(성수건대구간);
		이호선.addLineStation(왕십리뚝섬구간);
		칠호선.addLineStation(건대강남구청역구간);
		수인분당선.addLineStation(강남구청왕십리구간);
	}

	@DisplayName("Lines 생성")
	@Test
	void create() {
		Lines lines = new Lines(Arrays.asList(이호선, 칠호선, 수인분당선));
		assertThat(lines).isNotNull();
	}

	@DisplayName("Lines 모든 역 조회")
	@Test
	void getStations() {
		Lines lines = new Lines(Arrays.asList(이호선, 칠호선, 수인분당선));
		assertThat(lines.getStations()).containsAll(Arrays.asList(성수역, 뚝섬역, 건대입구역, 강남구청역, 왕십리역));
	}

	@DisplayName("Lines 라인 별 모든 구간 조회")
	@Test
	void getSectionsByLine() {
		Lines lines = new Lines(Arrays.asList(이호선, 칠호선, 수인분당선));
		assertThat(lines.getSectionsByLine()).containsAll(
			Arrays.asList(이호선.getSections(), 칠호선.getSections(), 수인분당선.getSections()));
	}

}

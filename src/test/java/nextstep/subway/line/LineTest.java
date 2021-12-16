package nextstep.subway.line;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;

public class LineTest {

	public static final String 신분당선 = "신분당선";
	public static final String 구분당선 = "구분당선";
	public static final String BG_RED_600 = "bg-red-600";
	public static final String BG_BLUE_600 = "bg-blue-600";
	public static final Station 삼성역 = new Station("삼성역");
	public static final Station 선릉역 = new Station("선릉역");
	public static final Station 역삼역 = new Station("역삼역");
	public static final Station 강남역 = new Station("강남역");

	@Test
	@DisplayName("지하철 역을 상행-하행 순으로 정렬하여 반환한다.")
	void getStationsByOrder_success() {
		Line line = new Line(신분당선, BG_RED_600, 삼성역, 선릉역, 5);
		line.addSection(new Section(line, 역삼역, 강남역, 3));
		line.addSection(new Section(line, 선릉역, 역삼역, 3));

		List<Station> actual = line.getOrderedStations();

		assertThat(Arrays.asList(삼성역, 선릉역, 역삼역, 강남역)).isEqualTo(actual);
	}
}

package nextstep.subway.line;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;

public class LineTest {

	public static final String 신분당선 = "신분당선";
	public static final String BG_RED_600 = "bg-red-600";
	public static final Station 삼성역 = new Station("삼성역");
	public static final Station 선릉역 = new Station("선릉역");

	// TODO: 3개 이상의 지하철역이 추가될 케이스를 addSection 구현 이후 추가하기
	@Test
	@DisplayName("지하철 역을 상행-하행 순으로 정렬하여 반환한다.")
	void getStationsByOrder_success() {
		Line line = new Line(신분당선, BG_RED_600, 삼성역, 선릉역, 5);

		List<Station> actual = line.getOrderedStations(line);

		assertThat(Arrays.asList(삼성역, 선릉역)).isEqualTo(actual);
	}
}

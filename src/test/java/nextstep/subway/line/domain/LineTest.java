package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.station.domain.Station;

public class LineTest {
	private static final String NAME = "2호선";
	private static final String COLOR = "green";
	private static final Station UP_STATION = new Station("문래역");
	private static final Station DOWN_STATION = new Station("잠실역");
	private static final int DISTANCE = 5;

	@Test
	@DisplayName("이름과 색으로 생성하면 구간이 없는 상태로 생성되어야한다.")
	void constructNameAndColor() {
		//when
		Line line = new Line(NAME, COLOR);

		//then
		assertThat(line.getName()).isEqualTo(NAME);
		assertThat(line.getColor()).isEqualTo(COLOR);
		assertThat(line.getSections()).isEmpty();
		assertThat(line.getStations()).isEmpty();
	}

	@Test
	@DisplayName("이름, 색, 상행종점, 하행종점, 종점간 거리로 노선을 생성하면 종점간 구간 1개를 갖는 노선이 생성되어야한다.")
	void constructNameAndColorAndEndStation() {
		//when
		Line line = new Line(NAME, COLOR, UP_STATION, DOWN_STATION, DISTANCE);

		//then
		assertThat(line.getName()).isEqualTo(NAME);
		assertThat(line.getColor()).isEqualTo(COLOR);
		hasOnlyOriginEndSection(line);
	}

	@Test
	@DisplayName("변경될 노선 정보에 따라 노선 이름과 색만 변경되어야한다.")
	void updateNameAndColor() {
		//given
		String modifiedName = "3호선";
		String modifiedColor = "orange";
		Line line = new Line(NAME, COLOR, UP_STATION, DOWN_STATION, DISTANCE);
		Line updateLine = new Line(modifiedName, modifiedColor, Arrays.asList(new Section(), new Section()));

		//when
		line.update(updateLine);

		//then
		assertThat(line.getName()).isEqualTo(modifiedName);
		assertThat(line.getColor()).isEqualTo(modifiedColor);
		hasOnlyOriginEndSection(line);
	}

	void hasOnlyOriginEndSection(Line line) {
		assertThat(line.getSections().size()).isEqualTo(1);
		assertThat(line.getStations().size()).isEqualTo(2);
		assertThat(line.getSections().get(0).getUpStation()).isEqualTo(UP_STATION);
		assertThat(line.getSections().get(0).getDownStation()).isEqualTo(DOWN_STATION);
		assertThat(line.getSections().get(0).getDistance()).isEqualTo(DISTANCE);
	}
}

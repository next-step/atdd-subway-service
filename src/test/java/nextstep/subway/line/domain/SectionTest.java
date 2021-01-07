package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.station.domain.Station;

public class SectionTest {

	private static final int ORIGIN_DISTANCE_VALUE = 12;
	private Line originLine;
	private Station originUpStation;
	private Station originDownStation;
	private Section originSection;

	@BeforeEach
	void setUp() {
		originLine = new Line("2호선", "green");
		originUpStation = new Station("문래역");
		originDownStation = new Station("잠실역");
		originSection = new Section(originLine, originUpStation, originDownStation, ORIGIN_DISTANCE_VALUE);
	}

	@Test
	@DisplayName("구간의 상행역을 변경하면 상행역이 변경되고 거리가 기존거리에서 추가된 거리를 뺀 값으로 재계산 되어야한다")
	void updateUpStation() {
		//given
		Station newStation = new Station("신도림역");

		//when
		originSection.updateUpStation(newStation, 4);

		//then
		assertThat(originSection.getUpStation()).isEqualTo(newStation);
		assertThat(originSection.getDownStation()).isEqualTo(originDownStation);
		assertThat(originSection.getDistance()).isEqualTo(ORIGIN_DISTANCE_VALUE - 4);
	}

	@Test
	@DisplayName("구간의 하행역을 변경하면 하행역이 변경되고 거리가 기존거리에서 추가된 거리를 뺀 값으로 재계산 되어야한다")
	void updateDownStation() {
		//given
		Station newStation = new Station("신도림역");

		//when
		originSection.updateDownStation(newStation, 4);

		//then
		assertThat(originSection.getUpStation()).isEqualTo(originUpStation);
		assertThat(originSection.getDownStation()).isEqualTo(newStation);
		assertThat(originSection.getDistance()).isEqualTo(ORIGIN_DISTANCE_VALUE - 4);
	}
}

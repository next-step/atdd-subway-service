package nextstep.subway.line.domain;

import nextstep.subway.common.ValidationException;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressWarnings("NonAsciiCharacters")
class LineTest {

	private Line 이호선;
	private Station 삼성역;
	private Station 잠실역;
	private Station 종합운동장역;
	private Station 선릉역;

	@BeforeEach
	void setUp() {
		삼성역 = new Station("삼성역");
		잠실역 = new Station("잠실역");
		종합운동장역 = new Station("종합운동장역");
		선릉역 = new Station("선릉역");
		이호선 = new Line("이호선", "green", 삼성역, 잠실역, 50, 0);
	}

	@Test
	void getStations() {
		assertThat(이호선.getStations()).containsExactly(삼성역, 잠실역);
	}

	@Test
	void addLineStation() {
		이호선.addLineStation(잠실역, 종합운동장역, 20);

		assertThat(이호선.getStations()).containsExactly(삼성역, 잠실역, 종합운동장역);
	}

	@Test
	void addLineStation_이미등록됨() {
		assertThatThrownBy(() -> 이호선.addLineStation(삼성역, 잠실역, 20))
				.isInstanceOf(ValidationException.class)
				.hasMessageContaining("이미 등록된 구간");
	}

	@Test
	void addLineStation_등록불가() {
		assertThatThrownBy(() -> 이호선.addLineStation(종합운동장역, 선릉역, 20))
				.isInstanceOf(ValidationException.class)
				.hasMessageContaining("등록할 수 없는 구간");
	}

	@Test
	void removeLineStation() {
		이호선.addLineStation(잠실역, 종합운동장역, 20);

		이호선.removeLineStation(잠실역);

		assertThat(이호선.getStations()).containsExactly(삼성역, 종합운동장역);
	}

	@Test
	void removeLineStation_제거가능역없음() {
		assertThatThrownBy(() -> 이호선.removeLineStation(잠실역)).isInstanceOf(ValidationException.class);
	}

	@Test
	void update() {
		이호선.update("이호선2", "red");

		assertThat(이호선.getName()).isEqualTo("이호선2");
		assertThat(이호선.getColor()).isEqualTo("red");
	}
}

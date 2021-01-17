package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.station.domain.Station;

@DisplayName("Sections의 Domain 단위테스트")
public class SectionsTest {

	private final Station 교대역 = new Station("교대역");
	private final Station 선릉역 = new Station("선릉역");
	private final Station 삼성역 = new Station("삼성역");
	private final Station 잠실역 = new Station("잠실역");
	private final Line 이호선 = new Line("2호선", "green");
	private final Sections sections = new Sections();

	@BeforeEach
	void addSection() {
		sections.addSection(이호선, 교대역, 삼성역, 10);
		sections.addSection(이호선, 선릉역, 삼성역, 5);
		sections.addSection(이호선, 삼성역, 잠실역, 7);
	}

	@Test
	void getStations() {
		assertThat(sections.getStations()).containsExactly(교대역, 선릉역, 삼성역, 잠실역);
	}

	@Test
	void removeSection() {
		sections.removeStation(이호선, 선릉역);
		assertThat(sections.getStations()).containsExactly(교대역, 삼성역, 잠실역);
	}
}

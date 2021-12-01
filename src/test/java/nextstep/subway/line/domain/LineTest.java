package nextstep.subway.line.domain;

import static nextstep.subway.line.domain.LineFixture.*;
import static nextstep.subway.line.domain.SectionFixture.*;
import static nextstep.subway.station.StationFixture.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.station.domain.Stations;

@DisplayName("지하철 노선")
class LineTest {

	@DisplayName("생성")
	@Test
	void of() {
		// given

		// when
		Line line = Line.of("신분당선", "red lighten-1", Section.of(강남역(), 광교역(), 20));

		// then
		assertThat(line).isNotNull();
	}

	@DisplayName("구간을 추가한다.")
	@Test
	void addSection() {
		// given
		Line line = 신분당선_강남역_광교역();
		Section section = 강남역_양재역_구간();

		// when
		line.addSection(section);

		// then
		assertThat(section.getLine()).isEqualTo(line);
		assertThat(line.getSections().contains(section)).isTrue();
	}

	@DisplayName("이름과 색을 업데이트 한다.")
	@Test
	void update() {
		// given
		Line line = 신분당선_강남역_광교역();
		String newName = "신신분당선";
		String newColor = "red lighten-2";

		// when
		line.update(newName, newColor);

		// then
		assertThat(line.getName()).isEqualTo(newName);
		assertThat(line.getColor()).isEqualTo(newColor);
	}

	@DisplayName("속해 있는 역 목록을 가져온다.")
	@Test
	void getStations() {
		// given
		Line line = 신분당선_강남역_광교역();

		// when
		Stations stations = line.getStations();

		// then
		assertThat(stations).isEqualTo(Stations.of(Arrays.asList(강남역(), 광교역())));
	}
}

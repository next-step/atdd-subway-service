package nextstep.subway.line.domain;

import static nextstep.subway.line.domain.SectionFixture.*;
import static nextstep.subway.station.StationFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.station.domain.Stations;

@DisplayName("지하철 구간들")
class SectionsTest {
	@DisplayName("구간들을 생성한다.")
	@Test
	void of() {
		// given
		List<Section> values = Arrays.asList(
			강남역_양재역_구간(),
			양재역_정자역_구간(),
			정자역_광교역_구간());

		// when
		Sections sections = Sections.of(values);

		// then
		assertAll(
			() -> assertThat(sections).isNotNull(),
			() -> assertThat(sections.size()).isEqualTo(values.size())
		);
	}

	@DisplayName("구간들 내 역들을 상행역부터 하행역순으로 가져온다.")
	@Test
	void getStations() {
		// given
		Sections sections = Sections.of(Arrays.asList(
			정자역_광교역_구간(),
			강남역_양재역_구간(),
			양재역_정자역_구간()));

		// when
		Stations stations = sections.getStations();

		// then
		assertThat(stations.getValues()).isEqualTo(Arrays.asList(
			강남역(),
			양재역(),
			정자역(),
			광교역()));
	}

}

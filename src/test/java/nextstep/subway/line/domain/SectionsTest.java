package nextstep.subway.line.domain;

import java.util.Arrays;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.station.domain.Station;

class SectionsTest {

	private Line line;
	private Station defaultUpStation;
	private Station defaultDownStation;

	@BeforeEach
	void setup(){
		defaultUpStation = new Station("강남역");
		defaultDownStation = new Station("광교역");
		line = new Line("신분당선","bg-red-600");
	}

	@DisplayName("지하철 노선의 역들을 정렬하여 조회")
	@Test
	void getOrderedStation() {
		// given
		Station newUpStation = new Station("신논현");
		Station newDownStation = new Station("호매실");
		Sections sections = new Sections(Arrays.asList(
			new Section(line,defaultUpStation,defaultDownStation,10),
			new Section(line, newUpStation,defaultUpStation,5),
			new Section(line,defaultDownStation,newDownStation,5)));

		// when
		List<Station> stations = sections.getOrderedStations();

		// then
		Assertions.assertThat(stations).containsExactlyElementsOf(Arrays.asList(newUpStation,defaultUpStation,defaultDownStation,newDownStation));
	}
}
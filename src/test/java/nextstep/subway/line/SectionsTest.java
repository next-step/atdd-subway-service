package nextstep.subway.line;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class SectionsTest {
	private Line 신분당선;

	private Station 강남역;
	private Station 광교역;

	@BeforeEach
	void setUp() {
		신분당선 = new Line("신분당선", "red");
		강남역 = new Station(1L, "강남역");
		광교역 = new Station(2L, "광교역");
	}

	@Test
	@DisplayName("라인에 존재하는 역들을 가져온다.")
	void getStationsTest() {
		Section section = new Section(신분당선, 강남역, 광교역, 10);
		신분당선.getSections().add(section);

		List<Station> stationsOnLine = 신분당선.getStations();
		List<String> stationsNamesOnLine = stationsOnLine.stream().map(Station::getName)
				.collect(Collectors.toList());

		assertThat(stationsNamesOnLine).containsAll(Arrays.asList(강남역.getName(), 광교역.getName()));
	}
}

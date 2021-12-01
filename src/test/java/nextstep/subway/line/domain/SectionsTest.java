package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import nextstep.subway.station.domain.Station;

@DataJpaTest
class SectionsTest {

	private Line line;
	private Station defaultUpStation;
	private Station defaultDownStation;

	@BeforeEach
	void setup() {
		defaultUpStation = new Station("강남역");
		defaultDownStation = new Station("광교역");
		line = new Line("신분당선", "bg-red-600");
	}

	@DisplayName("지하철 노선의 역들을 정렬하여 조회")
	@Test
	void getOrderedStation() {
		// given
		Station newUpStation = new Station("신논현");
		Station newDownStation = new Station("호매실");

		Sections sections = new Sections(Arrays.asList(
			new Section(line, defaultUpStation, defaultDownStation, 10),
			new Section(line, newUpStation, defaultUpStation, 5),
			new Section(line, defaultDownStation, newDownStation, 5)));

		// when
		List<Station> stations = sections.getOrderedStations();

		// then
		Assertions.assertThat(stations)
			.containsExactlyElementsOf(
				Arrays.asList(newUpStation, defaultUpStation, defaultDownStation, newDownStation));
	}

	@DisplayName("지하철 노선 구간 추가")
	@Test
	void addSection() {
		// given
		Sections sections = new Sections(new ArrayList<>(
			Arrays.asList(new Section(line, defaultUpStation, defaultDownStation, 10))
		));
		Station station = new Station("신논현");

		// when
		sections.addSection(line, defaultUpStation, station, 3);

		// then
		List<Station> stations = sections.getOrderedStations();
		Assertions.assertThat(stations)
			.containsExactlyElementsOf(Arrays.asList(defaultUpStation, station, defaultDownStation));
	}

	@DisplayName("이미 등록된 구간 추가 예외")
	@Test
	void addSection_exception1() {
		// given
		Sections sections = new Sections(new ArrayList<>(
			Arrays.asList(new Section(line, defaultUpStation, defaultDownStation, 10))
		));

		// when , then
		Assertions.assertThatThrownBy(() -> sections.addSection(line, defaultUpStation, defaultDownStation, 3))
			.isInstanceOf(RuntimeException.class);

	}

	@DisplayName("등록할 수 없는 구간 추가 예외")
	@Test
	void addSection_exception2() {
		// given
		Sections sections = new Sections(new ArrayList<>(
			Arrays.asList(new Section(line, defaultUpStation, defaultDownStation, 10))
		));
		Station station1 = new Station("신논현");
		Station station2 = new Station("양재역");

		// when , then
		Assertions.assertThatThrownBy(() -> sections.addSection(line, station1, station2, 10))
			.isInstanceOf(RuntimeException.class);
	}

	@DisplayName("지하철 노선 구간의 역 삭제")
	@Test
	void removeStation() {
		// given
		Station newUpStation = new Station("신논현");
		Station newDownStation = new Station("호매실");

		Sections sections = new Sections(new ArrayList<>(
			Arrays.asList(
				new Section(line, defaultUpStation, defaultDownStation, 10),
				new Section(line, newUpStation, defaultUpStation, 5),
				new Section(line, defaultDownStation, newDownStation, 5)))
		);

		// when
		sections.removeStation(line, defaultUpStation);

		// then
		List<Station> stations = sections.getOrderedStations();
		Assertions.assertThat(stations)
			.containsExactlyElementsOf(Arrays.asList(newUpStation, defaultDownStation, newDownStation));
	}

	@DisplayName("지하철 노선에 구간이 없거나 하나만 존재할 경우 예외")
	@Test
	void removeStation_exception() {
		// given
		Sections sections = new Sections(new ArrayList<>(
			Arrays.asList(
				new Section(line, defaultUpStation, defaultDownStation, 10))));

		// when, then
		Assertions.assertThatThrownBy(() -> sections.removeStation(line, defaultUpStation))
			.isInstanceOf(RuntimeException.class);
	}
}
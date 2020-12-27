package nextstep.subway.line.domain;

import static nextstep.subway.line.domain.SectionsTestFixture.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.station.domain.Station;

class SectionsTest {

	public static final int 굴포천역_삼산체육관역_거리 = 100;
	private Sections sections;
	private Line line;

	@BeforeEach
	void init() {
		line = new Line(
			"7호선",
			"darken-orange",
			굴포천역(),
			부천시청역(),
			10000
		);
		sections = line.getSections();
		sections.addSection(new Section(line, 굴포천역(), 삼산체육관역(), 굴포천역_삼산체육관역_거리));
	}

	@DisplayName("getStations 메소드를 호출하면 Stations 객체 리스트를 얻을 수 있다.")
	@Test
	void getStations() {

		List<Station> stations = sections.getStations();

		assertThat(stations).containsExactlyElementsOf(Arrays.asList(굴포천역(), 삼산체육관역(), 부천시청역()));
	}

	@DisplayName("addSection 상행 종점 교체")
	@Test
	void addSection1() {

		sections.addSection(new Section(line, 부평구청역(), 굴포천역(), 100));
		List<Station> stations = sections.getStations();

		assertThat(stations).containsExactlyElementsOf(Arrays.asList(부평구청역(), 굴포천역(), 삼산체육관역(), 부천시청역()));
	}

	@DisplayName("addSection 하행 종점 교체")
	@Test
	void addSection2() {

		sections.addSection(new Section(line, 부천시청역(), 장암역(), 100));
		List<Station> stations = sections.getStations();

		assertThat(stations).containsExactlyElementsOf(Arrays.asList(굴포천역(), 삼산체육관역(), 부천시청역(), 장암역()));
	}

	@DisplayName("addSection 상행역 매치 중간역 추가 케이스")
	@Test
	void addSection3() {

		sections.addSection(new Section(line, 삼산체육관역(), 상동역(), 100));
		List<Station> stations = sections.getStations();

		assertThat(stations).containsExactlyElementsOf(Arrays.asList(굴포천역(), 삼산체육관역(), 상동역(), 부천시청역()));
	}

	@DisplayName("addSection 하행역 매치 중간역 추가 케이스")
	@Test
	void addSection4() {

		sections.addSection(new Section(line, 상동역(), 부천시청역(), 100));
		List<Station> stations = sections.getStations();

		assertThat(stations).containsExactlyElementsOf(Arrays.asList(굴포천역(), 삼산체육관역(), 상동역(), 부천시청역()));
	}

	@DisplayName("addSection 상행역, 하행역 모두 이미 등록되어 있는 역으로 선택하고 등록하면 RuntimeException이 발생한다.")
	@Test
	void addSectionThrow1() {

		assertThatExceptionOfType(RuntimeException.class)
			.isThrownBy(() -> {
				sections.addSection(new Section(line, 굴포천역(), 삼산체육관역(), 100));
			});
	}

	@DisplayName("addSection 역과 역사이의 거리보다 큰 거리를 입력하면 RuntimeException이 발생한다.")
	@Test
	void addSectionThrow3() {

		assertThatExceptionOfType(RuntimeException.class)
			.isThrownBy(() -> {
				sections.addSection(new Section(line, 굴포천역(), 상동역(), 굴포천역_삼산체육관역_거리 + 1));
			});
	}

	@DisplayName("removeLineStation 상행 종점 제거")
	@Test
	void removeLineStation1() {

		sections.removeLineStation(line, 굴포천역());
		List<Station> stations = sections.getStations();

		assertThat(stations).containsExactlyElementsOf(Arrays.asList(삼산체육관역(), 부천시청역()));
	}

	@DisplayName("removeLineStation 하행 종점 제거")
	@Test
	void removeLineStation2() {

		sections.removeLineStation(line, 부천시청역());
		List<Station> stations = sections.getStations();

		assertThat(stations).containsExactlyElementsOf(Arrays.asList(굴포천역(), 삼산체육관역()));
	}

	@DisplayName("removeLineStation 중간역 제거")
	@Test
	void removeLineStation3() {

		sections.removeLineStation(line, 삼산체육관역());
		List<Station> stations = sections.getStations();

		assertThat(stations).containsExactlyElementsOf(Arrays.asList(굴포천역(), 부천시청역()));
	}

	@DisplayName("removeLineStation 두개역이 존재할때 역을 제거하게되면 RuntimeException이 발생한다.")
	@Test
	void removeLineStationThrow() {

		assertThatExceptionOfType(RuntimeException.class)
			.isThrownBy(() -> {
				sections.removeLineStation(line, 굴포천역());
				sections.removeLineStation(line, 삼산체육관역());
			});
	}

}
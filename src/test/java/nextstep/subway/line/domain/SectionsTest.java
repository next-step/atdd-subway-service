package nextstep.subway.line.domain;

import static nextstep.subway.line.domain.SectionFixture.*;
import static nextstep.subway.station.StationFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

	@DisplayName("상행역으로 구간을 찾을 수 있다.")
	@Test
	void findByUpStation() {
		// given
		Sections sections = Sections.of(Arrays.asList(
			강남역_양재역_구간(),
			양재역_정자역_구간(),
			정자역_광교역_구간()));

		// when
		Optional<Section> section = sections.findByUpStation(강남역());

		// then
		assertThat(section.isPresent()).isTrue();
		assertThat(section.get()).isEqualTo(강남역_양재역_구간());
	}

	@DisplayName("하행역으로 구간을 찾을 수 있다.")
	@Test
	void findByDownStation() {
		// given
		Sections sections = Sections.of(Arrays.asList(
			강남역_양재역_구간(),
			양재역_정자역_구간(),
			정자역_광교역_구간()));

		// when
		Optional<Section> section = sections.findByDownStation(정자역());

		// then
		assertThat(section.isPresent()).isTrue();
		assertThat(section.get()).isEqualTo(양재역_정자역_구간());
	}

	@DisplayName("구간 거리들을 상행역부터 하행역순으로 가져온다.")
	@Test
	void getDistancesInOrder() {
		// given
		Sections sections = Sections.of(Arrays.asList(
			정자역_광교역_구간(),
			강남역_양재역_구간(),
			양재역_정자역_구간()));

		// when
		List<Integer> distances = sections.getDistances();

		// then
		assertThat(distances).isEqualTo(Arrays.asList(
			강남역_양재역_구간().getDistance(),
			양재역_정자역_구간().getDistance(),
			정자역_광교역_구간().getDistance()));
	}

	@DisplayName("구간들이 비어있을 경우, 구간을 등록한다.")
	@Test
	void addOnEmpty() {
		// given
		Sections sections = Sections.empty();

		// when
		sections.add(강남역_양재역_구간());

		// then
		assertThat(sections.getStations().getValues()).isEqualTo(Arrays.asList(
			강남역(),
			양재역()));
	}

	@DisplayName("상행역과 하행역 둘 중 하나도 포함되어 있지 않을 때, 구간을 등록할 수 없다.")
	@Test
	void addFailOnBothStationsNotRegistered() {
		// given
		Sections sections = Sections.of(Collections.singletonList(강남역_양재역_구간()));

		// when & then
		assertThatThrownBy(() -> sections.add(정자역_광교역_구간()))
			.isInstanceOf(RuntimeException.class);
	}

	@DisplayName("상행역과 하행역이 이미 노선에 모두 등록되어 있을 때, 구간을 등록할 수 없다.")
	@Test
	void addFailOnBothStationsAlreadyRegistered() {
		// given
		Sections sections = Sections.of(Collections.singletonList(강남역_양재역_구간()));

		// when & then
		assertThatThrownBy(() -> sections.add(강남역_양재역_구간()))
			.isInstanceOf(RuntimeException.class);
	}

	@DisplayName("역 사이 왼쪽에 새로운 구간을 등록한다.")
	@Test
	void addBetweenStationsAtLeftSide() {
		// given
		Sections sections = Sections.of(Collections.singletonList(강남역_정자역_구간()));

		// when
		sections.add(강남역_양재역_구간());

		// then
		assertAll(
			() -> assertThat(sections.getStations().getValues()).isEqualTo(Arrays.asList(
				강남역(),
				양재역(),
				정자역())),
			() -> assertThat(sections.getDistances()).isEqualTo(Arrays.asList(2, 8)));
	}

	@DisplayName("역 사이 오른쪽에 새로운 구간을 등록한다.")
	@Test
	void addBetweenStationsAtRightSide() {
		// given
		Sections sections = Sections.of(Collections.singletonList(강남역_정자역_구간()));

		// when
		sections.add(양재역_정자역_구간());

		// then
		assertAll(
			() -> assertThat(sections.getStations().getValues()).isEqualTo(Arrays.asList(
				강남역(),
				양재역(),
				정자역())),
			() -> assertThat(sections.getDistances()).isEqualTo(Arrays.asList(2, 8)));
	}

	@DisplayName("역 사이 왼쪽에 새로운 구간을 등록할 때, 기존 역 사이 길이보다 크거나 같으면 등록 할 수 없다.")
	@Test
	void addBetweenStationsAtLeftSideFailOnIllegalDistance() {
		// given
		Sections sections = Sections.of(Collections.singletonList(강남역_정자역_구간()));

		// when & then
		assertThatThrownBy(
			() -> sections.add(Section.of(강남역(), 양재역(), 강남역_정자역_구간().getDistance())))
			.isInstanceOf(RuntimeException.class);
	}

	@DisplayName("역 사이 오른쪽에 새로운 구간을 등록할 때, 기존 역 사이 길이보다 크거나 같으면 등록 할 수 없다.")
	@Test
	void addBetweenStationsAtRightSideFailOnIllegalDistance() {
		// given
		Sections sections = Sections.of(Collections.singletonList(강남역_정자역_구간()));

		// when & then
		assertThatThrownBy(
			() -> sections.add(Section.of(양재역(), 정자역(), 강남역_정자역_구간().getDistance())))
			.isInstanceOf(RuntimeException.class);
	}

	@DisplayName("상행 종점에 새로운 구간을 등록한다.")
	@Test
	void addToUpStation() {
		// given

		Sections sections = Sections.of(Collections.singletonList(양재역_정자역_구간()));

		// when
		sections.add(강남역_양재역_구간());

		// then
		assertAll(
			() -> assertThat(sections.getStations().getValues()).isEqualTo(Arrays.asList(
				강남역(),
				양재역(),
				정자역())),
			() -> assertThat(sections.getDistances()).isEqualTo(Arrays.asList(2, 8)));
	}

	@DisplayName("하행 종점에 새로운 구간을 등록한다.")
	@Test
	void addToDownStation() {
		// given
		Sections sections = Sections.of(Collections.singletonList(강남역_양재역_구간()));

		// when
		sections.add(양재역_정자역_구간());

		// then
		assertAll(
			() -> assertThat(sections.getStations().getValues()).isEqualTo(Arrays.asList(
				강남역(),
				양재역(),
				정자역())),
			() -> assertThat(sections.getDistances()).isEqualTo(Arrays.asList(2, 8)));
	}

	@DisplayName("지하철 구간을 삭제한다. (세 역 중 첫번째 역이 제거될 경우)")
	@Test
	void removeByFirstStation() {
		// given
		Sections sections = Sections.of(Arrays.asList(
				강남역_양재역_구간(),
				양재역_정자역_구간()));

		// when
		sections.removeByStation(강남역());

		// then
		assertAll(
			() -> assertThat(sections.getStations().getValues()).isEqualTo(Arrays.asList(
				양재역(),
				정자역())),
			() -> assertThat(sections.getDistances()).isEqualTo(Collections.singletonList(8)));
	}

	@DisplayName("지하철 구간을 삭제한다. (세 역 중 두번째 역이 제거될 경우)")
	@Test
	void removeBySecondStation() {
		// given
		Sections sections = Sections.of(Arrays.asList(
			강남역_양재역_구간(),
			양재역_정자역_구간()));

		// when
		sections.removeByStation(양재역());

		// then
		assertAll(
			() -> assertThat(sections.getStations().getValues()).isEqualTo(Arrays.asList(
				강남역(),
				정자역())),
			() -> assertThat(sections.getDistances()).isEqualTo(Collections.singletonList(10)));
	}

	@DisplayName("지하철 구간을 삭제한다. (세 역 중 세번째 역이 제거될 경우)")
	@Test
	void removeByThirdStation() {
		// given
		Sections sections = Sections.of(Arrays.asList(
			강남역_양재역_구간(),
			양재역_정자역_구간()));

		// when
		sections.removeByStation(정자역());

		// then
		assertAll(
			() -> assertThat(sections.getStations().getValues()).isEqualTo(Arrays.asList(
				강남역(),
				양재역())),
			() -> assertThat(sections.getDistances()).isEqualTo(Collections.singletonList(2)));
	}

	@DisplayName("구간들에서 구간이 하나인 경우 마지막 구간은 제거할 수 없다.")
	@Test
	void removeStationFail() {
		// given
		Sections sections = Sections.of(Collections.singletonList(강남역_양재역_구간()));

		// when & then
		assertThatThrownBy(() -> sections.removeByStation(강남역()))
			.isInstanceOf(RuntimeException.class);
	}
}

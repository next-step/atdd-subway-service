package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.common.domain.Name;
import nextstep.subway.common.exception.DuplicateDataException;
import nextstep.subway.common.exception.InvalidDataException;
import nextstep.subway.station.domain.Station;

@DisplayName("구간 컬렉션 테스트")
class SectionsTest {

	private Section 구간;
	private Station 강남역;
	private Station 역삼역;
	private Station 선릉역;
	private Line 이호선;

	@BeforeEach
	void setup() {
		강남역 = Station.from(Name.from("강남역"));
		역삼역 = Station.from(Name.from("역삼역"));
		선릉역 = Station.from(Name.from("선릉역"));
		구간 = Section.of(null, 강남역, 역삼역, Distance.from(10));
		이호선 = Line.of(Name.from("이호선"), Color.from("green"), Sections.from(구간));
	}

	@Test
	@DisplayName("구간 컬렉션 생성")
	void createSectionsTest() {
		assertDoesNotThrow(() -> Sections.from(구간));
	}

	@Test
	@DisplayName("구간 컬렉션 생성 - 초기 구간이 null인 경우 예외")
	void createSectionsWithoutSectionTest() {
		assertThatIllegalArgumentException()
			.isThrownBy(() -> Sections.from((Section)null));
	}

	@Test
	@DisplayName("순서대로 정렬된 역 조회")
	void sortedStationsTest() {
		// when
		List<Station> stations = Sections.from(구간).sortedStations().list();

		// then
		assertAll(
			() -> assertThat(stations).hasSize(2),
			() -> assertThat(stations).containsExactly(강남역, 역삼역)
		);
	}

	@DisplayName("상행 종점 조회 테스트")
	@Test
	void findFirstUpStationTest() {
		// given
		Section 새로운_구간 = Section.of(이호선, 역삼역, 선릉역, Distance.from(10));

		Sections sections = Sections.from(구간);
		sections.connect(새로운_구간, Collections.singletonList(구간));

		// when
		Station firstUpStation = sections.firstUpStation();

		// then
		assertThat(firstUpStation).isEqualTo(강남역);
	}

	@DisplayName("하행 종점 조회 테스트")
	@Test
	void findLastDownStationTest() {
		// given
		Section 새로운_구간 = Section.of(이호선, 역삼역, 선릉역, Distance.from(10));

		Sections sections = Sections.from(구간);
		sections.connect(새로운_구간, Collections.singletonList(구간));

		// when
		Station lastDownStation = sections.lastDownStation();

		// then
		assertThat(lastDownStation).isEqualTo(선릉역);
	}

	@DisplayName("구간 추가 시 상행역, 하행역이 중복이면 예외 발생")
	@Test
	void addSectionWithDuplicateStationTest() {
		// given
		Sections sections = Sections.from(구간);

		// when, then
		assertThatThrownBy(() -> sections.connect(구간, Collections.singletonList(구간)))
			.isInstanceOf(DuplicateDataException.class);
	}

	@DisplayName("구간 추가 시 상행역, 하행역이 모두 존재하지 않으면 예외 발생")
	@Test
	void addSectionWithNotExistsStationTest() {
		// given
		Station 삼성역 = Station.from(Name.from("삼성역"));
		Section 새로운_구간 = Section.of(이호선, 삼성역, 선릉역, Distance.from(10));

		Sections sections = Sections.from(구간);

		// when, then
		assertThatThrownBy(() -> sections.connect(새로운_구간, Collections.singletonList(구간)))
			.isInstanceOf(InvalidDataException.class);
	}

	@DisplayName("구간 추가 시 상행역이 존재하고 하행역이 존재하지 않으면 구간 추가")
	@Test
	void addSectionWithExistsUpStationTest() {
		// given
		Section 새로운_구간 = Section.of(이호선, 강남역, 선릉역, Distance.from(5));

		Sections sections = Sections.from(구간);

		// when
		sections.connect(새로운_구간, Collections.singletonList(구간));

		// then
		assertAll(
			() -> assertThat(sections.getSections()).hasSize(2),
			() -> assertThat(sections.sortedStations().list()).containsExactly(강남역, 선릉역, 역삼역),
			() -> assertThat(sections.getSections().get(0).getDistance()).isEqualTo(5),
			() -> assertThat(sections.getSections().get(0).getUpStation()).isEqualTo(선릉역),
			() -> assertThat(sections.getSections().get(0).getDownStation()).isEqualTo(역삼역),
			() -> assertThat(sections.getSections().get(1).getDistance()).isEqualTo(5),
			() -> assertThat(sections.getSections().get(1).getUpStation()).isEqualTo(강남역),
			() -> assertThat(sections.getSections().get(1).getDownStation()).isEqualTo(선릉역)
		);

	}

	@DisplayName("구간 추가 시 하행역이 존재하고 상행역이 존재하지 않으면 구간 추가")
	@Test
	void addSectionWithExistsDownStationTest() {
		// given
		Section 새로운_구간 = Section.of(이호선, 선릉역, 역삼역, Distance.from(5));

		Sections sections = Sections.from(구간);

		// when
		sections.connect(새로운_구간, Collections.singletonList(구간));

		// then
		assertAll(
			() -> assertThat(sections.getSections()).hasSize(2),
			() -> assertThat(sections.sortedStations().list()).containsExactly(강남역, 선릉역, 역삼역),
			() -> assertThat(sections.getSections().get(0).getDistance()).isEqualTo(5),
			() -> assertThat(sections.getSections().get(0).getUpStation()).isEqualTo(강남역),
			() -> assertThat(sections.getSections().get(0).getDownStation()).isEqualTo(선릉역),
			() -> assertThat(sections.getSections().get(1).getDistance()).isEqualTo(5),
			() -> assertThat(sections.getSections().get(1).getUpStation()).isEqualTo(선릉역),
			() -> assertThat(sections.getSections().get(1).getDownStation()).isEqualTo(역삼역)
		);
	}

	@DisplayName("구간 추가 시 하행역이 상행 종점이면 구간 추가")
	@Test
	void addSectionWithDownStationIsFirstStationTest() {
		// given
		Section 새로운_구간 = Section.of(이호선, 선릉역, 강남역, Distance.from(5));

		Sections sections = Sections.from(구간);

		// when
		sections.connect(새로운_구간, Collections.singletonList(구간));

		// then
		assertAll(
			() -> assertThat(sections.getSections()).hasSize(2),
			() -> assertThat(sections.sortedStations().list()).containsExactly(선릉역, 강남역, 역삼역),
			() -> assertThat(sections.getSections().get(0).getDistance()).isEqualTo(10),
			() -> assertThat(sections.getSections().get(0).getUpStation()).isEqualTo(강남역),
			() -> assertThat(sections.getSections().get(0).getDownStation()).isEqualTo(역삼역),
			() -> assertThat(sections.getSections().get(1).getDistance()).isEqualTo(5),
			() -> assertThat(sections.getSections().get(1).getUpStation()).isEqualTo(선릉역),
			() -> assertThat(sections.getSections().get(1).getDownStation()).isEqualTo(강남역),
			() -> assertThat(sections.firstUpStation()).isEqualTo(선릉역)
		);
	}

	@DisplayName("구간 추가 시 상행역이 하행 종점이면 구간 추가")
	@Test
	void addSectionWithUpStationIsLastStationTest() {
		// given
		Section 새로운_구간 = Section.of(이호선, 역삼역, 선릉역, Distance.from(5));

		Sections sections = Sections.from(구간);

		// when
		sections.connect(새로운_구간, Collections.singletonList(구간));

		// then
		assertAll(
			() -> assertThat(sections.getSections()).hasSize(2),
			() -> assertThat(sections.sortedStations().list()).containsExactly(강남역, 역삼역, 선릉역),
			() -> assertThat(sections.getSections().get(0).getDistance()).isEqualTo(10),
			() -> assertThat(sections.getSections().get(0).getUpStation()).isEqualTo(강남역),
			() -> assertThat(sections.getSections().get(0).getDownStation()).isEqualTo(역삼역),
			() -> assertThat(sections.getSections().get(1).getDistance()).isEqualTo(5),
			() -> assertThat(sections.getSections().get(1).getUpStation()).isEqualTo(역삼역),
			() -> assertThat(sections.getSections().get(1).getDownStation()).isEqualTo(선릉역),
			() -> assertThat(sections.lastDownStation()).isEqualTo(선릉역)
		);
	}

	@DisplayName("구간 삭제 - 상행 종점 삭제")
	@Test
	void removeFirstUpStationTest() {
		// given
		Section 새로운_구간 = Section.of(이호선, 역삼역, 선릉역, Distance.from(5));

		Sections sections = Sections.from(구간);
		sections.connect(새로운_구간, Collections.singletonList(구간));

		// when
		sections.remove(구간, null);

		// then
		assertAll(
			() -> assertThat(sections.getSections()).hasSize(1),
			() -> assertThat(sections.sortedStations().list()).containsExactly(역삼역, 선릉역),
			() -> assertThat(sections.getSections().get(0).getDistance()).isEqualTo(5),
			() -> assertThat(sections.getSections().get(0).getUpStation()).isEqualTo(역삼역),
			() -> assertThat(sections.getSections().get(0).getDownStation()).isEqualTo(선릉역),
			() -> assertThat(sections.firstUpStation()).isEqualTo(역삼역)
		);
	}

	@DisplayName("구간 삭제 - 하행 종점 삭제")
	@Test
	void removeLastDownStationTest() {
		// given
		Section 새로운_구간 = Section.of(이호선, 역삼역, 선릉역, Distance.from(5));

		Sections sections = Sections.from(구간);
		sections.connect(새로운_구간, Collections.singletonList(구간));

		// when
		sections.remove(null, 새로운_구간);

		// then
		assertAll(
			() -> assertThat(sections.getSections()).hasSize(1),
			() -> assertThat(sections.sortedStations().list()).containsExactly(강남역, 역삼역),
			() -> assertThat(sections.getSections().get(0).getDistance()).isEqualTo(10),
			() -> assertThat(sections.getSections().get(0).getUpStation()).isEqualTo(강남역),
			() -> assertThat(sections.getSections().get(0).getDownStation()).isEqualTo(역삼역),
			() -> assertThat(sections.lastDownStation()).isEqualTo(역삼역)
		);
	}

	@DisplayName("구간 삭제 - 중간 역 삭제")
	@Test
	void removeMiddleStationTest() {
		// given
		Section 새로운_구간 = Section.of(이호선, 역삼역, 선릉역, Distance.from(5));

		Sections sections = Sections.from(구간);
		sections.connect(새로운_구간, Collections.singletonList(구간));

		// when
		sections.remove(새로운_구간, 구간);

		// then
		assertAll(
			() -> assertThat(sections.getSections()).hasSize(1),
			() -> assertThat(sections.sortedStations().list()).containsExactly(강남역, 선릉역),
			() -> assertThat(sections.getSections().get(0).getDistance()).isEqualTo(15),
			() -> assertThat(sections.getSections().get(0).getUpStation()).isEqualTo(강남역),
			() -> assertThat(sections.getSections().get(0).getDownStation()).isEqualTo(선릉역),
			() -> assertThat(sections.firstUpStation()).isEqualTo(강남역),
			() -> assertThat(sections.lastDownStation()).isEqualTo(선릉역)
		);
	}

	@DisplayName("구간 삭제 시, 구간이 하나인 경우 예외 발생")
	@Test
	void removeFromOnlyOneSectionTest() {
		// given
		Sections sections = Sections.from(구간);

		// when & then
		assertThatThrownBy(() -> sections.remove(구간, 구간))
			.isInstanceOf(InvalidDataException.class)
			.hasMessage("구간이 하나인 노선에서는 제거할 수 없습니다.");
	}
}
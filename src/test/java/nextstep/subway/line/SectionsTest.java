package nextstep.subway.line;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.common.exception.InternalServerException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;

public class SectionsTest {
	@Test
	@DisplayName("구간이 있는 상태에서 구간을 추가한다.")
	void add_success1() {
		Line line = new Line(LineTest.신분당선, LineTest.BG_RED_600);
		Section section1 = new Section(line, LineTest.삼성역, LineTest.선릉역, 5);
		Section section2 = new Section(line, LineTest.선릉역, LineTest.역삼역, 5);
		line.addSection(section1);
		Sections sections = new Sections(new ArrayList<>(Collections.singletonList(section1)));

		sections.add(section2);

		assertThat(new Sections(Arrays.asList(section1, section2))).isEqualTo(sections);
	}

	@Test
	@DisplayName("구간이 등록되지 않은 상태에서 구간을 추가한다.")
	void add_success2() {
		Line line = new Line(LineTest.신분당선, LineTest.BG_RED_600);
		Section section = new Section(line, LineTest.삼성역, LineTest.선릉역, 5);
		Sections sections = new Sections(new ArrayList<>());

		sections.add(section);

		assertThat(new Sections(Collections.singletonList(section))).isEqualTo(sections);
	}

	@Test
	@DisplayName("상행, 하행역이 동일한 구간을 추가하면 예외")
	void add_sameUpDownStation_exception() {
		Line line = new Line(LineTest.신분당선, LineTest.BG_RED_600);
		Sections sections = new Sections(new ArrayList<>());
		Section section = new Section(line, LineTest.삼성역, LineTest.선릉역, 5);

		sections.add(section);

		assertThatThrownBy(() -> sections.add(section))
			.isInstanceOf(InternalServerException.class)
			.hasMessage("이미 등록된 구간 입니다.");
	}

	@Test
	@DisplayName("구간이 등록된 상태에서 구간 추가 시 등록되어있는 구간의 상행, 하행 지하철역에 하나라도 포함되어 있지 않으면 예외")
	void add_hasNoUpDownStation_exception() {
		Line line = new Line(LineTest.신분당선, LineTest.BG_RED_600);
		Sections sections = new Sections(new ArrayList<>());
		Section section = new Section(line, LineTest.삼성역, LineTest.선릉역, 5);
		Section noRelationSection = new Section(line, LineTest.강남역, LineTest.역삼역, 5);

		sections.add(section);

		assertThatThrownBy(() -> sections.add(noRelationSection))
			.isInstanceOf(InternalServerException.class)
			.hasMessage("등록할 수 없는 구간 입니다.");
	}

	@Test
	@DisplayName("추가하려는 구간의 길이가 기존에 존재하는 길이와 같거나 긴 경우 예외")
	void add_insideSectionWithTooFarDistance_exception() {
		Line line = new Line(LineTest.신분당선, LineTest.BG_RED_600);
		Sections sections = new Sections(new ArrayList<>());
		Section section = new Section(line, LineTest.삼성역, LineTest.역삼역, 10);
		Section sectionTooFarDistance = new Section(line, LineTest.선릉역, LineTest.역삼역, 10);
		sections.add(section);

		assertThatThrownBy(() -> sections.add(sectionTooFarDistance))
			.isInstanceOf(InternalServerException.class)
			.hasMessage("역과 역 사이의 거리보다 좁은 거리를 입력해주세요");
	}

	@Test
	@DisplayName("상행 구간을 삭제한다.")
	void remove_success1() {
		Line line = new Line(LineTest.신분당선, LineTest.BG_RED_600);
		Sections sections = new Sections(new ArrayList<>());
		Section section1 = new Section(line, LineTest.삼성역, LineTest.선릉역, 5);
		Section section2 = new Section(line, LineTest.선릉역, LineTest.역삼역, 5);
		sections.add(section1);
		sections.add(section2);

		sections.removeByStation(LineTest.삼성역);
		Sections expected = new Sections(Collections.singletonList(new Section(line, LineTest.선릉역, LineTest.역삼역, 5)));

		assertThat(expected).isEqualTo(sections);
	}

	@Test
	@DisplayName("중간 구간을 삭제한다.")
	void remove_success2() {
		Line line = new Line(LineTest.신분당선, LineTest.BG_RED_600);
		Sections sections = new Sections(new ArrayList<>());
		Section section1 = new Section(line, LineTest.삼성역, LineTest.선릉역, 5);
		Section section2 = new Section(line, LineTest.선릉역, LineTest.역삼역, 5);
		sections.add(section1);
		sections.add(section2);

		sections.removeByStation(LineTest.선릉역);
		Sections expected = new Sections(Collections.singletonList(new Section(line, LineTest.삼성역, LineTest.역삼역, 10)));

		assertThat(expected).isEqualTo(sections);
	}

	@Test
	@DisplayName("하행 구간을 삭제한다.")
	void remove_success3() {
		Line line = new Line(LineTest.신분당선, LineTest.BG_RED_600);
		Sections sections = new Sections(new ArrayList<>());
		Section section1 = new Section(line, LineTest.삼성역, LineTest.선릉역, 5);
		Section section2 = new Section(line, LineTest.선릉역, LineTest.역삼역, 5);
		sections.add(section1);
		sections.add(section2);

		sections.removeByStation(LineTest.역삼역);
		Sections expected = new Sections(Collections.singletonList(new Section(line, LineTest.삼성역, LineTest.선릉역, 5)));

		assertThat(expected).isEqualTo(sections);
	}

	@Test
	@DisplayName("1개의 구간을 가지고 있을때 삭제하면 예외")
	void remove_onlyHasOneSection_exception() {
		Line line = new Line(LineTest.신분당선, LineTest.BG_RED_600);
		Sections sections = new Sections(new ArrayList<>());
		Section section1 = new Section(line, LineTest.삼성역, LineTest.선릉역, 5);
		sections.add(section1);

		assertThatThrownBy(() -> sections.removeByStation(LineTest.역삼역))
			.isInstanceOf(InternalServerException.class)
			.hasMessage("최소한 1개의 구간이 등록되어 있어야 합니다.");
	}

	@Test
	@DisplayName("상행구간부터 하행구간 순서대로 지하철역 반환")
	void getOrderedStations_success() {
		Line line = new Line(LineTest.신분당선, LineTest.BG_RED_600);
		Sections sections = new Sections(new ArrayList<>());
		Section section1 = new Section(line, LineTest.삼성역, LineTest.선릉역, 5);
		Section section2 = new Section(line, LineTest.선릉역, LineTest.역삼역, 5);
		sections.add(section2);
		sections.add(section1);

		List<Station> stations = sections.getOrderedStations();

		assertThat(Arrays.asList(LineTest.삼성역, LineTest.선릉역, LineTest.역삼역)).isEqualTo(stations);
	}

	@Test
	@DisplayName("구간들이 포함되는 노선들을 반환한다.")
	void getLinesDistinct_success() {
		Line 신분당선 = new Line(LineTest.신분당선, LineTest.BG_RED_600);
		Line 구분당선 = new Line(LineTest.구분당선, LineTest.BG_BLUE_600);
		Sections sections = new Sections(new ArrayList<>());
		Section 삼성_선릉_구간 = new Section(신분당선, LineTest.삼성역, LineTest.선릉역, 5);
		Section 선릉_역삼_구간 = new Section(신분당선, LineTest.선릉역, LineTest.역삼역, 5);
		Section 역삼_강남_구간 = new Section(구분당선, LineTest.역삼역, LineTest.강남역, 5);
		sections.add(삼성_선릉_구간);
		sections.add(선릉_역삼_구간);
		sections.add(역삼_강남_구간);

		assertThat(sections.getLinesDistinct().size()).isEqualTo(2);
		assertThat(sections.getLinesDistinct()).contains(신분당선, 구분당선);
	}

	@Test
	@DisplayName("구간들 안에 특정 지하철역이 있는지 여부를 반환한다.")
	void contains_success() {
		Line 신분당선 = new Line(LineTest.신분당선, LineTest.BG_RED_600);
		Sections sections = new Sections(new ArrayList<>());
		Section 삼성_선릉_구간 = new Section(신분당선, LineTest.삼성역, LineTest.선릉역, 5);
		Section 선릉_역삼_구간 = new Section(신분당선, LineTest.선릉역, LineTest.역삼역, 5);
		sections.add(삼성_선릉_구간);
		sections.add(선릉_역삼_구간);

		assertThat(sections.contains(LineTest.삼성역)).isTrue();
		assertThat(sections.contains(LineTest.선릉역)).isTrue();
		assertThat(sections.contains(LineTest.역삼역)).isTrue();
		assertThat(sections.contains(LineTest.강남역)).isFalse();
	}
}

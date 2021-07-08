package nextstep.subway.line.acceptance;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.section.domain.Section;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;

@DataJpaTest
public class LineTest {

	@Autowired
	LineRepository lineRepository;
	@Autowired
	StationRepository stationRepository;

	Line 신분당선;
	Station 강남역;
	Station 광교역;
	Station 양재역;
	Station 판교역;

	@BeforeEach
	void setUp() {
		강남역 = stationRepository.save(new Station("강남역"));
		광교역 = stationRepository.save(new Station("광교역"));
		양재역 = stationRepository.save(new Station("양재역"));
		판교역 = stationRepository.save(new Station("판교역"));
		신분당선 = lineRepository.save(new Line("신분당선", "bg-red-600", 강남역, 광교역, 10));
	}

	@DisplayName("새로운 구간 등록")
	@Test
	void addNewSection() {
		Section newSection = new Section(신분당선, 강남역, 양재역, 3);
		신분당선.addNewSection(newSection);
		assertThat(
			신분당선.getSections()
				.stream()
				.anyMatch(section -> section.getUpStation() == 강남역)
		).isTrue();

		assertThat(
			신분당선.getSections()
				.stream()
				.anyMatch(section -> section.getDownStation() == 양재역)
		).isTrue();
	}

	@DisplayName("이미 등록된 구간 등록")
	@Test
	void addNewSectionWithExistSection() {
		Section newSection = new Section(신분당선, 강남역, 광교역, 3);

		assertThatThrownBy(
			() -> 신분당선.addNewSection(newSection)
		).isInstanceOf(RuntimeException.class);
	}

	@DisplayName("등록할 수 없는 구간 등록")
	@Test
	void addNewSectionWithEnableSection() {
		Section newSection = new Section(신분당선, 판교역, 양재역, 3);

		assertThatThrownBy(
			() -> 신분당선.addNewSection(newSection)
		).isInstanceOf(RuntimeException.class);
	}

	@DisplayName("등록 불가능한 거리 등록")
	@Test
	void addNewSectionWithEnableDistance() {
		Section newSection = new Section(신분당선, 강남역, 양재역, 10);

		assertThatThrownBy(
			() -> 신분당선.addNewSection(newSection)
		).isInstanceOf(RuntimeException.class);
	}

	// TODO : 구간 삭제 테스트
	@DisplayName("하행 종점 제거")
	@Test
	void removeSectionWithFinalDownStation() {
		Section newSection = new Section(신분당선, 강남역, 양재역, 3);
		신분당선.addNewSection(newSection);

		신분당선.removeSection(광교역.getId());
		assertThat(
			신분당선.getSections()
				.stream()
				.noneMatch(section -> section.getDownStation() == 광교역)
		).isTrue();

		assertThat(
			신분당선.getSections()
				.stream()
				.noneMatch(section -> section.getUpStation() == 광교역)
		).isTrue();
	}

	@DisplayName("상행 종점 제거")
	@Test
	void removeSectionWithFirstUpStation() {
		Section newSection = new Section(신분당선, 강남역, 양재역, 3);
		신분당선.addNewSection(newSection);

		신분당선.removeSection(강남역.getId());
		assertThat(
			신분당선.getSections()
				.stream()
				.noneMatch(section -> section.getDownStation() == 강남역)
		).isTrue();

		assertThat(
			신분당선.getSections()
				.stream()
				.noneMatch(section -> section.getUpStation() == 강남역)
		).isTrue();
	}

	@DisplayName("중간역 제거")
	@Test
	void removeSectionWithMiddleStation() {
		Section newSection = new Section(신분당선, 강남역, 양재역, 3);
		신분당선.addNewSection(newSection);

		신분당선.removeSection(양재역.getId());
		assertThat(
			신분당선.getSections()
				.stream()
				.noneMatch(section -> section.getDownStation() == 양재역)
		).isTrue();

		assertThat(
			신분당선.getSections()
				.stream()
				.noneMatch(section -> section.getUpStation() == 양재역)
		).isTrue();
	}
}

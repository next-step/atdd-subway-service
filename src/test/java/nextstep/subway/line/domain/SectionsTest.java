package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.Message;
import nextstep.subway.station.domain.Station;

public class SectionsTest {
	private Line 신분당선;
	private Station 강남역;
	private Station 양재역;
	private Station 판교역;
	private Station 광교역;
	private Sections sections;

	@BeforeEach
	public void setUp() {
		강남역 = new Station("강남역");
		양재역 = new Station("양재역");
		판교역 = new Station("판교역");
		광교역 = new Station("광교역");
		신분당선 = new Line("신분당선", "bg-red-600", 강남역, 판교역, 10);
		sections = 신분당선.getSections();
	}

	@DisplayName("등록된 구간이 없을시 정상적으로 구간을 등록한다.")
	@Test
	void add_WhenEmptySections() {
		Section section = new Section(신분당선, 강남역, 양재역, 5);
		Sections sections = new Sections();
		sections.add(section);

		assertThat(sections.getSections())
			.hasSize(1)
			.containsExactly(section);
	}

	@DisplayName("상행역과 연결된 구간을 추가한다.")
	@Test
	void add_WhenValidSection1() {
		Section section = new Section(신분당선, 강남역, 양재역, 3);
		신분당선.addSection(section);

		Section upSection = 신분당선.getSections().findSectionByUpStation(강남역).orElse(new Section());
		Section downSection = 신분당선.getSections().findSectionByDownStation(판교역).orElse(new Section());

		assertThat(upSection.getUpStation().equals(강남역)).isTrue();
		assertThat(upSection.getDownStation().equals(양재역)).isTrue();
		assertThat(upSection.getDistance()).isEqualTo(3);
		assertThat(downSection.getUpStation().equals(양재역)).isTrue();
		assertThat(downSection.getDownStation().equals(판교역)).isTrue();
		assertThat(downSection.getDistance()).isEqualTo(7);
	}

	@DisplayName("하행역과 연결된 구간을 추가한다.")
	@Test
	void add_WhenValidSection2() {
		Section section = new Section(신분당선, 양재역, 판교역, 3);
		신분당선.addSection(section);

		Section upSection = 신분당선.getSections().findSectionByUpStation(강남역).orElse(new Section());
		Section downSection = 신분당선.getSections().findSectionByDownStation(판교역).orElse(new Section());

		assertThat(upSection.getUpStation().equals(강남역)).isTrue();
		assertThat(upSection.getDownStation().equals(양재역)).isTrue();
		assertThat(upSection.getDistance()).isEqualTo(7);
		assertThat(downSection.getUpStation().equals(양재역)).isTrue();
		assertThat(downSection.getDownStation().equals(판교역)).isTrue();
		assertThat(downSection.getDistance()).isEqualTo(3);
	}

	@DisplayName("이미 등록된 구간 생성시 오류가 발생한다.")
	@Test
	void createSection_ThrowRuntimeException1() {
		assertThatExceptionOfType(RuntimeException.class)
			.isThrownBy(() -> sections.add(new Section(신분당선, 강남역, 판교역, 3)))
			.withMessage(Message.EXIST_SECTION);
	}

	@DisplayName("연결되지 않은 구간 생성시 오류가 발생한다.")
	@Test
	void createSection_ThrowRuntimeException2() {
		assertThatExceptionOfType(RuntimeException.class)
			.isThrownBy(() -> sections.add(new Section(신분당선, 양재역, 광교역, 3)))
			.withMessage(Message.INVALID_SECTION);
	}

	@DisplayName("노선의 구간중 상행역이 같은 구간을 찾는다.")
	@Test
	void findSectionByUpStation() {
		Section section = sections.findSectionByUpStation(강남역).orElse(new Section());
		assertThat(section.getUpStation()).isEqualTo(강남역);
	}

	@DisplayName("노선의 구간중 하행역이 같은 구간을 찾는다.")
	@Test
	void findSectionByDownStation() {
		Section section = sections.findSectionByDownStation(판교역).orElse(new Section());
		assertThat(section.getDownStation()).isEqualTo(판교역);
	}
}

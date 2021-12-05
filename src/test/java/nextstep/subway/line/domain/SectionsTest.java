package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.station.domain.Station;

class SectionsTest {

	private final Station 강남역 = new Station("강남역");
	private final Station 양재역 = new Station("양재역");
	private final Station 판교역 = new Station("판교역");
	private final Station 광교역 = new Station("광교역");
	private final Line 신분당선 = new Line("신분당선", "red");

	@DisplayName("구간추가/상행종착")
	@Test
	void add_upTerminal() {
		final Sections sections = new Sections();
		sections.add(new Section(신분당선, 판교역, 광교역, 4));
		sections.add(new Section(신분당선, 강남역, 판교역, 10));

		assertThat(sections.getStations()).containsExactly(강남역, 판교역, 광교역);
	}

	@DisplayName("구간추가/하행종착")
	@Test
	void add_downTerminal() {
		final Sections sections = new Sections();
		sections.add(new Section(신분당선, 강남역, 양재역, 10));
		sections.add(new Section(신분당선, 양재역, 판교역, 10));

		assertThat(sections.getStations()).containsExactly(강남역, 양재역, 판교역);
	}

	@DisplayName("구간추가/구간사이/동일상행역")
	@Test
	void add_between_same_upStation() {
		final Sections sections = new Sections();
		sections.add(new Section(신분당선, 강남역, 광교역, 10));
		sections.add(new Section(신분당선, 강남역, 양재역, 3));

		assertThat(sections.getStations()).containsExactly(강남역, 양재역, 광교역);
	}

	@DisplayName("구간추가/구간사이/동일하행역")
	@Test
	void add_between_same_downStation() {
		final Sections sections = new Sections();
		sections.add(new Section(신분당선, 강남역, 광교역, 10));
		sections.add(new Section(신분당선, 판교역, 광교역, 4));

		assertThat(sections.getStations()).containsExactly(강남역, 판교역, 광교역);
	}

	@DisplayName("구간추가/중복이면 실패")
	@Test
	void add_duplicated_section() {
		final Section section = new Section(신분당선, 강남역, 광교역, 10);
		final Sections sections = new Sections();
		sections.add(section);

		assertThatExceptionOfType(IllegalArgumentException.class)
			.isThrownBy(() -> sections.add(section));
	}

	@DisplayName("구간추가/노선에 포함되지 않은 역으로만 구성되어 실패")
	@Test
	void add_not_found_stations_in_line() {
		final Station 신분당선아닌역1 = new Station("서울역");
		final Station 신분당선아닌역2 = new Station("신도림역");

		final Sections sections = new Sections();
		sections.add(new Section(신분당선, 강남역, 양재역, 10));

		assertThatExceptionOfType(IllegalArgumentException.class)
			.isThrownBy(() -> sections.add(new Section(신분당선, 신분당선아닌역1, 신분당선아닌역2, 10)));
	}

	@DisplayName("구간제거/상행종착역")
	@Test
	void delete_upTerminal() {
		final Sections sections = new Sections();
		sections.add(new Section(신분당선, 강남역, 양재역, 4));
		sections.add(new Section(신분당선, 양재역, 판교역, 6));
		sections.add(new Section(신분당선, 판교역, 광교역, 3));

		sections.delete(강남역);

		assertThat(sections.getStations()).containsExactly(양재역, 판교역, 광교역);
	}

	@DisplayName("구간제거/하행종착역")
	@Test
	void delete_downTerminal() {
		final Sections sections = new Sections();
		sections.add(new Section(신분당선, 강남역, 양재역, 4));
		sections.add(new Section(신분당선, 양재역, 판교역, 6));
		sections.add(new Section(신분당선, 판교역, 광교역, 3));

		sections.delete(광교역);

		assertThat(sections.getStations()).containsExactly(강남역, 양재역, 판교역);
	}

	@DisplayName("구간제거/구간사이역")
	@Test
	void delete_between() {
		final Sections sections = new Sections();
		sections.add(new Section(신분당선, 강남역, 양재역, 4));
		sections.add(new Section(신분당선, 양재역, 판교역, 6));
		sections.add(new Section(신분당선, 판교역, 광교역, 3));

		sections.delete(양재역);

		assertThat(sections.getStations()).containsExactly(강남역, 판교역, 광교역);
	}

	@DisplayName("구간제거/유일한 구간의 역 제거시 실패")
	@Test
	void delete_the_only_1_remaining() {
		final Sections sections = new Sections();
		sections.add(new Section(신분당선, 강남역, 양재역, 4));

		assertThatExceptionOfType(IllegalArgumentException.class)
			.isThrownBy(() -> sections.delete(강남역));
	}

	@DisplayName("상행->하행 순으로 정렬된 모든 역의 목록 조회")
	@Test
	void getStations() {
		final Sections sections = new Sections();
		sections.add(new Section(신분당선, 판교역, 광교역, 10));
		sections.add(new Section(신분당선, 강남역, 판교역, 10));

		assertThat(sections.getStations()).containsExactly(강남역, 판교역, 광교역);
	}

	@DisplayName("구간이 없을 때 모든 역의 목록 조회 시 비어있어야 함")
	@Test
	void getStations_empty() {
		final Sections sections = new Sections();
		assertThat(sections.getStations().size()).isEqualTo(0);
	}
}

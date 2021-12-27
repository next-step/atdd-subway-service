package nextstep.subway.line;

import static nextstep.subway.station.StationTest.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.StationTest;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;

@DisplayName("구간들 기능")
public class SectionsTest {

	@Test
	@DisplayName("생성 테스트")
	void createTest() {
		// given
		Sections expected = Sections.of(Collections.singletonList(SectionTest.SECTION_1));

		// when
		Sections sections = Sections.of(Collections.singletonList(SectionTest.SECTION_1));

		// then
		assertThat(sections).isEqualTo(expected);
	}

	@Test
	@DisplayName("추가 테스트")
	void addTest() {
		// given
		Sections sections = Sections.of();
		Sections expected = Sections.of(Collections.singletonList(SectionTest.SECTION_1));

		// when
		sections.add(SectionTest.SECTION_1);

		// then
		assertThat(sections).isEqualTo(expected);
	}

	@Test
	@DisplayName("역 목록 반환 테스트")
	void getStationsTest() {
		// given
		Sections sections = Sections.of(Collections.singletonList(SectionTest.SECTION_1));

		// when
		Stations stations = sections.getStations();

		// then
		assertThat(stations.isEqualSize(2)).isTrue();
	}

	@Test
	@DisplayName("역 목록 정렬 테스트")
	void getStationsSortTest() {
		// given
		Section section1 = Section.of(1L, 노포역, 서면역, 2);
		Section section2 = Section.of(2L, 서면역, 부산진역, 3);
		Section section3 = Section.of(3L, 부산진역, 다대포해수욕장역, 10);

		Sections sections = Sections.of(Arrays.asList(section1, section2, section3));

		// when
		Stations stations = sections.getStations();

		// then
		assertThat(stations.toList()).containsExactly(노포역, 서면역, 부산진역, 다대포해수욕장역);
	}

	@Test
	@DisplayName("구간들 사이에 새 구간을 추가한다")
	void updateSectionsTest() {
		// given
		Section section1 = Section.of(1L, 노포역, 서면역, 10);
		Section section2 = Section.of(2L, 서면역, 부산진역, 5);
		Section section3 = Section.of(3L, 부산진역, 다대포해수욕장역, 10);
		Sections sections = Sections.of(Arrays.asList(section1, section2, section3));
		Section section4 = Section.of(4L, 서면역, 범내골역, 1);

		// when
		sections.addStation(section4);

		// then
		assertAll(
			() -> assertThat(sections.getStations().toList()).containsExactly(노포역, 서면역, 범내골역, 부산진역, 다대포해수욕장역),
			() -> assertThat(section2.getDistance()).isEqualTo(Distance.of(4))
		);
	}

	@Test
	@DisplayName("구간들 사이에 새 구간을 추가한다2")
	void updateSectionsTest2() {
		// given
		Section section1 = Section.of(1L, 노포역, 서면역, 10);
		Section section2 = Section.of(2L, 서면역, 부산진역, 5);
		Section section3 = Section.of(3L, 부산진역, 다대포해수욕장역, 10);
		Sections sections = Sections.of(Arrays.asList(section1, section2, section3));
		Section section4 = Section.of(4L, 범내골역, 서면역, 1);

		// when
		sections.addStation(section4);

		// then
		assertAll(
			() -> assertThat(sections.getStations().toList())
				.containsExactly(노포역, 범내골역, 서면역, 부산진역, 다대포해수욕장역),
			() -> assertThat(section1.getDistance()).isEqualTo(Distance.of(9))
		);
	}

	@Test
	@DisplayName("상행종점에 새 구간을 추가한다")
	void updateSectionsTest3() {
		// given
		Section section1 = Section.of(1L, 노포역, 서면역, 10);
		Section section2 = Section.of(2L, 서면역, 부산진역, 5);
		Section section3 = Section.of(3L, 부산진역, 다대포해수욕장역, 10);
		Sections sections = Sections.of(Arrays.asList(section1, section2, section3));
		Station 새상행종점 = Station.of(99L, "새상행종점");
		Section section4 = Section.of(4L, 새상행종점, 노포역, 1);

		// when
		sections.addStation(section4);

		// then
		assertThat(sections.getStations().toList())
			.containsExactly(새상행종점, 노포역, 서면역, 부산진역, 다대포해수욕장역);
	}

	@Test
	@DisplayName("하행종점에 새 구간을 추가한다")
	void updateSectionsTest4() {
		// given
		Section section1 = Section.of(1L, 노포역, 서면역, 10);
		Section section2 = Section.of(2L, 서면역, 부산진역, 5);
		Section section3 = Section.of(3L, 부산진역, 다대포해수욕장역, 10);
		Sections sections = Sections.of(Arrays.asList(section1, section2, section3));
		Station 새하행종점 = Station.of(99L, "새하행종점");
		Section section4 = Section.of(4L, 다대포해수욕장역, 새하행종점, 1);

		// when
		sections.addStation(section4);

		// then
		assertThat(sections.getStations().toList())
			.containsExactly(노포역, 서면역, 부산진역, 다대포해수욕장역, 새하행종점);
	}

	@DisplayName("역들에 해당되는 노선을 탐색한다")
	@Test
	void getLinesTest() {
		// given
		List<Station> stationList = Arrays.asList(StationTest.노포역, StationTest.서면역, StationTest.전포역);
		Sections sections = LineTest.일호선.getSections();
		Sections sections2 = LineTest.이호선.getSections();
		sections.addAll(sections2);

		// when
		Lines lines = sections.getLines(Stations.of(stationList));

		// then
		assertThat(lines.toList()).containsExactly(LineTest.일호선, LineTest.이호선);
	}

}

package nextstep.subway.path.domain;

import static nextstep.subway.generator.SectionGenerator.*;
import static nextstep.subway.generator.StationGenerator.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.common.domain.Name;

@DisplayName("구간 간선")
class SectionWeightEdgeTest {

	@DisplayName("구간 간선 생성")
	@Test
	void createSectionWeightEdgeTest() {
		assertThatNoException()
			.isThrownBy(() -> assertDoesNotThrow(() ->
				SectionWeightEdge.from(section("강남역", "양재역", 10))));
	}

	@DisplayName("구간 간선 생성 실패")
	@Test
	void createSectionWeightEdgeFailTest() {
		assertThatIllegalArgumentException()
			.isThrownBy(() -> SectionWeightEdge.from(null));
	}

	@DisplayName("구간 간선 조회")
	@Test
	void getSectionWeightEdgeTest() {
		SectionWeightEdge sectionWeightEdge = SectionWeightEdge.from(section("강남역", "양재역", 10));
		assertAll(
			() -> assertThat(sectionWeightEdge.getWeight()).isEqualTo(10d),
			() -> assertThat(sectionWeightEdge.getSource()).isEqualTo(station(Name.from("강남역"))),
			() -> assertThat(sectionWeightEdge.getTarget()).isEqualTo(station(Name.from("양재역")))
		);
	}

}
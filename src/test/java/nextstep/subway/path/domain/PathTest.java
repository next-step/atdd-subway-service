package nextstep.subway.path.domain;

import static nextstep.subway.generator.SectionGenerator.*;
import static nextstep.subway.generator.StationGenerator.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.common.domain.Name;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Stations;

@DisplayName("경로")
class PathTest {

	@DisplayName("경로 생성")
	@Test
	void createSectionTest() {
		assertThatNoException()
			.isThrownBy(() -> Path.of(
				Stations.from(
					Arrays.asList(station(Name.from("강남역")), station(Name.from("양재역")))
				),
				Distance.from(5),
				Sections.from(
					Collections.singletonList(section("강남역", "양재역", 10)))
			));
	}

	@DisplayName("지하철 역 경로가 없을 시 예외 발생")
	@Test
	void createSectionFailTest() {
		assertThatIllegalArgumentException()
			.isThrownBy(() -> Path.of(null, Distance.from(5), Sections.from(Collections.emptyList())));
	}

	@DisplayName("거리가 없을 시 예외 발생")
	@Test
	void createSectionFailTest2() {
		assertThatIllegalArgumentException()
			.isThrownBy(() -> Path.of(
				Stations.from(
					Arrays.asList(station(Name.from("강남역")), station(Name.from("양재역")))
				),
				null,
				Sections.from(
					Collections.singletonList(section("강남역", "양재역", 10)))));
	}

}
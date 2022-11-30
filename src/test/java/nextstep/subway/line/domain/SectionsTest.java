package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.station.domain.Station;

@DisplayName("구간 컬렉션 테스트")
class SectionsTest {

	private Section 구간;
	private Station 강남역;
	private Station 역삼역;

	@BeforeEach
	void setup() {
		강남역 = Station.from("강남역");
		역삼역 = Station.from("역삼역");
		구간 = Section.of(null, 강남역, 역삼역, Distance.from(Integer.MAX_VALUE));
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

}
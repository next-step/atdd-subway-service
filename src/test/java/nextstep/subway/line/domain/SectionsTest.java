package nextstep.subway.line.domain;

import static nextstep.subway.line.domain.LineTest.*;
import static nextstep.subway.station.domain.StationTest.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SectionsTest {

	@DisplayName("역 사이에 상행역 기준으로 역을 추가할 수 있다.")
	@Test
	void addConnectingSectionTest1() {
		Section 강남_양재_구간 = spy(new Section(신분당선, 강남역, 양재역, 5));
		when(강남_양재_구간.getId()).thenReturn(1L);
		Section 강남_광교_구간 = spy(new Section(신분당선, 강남역, 광교역, 10));
		when(강남_광교_구간.getId()).thenReturn(2L);
		Sections 강남_광교_구간들 = Sections.of(강남_광교_구간);

		강남_광교_구간들.add(강남_양재_구간);

		assertThat(강남_광교_구간들.getStationsInOrder()).containsExactly(강남역, 양재역, 광교역);
	}

	@DisplayName("역 사이에 하행역 기준으로 역을 추가할 수 있다.")
	@Test
	void addConnectingSectionTest2() {
		Section 양재_광교_구간 = spy(new Section(신분당선, 양재역, 광교역, 5));
		when(양재_광교_구간.getId()).thenReturn(1L);
		Section 강남_광교_구간 = spy(new Section(신분당선, 강남역, 광교역, 10));
		when(강남_광교_구간.getId()).thenReturn(2L);
		Sections 강남_광교_구간들 = Sections.of(강남_광교_구간);

		강남_광교_구간들.add(양재_광교_구간);

		assertThat(강남_광교_구간들.getStationsInOrder()).containsExactly(강남역, 양재역, 광교역);
	}

	@DisplayName("상행 종점역을 추가할 수 있다.")
	@Test
	void addConnectingSectionTest3() {
		Section 양재_광교_구간 = spy(new Section(신분당선, 양재역, 광교역, 10));
		when(양재_광교_구간.getId()).thenReturn(1L);
		Section 강남_양재_구간 = spy(new Section(신분당선, 강남역, 양재역, 10));
		when(강남_양재_구간.getId()).thenReturn(2L);
		Sections 강남_광교_구간들 = Sections.of(양재_광교_구간);

		강남_광교_구간들.add(강남_양재_구간);

		assertThat(강남_광교_구간들.getStationsInOrder()).containsExactly(강남역, 양재역, 광교역);
	}

	@DisplayName("하행 종점역을 추가할 수 있다.")
	@Test
	void addConnectingSectionTest4() {
		Section 양재_광교_구간 = spy(new Section(신분당선, 양재역, 광교역, 10));
		when(양재_광교_구간.getId()).thenReturn(1L);
		Section 강남_양재_구간 = spy(new Section(신분당선, 강남역, 양재역, 10));
		when(강남_양재_구간.getId()).thenReturn(2L);
		Sections 강남_광교_구간들 = Sections.of(강남_양재_구간);

		강남_광교_구간들.add(양재_광교_구간);

		assertThat(강남_광교_구간들.getStationsInOrder()).containsExactly(강남역, 양재역, 광교역);
	}
}
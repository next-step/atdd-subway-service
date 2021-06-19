package nextstep.subway.line.domain;

import static nextstep.subway.line.domain.LineTest.*;
import static nextstep.subway.station.domain.StationTest.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SectionTest {

	static Section 강남_양재_구간 = spy(new Section(신분당선, 강남역, 양재역, 10));
	static Section 양재_양재시민의숲_구간 = spy(new Section(신분당선, 양재역, 양재시민의숲역, 10));
	static Section 양재시민의숲_광교_구간 = spy(new Section(신분당선, 양재시민의숲역, 광교역, 10));
	static {
		when(강남_양재_구간.getId()).thenReturn(1L);
		when(양재_양재시민의숲_구간.getId()).thenReturn(2L);
		when(양재시민의숲_광교_구간.getId()).thenReturn(3L);
	}

	@DisplayName("상행구간의 하행역은 하행구간의 상행역과 같다.")
	@Test
	void isUpwardDownwardTest() {
		assertThat(강남_양재_구간.isUpwardOf(양재_양재시민의숲_구간)).isTrue();
		assertThat(양재_양재시민의숲_구간.isUpwardOf(양재시민의숲_광교_구간)).isTrue();
		assertThat(양재_양재시민의숲_구간.isDownwardOf(강남_양재_구간)).isTrue();
		assertThat(양재시민의숲_광교_구간.isDownwardOf(양재_양재시민의숲_구간)).isTrue();
	}

	@DisplayName("구간과 구간을 상행역 기준으로 연결할 수 있다.")
	@Test
	void connectDownwardSection() {
		Section 강남_광교_구간 = spy(new Section(신분당선, 강남역, 광교역, 10));
		when(강남_광교_구간.getId()).thenReturn(1L);
		Section 강남_양재_구간 = spy(new Section(신분당선, 강남역, 양재역, 5));
		when(강남_양재_구간.getId()).thenReturn(2L);

		boolean isAdded = 강남_광교_구간.connectSection(강남_양재_구간);

		assertThat(isAdded).isTrue();
		assertThat(강남_광교_구간.getStations()).containsExactly(양재역, 광교역);
		assertThat(강남_광교_구간.getDistance()).isEqualTo(5);
	}

	@DisplayName("구간과 구간을 하행역 기준으로 연결할 수 있다.")
	@Test
	void connectUpwardSection() {
		Section 강남_광교_구간 = spy(new Section(신분당선, 강남역, 광교역, 10));
		when(강남_광교_구간.getId()).thenReturn(1L);
		Section 양재_광교_구간 = spy(new Section(신분당선, 양재역, 광교역, 5));
		when(양재_광교_구간.getId()).thenReturn(2L);

		boolean isAdded = 강남_광교_구간.connectSection(양재_광교_구간);

		assertThat(isAdded).isTrue();
		assertThat(강남_광교_구간.getStations()).containsExactly(강남역, 양재역);
		assertThat(강남_광교_구간.getDistance()).isEqualTo(5);
	}

	@DisplayName("구간과 구간의 상하행역이 겹치지 않으면 연결되지 않는다.")
	@Test
	void cannotConnectSectionTest() {
		Section 강남_광교_구간 = new Section(신분당선, 강남역, 광교역, 10);
		Section 양재_양재시민의숲_구간 = new Section(신분당선, 양재역, 양재시민의숲역, 5);

		boolean isAdded = 강남_광교_구간.connectSection(양재_양재시민의숲_구간);

		assertThat(isAdded).isFalse();
	}

	@DisplayName("같은 구간은 연결되지 않는다.")
	@Test
	void cannotConnectSameSectionTest() {
		Section 강남_광교_구간1 = new Section(신분당선, 강남역, 광교역, 10);
		Section 강남_광교_구간2 = new Section(신분당선, 강남역, 광교역, 10);

		boolean isAdded = 강남_광교_구간1.connectSection(강남_광교_구간2);

		assertThat(isAdded).isFalse();
	}
}

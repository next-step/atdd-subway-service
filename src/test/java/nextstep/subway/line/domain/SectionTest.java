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
}

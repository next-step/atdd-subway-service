package nextstep.subway.line.domain;

import static nextstep.subway.line.domain.LineTest.*;
import static nextstep.subway.line.domain.SectionTest.*;
import static nextstep.subway.station.domain.StationTest.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.station.domain.Station;

class SectionsTest {

	@Test
	@DisplayName("상행 -> 하행 순서로 정렬된 노선의 역들을 반환한다.")
	void getStationsInOrderTest() {
		Sections sections = Sections.of(양재_양재시민의숲_구간, 강남_양재_구간, 양재시민의숲_광교_구간);

		List<Station> stationsInOrder = sections.getStationsInOrder();

		assertThat(stationsInOrder).containsExactly(강남역, 양재역, 양재시민의숲역, 광교역);
	}

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

	@Test
	@DisplayName("이미 있는 구간은 추가할 수 없다.")
	void addSectionAlreadyExist() {
		Sections 강남_양재_양재시민의숲 = Sections.of(양재_양재시민의숲_구간, 강남_양재_구간);
		Section 강남_양재 = new Section(신분당선, 강남역, 양재역, 10);
		Section 강남_양재시민의숲 = new Section(신분당선, 강남역, 양재시민의숲역, 10);

		assertAll(
			() -> assertThatThrownBy(() -> 강남_양재_양재시민의숲.add(강남_양재))
				.isInstanceOf(IllegalArgumentException.class),
			() -> assertThatThrownBy(() -> 강남_양재_양재시민의숲.add(강남_양재시민의숲))
				.isInstanceOf(IllegalArgumentException.class)
		);
	}

	@Test
	@DisplayName("상행역 하행역 모두 노선에 존재하지 않는 구간은 등록할 수 없다.")
	void addSectionNotExist() {
		Sections 강남_양재_구간들 = Sections.of(강남_양재_구간);

		assertThatThrownBy(() -> 강남_양재_구간들.add(양재시민의숲_광교_구간))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("등록할 수 없는 구간 입니다.");
	}
}
package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.station.domain.Station;

@DisplayName("구간들 테스트")
public class SectionsTest {

	private Station 청량리역;
	private Station 종로3가역;
	private Station 서울역;
	private Station 신길역;
	private Line 일호선;
	private Section 청량리역_서울역;
	private Section 종로3가역_서울역;
	private Section 서울역_신길역;
	private Section 종로3가역_신길역;


	@BeforeEach
	void 초기화() {
		청량리역 = new Station("청량리역");
		종로3가역 = new Station("종로3가역");
		서울역 = new Station("서울역");
		신길역 = new Station("신길역");
		일호선 = new Line("1호선", "blue");
		청량리역_서울역 = new Section(일호선, 청량리역, 서울역, 15);
		종로3가역_서울역 = new Section(일호선, 종로3가역, 서울역, 4);
		서울역_신길역 = new Section(일호선, 서울역, 신길역, 6);
		종로3가역_신길역 = new Section(일호선, 종로3가역, 신길역, 10);
	}

	@Test
	void 구간들_생성() {
		// when
		Sections 구간들 = new Sections();

		// then
		assertThat(구간들).isNotNull();
	}

	@Test
	void 구간들_생성_구간_포함() {
		// when
		Sections 구간들 = new Sections(종로3가역_서울역);

		//then
		assertThat(구간들).isNotNull();
	}

	@Test
	void 역_목록_조회() {
		// given
		Sections 구간들 = new Sections(종로3가역_서울역);

		// when
		List<Station> 역들 = 구간들.stations();

		// then
		assertThat(역들).containsSequence(Arrays.asList(종로3가역, 서울역));
	}

	@Test
	void 구간_추가_구간들이_비어있는_경우() {
		// given
		Sections 구간들 = new Sections();

		// when
		구간들.addLineStation(종로3가역_서울역);

		// then
		assertThat(구간들.stations()).containsSequence(Arrays.asList(종로3가역, 서울역));
	}

	@Test
	void 구간_추가_기존_구간에서_하행역_연결() {
		// given
		Sections 구간들 = new Sections(종로3가역_서울역);

		// when
		구간들.addLineStation(서울역_신길역);

		// then
		assertThat(구간들.stations()).containsSequence(Arrays.asList(종로3가역, 서울역, 신길역));
	}

	@Test
	void 구간_추가_기존_구간에서_상행역_연결() {
		// given
		Sections 구간들 = new Sections(서울역_신길역);

		// when
		구간들.addLineStation(종로3가역_서울역);

		// then
		assertThat(구간들.stations()).containsSequence(Arrays.asList(종로3가역, 서울역, 신길역));
	}

	@Test
	void 내부_구간_추가_기존_구간에서_하행역_연결() {
		// given
		Sections 구간들 = new Sections(종로3가역_신길역);

		// when
		구간들.addLineStation(서울역_신길역);

		// then
		assertThat(구간들.stations()).containsSequence(Arrays.asList(종로3가역, 서울역, 신길역));
	}

	@Test
	void 내부_구간_추가_기존_구간에서_상행역_연결() {
		// given
		Sections 구간들 = new Sections(종로3가역_신길역);

		// when
		구간들.addLineStation(종로3가역_서울역);

		// then
		assertThat(구간들.stations()).containsSequence(Arrays.asList(종로3가역, 서울역, 신길역));
	}

	@Test
	void 구간_추가_기존_구간과_연관_없음_오류_발생() {
		// given
		Sections 구간들 = new Sections(종로3가역_신길역);

		// when

		// then
		assertThatThrownBy(() -> 구간들.addLineStation(청량리역_서울역)).isInstanceOf(RuntimeException.class);
	}

	@Test
	void 구간_추가_기존_구간과_동일한_역_오류_발생() {
		// given
		Sections 구간들 = new Sections(종로3가역_신길역);

		// when

		// then
		assertThatThrownBy(() -> 구간들.addLineStation(종로3가역_신길역)).isInstanceOf(RuntimeException.class);
	}

	@Test
	void 구간_삭제_내부역() {
		// given
		Sections 구간들 = new Sections(종로3가역_서울역);
		구간들.addLineStation(서울역_신길역);

		// when
		구간들.removeLineStation(일호선, 서울역);

		// then
		assertThat(구간들.stations()).containsSequence(Arrays.asList(종로3가역, 신길역));
	}

	@Test
	void 구간_삭제_상행종점역() {
		// given
		Sections 구간들 = new Sections(종로3가역_서울역);
		구간들.addLineStation(서울역_신길역);

		// when
		구간들.removeLineStation(일호선, 종로3가역);

		// then
		assertThat(구간들.stations()).containsSequence(Arrays.asList(서울역, 신길역));
	}

	@Test
	void 구간_삭제_하행종점역() {
		// given
		Sections 구간들 = new Sections(종로3가역_서울역);
		구간들.addLineStation(서울역_신길역);

		// when
		구간들.removeLineStation(일호선, 신길역);

		// then
		assertThat(구간들.stations()).containsSequence(Arrays.asList(종로3가역, 서울역));
	}

	@Test
	void 구간_삭제_존재하지_않는_역_요청시_삭제된_구간없음() {
		// given
		Sections 구간들 = new Sections(종로3가역_서울역);
		구간들.addLineStation(서울역_신길역);

		// when
		구간들.removeLineStation(일호선, 청량리역);

		// then
		assertThat(구간들.stations()).containsSequence(Arrays.asList(종로3가역, 서울역, 신길역));
	}
}

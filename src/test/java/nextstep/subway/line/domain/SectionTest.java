package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.station.domain.Station;

@DisplayName("구간 엔티티 테스트")
public class SectionTest {

	private Station 종로3가역;
	private Station 서울역;
	private Station 신길역;
	private Line 일호선;

	@BeforeEach
	void 초기화() {
		// given
		종로3가역 = new Station("종로3가역");
		서울역 = new Station("서울역");
		신길역 = new Station("신길역");
		일호선 = new Line("1호선", "blue", 종로3가역, 신길역, 10);
	}

	@Test
	void 구간_생성() {
		// when
		Section 구간 = new Section(일호선, 종로3가역, 신길역, 10);

		//then
		assertThat(구간).isNotNull();
	}

	@Test
	void 구간_상행역_수정() {
		// given
		Section 구간 = new Section(일호선, 종로3가역, 신길역, 10);

		// when
		구간.updateUpStation(서울역, 5);

		//then
		assertThat(구간.upStation()).isEqualTo(서울역);
	}

	@Test
	void 구간_하행역과_같은_역으로_상행역_수정_오류_발생() {
		// given
		Section 구간 = new Section(일호선, 종로3가역, 신길역, 10);

		// when

		//then
		assertThatThrownBy(() -> 구간.updateUpStation(신길역, 5)).isInstanceOf(RuntimeException.class);
	}

	@Test
	void 구간_하행역_수정() {
		// given
		Section 구간 = new Section(일호선, 종로3가역, 신길역, 10);

		// when
		구간.updateDownStation(서울역, 5);

		//then
		assertThat(구간.downStation()).isEqualTo(서울역);
	}

	@Test
	void 구간_상행역과_같은_역으로_하행역_수정_오류_발생() {
		// given
		Section 구간 = new Section(일호선, 종로3가역, 신길역, 10);

		// when

		//then
		assertThatThrownBy(() -> 구간.updateDownStation(종로3가역, 5)).isInstanceOf(RuntimeException.class);
	}
}

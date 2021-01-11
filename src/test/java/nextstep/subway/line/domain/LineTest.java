package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.station.domain.Station;

/**
 * @author : byungkyu
 * @date : 2021/01/11
 * @description :
 **/
@DisplayName("지하철노선 관련 기능")
class LineTest {
	Station 강남역;
	Station 광교역;
	Station 판교역;

	@BeforeEach
	void setUp() {
		// given
		강남역 = new Station("강남역");
		광교역 = new Station("광교역");
		판교역 = new Station("판교역");
	}

	@DisplayName("노선을_생성한다.")
	@Test
	void createLine(){
		// when
		Line actual = new Line("신분당선", "red");

		// then
		assertThat(actual.getName()).isEqualTo("신분당선");
		assertThat(actual.getColor()).isEqualTo("red");
	}

	@DisplayName("노선_생성시_상행역과_하행역_구간_생성")
	@Test
	void createLineWithUpStationAndDownStation(){
		// when
		Line actual = new Line("신분당선", "red", 강남역, 광교역, 8);

		// then
		assertThat(actual.getName()).isEqualTo("신분당선");
		assertThat(actual.getColor()).isEqualTo("red");
		assertThat(actual.getStations()).containsAll(Arrays.asList(강남역, 광교역));
	}

	@DisplayName("생성한_노선정보_변경")
	@Test
	void updateLineInfo(){
		// given
		Line actual = new Line("신분당선", "red", 강남역, 광교역, 8);

		// when
		actual.update(new Line("2호선", "green"));

		// then
		assertThat(actual.getName()).isEqualTo("2호선");
		assertThat(actual.getColor()).isEqualTo("green");
	}

	@DisplayName("기존_노선에_신규노선_추가(상행역 동일, 중간역 추가)")
	@Test
	void addStationMiddle(){
		// given
		Line actual = new Line("신분당선", "red", 강남역, 광교역, 8);

		// when
		actual.addSection(강남역, 판교역, 3);

		// then
		assertThat(actual.getStations()).containsExactly(강남역, 판교역, 광교역);

	}

	@DisplayName("기존_노선에_신규노선_추가(하행역 동일, 중간역 추가)")
	@Test
	void addStationMiddle2(){
		// given
		Line actual = new Line("신분당선", "red", 강남역, 광교역, 8);

		// when
		actual.addSection(판교역, 광교역, 3);

		// then
		assertThat(actual.getStations()).containsExactly(강남역, 판교역, 광교역);

	}

	@DisplayName("기존_노선에_신규노선_추가(신규상행역 추가)")
	@Test
	void addStationNewStart(){
		// given
		Station 신논현역 = new Station("신논현역");
		Line actual = new Line("신분당선", "red", 강남역, 광교역, 8);

		// when
		actual.addSection(신논현역, 강남역, 3);

		// then
		assertThat(actual.getStations()).containsExactly(신논현역, 강남역, 광교역);

	}

	@DisplayName("기존_노선에_신규노선_추가(신규하행역 추가)")
	@Test
	void addStationNewEnd(){
		// given
		Station 호매실역 = new Station("호매실역");
		Line actual = new Line("신분당선", "red", 강남역, 광교역, 8);

		// when
		actual.addSection(광교역, 호매실역, 3);

		// then
		assertThat(actual.getStations()).containsExactly(강남역, 광교역, 호매실역);

	}

	@DisplayName("기존_노선과_동일한_구간_오류")
	@Test
	void addStationDuplicateException(){
		// given
		Line actual = new Line("신분당선", "red", 강남역, 광교역, 8);

		// when
		// then
		assertThatThrownBy(() -> {
			actual.addSection(강남역, 광교역, 3);
		}).isInstanceOf(RuntimeException.class)
			.hasMessageContaining("이미 등록된 구간 입니다.");
	}

	@DisplayName("기존_노선과_일치하는_지하철역_없음")
	@Test
	void addStationMustContainExistStationException(){
		// given
		Line actual = new Line("신분당선", "red", 강남역, 광교역, 8);

		// when
		Station 서울역 = new Station("서울역");
		Station 양재역 = new Station("양재역");

		// then
		assertThatThrownBy(() -> {
			actual.addSection(서울역, 양재역, 3);
		}).isInstanceOf(RuntimeException.class)
			.hasMessageContaining("등록할 수 없는 구간 입니다.");
	}

	@DisplayName("노선내_상행_종점_삭제")
	@Test
	void remoteStationStart(){
		// given
		Line actual = new Line("신분당선", "red", 강남역, 광교역, 8);
		actual.addSection(강남역, 판교역, 3);

		// when
		actual.removeLineStation(강남역);

		// then
		assertThat(actual.getStations()).containsExactly(판교역, 광교역);

	}

	@DisplayName("노선내_하행_종점_삭제")
	@Test
	void remoteStationEnd(){
		// given
		Line actual = new Line("신분당선", "red", 강남역, 광교역, 8);
		actual.addSection(강남역, 판교역, 3);

		// when
		actual.removeLineStation(광교역);

		// then
		assertThat(actual.getStations()).containsExactly(강남역, 판교역);

	}

	@DisplayName("노선내_중간역_삭제")
	@Test
	void remoteStationMiddle(){
		// given
		Line actual = new Line("신분당선", "red", 강남역, 광교역, 8);
		actual.addSection(강남역, 판교역, 3);

		// when
		actual.removeLineStation(판교역);

		// then
		assertThat(actual.getStations()).containsExactly(강남역, 광교역);

	}
}
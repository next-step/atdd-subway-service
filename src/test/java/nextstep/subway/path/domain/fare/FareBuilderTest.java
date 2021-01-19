package nextstep.subway.path.domain.fare;

import static org.assertj.core.api.Assertions.*;

import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.PathFinder;
import nextstep.subway.station.domain.Station;

class FareBuilderTest {
	private Line line1;

	private Station 인천역;
	private Station 소요산역;

	@DisplayName("전철 요금을 구한다.(연령할인, 거리 추가요금, 라인 추가요금)")
	@ParameterizedTest
	@CsvSource(value = {"30:10:900:2150", "30:58:900:3050", "9:58:0:900", "16:58:0:1440"}, delimiter = ':')
	void selectFareTest(int age, int distance, int lineFare, int totalFare) {
		// given
		지하철_초기_데이터_생성(distance, lineFare);
		PathFinder finder = new PathFinder(Collections.singletonList(line1));
		LoginMember loginMember = new LoginMember(null, null, age);

		// when
		finder.selectShortPath(인천역, 소요산역);

		// then
		assertThat(FareBuilder.calculate(loginMember, finder)).isEqualTo(Money.of(totalFare));
	}

	private void 지하철_초기_데이터_생성(int distance, int lineFare) {
		소요산역 = 전철역_생성(1L, "소요산역");
		인천역 = 전철역_생성(2L, "인천역");

		line1 = 라인_생성("1호선", "blue", 인천역, 소요산역, distance, lineFare);
	}

	private Station 전철역_생성(Long id, String stationName) {
		return new Station(id, stationName);
	}

	private Line 라인_생성(String name, String color, Station upStation, Station downStation, int distance, int lineFare) {
		return new Line(name, color, upStation, downStation, distance, lineFare);
	}

}

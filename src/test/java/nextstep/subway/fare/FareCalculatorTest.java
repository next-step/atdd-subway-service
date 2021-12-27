package nextstep.subway.fare;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.fare.domain.Fare;
import nextstep.subway.fare.domain.FareCalculator;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.member.domain.Age;
import nextstep.subway.member.domain.Email;
import nextstep.subway.station.StationTest;
import nextstep.subway.station.domain.Stations;

@DisplayName("요금 계산기 도메인 테스트")
public class FareCalculatorTest {

	Sections sections;

	@BeforeEach
	public void setup() {
		Line 일호선 = Line.of(1L, "1호선", "red", 800);
		Section 노포역_서면역 = Section.of(1L, 일호선, StationTest.노포역, StationTest.서면역, 10);

		Line 이호선 = Line.of(2L, "2호선", "red", 800);
		Section 서면역_다대포역 = Section.of(2L, 이호선, StationTest.서면역, StationTest.다대포해수욕장역, 10);

		Line 삼호선 = Line.of(3L, "3호선", "red", 1100);
		Section 노포역_범내골역 = Section.of(3L, 삼호선, StationTest.노포역, StationTest.범내골역, 3);
		Section 범내골역_다대포역 = Section.of(4L, 삼호선, StationTest.범내골역, StationTest.다대포해수욕장역, 2);

		sections = Sections.of(Arrays.asList(노포역_서면역, 서면역_다대포역, 노포역_범내골역, 범내골역_다대포역));
	}

	@CsvSource({"11, 19, 2450", "11, 15, 1960", "11, 12, 1225", "5, 19, 2350"})
	@ParameterizedTest
	@DisplayName("요금을 계산한다")
	void calculateFareTest1(int distanceNumber, int ageNumber, int expected) {
		// given
		Distance distance = Distance.of(distanceNumber);
		Stations stations = Stations.of(Arrays.asList(StationTest.다대포해수욕장역, StationTest.범내골역, StationTest.노포역));
		LoginMember member = new LoginMember(1L, Email.of("mail@mail.com"), Age.of(ageNumber));

		// when
		Fare fare = FareCalculator.calculateFare(distance, sections, stations, member);

		// then
		assertThat(fare).isEqualTo(Fare.of(expected));
	}

}

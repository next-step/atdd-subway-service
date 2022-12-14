package nextstep.subway.path.domain;

import static nextstep.subway.generator.LineGenerator.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.common.domain.Age;
import nextstep.subway.common.domain.Email;
import nextstep.subway.common.domain.Fare;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;

@DisplayName("총 요금 계산기")
class TotalFareCalculatorTest {

	@DisplayName("총 요금 계산기 생성")
	@Test
	void createTotalFareCalculatorTest() {
		assertThatNoException()
			.isThrownBy(
				() -> new TotalFareCalculator(
					LoginMember.guest(),
					Distance.from(10),
					mock(Sections.class))
			);

	}

	@DisplayName("추가 요금 반영")
	@ParameterizedTest
	@CsvSource(value = {"0:1250", "100:1350", "1000:2250"}, delimiter = ':')
	void calculateExtraFareTest(int extraFare, int expectedFare) {
		TotalFareCalculator calculator = new TotalFareCalculator(
			사용자(20),
			Distance.from(10),
			Sections.from(신분당선(extraFare).getSections())
		);
		assertThat(calculator.fare()).isEqualTo(Fare.from(expectedFare));
	}

	@DisplayName("어린이 - 추가 요금 반영 총 요금 계산")
	@ParameterizedTest
	@CsvSource(value = {"0:450", "100:500", "1000:950"}, delimiter = ':')
	void calculateExtraFareTestForKids(int extraFare, int expectedFare) {
		TotalFareCalculator calculator = new TotalFareCalculator(
			사용자(7),
			Distance.from(10),
			Sections.from(신분당선(extraFare).getSections())
		);
		assertThat(calculator.fare()).isEqualTo(Fare.from(expectedFare));
	}

	@DisplayName("청소년 - 추가 요금 반영 총 요금 계산")
	@ParameterizedTest
	@CsvSource(value = {"0:720", "100:800", "1000:1520"}, delimiter = ':')
	void calculateExtraFareTestForTeenager(int extraFare, int expectedFare) {
		TotalFareCalculator calculator = new TotalFareCalculator(
			사용자(17),
			Distance.from(10),
			Sections.from(신분당선(extraFare).getSections())
		);
		assertThat(calculator.fare()).isEqualTo(Fare.from(expectedFare));
	}

	@DisplayName("비회원 - 추가 요금 반영 총 요금 계산")
	@ParameterizedTest
	@CsvSource(value = {"0:1250", "100:1350", "1000:2250"}, delimiter = ':')
	void calculateExtraFareTestForGuest(int extraFare, int expectedFare) {
		TotalFareCalculator calculator = new TotalFareCalculator(
			LoginMember.guest(),
			Distance.from(10),
			Sections.from(신분당선(extraFare).getSections())
		);
		assertThat(calculator.fare()).isEqualTo(Fare.from(expectedFare));
	}

	@DisplayName("유아 - 요금 면제 계산")
	@ParameterizedTest
	@ValueSource(ints = {0, 100, 1000})
	void calculateExtraFareTestForToddler(int extraFare) {
		TotalFareCalculator calculator = new TotalFareCalculator(
			사용자(5),
			Distance.from(10),
			Sections.from(신분당선(extraFare).getSections())
		);
		assertThat(calculator.fare()).isEqualTo(Fare.from(0));
	}

	@DisplayName("여러 노선 있을 시 가장 큰 추가 요금 반영 계산")
	@ParameterizedTest
	@CsvSource(value = {"100:1750", "500:1750", "1000:2250"}, delimiter = ':')
	void calculateExtraFareTestForMultipleLines(int extraFare, int expectedFare) {

		// given
		List<Section> sections = new ArrayList<>();
		List<Section> 신분당선_구간 = 신분당선(0).getSections();
		List<Section> 이호선_구간 = 이호선(500).getSections();
		List<Section> 삼호선_구간 = 삼호선(extraFare).getSections();

		sections.addAll(신분당선_구간);
		sections.addAll(이호선_구간);
		sections.addAll(삼호선_구간);

		// when
		TotalFareCalculator calculator = new TotalFareCalculator(
			사용자(20),
			Distance.from(10),
			Sections.from(sections)
		);

		// then
		assertThat(calculator.fare()).isEqualTo(Fare.from(expectedFare));
	}

	private LoginMember 사용자(int age) {
		return LoginMember.of(1L, Email.from("email@email.com"), Age.from(age));
	}

	private Line 신분당선(int extraFare) {
		return line("신분당선", "red", "강남역", "양재역", 10, extraFare);
	}

	private Line 이호선(int extraFare) {
		return line("이호선", "red", "교대역", "강남역", 10, extraFare);
	}

	private Line 삼호선(int extraFare) {
		return line("신분당선", "red", "교대역", "양재역", 10, extraFare);
	}
}

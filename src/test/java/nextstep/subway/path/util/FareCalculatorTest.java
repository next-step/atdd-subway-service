package nextstep.subway.path.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Distance;

/**
 * Fare 클래스 요금조회 테스트
 */
@DisplayName("Fare 클래스 요금조회 테스트")
class FareCalculatorTest {

    private LoginMember baby;
    private LoginMember child;
    private LoginMember teenager;
    private LoginMember adult;

    @BeforeEach
    void setUp() {
        baby = new LoginMember(1L, "h@gmail.com", 5);
        child = new LoginMember(2L, "h@gmail.com", 10);
        teenager = new LoginMember(3L, "h@gmail.com", 15);
        adult = new LoginMember(3L, "h@gmail.com", 25);
    }

    @ParameterizedTest
    @CsvSource(value = {"30:900", "8:1100", "51:0"}, delimiter = ':')
    @DisplayName("아기 요금계산")
    void babyFare_check(int distance, int surcharge) {
        // when
        FareCalculator fareCalculator = FareCalculator.of(new Distance(distance), surcharge, baby);

        // then
        assertThat(fareCalculator.getFare()).isZero();
    }

    @ParameterizedTest
    @CsvSource(value = {"30:900:1100", "8:1100:1000", "51:0:850"}, delimiter = ':')
    @DisplayName("어린이 요금계산")
    void childFare_check(int distance, int surcharge, int resultFare) {
        // when
        FareCalculator fareCalculator = FareCalculator.of(new Distance(distance), surcharge, child);

        // then
        assertThat(fareCalculator.getFare()).isEqualTo(resultFare);
    }

    @ParameterizedTest
    @CsvSource(value = {"30:900:1760", "8:1100:1600", "51:0:1360"}, delimiter = ':')
    @DisplayName("청소년 요금계산")
    void teenagerFare_check(int distance, int surcharge, int resultFare) {
        // when
        FareCalculator fareCalculator = FareCalculator.of(new Distance(distance), surcharge, teenager);

        // then
        assertThat(fareCalculator.getFare()).isEqualTo(resultFare);
    }

    @ParameterizedTest
    @CsvSource(value = {"30:900:2550", "8:1100:2350", "51:0:2050"}, delimiter = ':')
    @DisplayName("성인 또는 비로그인 사용자 요금계산")
    void adultFare_check(int distance, int surcharge, int resultFare) {
        // when
        FareCalculator fareCalculator = FareCalculator.of(new Distance(distance), surcharge, adult);

        // then
        assertThat(fareCalculator.getFare()).isEqualTo(resultFare);
    }
}

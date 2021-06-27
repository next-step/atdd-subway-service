package nextstep.subway.path.domain;

import static nextstep.subway.path.domain.FarePolicy.ADULT;
import static nextstep.subway.path.domain.FarePolicy.BABY;
import static nextstep.subway.path.domain.FarePolicy.CHILD;
import static nextstep.subway.path.domain.FarePolicy.TEENAGER;
import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.auth.domain.LoginMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("운임 계산기 테스트")
public class FareCalculatorTest {

    private LoginMember 성인 = new LoginMember(ADULT.getMaxAge());
    private LoginMember 청소년 = new LoginMember(TEENAGER.getMaxAge());
    private LoginMember 어린이 = new LoginMember(CHILD.getMaxAge());
    private LoginMember 유아 = new LoginMember(BABY.getMaxAge());

    @DisplayName("거리별 추가운임")
    @ParameterizedTest
    @CsvSource(delimiter = ':',
        value = {
            "1:1250", "10:1250",
            "11:1350", "15:1350",
            "16:1450", "20:1450",
            "46:2050", "50:2050",
            "51:2150", "58:2150",
            "59:2250", "66:2250",
            "171:3650", "178:3650"
        })
    void calculateByDistance(int distance, int amount) {
        assertThat(new FareCalculator(성인).calculate(distance, 0)).isEqualTo(new Fare(amount));
    }

    @DisplayName("거리별 추가운임 + 노선추가요금")
    @ParameterizedTest
    @CsvSource(delimiter = ':',
        value = {
            "1  :1350", "10 :1350",
            "11 :1450", "15 :1450",
            "16 :1550", "20 :1550",
            "46 :2150", "50 :2150",
            "51 :2250", "58 :2250",
            "59 :2350", "66 :2350",
            "171:3750", "178:3750"
        })
    void calculateByDistanceAndSurcharge(int distance, int amount) {
        assertThat(new FareCalculator(성인).calculate(distance, 100)).isEqualTo(new Fare(amount));
    }

    @DisplayName("성인일때 요금 계산")
    @Test
    void calculateAsAdult() {
        assertThat(new FareCalculator(성인).calculate(59, 0)).isEqualTo(new Fare(2_250));
    }

    @DisplayName("청소년일때 요금 계산")
    @Test
    void calculateAsTeenager() {
        assertThat(new FareCalculator(청소년).calculate(59, 0)).isEqualTo(new Fare(1_520));
    }

    @DisplayName("어린이일때 요금 계산")
    @Test
    void calculateAsChild() {
        assertThat(new FareCalculator(어린이).calculate(59, 0)).isEqualTo(new Fare(950));
    }

    @DisplayName("유아일때 요금 계산")
    @Test
    void calculateAsBady() {
        assertThat(new FareCalculator(유아).calculate(59, 0)).isEqualTo(new Fare(0));
    }

}

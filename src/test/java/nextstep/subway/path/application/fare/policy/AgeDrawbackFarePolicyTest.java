package nextstep.subway.path.application.fare.policy;

import nextstep.subway.path.application.fare.policy.AgeDrawbackFarePolicy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class AgeDrawbackFarePolicyTest {

    @DisplayName("연령별 요금 할인 적용 - 어린이")
    @ParameterizedTest
    @CsvSource({"1250,450", "1350,500", "2050,850", "2650,1150"})
    void calculateDrawbackFare1(int fare, int expectedDrawbackFare) {
        //given
        //when
        int drawbackFare = AgeDrawbackFarePolicy.valueOf(12).calculateFare(fare);
        //then
        assertThat(drawbackFare).isEqualTo(expectedDrawbackFare);
    }

    @DisplayName("연령별 요금 할인 적용 - 청소년")
    @ParameterizedTest
    @CsvSource({"1250,720", "1350,800", "2050,1360", "2650,1840"})
    void calculateDrawbackFare2(int fare, int expectedDrawbackFare) {
        //given
        //when
        int drawbackFare = AgeDrawbackFarePolicy.valueOf(13).calculateFare(fare);
        //then
        assertThat(drawbackFare).isEqualTo(expectedDrawbackFare);
    }

    @DisplayName("연령별 요금 할인 적용 - 성인")
    @ParameterizedTest
    @CsvSource({"1250,1250", "1350,1350", "2050,2050", "2650,2650"})
    void calculateDrawbackFare3(int fare, int expectedDrawbackFare) {
        //given
        //when
        int drawbackFare = AgeDrawbackFarePolicy.valueOf(19).calculateFare(fare);
        //then
        assertThat(drawbackFare).isEqualTo(expectedDrawbackFare);
    }
}
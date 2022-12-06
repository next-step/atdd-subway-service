package nextstep.subway.fare;

import nextstep.subway.fare.domain.AgePolicy;
import nextstep.subway.fare.exception.AgeFareException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AgePolicyTest {

    @DisplayName("청소년인경우 운임에서 350원을 공제한 금액의 20%g할인된금액을 반환")
    @ParameterizedTest
    @CsvSource(value = {"1350:13", "2350:14", "3350:15", "4350:16", "5350:17"}, delimiter = ':')
    void findFareTeen(int fare, int age) {
        assertThat(AgePolicy.valueOfAge(age).getFare(fare)).isEqualTo((int) ((fare - 350) * 0.8));
    }

    @DisplayName("어린이인경우 운임에서 350원을 공제한 금액의 50%g할인된금액을 반환")
    @ParameterizedTest
    @CsvSource(value = {"1350:6", "2350:7", "3350:8", "4350:9", "5350:12"}, delimiter = ':')
    void findFareChid(int fare, int age) {
        assertThat(AgePolicy.valueOfAge(age).getFare(fare)).isEqualTo((int) ((fare - 350) * 0.5));

    }

    @DisplayName("성인경우 원래금액 반환")
    @ParameterizedTest
    @CsvSource(value = {"1350:55", "2350:23", "3350:88", "4350:97", "5350:44"}, delimiter = ':')
    void findFareAdult(int fare, int age) {
        assertThat(AgePolicy.valueOfAge(age).getFare(fare)).isEqualTo(fare);
    }

    @DisplayName("성인,어린이,청소년에 해당하지 않는 나이대는 예외발생")
    @ParameterizedTest
    @CsvSource(value = {"1350:1", "2350:2", "3350:3", "4350:4", "5350:5"}, delimiter = ':')
    void findFareOther(int fare, int age) {
        assertThatThrownBy(() -> AgePolicy.valueOfAge(age).getFare(fare))
                .isInstanceOf(AgeFareException.class)
                .hasMessage("해당하는 나이가 없습니다");
    }
}

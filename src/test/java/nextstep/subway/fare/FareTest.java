package nextstep.subway.fare;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.fare.domain.Fare;
import nextstep.subway.line.domain.Distance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FareTest {
    @ParameterizedTest(name = "나이와 거리에 따른 요금을 반환한다.")
    @CsvSource({"20, 110, 2850", "19, 110, 2850", "18, 110, 2280", "13, 110, 2280", "12, 110, 1425", "6, 110, 1425", "5, 110, 0"})
    void getFare(int age, int distance, int fare) {
        LoginMember adult = mock(LoginMember.class);
        when(adult.getAge()).thenReturn(age);
        assertThat(Fare.getFare(new Distance(distance), adult)).isEqualTo(fare);
    }
}

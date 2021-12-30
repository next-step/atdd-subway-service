package nextstep.subway.fare;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.fare.domain.Fare;
import nextstep.subway.fare.domain.FareByDistance;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.path.domain.Path;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FareByDistanceTest {
    @ParameterizedTest(name = "거리가 50보다 큰 경우 OTHERS 요금 정책을 반환한다.")
    @CsvSource({"20, 51", "17, 51", "13, 100"})
    void getFareOfOthers(int age, int distance) {
        LoginMember loginMember = mock(LoginMember.class);
        when(loginMember.getAge()).thenReturn(age);
        Path path = mock(Path.class);
        when(path.getDistance()).thenReturn(new Distance(distance));

        assertThat(Fare.create(path, loginMember).getDistanceFare()).isEqualTo(FareByDistance.OTHERS);
    }

    @ParameterizedTest(name = "거리가 10보다 크고 50이하인 경우 MEDIUM 요금 정책을 반환한다.")
    @CsvSource({"20, 11", "17, 50", "13, 30"})
    void getFareOfMedium(int age, int distance) {
        LoginMember loginMember = mock(LoginMember.class);
        when(loginMember.getAge()).thenReturn(age);
        Path path = mock(Path.class);
        when(path.getDistance()).thenReturn(new Distance(distance));

        assertThat(Fare.create(path, loginMember).getDistanceFare()).isEqualTo(FareByDistance.MEDIUM);
    }

    @ParameterizedTest(name = "거리가 10이하인 경우 SHORT 요금 정책을 반환한다.")
    @CsvSource({"20, 1", "17, 5", "13, 10"})
    void getFareOfShort(int age, int distance) {
        LoginMember loginMember = mock(LoginMember.class);
        when(loginMember.getAge()).thenReturn(age);
        Path path = mock(Path.class);
        when(path.getDistance()).thenReturn(new Distance(distance));

        assertThat(Fare.create(path, loginMember).getDistanceFare()).isEqualTo(FareByDistance.SHORT);
    }
}

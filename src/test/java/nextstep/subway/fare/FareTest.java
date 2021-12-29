package nextstep.subway.fare;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.fare.domain.Fare;
import nextstep.subway.fare.domain.SubwayFare;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.path.domain.Path;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FareTest {
    @ParameterizedTest(name = "나이와 거리에 따른 요금을 반환한다.")
    @CsvSource({"20, 110, 2850", "19, 110, 2850", "18, 110, 2000", "13, 110, 2000", "12, 110, 1250", "6, 110, 1250", "5, 110, 0"})
    void getFare(int age, int distance, int fare) {
        LoginMember loginMember = mock(LoginMember.class);
        when(loginMember.getAge()).thenReturn(age);
        Path path = mock(Path.class);
        when(path.getDistance()).thenReturn(new Distance(distance));
        when(path.getMaxOverFareOfLine()).thenReturn(new SubwayFare(0));

        assertThat(Fare.create(path, loginMember).getFare().getSubwayFare()).isEqualTo(fare);
    }

    @Test
    @DisplayName("19세 이상 어른이면 요금 할인이 없다.")
    void getFareOfAdult() {
        LoginMember loginMember = mock(LoginMember.class);
        when(loginMember.getAge()).thenReturn(20);
        Path path = mock(Path.class);
        when(path.getDistance()).thenReturn(new Distance(110));
        when(path.getMaxOverFareOfLine()).thenReturn(new SubwayFare(0));

        assertThat(Fare.create(path, loginMember).getFare().getSubwayFare()).isEqualTo(2850);
    }

    @Test
    @DisplayName("13~18세 청소년이면 운임에서 350원을 공제한 후 20%를 할인한다.")
    void getFareOfTeenager() {
        LoginMember loginMember = mock(LoginMember.class);
        when(loginMember.getAge()).thenReturn(15);
        Path path = mock(Path.class);
        when(path.getDistance()).thenReturn(new Distance(110));
        when(path.getMaxOverFareOfLine()).thenReturn(new SubwayFare(0));

        assertThat(Fare.create(path, loginMember).getFare().getSubwayFare()).isEqualTo((int) ((2850 - 350) * 0.8));
    }

    @Test
    @DisplayName("6~12세 어린이면 운임에서 350원을 공제한 후 50%를 할인한다.")
    void getFareOfKid() {
        LoginMember loginMember = mock(LoginMember.class);
        when(loginMember.getAge()).thenReturn(7);
        Path path = mock(Path.class);
        when(path.getDistance()).thenReturn(new Distance(110));
        when(path.getMaxOverFareOfLine()).thenReturn(new SubwayFare(0));

        assertThat(Fare.create(path, loginMember).getFare().getSubwayFare()).isEqualTo((int) ((2850 - 350) * 0.5));
    }

    @Test
    @DisplayName("5세 이하 아기는 무료 요금을 적용한다.")
    void getFareOfBaby() {
        LoginMember loginMember = mock(LoginMember.class);
        when(loginMember.getAge()).thenReturn(4);
        Path path = mock(Path.class);
        when(path.getDistance()).thenReturn(new Distance(110));
        when(path.getMaxOverFareOfLine()).thenReturn(new SubwayFare(0));

        assertThat(Fare.create(path, loginMember).getFare().getSubwayFare()).isEqualTo(0);
    }

    @Test
    @DisplayName("비회원은 어른 요금을 적용한다.")
    void getFareOfAnonymous() {
        LoginMember loginMember = mock(LoginMember.class);
        when(loginMember.getAge()).thenReturn(null);
        Path path = mock(Path.class);
        when(path.getDistance()).thenReturn(new Distance(110));
        when(path.getMaxOverFareOfLine()).thenReturn(new SubwayFare(0));

        assertThat(Fare.create(path, loginMember).getFare().getSubwayFare()).isEqualTo(2850);
    }

}

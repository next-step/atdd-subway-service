package nextstep.subway.fare;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.fare.domain.DiscountPolicyByAge;
import nextstep.subway.fare.domain.Fare;
import nextstep.subway.fare.domain.SubwayFare;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.path.domain.Path;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DiscountPolicyByAgeTest {

    @Test
    @DisplayName("로그인 사용자 나이가 5이하인 경우 무료 요금 정책을 반환한다.")
    void getFareOfBaby() {
        LoginMember loginMember = mock(LoginMember.class);
        when(loginMember.getAge()).thenReturn(5);
        Path path = mock(Path.class);
        when(path.getDistance()).thenReturn(new Distance(100));
        when(path.getMaxOverFareOfLine()).thenReturn(new SubwayFare(0));

        assertThat(Fare.create(path, loginMember).getDiscountPolicy()).isEqualTo(DiscountPolicyByAge.BABY);
    }

    @ParameterizedTest(name = "로그인 사용자 나이가 6~12인 경우 어린이 요금 정책을 반환한다.")
    @ValueSource(ints = {12, 6})
    void getFareOfKid(int age) {
        LoginMember loginMember = mock(LoginMember.class);
        when(loginMember.getAge()).thenReturn(age);
        Path path = mock(Path.class);
        when(path.getDistance()).thenReturn(new Distance(100));
        when(path.getMaxOverFareOfLine()).thenReturn(new SubwayFare(0));

        assertThat(Fare.create(path, loginMember).getDiscountPolicy()).isEqualTo(DiscountPolicyByAge.KID);
    }

    @ParameterizedTest(name = "로그인 사용자 나이가 13~18인 경우 청소년 요금 정책을 반환한다.")
    @ValueSource(ints = {13, 18})
    void getFareOfTeenager(int age) {
        LoginMember loginMember = mock(LoginMember.class);
        when(loginMember.getAge()).thenReturn(age);
        Path path = mock(Path.class);
        when(path.getDistance()).thenReturn(new Distance(100));
        when(path.getMaxOverFareOfLine()).thenReturn(new SubwayFare(0));

        assertThat(Fare.create(path, loginMember).getDiscountPolicy()).isEqualTo(DiscountPolicyByAge.TEENAGER);
    }

    @ParameterizedTest(name = "로그인 사용자 나이가 19 이상인 경우 어른 요금 정책을 반환한다.")
    @ValueSource(ints = {19, 100})
    void getFareOfAdult(int age) {
        LoginMember loginMember = mock(LoginMember.class);
        when(loginMember.getAge()).thenReturn(age);
        Path path = mock(Path.class);
        when(path.getDistance()).thenReturn(new Distance(100));
        when(path.getMaxOverFareOfLine()).thenReturn(new SubwayFare(0));

        assertThat(Fare.create(path, loginMember).getDiscountPolicy()).isEqualTo(DiscountPolicyByAge.ADULT);
    }

    @Test
    @DisplayName("비로그인 사용자는 어른 요금 정책을 반환한다.")
    void getFareOfAnonymous() {
        LoginMember loginMember = mock(LoginMember.class);
        when(loginMember.getAge()).thenReturn(null);
        Path path = mock(Path.class);
        when(path.getDistance()).thenReturn(new Distance(100));
        when(path.getMaxOverFareOfLine()).thenReturn(new SubwayFare(0));

        assertThat(Fare.create(path, loginMember).getDiscountPolicy()).isEqualTo(DiscountPolicyByAge.ADULT);
    }
}

package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Distance;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

/***
 * 기본운임(10㎞ 이내) : 기본운임 1,250원
 * 이용 거리초과 시 추가운임 부과
 * 10km초과∼50km까지(5km마다 100원)
 * 50km초과 시 (8km마다 100원)
 *
 * 청소년(13세 이상~19세 미만): 운임에서 350원을 공제한 금액의 20%할인
 * 어린이(6세 이상~13세 미만): 운임에서 350원을 공제한 금액의 50%할인
 */
class FareTest {
    private final Distance 거리_10 = new Distance(10);
    private final Distance 거리_15 = new Distance(15);
    private final Distance 거리_55 = new Distance(55);

    private final LoginMember 성인 = new LoginMember(1L, "email@email.com", 20);
    private final LoginMember 청소년 = new LoginMember(2L, "email@email.com", 16);
    private final LoginMember 어린이 = new LoginMember(3L, "email@email.com", 8);
    private final LoginMember 비회원 = null;

    private final int 추가요금_0 = 0;
    private final int 추가요금_500 = 500;

    @Test
    void 기본운임_추가요금없음() {
        assertAll(
                () -> assertThat(Fare.of(거리_10, 추가요금_0, 성인).getFare()).isEqualTo(1250),
                () -> assertThat(Fare.of(거리_10, 추가요금_0, 청소년).getFare()).isEqualTo(720),
                () -> assertThat(Fare.of(거리_10, 추가요금_0, 어린이).getFare()).isEqualTo(450),
                () -> assertThat(Fare.of(거리_10, 추가요금_0, 비회원).getFare()).isEqualTo(1250)
        );
    }

    @Test
    void 추가운임15km_추가요금없음() {
        assertAll(
                () -> assertThat(Fare.of(거리_15, 추가요금_0, 성인).getFare()).isEqualTo(1350),
                () -> assertThat(Fare.of(거리_15, 추가요금_0, 청소년).getFare()).isEqualTo(800),
                () -> assertThat(Fare.of(거리_15, 추가요금_0, 어린이).getFare()).isEqualTo(500),
                () -> assertThat(Fare.of(거리_15, 추가요금_0, 비회원).getFare()).isEqualTo(1350)
        );
    }

    @Test
    void 추가운임55km_추가요금없음() {
        assertAll(
                () -> assertThat(Fare.of(거리_55, 추가요금_0, 성인).getFare()).isEqualTo(2150),
                () -> assertThat(Fare.of(거리_55, 추가요금_0, 청소년).getFare()).isEqualTo(1440),
                () -> assertThat(Fare.of(거리_55, 추가요금_0, 어린이).getFare()).isEqualTo(900),
                () -> assertThat(Fare.of(거리_55, 추가요금_0, 비회원).getFare()).isEqualTo(2150)
        );
    }

    @Test
    void 기본운임_추가요금500() {
        assertAll(
                () -> assertThat(Fare.of(거리_10, 추가요금_500, 성인).getFare()).isEqualTo(1750),
                () -> assertThat(Fare.of(거리_10, 추가요금_500, 청소년).getFare()).isEqualTo(1120),
                () -> assertThat(Fare.of(거리_10, 추가요금_500, 어린이).getFare()).isEqualTo(700),
                () -> assertThat(Fare.of(거리_10, 추가요금_500, 비회원).getFare()).isEqualTo(1750)
        );
    }

    @Test
    void 추가운임15km_추가요금500() {
        assertAll(
                () -> assertThat(Fare.of(거리_15, 추가요금_500, 성인).getFare()).isEqualTo(1850),
                () -> assertThat(Fare.of(거리_15, 추가요금_500, 청소년).getFare()).isEqualTo(1200),
                () -> assertThat(Fare.of(거리_15, 추가요금_500, 어린이).getFare()).isEqualTo(750),
                () -> assertThat(Fare.of(거리_15, 추가요금_500, 비회원).getFare()).isEqualTo(1850)
        );
    }

    @Test
    void 추가운임55km_추가요금500() {
        assertAll(
                () -> assertThat(Fare.of(거리_55, 추가요금_500, 성인).getFare()).isEqualTo(2650),
                () -> assertThat(Fare.of(거리_55, 추가요금_500, 청소년).getFare()).isEqualTo(1840),
                () -> assertThat(Fare.of(거리_55, 추가요금_500, 어린이).getFare()).isEqualTo(1150),
                () -> assertThat(Fare.of(거리_55, 추가요금_500, 비회원).getFare()).isEqualTo(2650)
        );
    }
}

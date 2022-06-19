package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Line;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class ChargeTest {
    @DisplayName("기본요금")
    @Test
    void baseCharge() {
        Charge actual = BaseCharge.BASE_CHARGE;
        Charge expected = new Charge(1250);
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("거리별 추가요금")
    @ParameterizedTest
    @CsvSource(value = {"5,0", "25,300", "75,1100"})
    void distanceSurcharge(int distance, int surcharge) {
        Charge actual = DistanceSurcharge.from(distance);
        Charge expected = new Charge(surcharge);
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("노선별 추가요금")
    @Test
    void lineSurcharge() {
        Line line1 = new Line("신분당선", "red", 1000);
        Line line2 = new Line("분당선", "yellow", 500);
        Charge actual = LineSurcharge.from(Arrays.asList(line1, line2));
        Charge expected = new Charge(1000);
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("청소년 요금할인")
    @ParameterizedTest
    @ValueSource(ints = {13, 18})
    void discountByTeenager(int age) {
        LoginMember teenager = new LoginMember(1L, "teenager@gmail.com", age);
        Charge given = new Charge(1350);
        Charge actual = given.discountBy(teenager);
        Charge expected = new Charge(800);
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("어린이 요금할인")
    @ParameterizedTest
    @ValueSource(ints = {6, 12})
    void discountByChildren(int age) {
        LoginMember children = new LoginMember(1L, "children@gmail.com", age);
        Charge given = new Charge(1350);
        Charge actual = given.discountBy(children);
        Charge expected = new Charge(500);
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("성인 요금 미할인")
    @Test
    void discountByAdult() {
        LoginMember adult = new LoginMember(1L, "adult@gmail.com", 19);
        Charge given = new Charge(1350);
        Charge actual = given.discountBy(adult);
        Charge expected = new Charge(1350);
        assertThat(actual).isEqualTo(expected);
    }
}

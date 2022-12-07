package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class FareAgeTest {


    @DisplayName("비회원은 요금할인이 적용되지 않는다.")
    @Test
    void none() {
        FareAge fareAge = FareAge.from(null);
        //when
        AgeDiscountPolicy ageDiscountPolicy = fareAge.getAgeDiscountPolicy();
        //then
        assertThat(ageDiscountPolicy.discount(1000)).isEqualTo(1000);
    }

    @DisplayName("유아는 요금할인이 적용되지 않는다.")
    @ParameterizedTest
    @ValueSource(ints = {1,2,3,4,5})
    void baby(int age) {
        //given
        FareAge fareAge = FareAge.from(age);
        AgeDiscountPolicy ageDiscountPolicy = fareAge.getAgeDiscountPolicy();
        //when
        int fare = ageDiscountPolicy.discount(1350);
        //then
        assertThat(fare).isEqualTo(1350);
    }

    @DisplayName("어린이 요금은 350을 공제한 금액의 50퍼센트 할인이다 ")
    @ParameterizedTest
    @ValueSource(ints = {6, 10, 12})
    void child(int age) {
        //given
        FareAge fareAge = FareAge.from(age);
        AgeDiscountPolicy ageDiscountPolicy = fareAge.getAgeDiscountPolicy();
        //when
        int fare = ageDiscountPolicy.discount(1350);
        //then
        assertThat(fare).isEqualTo(500);
    }

    @DisplayName("청소년 요금을 계산할 수 있다.")
    @ParameterizedTest
    @ValueSource(ints = {13, 15, 18})
    void youth(int age) {
        //given
        FareAge fareAge = FareAge.from(age);
        AgeDiscountPolicy ageDiscountPolicy = fareAge.getAgeDiscountPolicy();
        //when
        int fare = ageDiscountPolicy.discount(1350);
        //then
        assertThat(fare).isEqualTo(700);
    }

    @DisplayName("성인 요금할인이 적욛되지 않는다.")
    @ParameterizedTest
    @ValueSource(ints = {19, 30, 50})
    void adult(int age) {
        //given
        FareAge fareAge = FareAge.from(age);
        AgeDiscountPolicy ageDiscountPolicy = fareAge.getAgeDiscountPolicy();
        //when
        int fare = ageDiscountPolicy.discount(1000);
        //then
        assertThat(fare).isEqualTo(1000);
    }
}

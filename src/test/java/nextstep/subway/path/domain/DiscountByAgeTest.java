package nextstep.subway.path.domain;

import nextstep.subway.common.domain.DiscountByAge;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class DiscountByAgeTest {



    @DisplayName("유아 요금할인을 받을 수 있다")
    @Test
    void discountByKidAgeTest() {
        //요금은 서울교통공사 기준표 참고
        //given
        BigDecimal chargedFare = BigDecimal.valueOf(1250);
        BigDecimal discountedFare = BigDecimal.ZERO;

        //when
        BigDecimal kid = DiscountByAge.discount(chargedFare, 0);

        //then
        assertThat(kid).isEqualTo(discountedFare);

        //when
        kid = DiscountByAge.discount(chargedFare, 6);

        //then
        assertThat(kid).isEqualTo(discountedFare);

        //when
        kid = DiscountByAge.discount(chargedFare, 7);

        //then
        assertThat(kid).isNotEqualTo(discountedFare);
    }

    @DisplayName("어린이 요금할인을 받을 수 있다")
    @Test
    void discountByChildAgeTest() {
        //given
        BigDecimal chargedFare = BigDecimal.valueOf(1250);
        BigDecimal discountedFare = BigDecimal.valueOf(450);

        //when
        BigDecimal child = DiscountByAge.discount(chargedFare, 7);

        //then
        assertThat(child).isEqualTo(discountedFare);

        //when
        child = DiscountByAge.discount(chargedFare, 13);

        //then
        assertThat(child).isEqualTo(discountedFare);

        //when
        child = DiscountByAge.discount(chargedFare, 14);

        //then
        assertThat(child).isNotEqualTo(discountedFare);
    }

    @DisplayName("청소년 요금할인을 받을 수 있다")
    @Test
    void discountByTeenagerAgeTest() {
        //given
        BigDecimal chargedFare = BigDecimal.valueOf(1250);
        BigDecimal discountedFare = BigDecimal.valueOf(720);

        //when
        BigDecimal child = DiscountByAge.discount(chargedFare, 14);

        //then
        assertThat(child).isEqualTo(discountedFare);

        //when
        child = DiscountByAge.discount(chargedFare, 18);

        //then
        assertThat(child).isEqualTo(discountedFare);

        //when
        child = DiscountByAge.discount(chargedFare, 19);

        //then
        assertThat(child).isNotEqualTo(discountedFare);
    }

    @DisplayName("일반 요금할인을 받을 수 있다")
    @Test
    void discountByNormalAgeTest() {
        //given
        BigDecimal chargedFare = BigDecimal.valueOf(1250);
        BigDecimal discountedFare = BigDecimal.valueOf(1250);

        //when
        BigDecimal child = DiscountByAge.discount(chargedFare, 19);

        //then
        assertThat(child).isEqualTo(discountedFare);

        //when
        child = DiscountByAge.discount(chargedFare, 65);

        //then
        assertThat(child).isEqualTo(discountedFare);

        //when
        child = DiscountByAge.discount(chargedFare, 66);

        //then
        assertThat(child).isNotEqualTo(discountedFare);
    }

    @DisplayName("나이가 없다면 일반 요금할인을 받을 수 있다")
    @Test
    void discountByNotexistAgeTest() {
        //given
        BigDecimal chargedFare = BigDecimal.valueOf(1250);
        BigDecimal discountedFare = BigDecimal.valueOf(1250);

        //when
        BigDecimal child = DiscountByAge.discount(chargedFare, null);

        //then
        assertThat(child).isEqualTo(discountedFare);
    }

    @DisplayName("노인 할인 요금을 받을 수 있다.")
    @Test
    void discountByElderAgeTest() {
        //given
        BigDecimal chargedFare = BigDecimal.valueOf(1250);
        BigDecimal discountedFare = BigDecimal.valueOf(0);

        //when
        BigDecimal child = DiscountByAge.discount(chargedFare, 66);

        //then
        assertThat(child).isEqualTo(discountedFare);
    }

}
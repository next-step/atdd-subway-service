package nextstep.subway.path.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("나이 할인 관련 도메인 테스트")
class AgeDiscountTypeTest {

    @DisplayName("어린이 요금 관련 테스트")
    @Test
    void calculateAgeDiscountTypeKid() {
        // when
        AgeDiscountType ageDiscountType = AgeDiscountType.getAgeDiscountType(6);
        int fare = AgeDiscountType.getDiscountedPrice(1350, ageDiscountType);

        // then
        assertAll(
                () -> assertThat(ageDiscountType).isEqualTo(AgeDiscountType.KID),
                () -> assertThat(fare).isEqualTo(500)
        );
    }

    @DisplayName("청소년 요금 관련 테스트")
    @Test
    void calculateAgeDiscountTypeAdolescent() {
        // when
        AgeDiscountType ageDiscountType = AgeDiscountType.getAgeDiscountType(15);
        int fare = AgeDiscountType.getDiscountedPrice(1350, ageDiscountType);

        // then
        assertAll(
                () -> assertThat(ageDiscountType).isEqualTo(AgeDiscountType.ADOLESCENT),
                () -> assertThat(fare).isEqualTo(800)
        );
    }

    @DisplayName("아기 요금 관련 테스트")
    @Test
    void calculateAgeDiscountTypeBaby() {
        // when
        AgeDiscountType ageDiscountType = AgeDiscountType.getAgeDiscountType(4);
        int fare = AgeDiscountType.getDiscountedPrice(1350, ageDiscountType);

        // then
        assertAll(
                () -> assertThat(ageDiscountType).isEqualTo(AgeDiscountType.NONE),
                () -> assertThat(fare).isEqualTo(1350)
        );
    }

    @DisplayName("어른 요금 관련 테스트")
    @Test
    void calculateAgeDiscountTypeAdult() {
        // when
        AgeDiscountType ageDiscountType = AgeDiscountType.getAgeDiscountType(44);
        int fare = AgeDiscountType.getDiscountedPrice(1350, ageDiscountType);

        // then
        assertAll(
                () -> assertThat(ageDiscountType).isEqualTo(AgeDiscountType.NONE),
                () -> assertThat(fare).isEqualTo(1350)
        );
    }
}
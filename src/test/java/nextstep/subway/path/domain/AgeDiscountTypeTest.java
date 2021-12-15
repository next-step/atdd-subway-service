package nextstep.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("나이 할인 관련 도메인 테스트")
class AgeDiscountTypeTest {

    @DisplayName("거리 요금 관련 테스트")
    @Test
    void calculateDistanceOverFare() {
        // when
        AgeDiscountType ageDiscountType = AgeDiscountType.getAgeDiscountType(6);
        int fare = AgeDiscountType.getDiscountedPrice(1350, ageDiscountType);

        // then
        assertAll(
                () -> assertThat(ageDiscountType).isEqualTo(AgeDiscountType.KID),
                () -> assertThat(fare).isEqualTo(500)
        );
    }
}
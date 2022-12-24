package nextstep.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 1. 기본 생성(기본 운임)은 1,250원
 */
class FareTest {

    @DisplayName("요금 객체의 Default_운임(기본요금)은 1250원이다.")
    @Test
    void createFare() {
        Fare fare = Fare.from();
        assertThat(fare.currentFare()).isEqualTo(1250);
    }
}

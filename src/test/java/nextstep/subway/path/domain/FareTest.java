package nextstep.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;

public class FareTest {

    @DisplayName("기본거리 요금 생성")
    @Test
    void create() {
        Fare fare = Fare.from(10L);

        assertThat(fare).isNotNull();
        assertThat(fare.getFare()).isEqualTo(new BigInteger("1250"));
    }

    @DisplayName("거리별 요금 정책 검증")
    @Test
    void faresByDistance() {
        Fare fare = Fare.from(11L);

        assertThat(fare.getFare()).isEqualTo(new BigInteger("1550"));

        fare = Fare.from(23L);

        assertThat(fare.getFare()).isEqualTo(new BigInteger("1750"));

        fare = Fare.from(51L);

        assertThat(fare.getFare()).isEqualTo(new BigInteger("1950"));

    }
}

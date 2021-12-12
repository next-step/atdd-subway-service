package nextstep.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FareTest {

    @DisplayName("기본거리 요금 생성")
    @Test
    void create() {
        Fare fare = Fare.from(8L);

        assertThat(fare).isNotNull();
    }


}

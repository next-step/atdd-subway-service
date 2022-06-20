package nextstep.subway.line.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AdditionalFareTest {
    @Test
    @DisplayName("AdditionalFare 생성")
    void AdditionalFare_생성(){
        AdditionalFare fare = AdditionalFare.from(0);
        assertThat(fare.getValue()).isEqualTo(0);
    }

    @Test
    @DisplayName("AdditionalFare 생성 실패")
    void AdditionalFare_생성_실패(){
        assertThrows(IllegalArgumentException.class, () -> AdditionalFare.from(-1));
    }
}
package nextstep.subway.path.domain;

import nextstep.subway.path.enums.DistanceFare;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("거리에 따른 추가 요금")
class DistanceFareTest {


    @DisplayName("거리에 따른 추가 요금을 확인할 수 있다.")
    @ParameterizedTest(name = "#{index} - {0}km 이동하면 요금은 {1}원 이다.")
    @CsvSource(value = {"10=1250", "11=1350", "50=2050", "60=2250"}, delimiter = '=')
    void distance_extra_fare(int distance, int fare) {
        assertThat(DistanceFare.calculateDistanceFare(distance)).isEqualTo(fare);
    }

}

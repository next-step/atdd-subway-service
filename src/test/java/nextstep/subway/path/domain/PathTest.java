package nextstep.subway.path.domain;

import static nextstep.subway.station.domain.StationTestFixture.createStation;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import nextstep.subway.common.constant.ErrorCode;
import nextstep.subway.line.domain.AgeFarePolicy;
import nextstep.subway.line.domain.Fare;
import nextstep.subway.line.domain.Distance;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

public class PathTest {

    @DisplayName("경로 생성 시, 지하철역들이 비어있으면 생성되지 않는다.")
    @Test
    void createPathThrowErrorWhenStationsIsNull() {
        // when & then
        assertThatThrownBy(() -> Path.of(Collections.emptyList(), Distance.from(4), Fare.from(1250)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.경로는_비어있을_수_없음.getErrorMessage());
    }

    @DisplayName("경로 생성 시, 거리가 음수이면 생성되지 않는다.")
    @ParameterizedTest
    @ValueSource(ints = {-1,-2,-3})
    void createPathThrowErrorWhenDistanceSmallerThanZero(int distance) {
        // when & then
        assertThatThrownBy(() -> Path.of(Arrays.asList(createStation("강남역"), createStation("역삼역")), Distance.from(distance), Fare.from(0)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.거리는_0보다_작을_수_없음.getErrorMessage());

    }

    @DisplayName("경로 생성 시, 거리가 0이면 생성되지 않는다.")
    @Test
    void createPathThrowErrorWhenDistanceIsZero() {
        // when & then
        assertThatThrownBy(() -> Path.of(Arrays.asList(createStation("강남역"), createStation("역삼역")), Distance.from(0), Fare.from(0)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.노선거리는_0보다_작거나_같을_수_없음.getErrorMessage());
    }

    @DisplayName("어린이일 경우, 성인 경로 요금에서 350원을 제한 후 50% 할인 해준다.")
    @ParameterizedTest(name = "어른 요금: {0}원이면 어린이 요금: {1}")
    @CsvSource(value = {"1450:550", "1850:750", "2650:1150"}, delimiter = ':')
    void createChildFareInPath(BigDecimal adultFare, BigDecimal childFare) {
        // given
        Path path = Path.of(Arrays.asList(createStation("강남역"), createStation("역삼역")), Distance.from(10), Fare.from(adultFare));

        // when
        path.convertFareByAgeFarePolicy(AgeFarePolicy.CHILD);

        // then
        assertThat(path.getFare()).isEqualTo(Fare.from(childFare));
    }

    @DisplayName("청소년일 경우, 성인 경로 요금에서 350원을 제한 후 20% 할인 해준다.")
    @ParameterizedTest(name = "어른 요금: {0}원이면 청소년 요금: {1}")
    @CsvSource(value = {"1450:880", "1850:1200", "2650:1840"}, delimiter = ':')
    void createTeenagerFareInPath(BigDecimal adultFare, BigDecimal teenagerFare) {
        // given
        Path path = Path.of(Arrays.asList(createStation("강남역"), createStation("역삼역")), Distance.from(10), Fare.from(adultFare));

        // when
        path.convertFareByAgeFarePolicy(AgeFarePolicy.TEENAGER);

        // then
        assertThat(path.getFare()).isEqualTo(Fare.from(teenagerFare));
    }
}

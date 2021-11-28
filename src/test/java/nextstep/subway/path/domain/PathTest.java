package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Stream;
import nextstep.subway.common.domain.Name;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.Stations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("경로")
class PathTest {

    private Station 강남역;
    private Station 양재역;

    @BeforeEach
    void setUp() {
        강남역 = Station.from(Name.from("강남역"));
        양재역 = Station.from(Name.from("양재역"));
    }

    @Test
    @DisplayName("객체화")
    void instance() {
        assertThatNoException()
            .isThrownBy(() -> Path.of(Stations.from(Arrays.asList(강남역, 양재역)), Distance.from(5)));
    }

    @ParameterizedTest(name = "[{index}] {argumentsWithNames} 값 으로 객체화 할 수 없다.")
    @MethodSource
    @DisplayName("지하철 역들과 거리는 필수")
    void instance_nullArgument_thrownIllegalArgumentException(
        Stations stations, Distance distance) {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> Path.of(stations, distance))
            .withMessageEndingWith("필수입니다.");
    }

    @Test
    @DisplayName("경로를 구하기 위한 2개 이상의 지하철 역은 필수")
    void instance_lessThanTwo_thrownIllegalArgumentException() {
        assertThatIllegalArgumentException()
            .isThrownBy(
                () -> Path.of(Stations.from(Collections.singletonList(강남역)), Distance.from(1)))
            .withMessageStartingWith("경로의 지하철 역들은 적어도 ");
    }

    private static Stream<Arguments> instance_nullArgument_thrownIllegalArgumentException() {
        return Stream.of(
            Arguments.of(Stations.from(
                Arrays.asList(Station.from(Name.from("강남역")), Station.from(Name.from("양재역")))),
                null
            ),
            Arguments.of(null, Distance.from(1))
        );
    }
}

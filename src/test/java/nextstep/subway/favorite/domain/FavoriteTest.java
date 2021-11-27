package nextstep.subway.favorite.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;

import java.util.stream.Stream;
import nextstep.subway.common.domain.Name;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("즐겨찾기")
class FavoriteTest {

    @Test
    @DisplayName("객체화")
    void instance() {
        assertThatNoException()
            .isThrownBy(() -> Favorite.from(station("강남역"), station("광교역"), 1L));
    }

    @ParameterizedTest(name = "[{index}] {argumentsWithNames} 으로 객체화 불가능")
    @MethodSource
    @DisplayName("출발역, 도착역, 사용자 id는 필수")
    void instance_nullValue_thrownIllegalArgumentException(Station source, Station target) {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> Favorite.from(source, target, 1L))
            .withMessageEndingWith("필수입니다.");
    }

    @Test
    @DisplayName("출발역과 도착역은 달라야 함")
    void instance_sameSourceTarget_thrownIllegalArgumentException() {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> Favorite.from(station("강남역"), station("강남역"), 1L))
            .withMessageEndingWith("달라야 합니다.");
    }

    private static Stream<Arguments> instance_nullValue_thrownIllegalArgumentException() {
        return Stream.of(
            Arguments.of(station("강남역"), null),
            Arguments.of(null, station("광교역"))
        );
    }

    private static Station station(String name) {
        return Station.from(Name.from(name));
    }
}

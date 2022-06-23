package nextstep.subway.line.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class OverFareByDistanceTest {

    @ParameterizedTest
    @ValueSource(ints = {1, 10})
    void 구간별_추가금액이_없는건_10km_이하이다(int distance) {
        assertThat(OverFareByDistance.of(distance)).isEmpty();
    }

    @ParameterizedTest
    @ValueSource(ints = {11, 50})
    void 구간별_추가금액_첫번째는_10km_초과부터_이다(int distance) {
        assertThat(OverFareByDistance.of(distance)).isEqualTo(Optional.of(OverFareByDistance.FIRST));
    }

    @ParameterizedTest
    @ValueSource(ints = {51, 100})
    void 구간별_추가금액_두번째는_50km_초과부터_이다(int distance) {
        assertThat(OverFareByDistance.of(distance)).isEqualTo(Optional.of(OverFareByDistance.SECOND));
    }

}
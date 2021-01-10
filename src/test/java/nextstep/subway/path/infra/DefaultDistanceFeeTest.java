package nextstep.subway.path.infra;

import nextstep.subway.common.Money;
import nextstep.subway.path.domain.DefaultDistanceFee;
import nextstep.subway.path.domain.Distance;
import nextstep.subway.path.domain.DistanceFee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static nextstep.subway.path.domain.DefaultDistanceFee.ADD_FEE;
import static nextstep.subway.path.domain.DefaultDistanceFee.BASIC_FEE;
import static org.assertj.core.api.Assertions.assertThat;

class DefaultDistanceFeeTest {

    private DistanceFee distanceFee;

    @BeforeEach
    void setUp() {
        distanceFee = new DefaultDistanceFee();
    }

    @DisplayName("10km 이내의 기본운임 비용은 1,250원이다.")
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10})
    void basic(int value) {
        // given
        Distance distance = Distance.valueOf(value);

        // when
        Money basic = distanceFee.settle(distance);

        // then
        assertThat(basic).isEqualTo(Money.valueOf(BASIC_FEE));
    }

    @DisplayName("5km 마다 100원의 추가운임이 부과된다. (10km 초과 ∼ 50km 이하)")
    @ParameterizedTest
    @CsvSource(value = {"11,1", "50,40"})
    void add1(int total, int over) {
        // given
        Distance distance = Distance.valueOf(total);

        // when
        Money actual = distanceFee.settle(distance);

        // then
        Money basic = Money.valueOf(BASIC_FEE);
        Money add = Money.valueOf(ADD_FEE * (over / 5));
        assertThat(actual).isEqualTo(basic.add(add));
    }

    @DisplayName("8km 마다 100원의 추가운임이 부과된다. (50km 초과 시)")
    @ParameterizedTest
    @CsvSource(value = {"51,41", "70,60"})
    void add2(int total, int over) {
        // given
        Distance distance = Distance.valueOf(total);

        // when
        Money actual = distanceFee.settle(distance);

        // then
        Money basic = Money.valueOf(BASIC_FEE);
        Money add = Money.valueOf(ADD_FEE * (over / 8));
        assertThat(actual).isEqualTo(basic.add(add));
    }
}

package nextstep.subway.policy.price;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.policy.domain.Price;

@DisplayName("거리에따른 요금 정책관련")
public class DistancePricePolicyTest {
    Price 기본운임;

    @BeforeEach
    public void setUp() {
        // given
        기본운임 = Price.of(1250);
    }

    @DisplayName("이동 거리가 10Km 이내일시 기본운임이 적용된다.")
    @ValueSource(ints = {1, 5, 9, 10})
    @ParameterizedTest(name = "[{index}] {0} 거리만큼 이동")
    void apply_distanceUnder10Km(int distance) {
        // given
        PricePolicy pricePolicy = new DistancePricePolicy(Distance.of(distance));

        // when
        Price price = pricePolicy.apply();

        // then
        Assertions.assertThat(price).isEqualTo(기본운임);
    }


    @DisplayName("이동 거리가 10~50Km 일 시 기본운임 + 5Km마다 100원씩 적용된다.")
    @CsvSource({"10,0", "14,0", "15,100", "16,100", "50,800"})
    @ParameterizedTest(name = "[{index}] {0} 거리만큼 이동하면 추가 요금은 {1}")
    void apply_distanceBetween10KmAnd50Km(int distance, int extraFare) {
        // given
        PricePolicy pricePolicy = new DistancePricePolicy(Distance.of(distance));

        // when
        Price price = pricePolicy.apply();

        // then
        Assertions.assertThat(price).isEqualTo(기본운임.plus(Price.of(extraFare)));
    }

    @DisplayName("이동 거리가 50Km 초과 일 시 기본운임 + 800 + 8Km마다 100원씩 적용된다.")
    @CsvSource({"50,0", "54,0", "55,0", "57,0", "58,100", "59,100", "66,200"})
    @ParameterizedTest(name = "[{index}] {0} 거리만큼 이동하면 추가 요금은 {1}")
    void apply_distanceOver50Km(int distance, int extraFare) {
        // given
        PricePolicy pricePolicy = new DistancePricePolicy(Distance.of(distance));

        // when
        Price price = pricePolicy.apply();

        // then
        Assertions.assertThat(price).isEqualTo(기본운임.plus(Price.of(800)).plus(Price.of(extraFare)));
    }
}

package nextstep.subway.policy.price;

import java.util.List;
import java.util.NoSuchElementException;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.policy.domain.Price;

@DisplayName("요금 정책 관련 기능")
public class PricePolicyTest {
    Line 이호선;
    Line 삼호선;

    @BeforeEach
    public void setUp() {
        // given
        이호선 = new Line("이호선", "bg-green-600", Price.of(100));
        삼호선 = new Line("삼호선", "bg-orange-600", Price.of(200));
    }

    @DisplayName("최단 경로에 역이 1개가 존재하고 구간의 길이의 길이별 운임이 계산된다.")
    @CsvSource({"10,1350", "16,1450", "58,2250"})
    @ParameterizedTest(name = "[{index}] {0} 거리만큼 이동하면 요금은 {1}")
    void calculate_oneLine_mulipleDistance(int distance, int expectedFare) {
        // given
        PricePolicy linePricePolicy = new LinePricePolicy(List.of(이호선));
        PricePolicy distancePricePolicy = new DistancePricePolicy(Distance.of(distance));

        List<PricePolicy> pricePolicys = List.of(linePricePolicy, distancePricePolicy);

        // when
        Price totalFare = pricePolicys.stream()
                                        .map(PricePolicy::apply)
                                        .reduce((seed, result) -> seed.plus(result))
                                        .orElseThrow(() -> new NoSuchElementException("계산되는 운임이 없습니다."));

        // then
        Assertions.assertThat(totalFare).isEqualTo(Price.of(expectedFare));
    }

    @DisplayName("최단 경로에 역이 2개가 존재하고 구간의 길이별 운임이 계산된다.")
    @CsvSource({"10,1450", "16,1550", "58,2350"})
    @ParameterizedTest(name = "[{index}] {0} 거리만큼 이동하면 요금은 {1}")
    void calculate_twoLine_mulipleDistance(int distance, int expectedFare) {
        // given
        PricePolicy linePricePolicy = new LinePricePolicy(List.of(이호선, 삼호선));
        PricePolicy distancePricePolicy = new DistancePricePolicy(Distance.of(distance));

        List<PricePolicy> pricePolicys = List.of(linePricePolicy, distancePricePolicy);

        // when
        Price totalFare = pricePolicys.stream()
                                        .map(PricePolicy::apply)
                                        .reduce((seed, result) -> seed.plus(result))
                                        .orElseThrow(() -> new NoSuchElementException("계산되는 운임이 없습니다."));

        // then
        Assertions.assertThat(totalFare).isEqualTo(Price.of(expectedFare));
    }
}

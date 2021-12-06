package nextstep.subway.policy.price;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.line.domain.Line;
import nextstep.subway.policy.domain.Price;

@DisplayName("라인에따른 요금 정책관련")
public class LinePricePolicyTest {
    Line 이호선;
    Line 삼호선;
    Line 신분당선;

    @BeforeEach
    public void setUp() {
        // given
        이호선 = new Line("이호선", "bg-green-600", Price.of(100));
        삼호선 = new Line("삼호선", "bg-orange-600", Price.of(200));
        신분당선 = new Line("신분당선", "bg-red-600", Price.of(300));
    }

    @DisplayName("최단 경로의 라인이 1개만 있을시 추가요금을 조회한다.")
    @Test
    void search_shortestPath() {
        // given
        PricePolicy pricePolicy = new LinePricePolicy(List.of(이호선));

        // when
        Price price = pricePolicy.apply();

        // then
        Assertions.assertThat(price).isEqualTo(이호선.getExtreFare());
    }
    
    @DisplayName("최단 경로의 라인이 2개 이상일 시 라인들 중 가장 높은 추가요금만 조회된다.")
    @Test
    void search_shortestPath2() {
        // given
        PricePolicy pricePolicy = new LinePricePolicy(List.of(이호선, 삼호선, 신분당선));

        // when
        Price price = pricePolicy.apply();

        // then
        Assertions.assertThat(price).isEqualTo(신분당선.getExtreFare());
    }
}
package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class FareTest extends PathTestFixture {

    private GraphPath<Station, StationEdge> 경로;

    @BeforeEach
    public void setUp() {
        super.setUp();
        경로 = PathFinder.findPath(노선목록, 강남역, 남부터미널역);
    }

    @DisplayName("연령별 요금 할인 적용")
    @ParameterizedTest
    @CsvSource({"12,500", "15,800", "20,1350"})
    void 연령별_요금_할인_적용(int input, double expected) {
        // 청소년: 13세 이상~19세 미만 -350 공제 후 20% 할인
        // 어린이: 6세 이상~ 13세 미만 -350 공제 후 50% 할인
        Fare originalFare = new Fare(1350);
        Fare discountedFare = originalFare.applyAgeDiscount(input);

        assertThat(discountedFare.getFare()).isEqualTo(expected);
    }
}

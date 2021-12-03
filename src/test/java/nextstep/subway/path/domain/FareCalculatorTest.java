package nextstep.subway.path.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.*;

@DisplayName("이용 요금 계산기 테스트")
public class FareCalculatorTest {

    private Station 강남역;
    private Station 역삼역;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        역삼역 = new Station("역삼역");
    }

    @ParameterizedTest(name = "{displayName} - [{index}] {argumentsWithNames}")
    @CsvSource(
            value = {
                    "10:0:1250", "11:0:1350", "15:0:1350", "16:0:1450", "50:0:2050", "51:0:2150", "58:0:2150", "59:0:2250",
                    "10:1000:2250", "11:1000:2350", "15:1000:2350", "16:1000:2450", "50:1000:3050", "51:1000:3150",
                    "58:1000:3150", "59:1000:3250"
            },
            delimiter = ':'
    )
    @DisplayName("이용 요금을 계산한다.")
    void calculate(int distance, int addFare, int expectedFare) {
        // given
        Path path = new Path(Arrays.asList(강남역, 역삼역), distance, addFare);
        FareCalculator fareCalculator = new FareCalculator(path);

        // when
        int fare = fareCalculator.calculate();

        // then
        assertThat(fare).isEqualTo(expectedFare);
    }
}

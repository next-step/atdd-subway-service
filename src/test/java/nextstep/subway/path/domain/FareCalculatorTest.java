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
                    "10:0:5:0", "50:0:5:0", "65:0:5:0", "10:1000:5:0", "50:1000:5:0", "65:1000:5:0",
                    "10:0:6:450", "50:0:6:850", "65:0:6:950", "10:1000:6:950", "50:1000:6:1350", "65:1000:6:1450",
                    "10:0:13:720", "50:0:13:1360", "65:0:13:1520", "10:1000:13:1520", "50:1000:13:2160", "65:1000:13:2320",
                    "10:0:19:1250", "50:0:19:2050", "65:0:19:2250", "10:1000:19:2250", "50:1000:19:3050", "65:1000:19:3250"
            },
            delimiter = ':'
    )
    @DisplayName("이용 요금을 계산한다.")
    void calculate(int distance, int addFare, int age, int expectedFare) {
        // given
        Path path = new Path(Arrays.asList(강남역, 역삼역), distance, addFare);
        FareCalculator fareCalculator = new FareCalculator(path, age);

        // when
        int fare = fareCalculator.calculate();

        // then
        assertThat(fare).isEqualTo(expectedFare);
    }
}

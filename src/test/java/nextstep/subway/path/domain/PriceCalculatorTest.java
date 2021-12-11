package nextstep.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DisplayName("거리 가격 계산 테스트")
@ExtendWith(MockitoExtension.class)
class PriceCalculatorTest {

    @Mock
    ShortestPath shortestPath;

    @DisplayName("일반인 요금 확인")
    @ParameterizedTest(name = "{displayName} ({index}) -> param = [{arguments}]")
    @CsvSource(value = {"1:1250", "10:1250", "11:1350", "50:2050", "57:2150", "59:2250"}, delimiter = ':')
    void 일반인_요금_확인(int distance, int expectedPrice) {
        // given
        AgeType 일반인 = AgeType.DEFAULT;
        int 추가요금 = 0;

        when(shortestPath.getDistance()).thenReturn(distance);
        when(shortestPath.getAdditionalPrice()).thenReturn(추가요금);

        // when
        int actualPrice = PriceCalculator.process(일반인, shortestPath);

        // then
        assertThat(actualPrice).isEqualTo(expectedPrice);
    }

    @DisplayName("일반인 추가금액 요금_확인")
    @ParameterizedTest(name = "{displayName} ({index}) -> param = [{arguments}]")
    @CsvSource(value = {"1:1750", "10:1750", "11:1850", "50:2550", "57:2650", "59:2750"}, delimiter = ':')
    void 일반인_추가금액_요금_확인(int distance, int expectedPrice) {
        // given
        AgeType 일반인 = AgeType.DEFAULT;
        int 추가요금 = 500;

        when(shortestPath.getDistance()).thenReturn(distance);
        when(shortestPath.getAdditionalPrice()).thenReturn(추가요금);

        // when
        int actualPrice = PriceCalculator.process(일반인, shortestPath);

        // then
        assertThat(actualPrice).isEqualTo(expectedPrice);
    }

    @DisplayName("어린이 요금 확인")
    @ParameterizedTest(name = "{displayName} ({index}) -> param = [{arguments}]")
    @CsvSource(value = {"1:450", "10:450", "11:500", "50:850", "57:900", "59:950"}, delimiter = ':')
    void 어린이_요금_확인(int distance, int expectedPrice) {
        // given
        AgeType 어린이 = AgeType.CHILD;
        int 추가요금 = 0;

        when(shortestPath.getDistance()).thenReturn(distance);
        when(shortestPath.getAdditionalPrice()).thenReturn(추가요금);

        // when
        int actualPrice = PriceCalculator.process(어린이, shortestPath);

        // then
        assertThat(actualPrice).isEqualTo(expectedPrice);
    }

    @DisplayName("어린이 추가금액 요금_확인")
    @ParameterizedTest(name = "{displayName} ({index}) -> param = [{arguments}]")
    @CsvSource(value = {"1:700", "10:700", "11:750", "50:1100", "57:1150", "59:1200"}, delimiter = ':')
    void 어린이_추가금액_요금_확인(int distance, int expectedPrice) {
        // given
        AgeType 어린이 = AgeType.CHILD;
        int 추가요금 = 500;

        when(shortestPath.getDistance()).thenReturn(distance);
        when(shortestPath.getAdditionalPrice()).thenReturn(추가요금);

        // when
        int actualPrice = PriceCalculator.process(어린이, shortestPath);

        // then
        assertThat(actualPrice).isEqualTo(expectedPrice);
    }

    @DisplayName("청소년 요금 확인")
    @ParameterizedTest(name = "{displayName} ({index}) -> param = [{arguments}]")
    @CsvSource(value = {"1:720", "10:720", "11:800", "50:1360", "57:1440", "59:1520"}, delimiter = ':')
    void 청소년_요금_확인(int distance, int expectedPrice) {
        // given
        AgeType 청소년 = AgeType.TEENAGER;
        int 추가요금 = 0;

        when(shortestPath.getDistance()).thenReturn(distance);
        when(shortestPath.getAdditionalPrice()).thenReturn(추가요금);

        // when
        int actualPrice = PriceCalculator.process(청소년, shortestPath);

        // then
        assertThat(actualPrice).isEqualTo(expectedPrice);
    }

    @DisplayName("청소년 추가금액 요금_확인")
    @ParameterizedTest(name = "{displayName} ({index}) -> param = [{arguments}]")
    @CsvSource(value = {"1:1120", "10:1120", "11:1200", "50:1760", "57:1840", "59:1920"}, delimiter = ':')
    void 청소년(int distance, int expectedPrice) {
        // given
        AgeType 청소년 = AgeType.TEENAGER;
        int 추가요금 = 500;

        when(shortestPath.getDistance()).thenReturn(distance);
        when(shortestPath.getAdditionalPrice()).thenReturn(추가요금);

        // when
        int actualPrice = PriceCalculator.process(청소년, shortestPath);

        // then
        assertThat(actualPrice).isEqualTo(expectedPrice);
    }
}

package nextstep.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("구간별 추가 요금 확인 테스트")
class PathSectorTest {

    @DisplayName("초과된 거리 요금 확인")
    @ParameterizedTest(name = "{displayName} ({index}) -> param = [{arguments}]")
    @CsvSource(value = {"1:0", "10:0", "11:100", "50:800", "57:900", "59:1000"}, delimiter = ':')
    void 초과된_거리_요금_확인(int distance, int expectedPrice) {
        // when
        int actualPrice = PathSector.getOverPrice(distance);

        // then
        assertThat(actualPrice).isEqualTo(expectedPrice);
    }
}

package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Collections;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("요금 계산기")
class FareCalculatorTest {

    private FareCalculator fareCalculator = new FareCalculator();

    private Line 기본_노선;
    private Line 추가요금_노선;

    @BeforeEach
    void setUp() {
        기본_노선 = Line.of("기본 노선", "빨강");
        추가요금_노선 = Line.of("추가요금 노선", "노랑", 900);
    }

    @DisplayName("거리별 요금 정책")
    @Nested
    class 거리별_요금_정책 {
        @DisplayName("10Km 이내 기본운임을 계산할 수 있다.")
        @Test
        void 기본운임_계산() {
            assertThat(fareCalculator.calculate(Distance.from(9), Collections.singletonList(기본_노선))).isEqualTo(1250);
        }

        @DisplayName("이용 거리 10Km 초과 시 5Km마다 100원씩 추가운임 부과할 수 있다.")
        @Test
        void 이용거리_초과_10KM_50KM까지의_계산() {
            assertThat(fareCalculator.calculate(Distance.from(11), Collections.singletonList(기본_노선))).isEqualTo(1350);
        }

        @DisplayName("이용 거리 50Km 초과 시 8Km마다 100원씩 추가운임 부과할 수 있다.")
        @Test
        void 이용거리_50KM_초과_계산() {
            assertThat(fareCalculator.calculate(Distance.from(57), Collections.singletonList(기본_노선))).isEqualTo(2150);
        }
    }

    @DisplayName("노선별 추가 요금 정책")
    @Nested
    class 노선별_추가_요금_정책 {
        @DisplayName("10Km 이내 기본운임을 계산할 수 있다.")
        @Test
        void 기본운임_계산() {
            assertThat(fareCalculator.calculate(Distance.from(9), Collections.singletonList(추가요금_노선))).isEqualTo(2150);
        }

        @DisplayName("이용 거리 10Km 초과 시 5Km마다 100원씩 추가운임 부과할 수 있다.")
        @Test
        void 이용거리_초과_10KM_50KM까지의_계산() {
            assertThat(fareCalculator.calculate(Distance.from(11), Collections.singletonList(추가요금_노선))).isEqualTo(2250);
        }

        @DisplayName("이용 거리 50Km 초과 시 8Km마다 100원씩 추가운임 부과할 수 있다.")
        @Test
        void 이용거리_50KM_초과_계산() {
            assertThat(fareCalculator.calculate(Distance.from(57), Collections.singletonList(추가요금_노선))).isEqualTo(3050);
        }

        @DisplayName("경로 중 추가요금이 있는 노선을 환승 하여 이용 할 경우 가장 높은 금액의 추가 요금만 적용한다.")
        @Test
        void 추가_요금이_있는_노선들을_경유한_경우_계산() {
            Line 조금싼_추가요금_노선 = Line.of("조금 싼 추가요금 노선", "파랑", 500);

            assertThat(
                    fareCalculator.calculate(Distance.from(57), Arrays.asList(기본_노선, 조금싼_추가요금_노선, 추가요금_노선))).isEqualTo(
                    3050);
        }
    }
}

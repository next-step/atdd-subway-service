package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import nextstep.subway.line.domain.Line;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("노선별 추가 요금 정책")
class LineFarePolicyTest {
    @DisplayName("경로 중 추가요금이 있는 노선을 환승 하여 이용 할 경우 가장 높은 금액의 추가 요금만 적용한다.")
    @Test
    void 노선_환승시_노선에_대한_추가_요금_계산() {
        Line 일반_노선 = Line.of("일반 노선", "빨강");
        Line 추가_요금이_있는_노선 = Line.of("추가 요금이 있는 노선", "노랑", 500);
        Line 추가_요금이_더_비싼_노선 = Line.of("추가 요금이 더 비싼 노선", "파랑", 900);
        List<Line> 노선들 = Arrays.asList(일반_노선, 추가_요금이_있는_노선, 추가_요금이_더_비싼_노선);

        assertThat(LineFarePolicy.calculateExcessFare(노선들)).isEqualTo(900);
    }

    @DisplayName("경로 중 추가요금이 있는 노선을 환승 하여 이용 할 경우 가장 높은 금액의 추가 요금만 적용한다.")
    @Test
    void 노선에_대한_추가_요금_계산() {
        Line 일반_노선 = Line.of("일반 노선", "빨강");
        List<Line> 노선들 = Arrays.asList(일반_노선);

        assertThat(LineFarePolicy.calculateExcessFare(노선들)).isEqualTo(0);
    }

    @Test
    void 노선이_없으면_계산_실패() {
        assertThatThrownBy(() -> LineFarePolicy.calculateExcessFare(Collections.emptyList())).isInstanceOf(
                IllegalArgumentException.class);
    }
}

package nextstep.subway.path.domain.policy.fare.line;

import nextstep.subway.line.domain.Line;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class DefaultOverFareByLineStrategyTest {
    @DisplayName("추가요금 계산 - 노선이 하나인 경우")
    @Test
    public void 노선이하나인경우_추가요금계산_확인() throws Exception {
        //given
        Line line = new Line(1L, "이호선", "초록색", 300);
        
        //when
        DefaultOverFareByLineStrategy strategy = new DefaultOverFareByLineStrategy();
        int overFare = strategy.calculateOverFare(Arrays.asList(line));
        
        //then
        assertThat(overFare).isEqualTo(300);
    }

    @DisplayName("추가요금 계산 - 노선이 여러개인 경우")
    @Test
    public void 노선이여러개인경우_추가요금계산_확인() throws Exception {
        //given
        Line line1 = new Line(1L, "일호선", "초록색", 900);
        Line line2 = new Line(2L, "이호선", "초록색", 200);
        Line line3 = new Line(3L, "삼호선", "초록색", 300);

        //when
        DefaultOverFareByLineStrategy strategy = new DefaultOverFareByLineStrategy();
        int overFare = strategy.calculateOverFare(Arrays.asList(line1, line2, line3));

        //then
        assertThat(overFare).isEqualTo(900);
    }
}

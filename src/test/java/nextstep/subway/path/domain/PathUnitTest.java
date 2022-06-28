package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Path 단위 테스트")
public class PathUnitTest {
    private Line 칠호선;
    private Line 이호선;

    private Section 뚝유_건대;
    private Section 건대_구의;

    private Station 뚝섬유원지역;
    private Station 건대역;
    private Station 구의역;

    @BeforeEach
    void init() {
        뚝섬유원지역 = new Station("뚝섬유원지역");
        건대역 = new Station("건대역");
        구의역 = new Station("구의역");

        칠호선 = new Line("칠호선", "blue", 뚝섬유원지역, 건대역, 10, 500);
        이호선 = new Line("이호선", "green", 건대역, 구의역, 10, 300);

        뚝유_건대 = new Section(칠호선, 뚝섬유원지역, 건대역, 10);
        건대_구의 = new Section(이호선, 건대역, 구의역, 10);
    }

    @Test
    @DisplayName("노선 추가요금 계산 - 노선별 추가요금 중 가장 높은 추가금액 하나만 적용")
    void getLineExtraFare() {
        Path path = new Path(Arrays.asList(뚝섬유원지역, 건대역, 구의역), 20);
        
        assertThat(path.getLineExtraFare(new Sections(Arrays.asList(뚝유_건대, 건대_구의)))).isEqualTo(500);
    }
}

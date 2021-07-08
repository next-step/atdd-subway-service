package nextstep.subway.path;

import nextstep.subway.line.domain.Line;
import nextstep.subway.path.application.PathService;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@SpringBootTest
public class PathServiceTest {
    // given
    private Line 삼호선;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;
    private Station 잠실역;
    @Autowired
    private PathService pathService;

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */
    @BeforeEach
    void setUp() {
        // given
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널역");
        잠실역 = new Station("잠실역");

        삼호선 = new Line("2호선", "green", 교대역, 양재역, 5);

        삼호선.addLineStation(교대역, 남부터미널역, 3);
    }


    @Test
    @DisplayName("존재하지 않은 출발역이나 도착역을 조회 할 경우 오류가 발생한다.")
    public void notFoundSourceTarget() throws Exception {
        // when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> pathService.findPath(null, 교대역.getId(), 잠실역.getId()))
                .withMessageMatching("존재하지 않은 출발역이나 도착역입니다.");
    }
}
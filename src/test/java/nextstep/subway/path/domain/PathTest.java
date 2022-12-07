package nextstep.subway.path.domain;

import nextstep.subway.common.exception.InvalidDataException;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static nextstep.subway.station.StationFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PathTest {

    private Line 삼호선;
    private Line 이호선;
    private Line 신분당선;
    private Station 교대역;
    private Station 남부터미널역;
    private Station 양재역;
    private Station 강남역;
    private Station 당산역;
    private List<Line> lines = new ArrayList<>();

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                            |
     * *3호선*                   *신분당선*
     * |                            |
     * 남부터미널역  --- *3호선* ---  양재
     */
    @BeforeEach
    public void setUp() {
        교대역 = 교대역();
        남부터미널역 =남부터미널역();
        양재역 = 양재역();
        강남역 = 강남역();
        당산역 = 당산역();

        신분당선 = new Line("신분당선", "red", 강남역, 양재역, new Distance(10));
        이호선 = new Line("2호선", "Yellow-green", 교대역, 강남역, new Distance(10));
        삼호선 = new Line("3호선", "Orange", 교대역, 양재역, new Distance(5));
        삼호선.addLineStation(new Section(삼호선, 교대역, 남부터미널역, new Distance(3)));

        lines.add(삼호선);
        lines.add(이호선);
        lines.add(신분당선);
    }

    @DisplayName("역 간의 최단 경로를 조회한다.")
    @Test
    void 최단_경로_조회_테스트() {
        Path path = new Path(lines, 강남역, 남부터미널역);

        assertThat(path.getBestPath()).containsExactly(강남역, 양재역, 남부터미널역);
        assertThat(path.getBestPathDistance().value()).isEqualTo(12);
    }

    @DisplayName("동일역으로 최단 경로를 조회한다.")
    @Test
    void 동일역으로_최단_경로_생성_시_예외_테스트() {
        assertThatThrownBy(
                () -> new Path(lines, 강남역, 강남역)
        ).isInstanceOf(InvalidDataException.class);
    }

    @DisplayName("연결되지 않은 역으로 최단 경로를 생성한다.")
    @Test
    void 연결되지_않은_역으로_최단_경로_생성_시_예외_테스트() {
        assertThatThrownBy(
                () -> new Path(lines, 남부터미널역, 당산역)
        ).isInstanceOf(InvalidDataException.class);
    }

}
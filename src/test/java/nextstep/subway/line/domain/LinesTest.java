package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.collect.Lists;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class LinesTest {

    private Lines 전체_노선;
    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;

    private Line 신분당선;
    private Line 삼호선;
    private Line 이호선;

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
        강남역 = Station.of("강남역");
        양재역 = Station.of("양재역");
        교대역 = Station.of("교대역");
        남부터미널역 = Station.of("남부터미널역");

        신분당선 = new Line("신분당선", "bg-red-600", 강남역, 양재역, 10, 1000);
        삼호선 = new Line("삼호선", "bg-orange-600", 교대역, 양재역, 5, 200);
        이호선 = new Line("이호선", "bg-green-600", 교대역, 강남역, 8, 400);

        삼호선.addSections(Section.of(삼호선, 교대역, 남부터미널역, 3));

        전체_노선 = Lines.of(Lists.newArrayList(신분당선, 삼호선, 이호선));
    }

    @DisplayName("강남역, 양재역을 포함하는 노선은 신분당선")
    @Test
    void 경로를_포함하는_노선_구하기1() {
        // when
        Lines lines = this.전체_노선.getLinesInclude(Lists.newArrayList(강남역, 양재역));

        // then
        assertThat(lines.getLines().size()).isEqualTo(1);
        assertThat(lines.getLines()).contains(신분당선);
    }

    @DisplayName("교대역, 강남역, 양재역을 포함하는 노선은 이호선, 신분당선")
    @Test
    void 경로를_포함하는_노선_구하기2() {
        // when
        Lines lines = this.전체_노선.getLinesInclude(Lists.newArrayList(교대역, 강남역, 양재역));

        // then
        assertThat(lines.getLines().size()).isEqualTo(2);
        assertThat(lines.getLines()).contains(이호선, 신분당선);
    }

    @DisplayName("교대역, 남부터미널역, 양재역을 포함하는 노선은 삼호선")
    @Test
    void 경로를_포함하는_노선_구하기3() {
        // when
        Lines lines = this.전체_노선.getLinesInclude(Lists.newArrayList(교대역, 남부터미널역, 양재역));

        // then
        assertThat(lines.getLines().size()).isEqualTo(1);
        assertThat(lines.getLines()).contains(삼호선);
    }

    @DisplayName("교대역, 강남역, 양재역, 남부터미널역을 포함하는 노선은 이호선, 신분당선, 삼호선")
    @Test
    void 경로를_포함하는_노선_구하기4() {
        // when
        Lines lines = this.전체_노선.getLinesInclude(Lists.newArrayList(교대역, 강남역, 양재역, 남부터미널역));

        // then
        assertThat(lines.getLines().size()).isEqualTo(3);
        assertThat(lines.getLines()).contains(이호선, 삼호선, 신분당선);
    }

    @DisplayName("없는 경로를 노선에서 찾는 경우")
    @Test
    void 경로를_포함하는_노선_구하기5() {
        // when
        Lines lines = this.전체_노선.getLinesInclude(Lists.newArrayList(강남역, 남부터미널역));

        // then
        assertThat(lines.getLines().size()).isEqualTo(0);
    }
}

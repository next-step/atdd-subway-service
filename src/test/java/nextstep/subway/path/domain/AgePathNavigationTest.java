package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AgePathNavigationTest {

    private final String email = "joengyu0@gmail.com";
    private final String password = "password";
    private final int 어른_나이 = 20;
    private final int 청소년_나이 = 13;
    private final int 어린이_나이 = 6;
    private List<Line> lines;
    private Station 강남;
    private Station 광교;
    private Station 양재;
    private Station 교대;
    private Station 남부터미널;

    @BeforeEach
    void setUp() {
        강남 = new Station(1L, "강남");
        광교 = new Station(2L, "광교");
        양재 = new Station(3L, "양재");
        교대 = new Station(4L, "교대");
        남부터미널 = new Station(5L, "남부터미널");

        lines = new ArrayList<>();

        Line 신분당선 = new Line("신분당선", "gs-1123", 0, 강남, 양재, 15);
        Line 이호선 = new Line("이호선", "gs-12345", 0, 교대, 강남, 12);
        Line 삼호선 = new Line("삼호선", "gs-12345", 0, 교대, 양재, 27);
        삼호선.addSection(new Section(삼호선, 교대, 남부터미널, 9));
        lines.addAll(Lists.list(신분당선, 이호선, 삼호선));
    }

    @Test
    void 성인은_어떠한_할인도_받지_못한다() {
        assertThat(어른_나이).isGreaterThanOrEqualTo(20);
        Path shortestPath = PathNavigation.by(lines).findShortestPath(강남, 남부터미널, 어른_나이);

        assertThat(shortestPath.fee()).isEqualTo(1250);
    }

    @Test
    void 청소년은_350원을_공제한_금액의_20퍼센트를_할인한다() {
        assertThat(청소년_나이).isBetween(13, 18);
        Path shortestPath = PathNavigation.by(lines).findShortestPath(강남, 남부터미널, 청소년_나이);

        assertThat(shortestPath.fee()).isEqualTo(720);
    }


    @Test
    void 어린이은_350원을_공제한_금액의_50퍼센트를_할인한다() {
        assertThat(어린이_나이).isBetween(6, 12);
        Path shortestPath = PathNavigation.by(lines).findShortestPath(강남, 남부터미널, 어린이_나이);

        assertThat(shortestPath.fee()).isEqualTo(450);
    }
}
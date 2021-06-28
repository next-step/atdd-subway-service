package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PathNavigationTest {

    PathNavigation sut;
    private Station 강남;
    private Station 광교;
    private Station 구리;
    private Station 용산;
    private List<Line> lines;
    private Station 양재;
    private Station 교대;
    private Station 남부터미널;
    private Line 이호선;
    private Station B;
    private Station 강변;
    private Station A;
    private Station C;

    @BeforeEach
    void setUp() {
        강남 = new Station(1L, "강남");
        광교 = new Station(2L, "광교");
        구리 = new Station(3L, "구리");
        용산 = new Station(4L, "용산");
        양재 = new Station(5L, "양재");
        교대 = new Station(6L, "교대");
        남부터미널 = new Station(7L, "남부터미널");

        B = new Station(8L, "충정로");
        강변 = new Station(9L, "강변");
        A = new Station(10L, "많이-먼-역");
        C = new Station(11L, "많이-더-먼-역");

        lines = new ArrayList<>();
        lines.add(new Line("신분당선", "gs-1123", 강남, 양재, 15));
        이호선 = new Line("이호선", "gs-12345", 교대, 강남, 12);
        이호선.addSection(new Section(이호선, 강변, 교대, 107));
        이호선.addSection(new Section(이호선, B, 강변, 134));

        Line 삼호선 = new Line("삼호선", "gs-12345", 교대, 양재, 27);
        삼호선.addSection(new Section(삼호선, 교대, 남부터미널, 9));
        lines.add(삼호선);
        lines.add(이호선);
        lines.add(new Line("중앙선", "gs-1123", 구리, 용산, 200));
    }

    @Test
    void findShortestPath() {

        sut = PathNavigation.by(lines);

        Path shortestPath = sut.findShortestPath(강남, 남부터미널);

        assertThat(shortestPath.stations()).contains(강남, 교대, 남부터미널);
        assertThat(shortestPath.distance()).isEqualTo(21);
        assertThat(shortestPath.fee()).isEqualTo(1250);
    }

    @Test
    void findShortestPath_25KM_1350Fee() {
        lines.add(이호선);
        sut = PathNavigation.by(lines);
        Path shortestPath = sut.findShortestPath(B, 남부터미널);
        assertThat(shortestPath.distance()).isEqualTo(250);
        assertThat(shortestPath.fee()).isEqualTo(1650);
    }

    @Test
    void findShortestPath_50KM_1350Fee() {

        이호선.addSection(new Section(이호선, A, B, 500));
        lines.add(이호선);
        sut = PathNavigation.by(lines);
        Path shortestPath = sut.findShortestPath(A, B);
        assertThat(shortestPath.distance()).isEqualTo(500);
        assertThat(shortestPath.fee()).isEqualTo(2150);
    }

    @Test
    void IllegalArgumentException_when_add_EqualsStations() {
        sut = PathNavigation.by(lines);

        assertThatThrownBy(() -> sut.findShortestPath(강남, 강남))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("동일한 역을 입력하였습니다");
    }

    @Test
    void IllegalArgumentException_when_path_is_not_connected() {
        sut = PathNavigation.by(lines);

        assertThatThrownBy(() -> sut.findShortestPath(강남, 용산))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("역이 연결되어 있지 않습니다.");
    }

    @Test
    void IllegalArgumentException_when_path_is_not_registered() {
        sut = PathNavigation.by(lines);
        Station 없는역 = new Station(10L,"없는역");
        assertThatThrownBy(() -> sut.findShortestPath(강남, 없는역))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("존재하지 않은 출발역이나 도착역이 있습니다.");
    }
}
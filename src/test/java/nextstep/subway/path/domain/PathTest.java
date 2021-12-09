package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.application.exception.InvalidPathException;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("지하철 경로 기능")
class PathTest {
    private Station 강남;
    private Station 선릉;
    private Station 교대;
    private Station 남부터미널;
    private Station 양재;
    private Station 매봉;
    private Station 이수;
    private Station 사당;

    private Line 이호선;
    private Line 삼호선;

    private Path path;

    /**
     * 교대역 ------- (2) ---- 강남역 ---- (3) --- 선릉
     * |                      |
     * (1)                   { }
     * |                      |
     * 남부터미널역 --- (2) ---- 양재 ----- (4) --- 매봉
     *
     * 이수 --- (3) --- 사당
     */
    @BeforeEach
    void setUp() {
        강남 = new Station(1L, "강남");
        선릉 = new Station(2L, "선릉");
        교대 = new Station(3L, "교대");
        남부터미널 = new Station(4L, "남부터미널");
        양재 = new Station(5L, "양재");
        매봉 = new Station(6L, "매봉");
        이수 = new Station(7L, "이수");
        사당 = new Station(8L, "사당");

        이호선 = new Line("이호선", "bg-green-600", 교대, 강남, 2);
        이호선.addSection(new Section(강남, 선릉, 3));

        삼호선 = new Line("삼호선", "bg-orange-600", 교대, 남부터미널, 1);
        삼호선.addSection(new Section(남부터미널, 양재, 2));
        삼호선.addSection(new Section(양재, 매봉, 4));

        path = new Path(Arrays.asList(이호선, 삼호선));
    }


    @Test
    @DisplayName("최단 거리의 경로를 조회한다.")
    void shortestDistance() {
        // given
        // 양재 -> 강남 : (양재-6-강남) 6 / (양재-2-남부터미널-1-교대-2-강남) 5
        Line 신분당선 = new Line("신분당선", "bg-red-600", 강남, 양재, 6);
        path = new Path(Arrays.asList(이호선, 삼호선, 신분당선));

        // when
        List<Station> stations = path.shortestDistance(양재, 강남);

        // then
        assertThat(stations).extracting("name")
                .containsExactly("양재", "남부터미널", "교대", "강남");
    }

    @Test
    @DisplayName("최단 거리가 같은 경우 이동할 구간이 적은 경로가 선택된다.")
    void sameShortestDistance() {
        // given
        // 양재 -> 강남 : (양재-5-강남) 5 / (양재-2-남부터미-1-교대-2-강남) 5
        Line 신분당선 = new Line("신분당선", "bg-red-600", 강남, 양재, 5);
        path = new Path(Arrays.asList(이호선, 삼호선, 신분당선));

        // when
        List<Station> stations = path.shortestDistance(양재, 강남);

        // then
        assertThat(stations).extracting("name")
                .containsExactly("양재", "강남");
    }

    @Test
    @DisplayName("출발역과 도착역이 같은 경우 예외가 발생한다.")
    void validateSameDepartureArrival() {
        // given when then
        assertThatThrownBy(() -> path.shortestDistance(교대, 교대))
                .isInstanceOf(InvalidPathException.class);
    }

    @Test
    @DisplayName("지하철 노선에 존재하지 않는 역인 경우 예외가 발생한다.")
    void validateNotExist() {
        // given when then
        assertThatThrownBy(() -> path.shortestDistance(new Station("서초"), 교대))
                .isInstanceOf(InvalidPathException.class);
    }

    @Test
    @DisplayName("출발역과 도착역이 연결되지 않은 경우 예외가 발생한다.")
    void validateNotConnectable() {
        // given
        Line 사호선 = new Line("사호선", "bg-sky-600", 이수, 사당, 3);
        path = new Path(Arrays.asList(이호선, 삼호선, 사호선));

        // when then
        assertThatThrownBy(() -> path.shortestDistance(강남, 사당))
                .isInstanceOf(InvalidPathException.class);
    }
}

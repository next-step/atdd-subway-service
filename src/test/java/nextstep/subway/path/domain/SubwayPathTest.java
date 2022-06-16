package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("경로 조회 테스트")
public class SubwayPathTest {
    Station 강남역;
    Station 력삼역;
    Station 양재역;
    Station 교대역;
    Station 남부터미널역;
    Station 서울역;
    Sections sections;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        력삼역 = new Station("역삼역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널역");
        서울역 = new Station("서울역");

        Line 이호선 = new Line("2호선", "green", 강남역, 력삼역, 10);
        이호선.addLineStation(교대역, 강남역, 1000);
        Line 삼호선 = new Line("3호선", "orange", 교대역, 남부터미널역, 2);
        삼호선.addLineStation(남부터미널역, 양재역, 3);
        Line 신분당선 = new Line("신분당선", "dark-red", 양재역, 강남역, 4);

        sections = new Sections();
        sections.addAll(이호선.getSections(), 삼호선.getSections(), 신분당선.getSections());
    }

    @Test
    @DisplayName("최단거리 기준으로 경로를 구한다")
    void dijkstra() {
        //when
        List<Station> result = PathFinder.dijkstra(교대역, 력삼역, Arrays.asList(강남역, 력삼역, 양재역, 교대역, 남부터미널역), sections);
        Distance distance = sections.filteredBy(result).totalDistance();

        //then
        assertThat(result.get(0)).isEqualTo(교대역);
        assertThat(result.get(1)).isEqualTo(남부터미널역);
        assertThat(result.get(2)).isEqualTo(양재역);
        assertThat(result.get(3)).isEqualTo(강남역);
        assertThat(result.get(4)).isEqualTo(력삼역);
        assertThat(distance).isEqualTo(new Distance(19));
    }

    @Test
    @DisplayName("동일한 역으로 경로를 검색하면 예외가 발생한다")
    void sameStationPathException() {
        assertThatThrownBy(
                () -> PathFinder.dijkstra(교대역, 교대역, Arrays.asList(강남역, 력삼역, 양재역, 교대역, 남부터미널역), sections))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("시작역과 종료역은 같을 수 없습니다.");
    }

    @Test
    @DisplayName("경로를 찾지 못하면 예외가 발생한다")
    void notFoundPath() {
        assertThatThrownBy(
                () -> PathFinder.dijkstra(서울역, 교대역, Arrays.asList(강남역, 력삼역, 양재역, 교대역, 남부터미널역), sections))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("graph must contain the source vertex");
    }
}

package nextstep.subway.path.infrastructure;

import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.dto.PathAnalysisKey;
import nextstep.subway.path.dto.ShortestPathInfo;
import nextstep.subway.policy.domain.Price;
import nextstep.subway.station.domain.Station;

@DisplayName("경로분석 관련 기능")
public class PathAnalysisTest {
    private Station 강남역;
    private Station 양재역;
    private Station 교대역;

    private Section 강남_양재_구간;
    private Section 교대_강남_구간;
    private Section 교대_양재_구간;

    @BeforeEach
    public void setUp() {
        // given
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");

        강남_양재_구간 = new Section(null, 강남역, 양재역, Distance.of(3));
        교대_강남_구간 = new Section(null, 교대역, 강남역, Distance.of(1));
        교대_양재_구간 = new Section(null, 교대역, 양재역, Distance.of(5));
    }

    @DisplayName("제공된 경로들을 기준으로 출발역에서 도착역으로가는 최단 경로를 조회한다.")
    @Test
    void search_shortestPath() {
        // given
        Sections sections = Sections.of(교대_강남_구간, 강남_양재_구간, 교대_양재_구간);
        PathAnalysis pathAnalysis = PathAnalysis.of(sections);

        // when
        ShortestPathInfo shortestPathInfo = pathAnalysis.findShortestPaths(양재역, 교대역);

        // then
        assertAll(
            () -> Assertions.assertThat(shortestPathInfo.getPathAnalysisKeys()).isEqualTo(List.of(PathAnalysisKey.of(양재역),
                                                                                                    PathAnalysisKey.of(강남역),
                                                                                                    PathAnalysisKey.of(교대역))),
            () -> Assertions.assertThat(shortestPathInfo.getDistance()).isEqualTo(Distance.of(4))
        );
    }

    @DisplayName("제공된 경로들이 없을 상태에 구간분석 객체를 생성시 예외가 발생한다.")
    @Test
    void exception_generateWhenNotHasSection() {
        // given
        Sections sections = Sections.of();

        // when
        // then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> PathAnalysis.of(sections));
    }

    @DisplayName("노선이 다르나 거리가 같은 구간에대해 최단거리는 노선의 추가요금이 저렵한 구간을 선택하여 조회된다.")
    @Test
    void challenge_shortestPath() {
        // given
        Line 신분당선 = new Line("신분당선", "bg-red-600", 강남역, 양재역, Distance.of(10), Price.of(220));
        Line 이호선 = new Line("이호선", "bg-red-600", 강남역, 양재역, Distance.of(10), Price.of(100));
        Line 삼호선 = new Line("삼호선", "bg-red-600", 강남역, 양재역, Distance.of(10), Price.of(110));

        Sections sections = 신분당선.getSections();
        sections.add(이호선.getSections());
        sections.add(삼호선.getSections());

        PathAnalysis pathAnalysis = PathAnalysis.of(sections);

        // when
        ShortestPathInfo shortestPathInfo = pathAnalysis.findShortestPaths(강남역, 양재역);

        // then
        Assertions.assertThat(shortestPathInfo.getLines()).isEqualTo(List.of(이호선));
    }
}

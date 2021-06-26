package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PathFinderTest {

    private Line 신분당선 = new Line("신분당선", "red");
    private Station 강남역 = new Station("강남역");
    private Station 양재역 = new Station("양재역");
    private Station 청계산역 = new Station("청계산역");
    private Station 판교역 = new Station("판교역");
    private Station 정자역 = new Station("정자역");

    @DisplayName("최단경로 조회 - 단일 노선")
    @Test
    void findPaths() {
        
        // given
        신분당선.add(강남역, 양재역, 5);
        신분당선.add(양재역, 청계산역, 10);
        신분당선.add(청계산역, 판교역, 15);
        신분당선.add(판교역, 정자역, 7);

        List<Line> lines = new ArrayList<>();
        lines.add(신분당선);
        
        // when
        PathFinder pathFinder = new PathFinder(lines);
        List<Station> paths = pathFinder.findPaths(양재역, 정자역);

        // then
        assertThat(paths.size()).isEqualTo(4);
        assertThat(paths).isEqualTo(Arrays.asList(양재역, 청계산역, 판교역, 정자역));
    }
}
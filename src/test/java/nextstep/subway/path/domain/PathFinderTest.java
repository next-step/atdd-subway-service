package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PathFinderTest {

    @DisplayName("최적 경로를 검색한다.")
    @Test
    void findShortestPath() {
        Station 강남역 = new Station("강남역");
        Station 서초역 = new Station("서초역");

        Line line = new Line("2호선", "red", 강남역, 서초역, 10);

        // source 에 해당하는 line을 조회한다.
        // target 에 해당하는 line을 조회한다.
        // source line 에 해당하는 모든 section을 그래프에 저장하고, target line 의 모든 section을 그래프에 저장한다.
        // 라이브러리 경로 리턴이 null 이면 예외, 결과를 리턴한다.

    }
}

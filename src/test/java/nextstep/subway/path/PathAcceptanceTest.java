package nextstep.subway.path;

import nextstep.subway.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("지하철 경로 조회")
class PathAcceptanceTest extends AcceptanceTest {

    /**
     *  Given 지하철 노선에 지하철 역이 등록되어 있고
     *  When 연결되어 있는 출발, 도착역을 경로조회 하면
     *  Then 최단거리 결과로 알려준다.
     */
    @Test
    @DisplayName("정상적인 출발, 도착역을 경로조회하면 최단거리를 알려준다.")
    void searchShortestPath() {
    }

    /**
     *  Given 지하철 노선에 지하철 역이 등록되어 있고
     *  When 연결되어 있지 않은 출발, 도착역을 경로조회 하면
     *  Then 경로 조회를 할 수 없다.
     */
    @Test
    @DisplayName("연결되어 있지 않은 출발, 도착역은 경로조회 할 수 없다.")
    void searchNotLinkedPath() {
    }

    /**
     *  Given 지하철 노선에 지하철 역이 등록되어 있고
     *  When 출발, 도착역이 같으면
     *  Then 경로 조회를 할 수 없다.
     */
    @Test
    @DisplayName("출발, 도착역이 동일하면 경로조회 할 수 없다.")
    void searchSameStationPath() {
    }

    /**
     *  Given 지하철 노선에 지하철 역이 등록되어 있고
     *  When 존재하지 않는 역을 경로조회 하면
     *  Then 경로 조회를 할 수 없다.
     */
    @Test
    @DisplayName("존재하지 않는 역은 경로조회 할 수 없다.")
    void searchNotExistStationPath() {
    }
}

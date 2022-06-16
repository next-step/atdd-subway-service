package nextstep.subway.path;

import nextstep.subway.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    /*
    *   Given 2개 이상의 지하철역이 등록되어 있음
    *   And 1개 이상의 지하철 노선이 등록되어 있음
    *   And 지하철 노선에 지하철역이 등록되어 있음
    *   When 출발역과 도착역을 선택하고 경로를 검색하면
    *   Then 출발역에서 도착역까지의 최단거리 경로가 조회된다.
    */
    @DisplayName("같은 지하철 노선에 포함된 역 사이의 최단거리 경로를 조회한다.")
    @Test
    void findPath_sameLine() {
    }

    /*
     *   Given 2개 이상의 지하철역이 등록되어 있음
     *   And 1개 이상의 지하철 노선이 등록되어 있음
     *   And 지하철 노선에 지하철역이 등록되어 있음
     *   When 출발역과 도착역을 선택하고 경로를 검색하면
     *   Then 출발역에서 도착역까지의 최단거리 경로가 조회된다.
     */
    @DisplayName("서로 다른 지하철 노선에 포함된 역 사이의 최단거리 경로를 조회한다.")
    @Test
    void findPath_differentLines() {
    }

    @DisplayName("출발역과 도착역이 같은 경우 최단거리 경로를 조회할 수 없다.")
    @Test
    void findPath_throwsException_ifStationsAreSame() {

    }

    @DisplayName("출발역과 도착역이 연결이 되어 있지 않으면 최단거리 경로를 조회할 수 없다.")
    @Test
    void findPath_throwsException_ifStationsAreNotConnected() {

    }

    @DisplayName("존재하지 않은 출발역이나 도착역으로는 최단거리 경로를 조회할 수 없다.")
    @Test
    void findPath_throwsException_ifFromOrToNonExistentStation() {

    }
}

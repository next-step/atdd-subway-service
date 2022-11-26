package nextstep.subway.path;

import nextstep.subway.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {

    /**
     *  양재 -- 강남 -- 신논현 -- 논현 -- 신사 (신분당선)
     *   |                           |
     *  남부터미널 -- 교대 -- 고속터미널 -- 잠원 (3호선)
     *
     * Feature: 지하철 구간 관련 기능
     *   Background
     *     Given 지하철역 등록되어 있음
     *     And 지하철 노선 등록되어 있음
     *     And 지하철 노선에 지하철역 등록되어 있음
     *     And 지하철 구간 등록되어 있음
     *   Scenario: 지하철 최단 경로를 조회
     *     When 지하철의 최단 경로를 조회한다
     *     Then 최단 경로의 역 목록을 순서대로 알 수 있다.
     *     Then 최단 경로의 총 이동 거리를 알 수 있다
     */
    @Test
    @DisplayName("지하철 최단 경로 조회")
    void findShortestPath() {

    }
}

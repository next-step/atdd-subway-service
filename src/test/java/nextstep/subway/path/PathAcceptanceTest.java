package nextstep.subway.path;

import nextstep.subway.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {


    /*
        Scenario: 출발역과 도착역이 같은 두 역의 최단 거리 경로를 조회

        Given 지하철역이 등록되어있음
        And 지하철 노선이 등록되어있음
        And 지하철 노선에 지하철역이 등록되어있음

        When 출발역과 도착역이 같은 최단 거리 경로 조회를 요청

        Then 경로 조회에 실패함
    */
    @Test
    void findPath_fail_sameStation() {
    }

    /*
        Scenario: 연결되지 않은 두 역의 최단 거리 경로를 조회

        Given 지하철역이 등록되어있음
        And A 노선이 등록되어 있음
        And B 노선이 등록되어 있음
        And A 노선과 B 노선이 연결되어 있지 않음

        When 연결되어 있지 않은 A 노선과 B 노선의 역의 최단 거리 경로를 조회 요청

        Then 경로 조회에 실패함
    */
    @Test
    void findPath_fail_notConnect() {
    }

    /*
        Scenario: 두 역의 최단 거리 경로를 조회

        Given 지하철역이 등록되어있음
        And 지하철 노선이 등록되어있음
        And 지하철 노선에 지하철역이 등록되어있음

        When 출발역에서 도착역까지의 최단 거리 경로 조회를 요청

        Then 최단 거리 경로를 응답
    */
    @Test
    void findPath_success() {
    }

    /*
        Scenario: 존재하지 않는 역의 최단 경로르 ㄹ조회

        Given 지하철역이 등록되어있음
        And 지하철 노선이 등록되어있음
        And 지하철 노선에 지하철역이 등록되어있음

        When 존재하지 않는 역의 최단 거리 경로 조회를 요청

        Then 경로 조회에 실패함
    */
    @Test
    void findPath_fail_notExist() {
    }
}

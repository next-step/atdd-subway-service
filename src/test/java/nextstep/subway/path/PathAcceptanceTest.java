package nextstep.subway.path;

import nextstep.subway.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {

    @DisplayName("최단 경로 찾기")
    @Test
    void findPathByPoints() {
        // When
        // 서대문역 -> 시청역 이동 최단 경로 조회

        // Then
        // 서대문역 -> 충정로역 -> 시청역 순으로 정렬된 목록을 반환한다.
    }

    @DisplayName("최단 경로 찾기 - 동일한 경로가 여럿인경우(거쳐가는 지하철역 개수가 동일) 짧은 거리를 기준")
    @Test
    void findPathByPointsWithDistance() {
        // When
        // 광화문역에서 시청역을 가는 최단 경로를 찾는다.

        // Then
        // 광화문역 -> 종로3가역 -> 종각역 -> 시청역 순으로 정렬된 목록을 반환한다.
    }

    @DisplayName("출발역과 도착역이 같을 경우 예외 발생")
    @Test
    void originAndDestinationSame() {
        // When
        // 동일한 출발역과 도착역을 인자값으로 경로를 찾는다.

        // Then
        // 출발역과 도착역이 같다는 예외가 발생한다.
    }

    @DisplayName("출발역과 도착역이 연결되어 있지 않은 경우 예외 발생")
    @Test
    void notConnectedOriginAndDestination() {
        // When
        // 출발역과 연결되지 않은 도착역을 인자값으로 전달하여 경로를 찾는다.

        // Then
        // 출발역과 도착역이 연결되어 있지 않다는 예외가 발생한다.
    }

    @DisplayName("존재하지 않은 출발역이나 도착역을 조회 할 경우 예외 발생")
    @Test
    void notExistOriginAndDestination() {
        // When
        // 존재하지 않은 출발역이나 도착역을 인자값으로 사용하여 경로를 찾는다.

        // Then
        // 존재하지 않은 출발역이나 도착역이라는 예외가 발생한다.
    }

}

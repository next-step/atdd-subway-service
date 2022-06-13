package nextstep.subway.favorite;

import nextstep.subway.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("즐겨찾기 관련 기능")
class FavoriteAcceptanceTest extends AcceptanceTest {

    /**
     *  Given 지하철역 등록되어 있고
     *    And 지하철 노선 등록되어 있고
     *    And 지하철 노선 지하철역 등록되어 있고
     *    And 회원 등록되어 있고
     *    And 로그인 되어 있다
     *   When 즐겨찾기 생성을 요청하면
     *   Then 즐겨찾기가 생성된다
     *   When 즐겨찾기 목록 조회하면
     *   Then 즐겨찾기 목록이 조회된다
     *   When 즐겨찾기 삭제 요청하면
     *   Then 즐겨찾기가 삭제된다
     */
    @Test
    @DisplayName("즐겨찾기 관리 시나리오")
    void favoriteScenario() {
    }
}

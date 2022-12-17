package nextstep.subway.favorite;

import nextstep.subway.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;

/**
 * Feature: 즐겨찾기를 관리한다.
 *
 *   Background
 *     Given 지하철역 등록되어 있음
 *     And 지하철 노선 등록되어 있음
 *     And 지하철 노선에 지하철역 등록되어 있음
 *     And 회원 등록되어 있음
 *     And 로그인 되어있음
 *
 *   Scenario: 즐겨찾기를 관리
 *     When 즐겨찾기 생성을 요청
 *     Then 즐겨찾기 생성됨
 *     When 즐겨찾기 목록 조회 요청
 *     Then 즐겨찾기 목록 조회됨
 *     When 즐겨찾기 삭제 요청
 *     Then 즐겨찾기 삭제됨
 */
@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
}

package nextstep.subway.favorite;

import nextstep.subway.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/*
Feature: 즐겨찾기를 관리한다.
  Background
    Given 지하철역 등록되어 있음
    And 지하철 노선 등록되어 있음
    And 지하철 노선에 지하철역 등록되어 있음
    And 회원 등록되어 있음
    And 로그인 되어있음
  Scenario: 즐겨찾기를 관리
    When 즐겨찾기 생성을 요청
    Then 즐겨찾기 생성됨
    When 즐겨찾기 목록 조회 요청
    Then 즐겨찾기 목록 조회됨
    When 즐겨찾기 삭제 요청
    Then 즐겨찾기 삭제됨
 */
@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    @DisplayName("토큰이 유효하면 즐겨찾기를 생성한다")
    @Test
    void 즐겨찾기_생성_성공() {

    }

    @DisplayName("토큰이 유효하지 않다면 생성을 실패한다.")
    @Test
    void 즐겨찾기_생성_토큰_실패() {

    }

    @DisplayName("토큰이 유효하면 즐겨찾기를 조회한다.")
    @Test
    void 즐겨찾기_조회_성공() {

    }

    @DisplayName("토큰이 유효하지 않다면 즐겨찾기를 조회를 실패한다.")
    @Test
    void 즐겨찾기_조회_토큰_실패() {

    }

    @DisplayName("토큰이 유효하면 즐겨찾기를 삭제한다.")
    @Test
    void 즐겨찾기_삭제_성공() {

    }

    @DisplayName("토큰이 유효하지 않다면 즐겨찾기를 삭제를 실패한다.")
    @Test
    void 즐겨찾기_삭제_토큰_실패() {

    }
}

package nextstep.subway.favorite;

import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;

import static nextstep.subway.member.MemberAcceptanceTest.*;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
//    Given 지하철역 등록되어 있음
//    And 지하철 노선 등록되어 있음
//    And 지하철 노선에 지하철역 등록되어 있음
//    And 회원 등록되어 있음
        회원_생성을_요청(EMAIL, PASSWORD, AGE);
//    And 로그인 되어있음
        TokenResponse tokenResponse = AuthAcceptanceTest.로그인_요청_성공(EMAIL, PASSWORD);

    }

    @Test
    @DisplayName("즐겨찾기를 관리한다")
    void manageFavorite() {
//    When 즐겨찾기 생성을 요청
//    Then 즐겨찾기 생성됨
//    When 즐겨찾기 목록 조회 요청
//    Then 즐겨찾기 목록 조회됨
//    When 즐겨찾기 삭제 요청
//    Then 즐겨찾기 삭제됨

    }
}
package nextstep.subway.member.acceptance;

import static nextstep.subway.member.step.FavoriteAcceptanceStep.*;
import static nextstep.subway.member.step.FavoriteAuthAcceptanceFixtures.*;
import static nextstep.subway.member.step.FavoriteLineAcceptanceFixtures.노선등록되어있음;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("즐겨찾기 관련 기능")
class FavoriteAcceptanceTest extends AcceptanceTest {

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final int AGE = 20;
    private String token;

    @BeforeEach
    public void setUp() {
        super.setUp();

        // given
        노선등록되어있음();
        token = 로그인_토큰_발급(EMAIL, PASSWORD, AGE);
    }

    @Test
    @DisplayName("즐겨찾기를 관리")
    void 즐겨찾기_관리() {
        // given
        Integer sourceId = 1;
        Integer targetId = 2;

        // when
        ExtractableResponse<Response> 즐겨찾기_추가_응답 = 즐겨찾기_생성_됨(token, sourceId, targetId);
        // then
        즐거찾기_추가_정상(즐겨찾기_추가_응답);

        // when 즐겨찾기 조회
        ExtractableResponse<Response> 즐겨찾기_조회_응답 = 로그인_회원_즐겨찾기_조회(token);
        // then
        즐겨찾기_조회_정상(즐겨찾기_조회_응답, sourceId, targetId);

        // when 즐겨찾기 삭제
        ExtractableResponse<Response> 즐겨찾기_삭제_응답 = 로그인_회원_즐겨찾기_삭제(즐겨찾기_조회_응답, token);
        즐겨찾기_삭제_정상(즐겨찾기_삭제_응답);
    }


}
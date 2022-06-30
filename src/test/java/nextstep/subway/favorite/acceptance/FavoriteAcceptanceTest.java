package nextstep.subway.favorite.acceptance;

import static nextstep.subway.auth.acceptance.AuthTestUtils.*;
import static nextstep.subway.favorite.acceptance.FavoriteTestUtils.*;
import static nextstep.subway.member.MemberTestUtils.*;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.line.acceptance.LineTestUtils;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.station.StationTestUtils;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    private StationResponse 강남역;
    private StationResponse 광교역;
    private LineResponse 신분당선;
    private TokenResponse 로그인_토큰;

    @BeforeEach
    void init() {
        강남역 = StationTestUtils.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        광교역 = StationTestUtils.지하철역_등록되어_있음("광교역").as(StationResponse.class);
        신분당선 = LineTestUtils.지하철_노선_생성_요청(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 10, 0)).as(LineResponse.class);

        회원_생성을_요청(MemberAcceptanceTest.EMAIL, MemberAcceptanceTest.PASSWORD, MemberAcceptanceTest.AGE);
        로그인_토큰 = 로그인_요청(MemberAcceptanceTest.EMAIL, MemberAcceptanceTest.PASSWORD).as(TokenResponse.class);
    }

    @DisplayName("즐겨찾기를 관리한다.")
    @Test
    void manageFavorite() {
        // 생성
        ExtractableResponse<Response> createReponse = 즐겨찾기_생성_요청(로그인_토큰, 강남역, 광교역);
        즐겨찾기_생성_성공(createReponse);

        // 조회
        ExtractableResponse<Response> findResponse = 즐겨찾기_목록_조회_요청(로그인_토큰);
        즐겨찾기_목록_조회_성공(findResponse);

        // 삭제
        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(로그인_토큰, createReponse);
        즐겨찾기_삭제_성공(deleteResponse);
    }

    @DisplayName("즐겨찾기 생성 실패 (같은 출발역과 도착역으로 생성)")
    @Test
    void createFavoriteFail_eqaulStation() {
        // when
        ExtractableResponse<Response> createReponse = 즐겨찾기_생성_요청(로그인_토큰, 강남역, 강남역);

        // then
        즐겨찾기_생성_실패(createReponse);
    }

    @DisplayName("즐겨찾기 생성 실패 (존재하지 않는 역으로 생성)")
    @Test
    void createFavoriteFail_notFoundStation() {
        // when
        ExtractableResponse<Response> createReponse = 즐겨찾기_생성_요청(로그인_토큰, new StationResponse(), new StationResponse());

        // then
        즐겨찾기_생성_실패(createReponse);
    }

    @DisplayName("즐겨찾기 삭제 실패 (작성자가 아닌 사용자로 로그인)")
    @Test
    void createFavoriteFail_notOwner() {
        // given
        ExtractableResponse<Response> createReponse = 즐겨찾기_생성_요청(로그인_토큰, 강남역, 광교역);

        // when
        ExtractableResponse<Response> deleteReponse = 즐겨찾기_삭제_요청(new TokenResponse("invalidToken"), createReponse);

        // then
        즐겨찾기_삭제_실패(deleteReponse);
    }
}

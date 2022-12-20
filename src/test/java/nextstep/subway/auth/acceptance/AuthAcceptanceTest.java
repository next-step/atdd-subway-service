package nextstep.subway.auth.acceptance;

import static nextstep.subway.favorite.FavoriteAcceptanceTestFixture.즐겨찾기_등록되어_있음;
import static nextstep.subway.favorite.FavoriteAcceptanceTestFixture.즐겨찾기_목록;
import static nextstep.subway.favorite.FavoriteAcceptanceTestFixture.즐겨찾기_삭제_요청;
import static nextstep.subway.favorite.FavoriteAcceptanceTestFixture.즐겨찾기_생성_요청;
import static nextstep.subway.favorite.FavoriteAcceptanceTestFixture.즐겨찾기_정보_조회_요청;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTestFixture.지하철_노선에_지하철역_등록되어_있음;
import static nextstep.subway.member.MemberAcceptanceTestFixture.나의_정보_삭제_실패;
import static nextstep.subway.member.MemberAcceptanceTestFixture.나의_정보_삭제_요청;
import static nextstep.subway.member.MemberAcceptanceTestFixture.나의_정보_수정_실패;
import static nextstep.subway.member.MemberAcceptanceTestFixture.나의_정보_수정_요청;
import static nextstep.subway.member.MemberAcceptanceTestFixture.나의_정보_조회_실패;
import static nextstep.subway.member.MemberAcceptanceTestFixture.나의_정보_조회_요청;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.favorite.dto.FavoriteCreateRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AuthAcceptanceTest extends AuthAcceptanceTestFixture {

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        ExtractableResponse<Response> response = 토큰_생성_요청(new TokenRequest(EMAIL, PASSWORD));
        토큰_생성됨(response);
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        String 잘못된_이메일 = EMAIL + "1111";
        String 잘못된_비밀번호 = PASSWORD + "1111";
        TokenRequest request1 = new TokenRequest(EMAIL, 잘못된_비밀번호);
        TokenRequest request2 = new TokenRequest(잘못된_이메일, PASSWORD);

        ExtractableResponse<Response> response1 = 토큰_생성_요청(request1);
        ExtractableResponse<Response> response2 = 토큰_생성_요청(request2);

        assertAll(
                () -> 토큰_생성_실패함(response1),
                () -> 토큰_생성_실패함(response2)
        );
    }

    /**
     * Given 회원 생성되어 있음
     *
     * When 유효하지 않은 토큰 사용하여 나의 정보 조회 요청하면
     * Then 나의 정보 조회에 실패한다
     *
     * When 유효하지 않은 토큰 사용하여 나의 정보 수정 요청하면
     * Then 나의 정보 수정에 실패한다
     *
     * When 유효하지 않은 토큰 사용하여 나의 정보 삭제 요청하면
     * Then 나의 정보 삭제에 실패한다
     */
    @DisplayName("Bearer Auth 유효하지 않은 토큰으로 나의 정보 관련 기능 요청")
    @Test
    void myInfoWithWrongBearerAuth() {
        String notValidToken = "@@@@@";

        // When 유효하지 않은 토큰 사용하여 나의 정보 조회 요청하면
        ExtractableResponse<Response> response1 = 나의_정보_조회_요청(notValidToken);
        // Then 나의 정보 조회에 실패한다
        나의_정보_조회_실패(response1);

        // When 유효하지 않은 토큰 사용하여 나의 정보 수정 요청하면
        ExtractableResponse<Response> response2 = 나의_정보_수정_요청(notValidToken,
                new MemberRequest(NEW_EMAIL, NEW_PASSWORD, NEW_AGE));
        // Then 나의 정보 수정에 실패한다
        나의_정보_수정_실패(response2);

        // When 유효하지 않은 토큰 사용하여 나의 정보 삭제 요청하면
        ExtractableResponse<Response> response3 = 나의_정보_삭제_요청(notValidToken);
        // Then 나의 정보 삭제에 실패한다
        나의_정보_삭제_실패(response3);
    }

    /**
     * Given 지하철역 등록되어 있음
     * And 지하철 노선 등록되어 있음
     * And 지하철 노선에 지하철역 등록되어 있음
     * And 로그인 되어있음
     * And 즐겨찾기 등록되어 있음
     *
     * When 유효하지 않은 토큰 사용하여 즐겨찾기 생성 요청하면
     * Then 즐겨찾기 생성에 실패한다
     *
     * When 유효하지 않은 토큰 사용하여 즐겨찾기 조회 요청하면
     * Then 즐겨찾기 조회에 실패한다
     *
     * When 유효하지 않은 토큰 사용하여 즐겨찾기 삭제 요청하면
     * Then 즐겨찾기 삭제에 실패한다
     */
    @DisplayName("Bearer Auth 유효하지 않은 토큰으로 즐겨찾기 관리 기능 요청")
    @Test
    void favoriteManageWithWrongBearerAuth() {
        String notValidToken = "@@@@@";

        // Given
        StationResponse 강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        StationResponse 광교역 = StationAcceptanceTest.지하철역_등록되어_있음("광교역").as(StationResponse.class);
        StationResponse 양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        StationResponse 정자역 = StationAcceptanceTest.지하철역_등록되어_있음("정자역").as(StationResponse.class);

        LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 15);
        LineResponse 신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);
        지하철_노선에_지하철역_등록되어_있음(신분당선, 강남역, 양재역, 4);
        지하철_노선에_지하철역_등록되어_있음(신분당선, 양재역, 정자역, 6);
        String myAccessToken = 토큰_값(로그인_되어_있음(new TokenRequest(EMAIL, PASSWORD)));
        즐겨찾기_등록되어_있음(myAccessToken, new FavoriteCreateRequest(광교역.getId(), 양재역.getId()));
        List<FavoriteResponse> 조회된_즐겨찾기_목록 = 즐겨찾기_목록(즐겨찾기_정보_조회_요청(myAccessToken));

        // When 유효하지 않은 토큰 사용하여 즐겨찾기 생성 요청하면
        FavoriteCreateRequest favoriteCreateRequest = new FavoriteCreateRequest(강남역.getId(), 정자역.getId());
        ExtractableResponse<Response> response = 즐겨찾기_생성_요청(notValidToken, favoriteCreateRequest);
        // Then 즐겨찾기 생성에 실패한다
        토큰_인증_실패함(response);

        // When 유효하지 않은 토큰 사용하여 즐겨찾기 조회 요청하면
        response = 즐겨찾기_정보_조회_요청(notValidToken);
        // Then 즐겨찾기 조회에 실패한다
        토큰_인증_실패함(response);

        // When 유효하지 않은 토큰 사용하여 즐겨찾기 삭제 요청하면
        response = 즐겨찾기_삭제_요청(notValidToken, 조회된_즐겨찾기_목록.get(0).getId());
        // Then 즐겨찾기 삭제에 실패한다
        토큰_인증_실패함(response);
    }
}

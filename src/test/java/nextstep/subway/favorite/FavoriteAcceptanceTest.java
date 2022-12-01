package nextstep.subway.favorite;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceIntegrationTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.MembersMeAcceptanceStep;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.auth.application.AuthServiceTest.AGE;
import static nextstep.subway.auth.application.AuthServiceTest.EMAIL;
import static nextstep.subway.auth.application.AuthServiceTest.PASSWORD;
import static nextstep.subway.favorite.FavoriteAcceptanceStep.즐겨찾기_목록_조회_요청;
import static nextstep.subway.favorite.FavoriteAcceptanceStep.즐겨찾기_목록_조회됨;
import static nextstep.subway.favorite.FavoriteAcceptanceStep.즐겨찾기_삭제_요청;
import static nextstep.subway.favorite.FavoriteAcceptanceStep.즐겨찾기_삭제됨;
import static nextstep.subway.favorite.FavoriteAcceptanceStep.즐겨찾기_생성_요청;
import static nextstep.subway.favorite.FavoriteAcceptanceStep.즐겨찾기_생성됨;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록됨;
import static nextstep.subway.member.MemberAcceptanceStep.회원_생성됨;
import static nextstep.subway.member.MemberAcceptanceStep.회원_생성을_요청;

@DisplayName("즐겨찾기 관련 기능")
class FavoriteAcceptanceTest extends AcceptanceTest {

    private StationResponse 판교역;
    private StationResponse 강남역;
    private LineResponse 신분당선;
    private StationResponse 양재역;
    private TokenResponse 로그인_토큰;

    /**
     * Feature: 즐겨찾기 관리 기능
     *  Background
     *    Given 지하철역 등록되어 있음
     *    And 지하철 노선 등록되어 있음
     *    And 지하철 노선에 지하철역 등록되어 있음
     *    And 회원 등록되어 있음
     *    And 로그인 되어있음
     *  Scenario: 즐겨찾기를 관리
     *    When 즐겨찾기 생성을 요청
     *    Then 즐겨찾기 생성됨
     *    When 즐겨찾기 목록 조회 요청
     *    Then 즐겨찾기 목록 조회됨
     *    When 즐겨찾기 삭제 요청
     *    Then 즐겨찾기 삭제됨
     */
    @DisplayName("즐겨찾기를 관리한다")
    @Test
    void manageMyFavorites() {
        지하철역_등록되어_있음();
        지하철_노선_등록되어_있음();
        지하철_노선에_지하철역_등록되어_있음();

        회원_등록되어_있음();
        로그인_되어있음();

        ExtractableResponse<Response> 생성_응답 = 즐겨찾기_생성_요청(로그인_토큰, 양재역, 강남역);
        즐겨찾기_생성됨(생성_응답);

        ExtractableResponse<Response> 조회_응답 = 즐겨찾기_목록_조회_요청(로그인_토큰);
        즐겨찾기_목록_조회됨(조회_응답, 양재역, 강남역);

        즐겨찾기_삭제_요청();
        즐겨찾기_삭제됨();
    }

    private void 지하철_노선에_지하철역_등록되어_있음() {
        ExtractableResponse<Response> response = 지하철_노선에_지하철역_등록_요청(신분당선, 판교역, 양재역, 5);
        지하철_노선에_지하철역_등록됨(response);
    }

    private void 지하철역_등록되어_있음() {
        판교역 = LineSectionAcceptanceIntegrationTest.지하철역_등록되어_있음("판교역");
        양재역 = LineSectionAcceptanceIntegrationTest.지하철역_등록되어_있음("양재역");
        강남역 = LineSectionAcceptanceIntegrationTest.지하철역_등록되어_있음("강남역");
    }

    private void 지하철_노선_등록되어_있음() {
        LineRequest lineRequest = new LineRequest("신분당선", "color", 판교역.getId(), 강남역.getId(), 10);
        ExtractableResponse<Response> response = LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest);
        신분당선 = response.body().as(LineResponse.class);
    }

    private void 로그인_되어있음() {
        로그인_토큰 = MembersMeAcceptanceStep.로그인됨(EMAIL, PASSWORD, AGE);
    }

    private void 회원_등록되어_있음() {
        ExtractableResponse<Response> 생성요청 = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        회원_생성됨(생성요청);
    }
}
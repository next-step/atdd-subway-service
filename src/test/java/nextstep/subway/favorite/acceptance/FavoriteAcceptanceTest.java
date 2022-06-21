package nextstep.subway.favorite.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.auth.acceptance.AuthAcceptanceMethod.로그인_요청;
import static nextstep.subway.favorite.acceptance.FavoriteAcceptanceMethod.*;
import static nextstep.subway.line.acceptance.LineAcceptanceMethod.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceMethod.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.member.MemberAcceptanceMethod.회원_생성을_요청;
import static nextstep.subway.member.MemberAcceptanceTest.*;
import static nextstep.subway.station.StationAcceptanceMethod.지하철역_등록되어_있음;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private TokenResponse 토큰;

    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10)).as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-green-600", 교대역.getId(), 강남역.getId(), 10)).as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-orange-600", 교대역.getId(), 양재역.getId(), 5)).as(LineResponse.class);

        지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);

        회원_생성을_요청(EMAIL, PASSWORD, AGE);
        토큰 = 로그인_요청(new TokenRequest(EMAIL, PASSWORD)).as(TokenResponse.class);
    }

    /**
     * Scenario: 즐겨찾기 생성
     *  When 즐겨찾기 생성을 요청하면
     *  Then 즐겨찾기가 생성됨
     * Scenario: 즐겨찾기 조회
     *  When 즐겨찾기 목록 조회를 요청하면
     *  Then 즐겨찾기 목록이 조회됨
     * Scenario: 즐겨찾기 삭제
     *  When 즐겨찾기 삭제를 요청하면
     *  Then 즐겨찾기가 삭제됨
     */
    @DisplayName("즐겨찾기를 관리한다.")
    @Test
    void manageFavorite() {
        // Scenario: 즐겨찾기 생성
        // when
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(토큰, 강남역.getId(), 교대역.getId());
        // then
        즐겨찾기_생성됨(createResponse);

        // Scenario: 즐겨찾기 조회
        // when
        ExtractableResponse<Response> findResponse = 즐겨찾기_목록_조회_요청(토큰);
        // then
        즐겨찾기_목록_조회됨(findResponse, 강남역, 교대역);

        // Scenario: 즐겨찾기 삭제
        // when
        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(토큰, createResponse);
        // then
        즐겨찾기_삭제됨(deleteResponse);
    }

    /**
     * Given 즐겨찾기를 생성하고
     * And 다른 사용자의 토큰으로
     * When 즐겨찾기 삭제를 요청하면
     * Then 즐겨찾기를 삭제할 수 없음
     */
    @DisplayName("다른 사용자가 등록한 즐겨찾기는 삭제할 수 없다.")
    @Test
    void delete_other_user_favorite() {
        // given
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(토큰, 강남역.getId(), 교대역.getId());
        회원_생성을_요청(NEW_EMAIL, NEW_PASSWORD, NEW_AGE);
        TokenResponse 다른_사용자_토큰 = 로그인_요청(new TokenRequest(NEW_EMAIL, NEW_PASSWORD)).as(TokenResponse.class);

        // when
        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(다른_사용자_토큰, createResponse);

        // then
        즐겨찾기_삭제_실패됨(deleteResponse);
    }

    /**
     * Given 등록되지 않은 지하철역으로
     * When 즐겨찾기 생성을 요청하면
     * Then 즐겨찾기를 생성할 수 없음
     */
    @DisplayName("등록되지 않은 지하철역을 즐겨찾기로 추가할 수 없다.")
    @Test
    void create_not_exist_station_favorite() {
        // given
        Long 동대구역_ID = 0L;

        // when
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(토큰, 강남역.getId(), 동대구역_ID);

        // then
        즐겨찾기_생성_실패됨(createResponse);
    }
}
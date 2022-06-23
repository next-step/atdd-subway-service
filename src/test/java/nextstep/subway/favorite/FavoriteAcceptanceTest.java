package nextstep.subway.favorite;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_되어_있음;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_요청;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.member.MemberAcceptanceTest.AGE;
import static nextstep.subway.member.MemberAcceptanceTest.EMAIL;
import static nextstep.subway.member.MemberAcceptanceTest.PASSWORD;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;
import static nextstep.subway.utils.RestAssuredRequest.deleteWithOAuth;
import static nextstep.subway.utils.RestAssuredRequest.getWithOAuth;
import static nextstep.subway.utils.RestAssuredRequest.postWithOAuth;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import nextstep.subway.utils.RestAssuredRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private LineResponse 공항철도선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 개화역;
    private StationResponse 김포공항역;
    private String token;

    /**
     *   개화역         교대역     --- *2호선* ---     강남역
     *     |             |                            |
     * *김포공항선*     *3호선*                     *신분당선*
     *     |             |                            |
     *  김포공항역     남부터미널역  --- *3호선* ---     양재
     */

    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        개화역 = StationAcceptanceTest.지하철역_등록되어_있음("개화역").as(StationResponse.class);
        김포공항역 = StationAcceptanceTest.지하철역_등록되어_있음("김포공항역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10)).as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-green-600", 교대역.getId(), 강남역.getId(), 10)).as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-orange-600", 교대역.getId(), 양재역.getId(), 5)).as(LineResponse.class);
        공항철도선 = 지하철_노선_등록되어_있음(new LineRequest("육호선", "bg-sky-600", 개화역.getId(), 김포공항역.getId(), 5)).as(LineResponse.class);
        지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);

        회원_생성을_요청(EMAIL, PASSWORD, AGE);
        token = 로그인_되어_있음(EMAIL, PASSWORD);
    }

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
    @DisplayName("즐겨찾기를 관리한다.")
    @Test
    void manageFavorite() {
        // when
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(token, 강남역, 남부터미널역);
        // then
        즐겨찾기_생성됨(createResponse);

        // when
        ExtractableResponse<Response> findResponse = 즐겨찾기_목록_조회_요청(token);
        // then
        즐겨찾기_목록_응답됨(findResponse);
        즐겨찾기_목록_포함됨(findResponse, Arrays.asList(createResponse));

        // when
        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(token, createResponse);
        // then
        즐겨찾기_삭제됨(deleteResponse);
    }

    private ExtractableResponse<Response> 즐겨찾기_생성_요청(String token, StationResponse source, StationResponse target) {
        FavoriteRequest favoriteRequest = new FavoriteRequest(source.getId(), target.getId());
        return postWithOAuth("/favorites", favoriteRequest, token);
    }

    private void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(String token) {
        return getWithOAuth("/favorites", token);
    }

    private void 즐겨찾기_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 즐겨찾기_목록_포함됨(ExtractableResponse<Response> response, List<ExtractableResponse<Response>> createdResponses) {
        List<Long> expectedFavoriteIds = createdResponses.stream()
            .map(it -> Long.parseLong(it.header("Location").split("/")[2]))
            .collect(Collectors.toList());

        List<Long> resultFavoriteIds = response.jsonPath().getList(".", FavoriteResponse.class).stream()
            .map(FavoriteResponse::getId)
            .collect(Collectors.toList());

        assertThat(resultFavoriteIds).containsAll(expectedFavoriteIds);
    }

    private ExtractableResponse<Response> 즐겨찾기_삭제_요청(String token, ExtractableResponse<Response> createResponse) {
        String uri = createResponse.header("Location");
        return deleteWithOAuth(uri, token);
    }

    private void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}

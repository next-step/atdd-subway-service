package nextstep.subway.favorite;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_되어_있음;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_요청;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.member.MemberAcceptanceTest.내정보_삭제_요청;
import static nextstep.subway.member.MemberAcceptanceTest.내정보_삭제됨;
import static nextstep.subway.member.MemberAcceptanceTest.내정보_조회_요청;
import static nextstep.subway.member.MemberAcceptanceTest.내정보_조회됨;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    final String EMAIL = "email@email.com";
    final String PASSWORD = "password";
    final int AGE = 10;

    String accessToken;

    StationResponse 교대역;
    StationResponse 강남역;
    StationResponse 양재역;
    StationResponse 남부터미널역;
    StationResponse 서초역;
    StationResponse 양재시민의숲;

    LineResponse 신분당선;
    LineResponse 이호선;
    LineResponse 삼호선;

    /**
     * 서초역  --- *2호선* 5--- 교대역  --- *2호선* 10 -----  강남역
     *                        |                          |
     *                      *3호선* 3                 *신분당선* 10
     *                        |                          |
     *                      남부터미널역  --- *3호선* 2 --- 양재
     *                                                  |
     *                                               *신분당선* 4
     *                                                  |
     *                                               양재시민의숲
     */
    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        // Given: 지하철 역 등록되어 있음
        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        서초역 = 지하철역_등록되어_있음("서초역").as(StationResponse.class);
        양재시민의숲 = 지하철역_등록되어_있음("양재시민의숲").as(StationResponse.class);

        // And: 지하철 노선 등록되어 있음
        // And: 지하철 노선에 지하철 역 등록되어 있음
        //신분당선 (강남-양재-양재시민의숲, 10-4)
        LineRequest lineRequest_신분당선 = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10, 900);
        신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest_신분당선).as(LineResponse.class);
        지하철_노선에_지하철역_등록_요청(신분당선, 양재역, 양재시민의숲, 4);

        //2호선 (서초-교대-강남, 5-10)
        LineRequest lineRequest_이호선 = new LineRequest("이호선", "green", 교대역.getId(), 강남역.getId(), 10, 0);
        이호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest_이호선).as(LineResponse.class);
        지하철_노선에_지하철역_등록_요청(이호선, 서초역, 교대역, 5);

        //3호선 (교대-남부터미널-양재, 3-2)
        LineRequest lineRequest_삼호선 = new LineRequest("삼호선", "orange", 교대역.getId(), 남부터미널역.getId(), 3, 200);
        삼호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest_삼호선).as(LineResponse.class);
        지하철_노선에_지하철역_등록_요청(삼호선, 남부터미널역, 양재역, 2);

        // And: 회원 등록되어 있음
        회원_생성을_요청(EMAIL, PASSWORD, AGE);
        // And: 로그인 되어 있음
        accessToken = 로그인_되어_있음(EMAIL, PASSWORD);
    }

    @Test
    @DisplayName("즐겨찾기를 관리한다.")
    void manageFavorite() {
        // When: 즐겨 찾기 생성을 요청 서초역_강남역
        ExtractableResponse<Response> 서초역_강남역 = 즐겨찾기_생성_요청(accessToken, 서초역, 강남역);
        // Then: 즐겨 찾기 생성됨
        즐겨찾기_생성됨(서초역_강남역);

        // When: 즐겨 찾기 생성을 요청 교대역_양재시민의숲
        ExtractableResponse<Response> 교대역_양재시민의숲 = 즐겨찾기_생성_요청(accessToken, 교대역, 양재시민의숲);
        // Then: 즐겨 찾기 생성됨
        즐겨찾기_생성됨(교대역_양재시민의숲);

        // When: 즐겨 찾기 목록 조회 요청
        ExtractableResponse<Response> findResponse = 즐겨찾기_목록조회_요청(accessToken);
        // Then: 즐겨 찾기 목록 조회됨
        즐겨찾기_목록조회됨(findResponse, Arrays.asList(서초역_강남역, 교대역_양재시민의숲));

        // When: 즐겨 찾기 삭제 요청
        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(accessToken, 교대역_양재시민의숲);
        // Then: 즐겨 찾기 삭제됨
        즐겨찾기_삭제됨(deleteResponse);

        // When: 즐겨 찾기 목록 조회 요청
        findResponse = 즐겨찾기_목록조회_요청(accessToken);
        // Then: 즐겨 찾기 목록 조회됨
        즐겨찾기_목록조회됨(findResponse, Arrays.asList(서초역_강남역));

    }

    public static ExtractableResponse<Response> 즐겨찾기_생성_요청(String accessToken, StationResponse source, StationResponse target) {
        FavoriteRequest request = new FavoriteRequest(source.getId(), target.getId());
        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .auth().oauth2(accessToken)
            .body(request)
            .when()
            .post("/favorites")
            .then().log().all()
            .extract();
    }

    public static void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static ExtractableResponse<Response> 즐겨찾기_목록조회_요청(String accessToken) {
        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .auth().oauth2(accessToken)
            .when()
            .get("/favorites")
            .then().log().all()
            .extract();
    }

    public static void 즐겨찾기_목록조회됨(ExtractableResponse<Response> response, List<ExtractableResponse<Response>> expected) {
        List<String> responseNames = getFavoriteResponseNames(response);
        List<String> expectedNames = getExpectedNames(expected);

        assertAll(() -> {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(responseNames).containsExactlyElementsOf(expectedNames);
        });
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(String accessToken, ExtractableResponse<Response> response) {
        String uri = response.header("Location");
        return RestAssured
            .given().log().all()
            .auth().oauth2(accessToken)
            .when().delete(uri)
            .then().log().all()
            .extract();
    }

    public static void 즐겨찾기_삭제됨(ExtractableResponse<Response> response){
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private static List<String> getFavoriteResponseNames(ExtractableResponse<Response> response) {
        return response.jsonPath().getList(".", FavoriteResponse.class)
            .stream()
            .map(it -> it.getSource().getName() + "-" + it.getTarget().getName())
            .collect(Collectors.toList());
    }

    private static List<String> getExpectedNames(List<ExtractableResponse<Response>> expected) {
        return expected.stream()
            .map(it -> it.as(FavoriteResponse.class))
            .map(it -> it.getSource().getName() + "-" + it.getTarget().getName())
            .collect(Collectors.toList());
    }
}
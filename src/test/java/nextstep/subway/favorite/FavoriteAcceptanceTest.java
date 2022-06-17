package nextstep.subway.favorite;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 정자역;
    private StationResponse 광교역;
    private String accessToken;

    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        정자역 = StationAcceptanceTest.지하철역_등록되어_있음("정자역").as(StationResponse.class);
        광교역 = StationAcceptanceTest.지하철역_등록되어_있음("광교역").as(StationResponse.class);

        LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 10);
        신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 양재역, 2);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(신분당선, 정자역, 강남역, 5);

        ExtractableResponse<Response> response = LineAcceptanceTest.지하철_노선_조회_요청(신분당선);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록됨(response);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_순서_정렬됨(response, Arrays.asList(정자역, 강남역, 양재역, 광교역));

        ExtractableResponse<Response> createResponse = MemberAcceptanceTest.회원_생성을_요청(EMAIL, PASSWORD, AGE);
        MemberAcceptanceTest.회원_생성됨(createResponse);

        ExtractableResponse<Response> loginResponse = AuthAcceptanceTest.로그인_요청(EMAIL, PASSWORD);
        AuthAcceptanceTest.로그인_성공(loginResponse);
        accessToken = loginResponse.as(TokenResponse.class).getAccessToken();
    }

    /**
     * Background
     * Given 지하철역 등록되어 있음
     * And 지하철 노선 등록되어 있음
     * And 지하철 노선에 지하철역 등록되어 있음
     * And 회원 등록되어 있음
     * And 로그인 되어있음
     * <p>
     * Scenario: 즐겨찾기를 관리
     * When 즐겨찾기 생성을 요청
     * Then 즐겨찾기 생성됨
     * When 즐겨찾기 목록 조회 요청
     * Then 즐겨찾기 목록 조회됨
     * When 즐겨찾기 삭제 요청
     * Then 즐겨찾기 삭제됨
     **/
    @DisplayName("즐겨찾기 관련 기능을 확인한다")
    @Test
    void integration_scenario1() {
        //when
        ExtractableResponse<Response> addFavoriteResponse =
                즐겨찾기_생성_요청(accessToken, new FavoriteRequest(강남역.getId(), 양재역.getId()));
        //then
        즐겨찾기_생성_됨(addFavoriteResponse);

        //when
        ExtractableResponse<Response> listFavoriteResponse = 즐겨찾기_목록_조회_요청(accessToken);
        //then
        즐겨찾기_목록_응답됨(listFavoriteResponse);

        // when
        ExtractableResponse<Response> removeResponse = 즐겨찾기_삭제_요청(accessToken,
                listFavoriteResponse.jsonPath().getList("id", Long.class).stream().findFirst().get());
        //then
        즐겨찾기_삭제_됨(removeResponse);
    }

    @Test
    void 즐겨찾기는_중복_추가가_불가능하다() {
        //when
        ExtractableResponse<Response> addFavoriteResponse =
                즐겨찾기_생성_요청(accessToken, new FavoriteRequest(강남역.getId(), 양재역.getId()));
        //then
        즐겨찾기_생성_됨(addFavoriteResponse);

        //when
        ExtractableResponse<Response> addFavoriteResponse2 =
                즐겨찾기_생성_요청(accessToken, new FavoriteRequest(강남역.getId(), 양재역.getId()));
        //then
        즐겨찾기_생성_실패됨(addFavoriteResponse2);

    }

    public static ExtractableResponse<Response> 즐겨찾기_생성_요청(String accessToken, FavoriteRequest favoriteRequest) {
        return RestAssured.given().log().all().auth().oauth2(accessToken).contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(favoriteRequest).when().post("/favorites").then().log().all().extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(String accessToken) {
        return RestAssured.given().log().all().auth().oauth2(accessToken).when().get("/favorites").then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(String accessToken, long id) {
        return RestAssured.given().log().all().auth().oauth2(accessToken).when().delete("/favorites/{id}", id).then()
                .log().all().extract();
    }

    public static void 즐겨찾기_생성_됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 즐겨찾기_삭제_됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static void 즐겨찾기_생성_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public static void 즐겨찾기_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}

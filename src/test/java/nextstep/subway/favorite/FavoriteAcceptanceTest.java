package nextstep.subway.favorite;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_요청;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.member.MemberAcceptanceTest.AGE;
import static nextstep.subway.member.MemberAcceptanceTest.EMAIL;
import static nextstep.subway.member.MemberAcceptanceTest.NEW_EMAIL;
import static nextstep.subway.member.MemberAcceptanceTest.PASSWORD;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private LineResponse 신분당선;
    private StationResponse 강남역;
    private StationResponse 광교역;
    private StationResponse 양재역;
    private StationResponse 정자역;
    private TokenResponse 사용자;
    private TokenResponse 사용자2;

    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        광교역 = StationAcceptanceTest.지하철역_등록되어_있음("광교역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        정자역 = StationAcceptanceTest.지하철역_등록되어_있음("정자역").as(StationResponse.class);

        LineRequest lineRequest1 = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(),
            10);
        신분당선 = 지하철_노선_등록되어_있음(lineRequest1).as(LineResponse.class);

        지하철_노선에_지하철역_등록_요청(신분당선, 광교역, 양재역, 3);
        지하철_노선에_지하철역_등록_요청(신분당선, 양재역, 정자역, 3);

        회원_생성을_요청(EMAIL, PASSWORD, AGE);
        사용자 = 로그인_요청(EMAIL, PASSWORD).as(TokenResponse.class);

        회원_생성을_요청(NEW_EMAIL, PASSWORD, AGE);
        사용자2 = 로그인_요청(NEW_EMAIL, PASSWORD).as(TokenResponse.class);
    }

    @Test
    void 즐겹찾기_생성() {
        // when
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성을_요청(사용자, 강남역, 양재역);

        // then
        즐겨찾기_생성됨(createResponse);
    }

    @Test
    void 즐겹찾기_목록조회() {
        // given
        즐겨찾기_생성을_요청(사용자, 강남역, 양재역);
        즐겨찾기_생성을_요청(사용자, 강남역, 정자역);
        즐겨찾기_생성을_요청(사용자2, 광교역, 정자역);

        // when
        ExtractableResponse<Response> response = 즐겹찾기_목록_조회(사용자);

        // then
        즐겨찾기_목록_확인(response);
    }

    @Test
    void 즐겹찾기_삭제() {
        // given
        ExtractableResponse<Response> createResponse1 = 즐겨찾기_생성을_요청(사용자, 강남역, 양재역);
        ExtractableResponse<Response> createResponse2 = 즐겨찾기_생성을_요청(사용자, 광교역, 양재역);
        long id = createResponse1.jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제(사용자, id);

        // then
        즐겨찾기_삭제됨(deleteResponse);
    }

    @Test
    void 본인이_즐겨찾기_하지않은_경우_삭제_불가() {
        // given
        ExtractableResponse<Response> createResponse1 = 즐겨찾기_생성을_요청(사용자, 강남역, 양재역);
        long id = createResponse1.jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제(사용자2, id);

        // then
        즐겨찾기_삭제실패(deleteResponse);
    }

    public static ExtractableResponse<Response> 즐겨찾기_생성을_요청(TokenResponse user,
        StationResponse startStation, StationResponse endStation) {
        return RestAssured
            .given().log().all()
            .auth().oauth2(user.getAccessToken())
            .accept(APPLICATION_JSON_VALUE)
            .contentType(APPLICATION_JSON_VALUE)
            .body(FavoriteRequest.of(startStation.getId(), endStation.getId()))
            .when().post("/favorites")
            .then().log().all()
            .extract();
    }

    private void 즐겨찾기_생성됨(ExtractableResponse<Response> createResponse) {
        assertThat(createResponse.statusCode()).isEqualTo(CREATED.value());
    }

    public static ExtractableResponse<Response> 즐겹찾기_목록_조회(TokenResponse user) {
        return RestAssured
            .given().log().all()
            .auth().oauth2(user.getAccessToken())
            .accept(APPLICATION_JSON_VALUE)
            .when().get("/favorites")
            .then().log().all()
            .extract();
    }

    public static void 즐겨찾기_목록_확인(ExtractableResponse<Response> response) {
        List<String> source = response.jsonPath().getList("source.name");
        List<String> target = response.jsonPath().getList("target.name");
        assertThat(source).containsExactly("강남역","강남역");
        assertThat(target).containsExactly("양재역","정자역");
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제(TokenResponse tokenResponse, Long id) {
        return RestAssured
            .given().log().all()
            .auth().oauth2(tokenResponse.getAccessToken())
            .pathParams("id", id)
            .accept(APPLICATION_JSON_VALUE)
            .when().delete("/favorites/{id}")
            .then().log().all()
            .extract();
    }

    public static void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(NO_CONTENT.value());
    }

    public static void 즐겨찾기_삭제실패(ExtractableResponse<Response> deleteResponse) {
        assertThat(deleteResponse.statusCode()).isEqualTo(UNAUTHORIZED.value());
    }
}
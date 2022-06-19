package nextstep.subway.favorite;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.stream.Stream;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_되어_있음;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록되어_있음;
import static nextstep.subway.member.MemberAcceptanceTest.*;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 정자역;

    private TokenResponse 사용자;
    private ExtractableResponse<Response> 즐겨찾기_생성_응답;

    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        정자역 = 지하철역_등록되어_있음("정자역").as(StationResponse.class);

        LineResponse 신분당선 = 지하철_노선_등록되어_있음("신분당선", "red", 강남역, 양재역, 10);
        지하철_노선에_지하철역_등록되어_있음(신분당선, 양재역, 정자역, 10);

        회원_생성을_요청(EMAIL, PASSWORD, AGE);
        사용자 = 로그인_되어_있음(EMAIL, PASSWORD);

        즐겨찾기_생성_응답 = 즐겨찾기_생성을_요청(사용자, 강남역, 정자역);
    }

    @TestFactory
    Stream<DynamicTest> 즐겨찾기_관련_기능_시나리오() {
        return Stream.of(
                dynamicTest("즐겨찾기를 생성한다.", this::즐겨찾기를_생성한다),
                dynamicTest("즐겨찾기 목록을 조회한다.", this::즐겨찾기_목록을_조회한다),
                dynamicTest("즐겨찾기를 삭제한다.", this::즐겨찾기를_삭제한다)
        );
    }

    private void 즐겨찾기를_생성한다() {
        // then
        즐겨찾기_생성됨(즐겨찾기_생성_응답);
    }

    private void 즐겨찾기_목록을_조회한다() {
        // when
        ExtractableResponse<Response> 즐겨찾기_목록_조회_응답 = 즐겨찾기_목록_조회_요청(사용자);

        // then
        즐겨찾기_목록_조회됨(즐겨찾기_목록_조회_응답);
    }

    private void 즐겨찾기를_삭제한다() {
        // when
        ExtractableResponse<Response> 즐겨찾기_삭제_응답 = 즐겨찾기_삭제_요청(사용자, 즐겨찾기_생성_응답);

        // then
        즐겨찾기_삭제됨(즐겨찾기_삭제_응답);
    }

    public static ExtractableResponse<Response> 즐겨찾기_생성을_요청(TokenResponse tokenResponse, StationResponse source, StationResponse target) {
        FavoriteRequest request = new FavoriteRequest(source.getId(), target.getId());

        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }

    public static void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(TokenResponse tokenResponse) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/favorites")
                .then().log().all()
                .extract();
    }

    public static void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(TokenResponse tokenResponse, ExtractableResponse<Response> createResponse) {
        String uri = createResponse.header("Location");

        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().delete(uri)
                .then().log().all()
                .extract();
    }

    public static void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}

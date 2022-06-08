package nextstep.subway.favorite;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("즐겨찾기 관련 인수테스트")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final int AGE = 15;

    private TokenResponse 사용자;

    private StationResponse 사당역;
    private StationResponse 교대역;
    private StationResponse 강남역;
    private StationResponse 삼성역;
    private StationResponse 잠실역;
    private LineResponse 이호선;

    @BeforeEach
    public void setUp() {
        super.setUp();

        //  given
        사당역 = StationAcceptanceTest.지하철역_등록되어_있음("사당역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        삼성역 = StationAcceptanceTest.지하철역_등록되어_있음("삼성역").as(StationResponse.class);
        잠실역 = StationAcceptanceTest.지하철역_등록되어_있음("잠실역").as(StationResponse.class);

        이호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-green-600", 강남역.getId(), 교대역.getId(), 10)).as(LineResponse.class);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_되어_있음(이호선, 사당역, 강남역, 5);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_되어_있음(이호선, 교대역, 삼성역, 10);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_되어_있음(이호선, 삼성역, 잠실역, 15);
        
        MemberAcceptanceTest.회원_등록_되어있음(EMAIL, PASSWORD, AGE);
        사용자 = AuthAcceptanceTest.로그인_되어있음(new TokenRequest(EMAIL, PASSWORD));
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
    @TestFactory
    Stream<DynamicTest> managefavorite(){

        return Stream.of(
            DynamicTest.dynamicTest("즐겨찾기를 생성한다.",()->{

                //when
                ExtractableResponse<Response> response = 즐겨찾기_생성_요청(사용자, 사당역, 잠실역);

                //then
                즐겨찾기_생성됨(response);

            }),

            DynamicTest.dynamicTest("즐겨찾기 목록을 조회한다.",()->{

                //when
                ExtractableResponse<Response> response = 즐겨찾기_목록_조회_요청(사용자);

                //then
                즐겨찾기_목록_조회됨(response);

            }),

            DynamicTest.dynamicTest("즐겨찾기를 삭제한다.",()->{
                //when
                ExtractableResponse<Response> response = 즐겨찾기_삭제_요청(사용자, 1L);

                //then
                즐겨찾기_목록_삭제됨(response);
            })
        );
    }

    public static ExtractableResponse<Response> 즐겨찾기_생성_요청(TokenResponse tokenResponse, StationResponse source, StationResponse target){
        Map<String, Object> params = new HashMap<>();
        params.put("source",source.getId());
        params.put("target",target.getId());

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(tokenResponse.getAccessToken())
                .body(params)
                .when().post("/favorites")
                .then().log().all()
                .extract();
    } 

    public static ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(TokenResponse tokenResponse){

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(tokenResponse.getAccessToken())
                .when().get("/favorites")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(TokenResponse tokenResponse, long favoriteId){
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(tokenResponse.getAccessToken())
                .when().delete("/favorites/{id}", favoriteId)
                .then().log().all()
                .extract();
    }

    private void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> response) {
        assertAll(
                ()-> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                ()-> assertThat(response.jsonPath().getString("source.name")).isEqualTo("[사당역]"),
                ()-> assertThat(response.jsonPath().getString("target.name")).isEqualTo("[잠실역]")
        );
    }

    private void 즐겨찾기_목록_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

}

package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.path.domain.AgeFarePolicy;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.exception.PathException;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.회원_로그인_성공확인;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.회원_로그인을_시도한다;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 경로 조회 인수테스트")
public class PathAcceptanceTest extends AcceptanceTest {
    public static int CHILDREN_AGE = 10;
    public static int FREE_AGE = 3;
    public static int TEENAGER_AGE = 15;
    public static int ALL_AGE = 30;

    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private LineResponse 칠호선;
    private StationResponse 청담역;
    private StationResponse 강남구청역;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 뚝섬유원지역;

    static TokenResponse loginToken;

    /**
     * 교대역    --- *2호선 (10)* ---   강남역
     * |                                |
     * *3호선 (3)*                 *신분당선 (10)*
     * |                                |
     * 남부터미널역  --- *3호선 (10)* --- 양재
     * 
     * 강남구청역 --- 7호선 (5) --- 청담역
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        뚝섬유원지역 = StationAcceptanceTest.지하철역_등록되어_있음("뚝섬유원지역").as(StationResponse.class);
        청담역 = StationAcceptanceTest.지하철역_등록되어_있음("청담역").as(StationResponse.class);
        강남구청역 = StationAcceptanceTest.지하철역_등록되어_있음("강남구청역").as(StationResponse.class);

        LineRequest 신분당선_request = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10, 500);
        LineRequest 이호선_request = new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10, 300);
        LineRequest 삼호선_request = new LineRequest("삼호선", "bg-red-600", 남부터미널역.getId(), 양재역.getId(), 10, 700);
        LineRequest 칠호선_request = new LineRequest("칠호선", "bg-red-600", 강남구청역.getId(), 청담역.getId(), 5, 100);

        신분당선 = 지하철_노선_등록되어_있음(신분당선_request).as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음(이호선_request).as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(삼호선_request).as(LineResponse.class);
        칠호선 = 지하철_노선_등록되어_있음(칠호선_request).as(LineResponse.class);
        지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);
    }

    @Test
    @DisplayName("Distance기준 최단 거리 구간 조회 (HappyPath)")
    void 교대역에서_양재역_최단_구간은_교대역_남부터미널역_양재역_청소년금액정책() {
        loginToken = 나이별_회원_생성요청(TEENAGER_AGE);

        PathRequest pathRequest = new PathRequest(교대역.getId(), 양재역.getId());
        ExtractableResponse<Response> response = 최단구간을_조회한다(pathRequest);

        최단구간이_정상적으로_조회됨(response);
        int fare = 1_250 + 200 + 삼호선.getSurcharge(); //기본요금 + 거리 추가요금 + 노선 추가요금
        최단거리_조회_결과_확인(response, Arrays.asList(교대역, 남부터미널역, 양재역), 13, AgeFarePolicy.TEENAGER.getOperator().apply(fare).intValue());
    }

    @Test
    @DisplayName("Distance기준 최단 거리 구간 조회 (HappyPath)")
    void 교대역에서_양재역_최단_구간은_교대역_남부터미널역_양재역_아동금액정책() {
        loginToken = 나이별_회원_생성요청(CHILDREN_AGE);

        PathRequest pathRequest = new PathRequest(교대역.getId(), 양재역.getId());
        ExtractableResponse<Response> response = 최단구간을_조회한다(pathRequest);

        최단구간이_정상적으로_조회됨(response);
        int fare = 1_250 + 200 + 삼호선.getSurcharge(); //기본요금 + 거리 추가요금 + 노선 추가요금
        최단거리_조회_결과_확인(response, Arrays.asList(교대역, 남부터미널역, 양재역), 13, AgeFarePolicy.CHILDREN.getOperator().apply(fare).intValue());
    }

    @Test
    @DisplayName("Distance기준 최단 거리 구간 조회 (HappyPath)")
    void 교대역에서_양재역_최단_구간은_교대역_남부터미널역_양재역_무료금액정책() {
        loginToken = 나이별_회원_생성요청(FREE_AGE);

        PathRequest pathRequest = new PathRequest(교대역.getId(), 양재역.getId());
        ExtractableResponse<Response> response = 최단구간을_조회한다(pathRequest);

        최단구간이_정상적으로_조회됨(response);
        int fare = 1_250 + 200 + 삼호선.getSurcharge(); //기본요금 + 거리 추가요금 + 노선 추가요금
        최단거리_조회_결과_확인(response, Arrays.asList(교대역, 남부터미널역, 양재역), 13, AgeFarePolicy.FREE.getOperator().apply(fare).intValue());
    }

    @Test
    @DisplayName("Distance기준 최단 거리 구간 조회 (HappyPath)")
    void 교대역에서_양재역_최단_구간은_교대역_남부터미널역_양재역_일반금액정책() {
        loginToken = 나이별_회원_생성요청(ALL_AGE);

        PathRequest pathRequest = new PathRequest(교대역.getId(), 양재역.getId());
        ExtractableResponse<Response> response = 최단구간을_조회한다(pathRequest);

        최단구간이_정상적으로_조회됨(response);
        int fare = 1_250 + 200 + 삼호선.getSurcharge(); //기본요금 + 거리 추가요금 + 노선 추가요금
        최단거리_조회_결과_확인(response, Arrays.asList(교대역, 남부터미널역, 양재역), 13, AgeFarePolicy.ALL.getOperator().apply(fare).intValue());
    }
    
    @Test
    void 경로_출발역과_도착역이_같은_경우_BAD_REQUEST_반환() {
        loginToken = 나이별_회원_생성요청(ALL_AGE);

        PathRequest pathRequest = new PathRequest(교대역.getId(), 교대역.getId());
        ExtractableResponse<Response> response = 최단구간을_조회한다(pathRequest);

        최단구간이_정상적으로_조회되지않음(response);
        최단거리_조회중_에러메세지_확인(response, PathException.SAME_SOURCE_TARGET_STATION_MSG);
    }

    @Test
    void 경로_출발역과_도착역이_경로가_이어지지_않는_경우_BAD_REQUEST_반환() {
        loginToken = 나이별_회원_생성요청(ALL_AGE);

        PathRequest pathRequest = new PathRequest(교대역.getId(), 강남구청역.getId());
        ExtractableResponse<Response> response = 최단구간을_조회한다(pathRequest);

        최단구간이_정상적으로_조회되지않음(response);
        최단거리_조회중_에러메세지_확인(response, PathException.PATH_FIND_NO_SEARCH_MSG);
    }

    @Test
    void 출발역이나_도착역이_노선에_등록되어있지_않은_경우_BAD_REQUEST_반환() {
        loginToken = 나이별_회원_생성요청(ALL_AGE);

        PathRequest pathRequest = new PathRequest(교대역.getId(), 뚝섬유원지역.getId());
        ExtractableResponse<Response> response = 최단구간을_조회한다(pathRequest);

        최단구간이_정상적으로_조회되지않음(response);
        최단거리_조회중_에러메세지_확인(response, PathException.NO_CONTAIN_STATION_MSG);
    }

    public static ExtractableResponse<Response> 최단구간을_조회한다(PathRequest pathRequest) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(loginToken.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(pathRequest)
                .when().get("/paths")
                .then().log().all().
                extract();
    }

    public static void 최단구간이_정상적으로_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 최단구간이_정상적으로_조회되지않음(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 최단거리_조회_결과_확인(ExtractableResponse<Response> response, List<StationResponse> stationResponses, int distance, int fare) {
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getStations()).containsExactlyElementsOf(stationResponses);
        assertThat(pathResponse.getDistance()).isEqualTo(distance);
        assertThat(pathResponse.getFare()).isEqualTo(fare);
    }

    public static void 최단거리_조회중_에러메세지_확인(ExtractableResponse<Response> response, String errorMsg) {
        assertThat(response.asString()).isEqualTo(errorMsg);
    }

    private static TokenResponse 나이별_회원_생성요청 (int age) {
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(MemberAcceptanceTest.EMAIL, MemberAcceptanceTest.PASSWORD, age);

        ExtractableResponse<Response> loginResponse = 회원_로그인을_시도한다(MemberAcceptanceTest.EMAIL, MemberAcceptanceTest.PASSWORD);
        회원_로그인_성공확인(loginResponse);

        return loginToken = loginResponse.as(TokenResponse.class);
    }
}

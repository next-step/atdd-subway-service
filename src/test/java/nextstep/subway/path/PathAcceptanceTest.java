package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.토큰_발급_요청;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {

    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private LineResponse 사호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 홍대역;

    private StationResponse 사당역;
    private StationResponse 이수역;

    private StationResponse 역삼역;
    private StationResponse 선릉역;

    private LineResponse 수인분당선;
    private StationResponse 선정릉역;

    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */
    @BeforeEach
    public void setUp() {
        super.setUp();
        홍대역 = StationAcceptanceTest.지하철역_등록되어_있음("홍대역").as(StationResponse.class);
        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        사당역 = StationAcceptanceTest.지하철역_등록되어_있음("사당역").as(StationResponse.class);
        이수역 = StationAcceptanceTest.지하철역_등록되어_있음("이수역").as(StationResponse.class);
        역삼역 = StationAcceptanceTest.지하철역_등록되어_있음("역삼역").as(StationResponse.class);
        선릉역 = StationAcceptanceTest.지하철역_등록되어_있음("선릉역").as(StationResponse.class);
        선정릉역 = StationAcceptanceTest.지하철역_등록되어_있음("선정릉역").as(StationResponse.class);

        이호선 = 지하철_노선_등록되어_있음(LineRequest.of("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10, BigDecimal.ZERO))
                .as(LineResponse.class);
        신분당선 = 지하철_노선_등록되어_있음(LineRequest.of("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 8, BigDecimal.ZERO))
                .as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(LineRequest.of("삼호선", "bg-red-600", 양재역.getId(), 남부터미널역.getId(), 5, BigDecimal.ZERO))
                .as(LineResponse.class);
        사호선 = 지하철_노선_등록되어_있음(LineRequest.of("사호선", "bg-blue-600", 이수역.getId(), 사당역.getId(), 7, BigDecimal.valueOf(500)))
                .as(LineResponse.class);
        수인분당선 = 지하철_노선_등록되어_있음(LineRequest.of("수인분당선", "bg-yellow-600", 선릉역.getId(), 선정릉역.getId(), 7, BigDecimal.valueOf(700)))
                .as(LineResponse.class);


        지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);
        지하철_노선에_지하철역_등록_요청(이호선, 사당역, 교대역, 15);
        지하철_노선에_지하철역_등록_요청(이호선, 강남역, 역삼역, 13);
        지하철_노선에_지하철역_등록_요청(이호선, 역삼역, 선릉역, 10);

    }

    @Test
    @DisplayName("지하철 노선에 미등록된 역 조회 (교대 -> 강남 -> 양재 -> 남부터미널역)")
    public void 지하철_노선에_미등록_역_조회_요청() {
        ExtractableResponse<Response> response = 최단_거리_조회_요청(강남역, 홍대역);

        응답_BAD_REQUEST(response);
    }


    @Test
    @DisplayName("지하철 노선에 미등록된 역 조회 (교대 -> 강남 -> 양재 -> 남부터미널역)")
    public void 지하철_노선에_조회_요청_출발역_도착역_동일_오류() {
        ExtractableResponse<Response> response = 최단_거리_조회_요청(강남역, 강남역);

        응답_BAD_REQUEST(response);
    }

    @Test
    @DisplayName(
            "지하철 노선에 등록된 역 기준으로 순방향 최단거리 검증"
                    + "(교대 --(10)> 강남 --(8)> 양재 --(5)> 남부터미널역)"
                    + "(교대 --(3)> 남부터미널역 <========= 가장 최단거리)"
    )
    public void 지하철_노선에_등록_역_최단거리_검증() {
        ExtractableResponse<Response> response = 최단_거리_조회_요청(교대역, 남부터미널역);

        PathResponse actual = response.as(PathResponse.class);

        응답_OK(response);
        assertThat(actual.getDistance()).isEqualTo(3);
    }

    @Test
    @DisplayName(
            "지하철 노선에 등록된 역 기준으로 역방향 최단거리 검증"
                    + "(교대 <(10)-- 강남 <(8)-- 양재 <(5)-- 남부터미널역)"
                    + "(교대 <(3)-- 남부터미널역 <========= 가장 최단거리)"
    )
    public void 지하철_노선에_등록_역_역방향_최단거리_검증() {
        ExtractableResponse<Response> response = 최단_거리_조회_요청(남부터미널역, 교대역);

        PathResponse actual = response.as(PathResponse.class);

        응답_OK(response);
        assertThat(actual.getDistance()).isEqualTo(3);
    }

    @Test
    @DisplayName(
            "최단 거리 운임 비용 검증"
                    + "(교대 --(10)> 강남 --(8)> 양재 --(5)> 남부터미널역)"
                    + "(강남 --> 남부터미널역)"
                    + "거리 : 13 / 예상 운임비용 : 1350"
    )
    public void 최단거리_운임_검증() {
        ExtractableResponse<Response> response = 최단_거리_조회_요청(강남역, 남부터미널역);

        PathResponse actual = response.as(PathResponse.class);

        응답_OK(response);
        운임_비용_확인(actual, BigDecimal.valueOf(1350));
    }


    @Test
    @DisplayName(
            "최단 거리 운임 비용 검증"
                    + "[4호선 추가 요금 500원 ] 이수역 --(7)> 사당역"
                    + "[2호선 추가 요금 없음] 사당역 --(15)> 교대 --(10)> 강남 --(13)> 역삼역 --(10)> 선릉역"
                    + "[수인분당선 추가 요금 700원] 선릉역 --(7)> 선정릉역"
                    + "(이수역 --> 선정릉역)"
                    + "거리 : 62 / 예상 운임비용 : 1250 + 600(거리초과 8KM 당 100원 추가) + 700 (500(4호선 환승운임) < 700(수인분당선 환승운임))"
    )
    public void 최단거리_운임_노선_추가요금_검증() {
        ExtractableResponse<Response> response = 최단_거리_조회_요청(이수역, 선정릉역);

        PathResponse actual = response.as(PathResponse.class);

        응답_OK(response);
        운임_비용_확인(actual, BigDecimal.valueOf(2650));
    }

    @Test
    @DisplayName(
            "최단 거리 운임 비용 검증 어린이"
                    + "[4호선 추가 요금 500원 ] 이수역 --(7)> 사당역"
                    + "[2호선 추가 요금 없음] 사당역 --(15)> 교대 --(10)> 강남 --(13)> 역삼역 --(10)> 선릉역"
                    + "[수인분당선 추가 요금 700원] 선릉역 --(7)> 선정릉역"
                    + "(이수역 --> 선정릉역)"
                    + "거리 : 62 / 예상 운임비용 : (2650 - 350) * 0.5 )"
    )
    public void 최단거리_운임_노선_추가요금_검증_어린이() {
        MemberAcceptanceTest.회원_생성을_요청("이메일", "비밀번호", 10);
        TokenResponse 인증_토큰 = 토큰_발급_요청(TokenRequest.of("이메일", "비밀번호")).as(TokenResponse.class);

        ExtractableResponse<Response> response = 최단_거리_조회_요청_회원용(이수역, 선정릉역, 인증_토큰);

        PathResponse actual = response.as(PathResponse.class);

        응답_OK(response);
        운임_비용_확인(actual, BigDecimal.valueOf((2650 - 350) * 0.5).setScale(0));
    }

    @Test
    @DisplayName(
            "최단 거리 운임 비용 검증 청소년"
                    + "[4호선 추가 요금 500원 ] 이수역 --(7)> 사당역"
                    + "[2호선 추가 요금 없음] 사당역 --(15)> 교대 --(10)> 강남 --(13)> 역삼역 --(10)> 선릉역"
                    + "[수인분당선 추가 요금 700원] 선릉역 --(7)> 선정릉역"
                    + "(이수역 --> 선정릉역)"
                    + "거리 : 62 / 예상 운임비용 : (2650 - 350) * 0.5 )"
    )
    public void 최단거리_운임_노선_추가요금_검증_청소년() {
        MemberAcceptanceTest.회원_생성을_요청("이메일", "비밀번호", 18);
        TokenResponse 인증_토큰 = 토큰_발급_요청(TokenRequest.of("이메일", "비밀번호")).as(TokenResponse.class);

        ExtractableResponse<Response> response = 최단_거리_조회_요청_회원용(이수역, 선정릉역, 인증_토큰);

        PathResponse actual = response.as(PathResponse.class);

        응답_OK(response);
        운임_비용_확인(actual, BigDecimal.valueOf((2650 - 350) * 0.8).setScale(0));
    }

    private static void 운임_비용_확인(PathResponse actual, BigDecimal expected) {
        assertThat(actual.getPareMoney()).isEqualTo(expected);
    }

    private static ExtractableResponse<Response> 최단_거리_조회_요청(StationResponse 출발역, StationResponse 도착역) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParams("source", 출발역.getId(), "target", 도착역.getId())
                .when().get("/paths?source={source}&target={target}")
                .then().log().all()
                .extract();

    }

    private static ExtractableResponse<Response> 최단_거리_조회_요청_회원용(StationResponse 출발역, StationResponse 도착역, TokenResponse 토큰) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(토큰.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParams("source", 출발역.getId(), "target", 도착역.getId())
                .when().get("/paths?source={source}&target={target}")
                .then().log().all()
                .extract();

    }

    private static void 응답_OK(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private static void 응답_BAD_REQUEST(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

}

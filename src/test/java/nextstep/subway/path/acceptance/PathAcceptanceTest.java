package nextstep.subway.path.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.acceptance.StationAcceptanceTest;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.인증_성공;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.인증_요청;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.인증_요청_데이터_생성;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.member.acceptance.MemberAcceptanceTest.회원_생성됨;
import static nextstep.subway.member.acceptance.MemberAcceptanceTest.회원_생성을_요청;
import static nextstep.subway.path.acceptance.PathChargeAcceptanceTest.요금_조회_성공;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 경로 조회")
class PathAcceptanceTest extends AcceptanceTest {

    private static final long NOT_EXIST_ID = 9999;
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;

    StationResponse 강남역;
    StationResponse 양재역;
    StationResponse 교대역;
    StationResponse 남부터미널역;
    StationResponse 삼전역;
    StationResponse 잠실새내역;
    StationResponse 종합운동장역;

    LineResponse 신분당선;
    LineResponse 이호선;
    LineResponse 삼호선;
    LineResponse 구호선;

    String token;

    /**
     * [] = 지하철역, ----XXXX----> = 노선 구간,  (n) = 거리
     * <p>
     * [강남역] ----신분당선(1)--->   [양재역]   ----신분당선(1)---->  [교대역] ----삼호선(1)----> [남부터미널역]
     * |                        |                                                        |
     * |                        |                                                        |
     * 이호선(10)                구호선(1)                                                  삼호선(100)
     * |                        |                                                        |
     * V                        V                                                        V
     * [잠실새내역] ---이호선(10)----> [종합운동장역]     ----------이호선,구호선(1)------------>    [삼전역]
     */
    @BeforeEach
    public void setUp() {
        super.setUp();
        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        삼전역 = StationAcceptanceTest.지하철역_등록되어_있음("삼전역").as(StationResponse.class);
        잠실새내역 = StationAcceptanceTest.지하철역_등록되어_있음("잠실새내역").as(StationResponse.class);
        종합운동장역 = StationAcceptanceTest.지하철역_등록되어_있음("종합운동장역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 1);
        이호선 = 지하철_노선_등록되어_있음("이호선", "bg-red-700", 강남역, 잠실새내역, 10);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-red-800", 교대역, 남부터미널역, 1);
        구호선 = 지하철_노선_등록되어_있음("구호선", "bg-red-900", 양재역, 종합운동장역, 1);

        지하철_노선에_지하철역_등록_요청(신분당선, 양재역, 교대역, 1);
        지하철_노선에_지하철역_등록_요청(이호선, 잠실새내역, 종합운동장역, 10);
        지하철_노선에_지하철역_등록_요청(이호선, 종합운동장역, 삼전역, 1);
        지하철_노선에_지하철역_등록_요청(삼호선, 남부터미널역, 삼전역, 100);
        지하철_노선에_지하철역_등록_요청(구호선, 종합운동장역, 삼전역, 1);

        // when
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        // then
        회원_생성됨(createResponse);

        // when
        ExtractableResponse<Response> authResponse = 인증_요청(인증_요청_데이터_생성(EMAIL, PASSWORD));
        // then
        인증_성공(authResponse);

        token = authResponse.as(TokenResponse.class).getAccessToken();
    }

    @DisplayName("최단 경로 조회 성공")
    @Test
    void 최단_경로_조회() {
        /**
         * [] = 지하철역, ----XXXX----> = 노선 구간,  (n) = 거리
         * <p>
         * [강남역] ----신분당선(1)--->   [양재역]   ----신분당선(1)---->  [교대역] ----삼호선(1)----> [남부터미널역]
         * |                        |                                                        |
         * |                        |                                                        |
         * 이호선(10)                구호선(1)                                                  삼호선(100)
         * |                        |                                                        |
         * V                        V                                                        V
         * [잠실새내역] ---이호선(10)----> [종합운동장역]     ----------이호선,구호선(1)------------>    [삼전역]
         */

        //when:
        PathResponse response = 최단_경로_조회_요청(강남역, 삼전역).as(PathResponse.class);
        //then:
        최단_경로_조회됨(response, new String[]{강남역.getName(), 양재역.getName(), 종합운동장역.getName(), 삼전역.getName()});
        //일반 사용자 기본요금(1250) + 노선 추가금액(0) + 이동거리(10km 이하 : 0) = 1250원
        요금_조회_성공(response,1250);
    }

    private void 최단_경로_조회됨(PathResponse response, String[] stationNames) {
        assertThat(response.stationNames()).containsSequence(stationNames);
    }

    @DisplayName("최단 경로 조회 실패 - 출발역과 도착역이 같은 경우")
    @Test
    void 최단_경로_조회_출발역과_도착역이_같은_경우() {
        /**
         * [] = 지하철역, ----XXXX----> = 노선 구간,  (n) = 거리
         * <p>
         * [강남역] ----신분당선(1)--->   [양재역]   ----신분당선(1)---->  [교대역] ----삼호선(1)----> [남부터미널역]
         * |                        |                                                        |
         * |                        |                                                        |
         * 이호선(10)                구호선(1)                                                  삼호선(100)
         * |                        |                                                        |
         * V                        V                                                        V
         * [잠실새내역] ---이호선(10)----> [종합운동장역]     ----------이호선,구호선(1)------------>    [삼전역]
         */

        //when, then:
        assertThat(최단_경로_조회_요청(강남역, 강남역).statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("최단 경로 조회 실패 - 존재하지 않는 지하철역을 조회할 경우")
    @Test
    void 최단_경로_조회_존재하지_않는_지하철역을_조회할_경우() {
        /**
         * [] = 지하철역, ----XXXX----> = 노선 구간,  (n) = 거리
         * <p>
         * [강남역] ----신분당선(1)--->   [양재역]   ----신분당선(1)---->  [교대역] ----삼호선(1)----> [남부터미널역]
         * |                        |                                                        |
         * |                        |                                                        |
         * 이호선(10)                구호선(1)                                                  삼호선(100)
         * |                        |                                                        |
         * V                        V                                                        V
         * [잠실새내역] ---이호선(10)----> [종합운동장역]     ----------이호선,구호선(1)------------>    [삼전역]
         */
        Station 등록되지_않은_역 = Station.of(NOT_EXIST_ID, "등록되지 않은 역");

        //when, then:
        assertThat(최단_경로_조회_요청(강남역.getId(), 등록되지_않은_역.getId()).statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("최단 경로 조회 실패 - 출발역과 도착역이 연결되지 않은 경우")
    @Test
    void 최단_경로_조회_출발역과_도착역이_연결되지_않은_경우() {
        /**
         * [] = 지하철역, ----XXXX----> = 노선 구간,  (n) = 거리
         * <p>
         * [강남역] ----신분당선(1)--->   [양재역]   ----신분당선(1)---->  [교대역] ----삼호선(1)----> [남부터미널역]
         * |                        |                                                        |
         * |                        |                                                        |
         * 이호선(10)                구호선(1)                                                  삼호선(100)
         * |                        |                                                        |
         * V                        V                                                        V
         * [잠실새내역] ---이호선(10)----> [종합운동장역]     ----------이호선,구호선(1)------------>    [삼전역]
         *
         * 연결되지 않은 구간
         * [인천공항역] ---공항철도(1)----> [김포공항역]
         */
        //given:
        StationResponse 인천공항역 = StationAcceptanceTest.지하철역_등록되어_있음("인천공항역").as(StationResponse.class);
        StationResponse 김포공항역 = StationAcceptanceTest.지하철역_등록되어_있음("김포공항역").as(StationResponse.class);
        LineResponse 공항철도 = 지하철_노선_등록되어_있음("공항철도", "bg-red-500", 인천공항역, 김포공항역, 1);

        //when, then:
        assertThat(최단_경로_조회_요청(강남역, 김포공항역).statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static ExtractableResponse<Response> 최단_경로_조회_요청(StationResponse 출발역, StationResponse 도착역, String token) {
        return 최단_경로_조회_요청(출발역.getId(), 도착역.getId(), token);

    }

    private ExtractableResponse<Response> 최단_경로_조회_요청(StationResponse 출발역, StationResponse 도착역) {
        return 최단_경로_조회_요청(출발역.getId(), 도착역.getId());

    }

    private ExtractableResponse<Response> 최단_경로_조회_요청(long 출발역_id, long 도착역_id) {
        return 최단_경로_조회_요청(출발역_id, 도착역_id, token);
    }

    private static ExtractableResponse<Response> 최단_경로_조회_요청(long 출발역_id, long 도착역_id, String token) {
        return RestAssured.given().log().all()
                .auth().oauth2(token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("source", 출발역_id)
                .param("target", 도착역_id)
                .when().get("paths")
                .then().log().all()
                .extract();

    }
}

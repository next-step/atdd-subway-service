package nextstep.subway.path.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.acceptance.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.인증_요청;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.인증_요청_데이터_생성;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.member.acceptance.MemberAcceptanceTest.회원_생성을_요청;
import static nextstep.subway.path.acceptance.PathAcceptanceTest.최단_경로_조회_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("요금 조회")
public class PathChargeAcceptanceTest extends AcceptanceTest {

    public static final String NORMAL_EMAIL = "normal@email.com";
    public static final String NORMAL_PASSWORD = "password";
    public static final int NORMAL_AGE = 29;

    public static final String CHILD_EMAIL = "child@email.com";
    public static final String CHILD_PASSWORD = "password";
    public static final int CHILD_AGE = 8;

    public static final String TEENAGER_EMAIL = "teenager@email.com";
    public static final String TEENAGER_PASSWORD = "password";
    public static final int TEENAGER_AGE = 15;

    StationResponse 인천공항역;
    StationResponse 강남역;
    StationResponse 양재역;
    StationResponse 교대역;
    StationResponse 남부터미널역;
    StationResponse 삼전역;

    LineResponse 공항철도;
    LineResponse 신분당선;
    LineResponse 삼호선;

    String 일반_사용자;
    String 어린이_사용자;
    String 청소년_사용자;

    /**
     * 거리 :                10m                  1m                  10m                 10m                    100m
     * 구간 :  [인천공항역] ---공항철도---> [강남역] ---신분당선---> [양재역] ---신분당선---> [교대역] ---삼호선---> [남부터미널역] ---삼호선---> [삼전역]
     * 추가요금 :              0원                 500원                500원                900원                   900원
     */
    @BeforeEach
    void before() {
        setUp();
        인천공항역 = StationAcceptanceTest.지하철역_등록되어_있음("인천공항역").as(StationResponse.class);
        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        삼전역 = StationAcceptanceTest.지하철역_등록되어_있음("삼전역").as(StationResponse.class);
        // 노선 등록
        공항철도 = 지하철_노선_등록되어_있음("공항철도", "bg-red-500", 인천공항역, 강남역, 10);
        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 1, 500);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-red-700", 교대역, 남부터미널역, 10, 900);
        // 구간 등록
        지하철_노선에_지하철역_등록_요청(신분당선, 양재역, 교대역, 10);
        지하철_노선에_지하철역_등록_요청(삼호선, 남부터미널역, 삼전역, 100);

        // 일반 회원을 생성
        회원_생성을_요청(NORMAL_EMAIL, NORMAL_PASSWORD, NORMAL_AGE);
        ExtractableResponse<Response> normalAuthResponse = 인증_요청(인증_요청_데이터_생성(NORMAL_EMAIL, NORMAL_PASSWORD));
        일반_사용자 = normalAuthResponse.as(TokenResponse.class).getAccessToken();
        // 어린이 회원을 생성
        회원_생성을_요청(CHILD_EMAIL, CHILD_PASSWORD, CHILD_AGE);
        ExtractableResponse<Response> childAuthResponse = 인증_요청(인증_요청_데이터_생성(CHILD_EMAIL, CHILD_PASSWORD));
        어린이_사용자 = childAuthResponse.as(TokenResponse.class).getAccessToken();
        // 청소년 회원을 생성
        회원_생성을_요청(TEENAGER_EMAIL, TEENAGER_PASSWORD, TEENAGER_AGE);
        ExtractableResponse<Response> teenagerAuthResponse = 인증_요청(인증_요청_데이터_생성(TEENAGER_EMAIL, TEENAGER_PASSWORD));
        청소년_사용자 = teenagerAuthResponse.as(TokenResponse.class).getAccessToken();
    }

    @DisplayName("일반 사용자 - 노선 추가 요금 없음 - 10m 이동")
    @Test
    void 일반사용자_노선추가요금없음_10m() {
        /**
         * 거리 :                10m
         * 구간 :  [인천공항역] ---공항철도---> [강남역]
         * 추가요금 :              0원
         * 총 요금 : 일반 사용자 기본료(1250) + 노선 추가금(0) + 이동거리(10km 이내. 0원) = 1250원
         */
        PathResponse response = 최단_경로_조회_요청(인천공항역, 강남역, 일반_사용자).as(PathResponse.class);
        요금_조회_성공(response, 1250);
    }

    @DisplayName("일반 사용자 - 노선 추가 요금 있음 - 10m 이동")
    @Test
    void 일반사용자_노선추가요금있음_10m() {
        /**
         * 거리 :              10m
         * 구간 :   [양재역] ---신분당선---> [교대역]
         * 추가요금 :           500원
         * 총 요금 : 일반 사용자 기본료(1250) + 노선 추가금(500) + 이동거리(10km 이내. 0원) = 1750원
         */
        PathResponse response = 최단_경로_조회_요청(양재역, 교대역, 일반_사용자).as(PathResponse.class);
        요금_조회_성공(response, 1750);
    }

    @DisplayName("일반 사용자 - 노선 추가 요금 있음 - 100m 이동")
    @Test
    void 일반사용자_노선추가요금있음_100m() {
        /**
         * 거리 :                 100m
         * 구간 :   [남부터미널역] ---삼호선---> [삼전역]
         * 추가요금 :              900원
         * 총 요금 : 일반 사용자 기본료(1250) + 노선 추가금(900) + 이동거리(50km 이상 -> 1200원) = 3350원
         */
        PathResponse response = 최단_경로_조회_요청(남부터미널역, 삼전역, 일반_사용자).as(PathResponse.class);
        요금_조회_성공(response, 3350);
    }

    @DisplayName("어린이 사용자 - 노선 추가 요금 없음 - 10m 이동")
    @Test
    void 어린이사용자_노선추가요금없음_10m() {
        /**
         * 거리 :                10m
         * 구간 :  [인천공항역] ---공항철도---> [강남역]
         * 추가요금 :              0원
         * 총 요금 : 어린이 사용자 기본료(450) + 노선 추가금(0) + 이동거리(10km 이내. 0원) = 450원
         */
        PathResponse response = 최단_경로_조회_요청(인천공항역, 강남역, 어린이_사용자).as(PathResponse.class);
        요금_조회_성공(response, 450);
    }

    @DisplayName("어린이 사용자 - 노선 추가 요금 있음 - 10m 이동")
    @Test
    void 어린이사용자_노선추가요금있음_10m() {
        /**
         * 거리 :              10m
         * 구간 :   [양재역] ---신분당선---> [교대역]
         * 추가요금 :           500원
         * 총 요금 : 어린이 사용자 기본료(450) + 노선 추가금(500) + 이동거리(10km 이내. 0원) = 950원
         */
        PathResponse response = 최단_경로_조회_요청(양재역, 교대역, 어린이_사용자).as(PathResponse.class);
        요금_조회_성공(response, 950);
    }

    @DisplayName("어린이 사용자 - 노선 추가 요금 있음 - 100m 이동")
    @Test
    void 어린이사용자_노선추가요금있음_100m() {
        /**
         * 거리 :                 100m
         * 구간 :   [남부터미널역] ---삼호선---> [삼전역]
         * 추가요금 :              900원
         * 총 요금 : 어린이 사용자 기본료(450) + 노선 추가금(900) + 이동거리(50km 이상 -> 1200원) = 2550원
         */
        PathResponse response = 최단_경로_조회_요청(남부터미널역, 삼전역, 어린이_사용자).as(PathResponse.class);
        요금_조회_성공(response, 2550);
    }

    @DisplayName("청소년 사용자 - 노선 추가 요금 없음 - 10m 이동")
    @Test
    void 청소년사용자_노선추가요금없음_10m() {
        /**
         * 거리 :                10m
         * 구간 :  [인천공항역] ---공항철도---> [강남역]
         * 추가요금 :              0원
         * 총 요금 : 청소년 사용자 기본료(720) + 노선 추가금(0) + 이동거리(10km 이내. 0원) = 720원
         */
        PathResponse response = 최단_경로_조회_요청(인천공항역, 강남역, 청소년_사용자).as(PathResponse.class);
        요금_조회_성공(response, 720);
    }

    @DisplayName("청소년 사용자 - 노선 추가 요금 있음 - 10m 이동")
    @Test
    void 청소년사용자_노선추가요금있음_10m() {
        /**
         * 거리 :              10m
         * 구간 :   [양재역] ---신분당선---> [교대역]
         * 추가요금 :           500원
         * 총 요금 : 청소년 사용자 기본료(720) + 노선 추가금(500) + 이동거리(10km 이내. 0원) = 1220원
         */
        PathResponse response = 최단_경로_조회_요청(양재역, 교대역, 청소년_사용자).as(PathResponse.class);
        요금_조회_성공(response, 1220);
    }

    @DisplayName("청소년 사용자 - 노선 추가 요금 있음 - 100m 이동")
    @Test
    void 청소년사용자_노선추가요금있음_100m() {
        /**
         * 거리 :                 100m
         * 구간 :   [남부터미널역] ---삼호선---> [삼전역]
         * 추가요금 :              900원
         * 총 요금 : 청소년 사용자 기본료(720) + 노선 추가금(900) + 이동거리(50km 이상 -> 1200원) = 2820원
         */
        PathResponse response = 최단_경로_조회_요청(남부터미널역, 삼전역, 청소년_사용자).as(PathResponse.class);
        요금_조회_성공(response, 2820);
    }

    @DisplayName("일반 사용자 - 추가 요금이 가장 큰 노선의 추가요금  - 10m 이동")
    @Test
    void 일반사용자_가장큰추가요금_131m() {

        /**
         * 거리 :                10m                  1m                  10m                 10m                    100m
         * 구간 :  [인천공항역] ---공항철도---> [강남역] ---신분당선---> [양재역] ---신분당선---> [교대역] ---삼호선---> [남부터미널역] ---삼호선---> [삼전역]
         * 추가요금 :              0원                 500원                500원                900원                   900원
         * 총 요금 : 일반 사용자 기본료(1250) + 노선 추가금(900) + 이동거리(50km 이상. 1600원) = 3750원
         */
        PathResponse response = 최단_경로_조회_요청(인천공항역, 삼전역, 일반_사용자).as(PathResponse.class);
        요금_조회_성공(response, 3750);
    }
    public static void 요금_조회_성공(PathResponse response, double expectedCharge) {
        assertThat(response.getCharge()).isEqualTo(expectedCharge);
    }
}

package nextstep.subway.path;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_요청;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;

@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {

    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private LineResponse 오호선;
    private LineResponse 육호선;

    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 선릉역;
    private StationResponse 잠실역;
    private StationResponse 오리역;
    private StationResponse 분당역;
    private LineResponse 사호선;
    private LineResponse 자바선;
    private LineResponse 호남선;
    private StationResponse 시흥대야역;
    private StationResponse 은계역;
    private LineResponse 서해선;
    private StationResponse 사당역;
    private StationResponse 서울대역;
    private StationResponse 개성역;
    private StationResponse 잠실나루역;
    private StationResponse 강변역;

    /**                           개성역
     *                            |
     *                            *신분당선*(60)
     *                            |
     * 교대역    --- *2호선*(10) ---   강남역  --- *2호선*(10) --- 선릉역--- *2호선*(5) --- 잠실역 --- 5호선(10)(추가요금 900) --- 잠실나루역 --- 6호선(20)(추가요금 1000) --- 강변역
     * |                          |                         |                     |
     * *3호선*(3)                  *신분당선*(10)              *자바선*(1)             *호남선*(10)
     * |                          |                         |                     |
     * 남부터미널역  --- *3호선*(2) --- 양재역 ---  *4호선*(7) --- 오리역 --- *4호선*(10) --- 분당역
     */

    /**
     * 시흥대야역
     * |
     * *서해선*(3)
     * |
     * 은계역
     */
    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        //given
        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        선릉역 = 지하철역_등록되어_있음("선릉역").as(StationResponse.class);
        잠실역 = 지하철역_등록되어_있음("잠실역").as(StationResponse.class);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        오리역 = 지하철역_등록되어_있음("오리역").as(StationResponse.class);
        분당역 = 지하철역_등록되어_있음("분당역").as(StationResponse.class);
        시흥대야역 = 지하철역_등록되어_있음("시흥대야역").as(StationResponse.class);
        은계역 = 지하철역_등록되어_있음("은계역").as(StationResponse.class);
        사당역 = 지하철역_등록되어_있음("사당역").as(StationResponse.class);
        서울대역 = 지하철역_등록되어_있음("서울대역").as(StationResponse.class);
        개성역 = 지하철역_등록되어_있음("개성역").as(StationResponse.class);
        잠실나루역 = 지하철역_등록되어_있음("잠실나루역").as(StationResponse.class);
        강변역 = 지하철역_등록되어_있음("강변역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 10);
        이호선 = 지하철_노선_등록되어_있음("이호선", "bg-red-600", 교대역, 강남역, 10);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-red-600", 교대역, 양재역, 5);
        사호선 = 지하철_노선_등록되어_있음("사호선", "bg-red-600", 양재역, 오리역, 7);
        오호선 = 지하철_노선_등록되어_있음("오호선", "bg-red-600", 잠실역, 잠실나루역, 10, 900);
        육호선 = 지하철_노선_등록되어_있음("육호선", "bg-red-600", 잠실나루역, 강변역, 10, 1000);
        자바선 = 지하철_노선_등록되어_있음("자바선", "bg-red-600", 선릉역, 오리역, 1);
        호남선 = 지하철_노선_등록되어_있음("호남선", "bg-red-600", 잠실역, 분당역, 10);
        서해선 = 지하철_노선_등록되어_있음("서해선", "bg-red-600", 시흥대야역, 은계역, 3);

        지하철_노선에_지하철역_등록_요청(이호선, 강남역, 선릉역, 10);
        지하철_노선에_지하철역_등록_요청(이호선, 선릉역, 잠실역, 5);
        지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);
        지하철_노선에_지하철역_등록_요청(사호선, 오리역, 분당역, 10);
        지하철_노선에_지하철역_등록_요청(신분당선, 개성역, 강남역, 60);
    }

    @Test
    @DisplayName("최단거리 경로를 요청한다. - 성공 케이스")
    void findPathBySourceAndTargetSuccess() {
        // When 최단거리 경로 요청
        ExtractableResponse<Response> response = 최단거리_경로_요청(양재역, 잠실역);

        //then 촤단 거리 응답을 받는다
        최단거리_응답이_존재한다(response);
        최단거리를_확인한다(response, 13);
        최단거리를_역을_확인한다(response, 양재역.getId(), 오리역.getId(), 선릉역.getId(), 잠실역.getId());
        최단거리를_요금을_확인한다(response, 1250);

        // When 최단거리 경로 요청 20km
        response = 최단거리_경로_요청(교대역, 선릉역);

        //then 10km초과∼50km까지(5km마다 100원)
        최단거리를_요금을_확인한다(response, 1450);

        // When 최단거리 경로 요청 60km
        response = 최단거리_경로_요청(개성역, 강남역);

        //then 50km초과 시 (8km마다 100원) 증가한다. 1250(10km) + 800(40km) + 100(10km)
        최단거리를_요금을_확인한다(response, 2150);

        // When 최단거리 경로 요청 - 추가요금이 있는 노선
        response = 최단거리_경로_요청(잠실역, 잠실나루역);

        //then 추가 운임이 계산된다.
        최단거리를_요금을_확인한다(response, 2150);

        // When 최단거리 경로 요청 - 추가요금이 있는 노선 2개를 거치는 노선을 지나간다.
        response = 최단거리_경로_요청(잠실역, 강변역);

        //then 가장 높은 금액의 추가 요금만 적용된다.
        최단거리를_요금을_확인한다(response, 2450);

    }

    @Test
    @DisplayName("최단거리 경로를 요청한다. - 로그인 케이스")
    void findPathBySourceAndTargetSuccessWithLogin() {
        //given
        // 회원이 존재한다.
        회원_생성을_요청("7271kim@naver.com", "pw", 13);
        회원_생성을_요청("7271kim@naver.com2", "pw", 12);

        // 로그인 하다.
        TokenResponse 사용자_토큰_13세 = 로그인_요청("7271kim@naver.com", "pw").as(TokenResponse.class);
        TokenResponse 사용자_토큰_12세 = 로그인_요청("7271kim@naver.com2", "pw").as(TokenResponse.class);

        // When 최단거리 경로 요청 60km
        ExtractableResponse<Response> response_13세 = 최단거리_경로_요청(개성역, 강남역, 사용자_토큰_13세);
        ExtractableResponse<Response> response_12세 = 최단거리_경로_요청(개성역, 강남역, 사용자_토큰_12세);

        //then 50km초과 시 (8km마다 100원) 증가한다. 1250(10km) + 800(40km) + 100(10km)
        최단거리를_요금을_확인한다(response_13세, 1440);
        최단거리를_요금을_확인한다(response_12세, 900);
    }

    @Test
    @DisplayName("최단거리 경로를 요청한다. - 실패 케이스")
    void findPathBySourceAndTargetFail() {
        // When 출발역과 도착역이 같은 경우
        ExtractableResponse<Response> response = 최단거리_경로_요청(양재역, 양재역);

        //then 실패 응답을 받는다
        경로_요청_응답_실패(response);

        // When 출발역과 도착역이 연결이 되어 있지 않은 경우
        response = 최단거리_경로_요청(교대역, 은계역);

        //then 실패 응답을 받는다
        경로_요청_응답_실패(response);

        // When 존재하지 않은 출발역이나 도착역을 조회 할 경우
        response = 최단거리_경로_요청(사당역, 서울대역);

        //then 실패 응답을 받는다
        경로_요청_응답_실패(response);
    }

    private ExtractableResponse<Response> 최단거리_경로_요청(StationResponse source, StationResponse target,
            TokenResponse toekn) {
        return RestAssured
            .given().log().all()
            .auth().oauth2(toekn.getAccessToken())
            .when().get(String.format("/paths?source=%d&target=%d", source.getId(), target.getId()))
            .then().log().all().extract();
    }

    public static void 최단거리를_요금을_확인한다(ExtractableResponse<Response> response, int fare) {
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getFare()).isEqualTo(fare);
    }

    public static void 경로_요청_응답_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 최단거리를_역을_확인한다(ExtractableResponse<Response> response, Long... stationsId) {
        List<Long> reponseStationsId = new ArrayList<>(response.jsonPath().getList("stations.id", Long.class));
        assertThat(reponseStationsId).containsExactly(stationsId);
    }

    public ExtractableResponse<Response> 최단거리_경로_요청(StationResponse source, StationResponse target) {
        return RestAssured
            .given().log().all()
            .when().get(String.format("/paths?source=%d&target=%d", source.getId(), target.getId()))
            .then().log().all().extract();
    }

    public static void 최단거리를_확인한다(ExtractableResponse<Response> response, int distance) {
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getDistance()).isEqualTo(distance);
    }

    public static void 최단거리_응답이_존재한다(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static LineResponse 지하철_노선_등록되어_있음(String name, String color, StationResponse upStaion,
            StationResponse downStation, int distance) {
        return LineAcceptanceTest
            .지하철_노선_등록되어_있음(new LineRequest(name, color, upStaion.getId(), downStation.getId(), distance))
            .as(LineResponse.class);
    }

    public static LineResponse 지하철_노선_등록되어_있음(String name, String color, StationResponse upStaion,
            StationResponse downStation, int distance, int addFare) {
        return LineAcceptanceTest
            .지하철_노선_등록되어_있음(new LineRequest(name, color, upStaion.getId(), downStation.getId(), distance, addFare))
            .as(LineResponse.class);
    }
}

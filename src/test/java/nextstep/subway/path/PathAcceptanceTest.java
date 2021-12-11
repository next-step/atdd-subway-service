package nextstep.subway.path;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.getIdsByStationResponses;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_순서_정렬됨;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;


@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    final String EMAIL = "email@email.com";
    final String PASSWORD = "password";

    StationResponse 교대역;
    StationResponse 강남역;
    StationResponse 양재역;
    StationResponse 남부터미널역;
    StationResponse 서초역;
    StationResponse 양재시민의숲;
    StationResponse 청계산입구;

    LineResponse 신분당선;
    LineResponse 이호선;
    LineResponse 삼호선;

    /**
     * 추가운임 : 신분당선 900, 2호선 0, 3호선 200
     * 서초역  --- *2호선* 10--- 교대역  --- *2호선* 15 -----  강남역
     *                        |                          |
     *                      *3호선* 10                 *신분당선* 10
     *                        |                          |
     *                      남부터미널역  --- *3호선* 5 --- 양재
     *                                                  |
     *                                               *신분당선* 7
     *                                                  |
     *                                               양재시민의숲
     *                                                  |
     *                                               *신분당선* 22
     *                                                  |
     *                                               청계산입구
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        //background
        //given
        // 지하철 역 등록되어 있음
        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        서초역 = 지하철역_등록되어_있음("서초역").as(StationResponse.class);
        양재시민의숲 = 지하철역_등록되어_있음("양재시민의숲").as(StationResponse.class);
        청계산입구 = 지하철역_등록되어_있음("청계산입구").as(StationResponse.class);

        // 노선 등록되어 있음
        //신분당선 (강남-양재-양재시민의숲-청계산입구, 10-10-22, 900)
        LineRequest lineRequest_신분당선 = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10, 900);
        신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest_신분당선).as(LineResponse.class);
        지하철_노선에_지하철역_등록_요청(신분당선, 양재역, 양재시민의숲, 7);
        지하철_노선에_지하철역_등록_요청(신분당선, 양재시민의숲, 청계산입구, 22);

        //2호선 (서초-교대-강남, 10-15, 0)
        LineRequest lineRequest_이호선 = new LineRequest("이호선", "green", 교대역.getId(), 강남역.getId(), 15, 0);
        이호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest_이호선).as(LineResponse.class);
        지하철_노선에_지하철역_등록_요청(이호선, 서초역, 교대역, 10);

        //3호선 (교대-남부터미널-양재, 10-5, 200)
        LineRequest lineRequest_삼호선 = new LineRequest("삼호선", "orange", 교대역.getId(), 남부터미널역.getId(), 10, 200);
        삼호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest_삼호선).as(LineResponse.class);
        지하철_노선에_지하철역_등록_요청(삼호선, 남부터미널역, 양재역, 5);

    }

    @Test
    @DisplayName("강남역-남부터미널역 최단 거리 경로, 요금 조회")
    void shortestPath_강남_남부터미널() {
        //when
        ExtractableResponse<Response> response = 경로_조회_요청(강남역.getId(), 남부터미널역.getId());

        PathResponse pathResponse = response.as(PathResponse.class);
        //최단 경로 지하철 역 목록 조회됨
        최단_경로_지하철역_순서_정렬됨(pathResponse, Arrays.asList(강남역, 양재역, 남부터미널역));
        // 총겨리 조회됨
        총거리_조회됨(pathResponse, 15);
        // 총요금 조회됨 (신분당선 -> 3호선 환승, 추가운임 900)
        총요금_조회됨(pathResponse, 2250);
    }

    @Test
    @DisplayName("서초역-양재시민의숲 최단 거리 경로, 요금 조회")
    void shortestPath_서초역_양재시민의숲() {
        //when
        ExtractableResponse<Response> response = 경로_조회_요청(서초역.getId(), 양재시민의숲.getId());

        PathResponse pathResponse = response.as(PathResponse.class);
        //then
        // 최단 경로 지하철 역 목록 조회됨
        최단_경로_지하철역_순서_정렬됨(pathResponse, Arrays.asList(서초역, 교대역, 남부터미널역, 양재역, 양재시민의숲));
        // 총겨리 조회됨
        총거리_조회됨(pathResponse, 32);
        // 총요금 조회됨 (이호선 -> 삼호선 -> 신분당선 환승, 추가운임 900)
        총요금_조회됨(pathResponse, 2650);

    }

    @Test
    @DisplayName("서초역-청계산입구 최단 거리 경로, 요금 조회")
    void shortestPath_서초역_청계산입구() {
        //when
        ExtractableResponse<Response> response = 경로_조회_요청(서초역.getId(), 청계산입구.getId());

        PathResponse pathResponse = response.as(PathResponse.class);
        //then
        // 최단 경로 지하철 역 목록 조회됨
        최단_경로_지하철역_순서_정렬됨(pathResponse, Arrays.asList(서초역, 교대역, 남부터미널역, 양재역, 양재시민의숲, 청계산입구));
        // 총겨리 조회됨
        총거리_조회됨(pathResponse, 54);
        // 총요금 조회됨 (이호선 -> 삼호선 -> 신분당선 환승, 추가운임 900)
        총요금_조회됨(pathResponse, 3050);
    }

    @Test
    @DisplayName("서초역-강남역 최단 거리 경로, 요금 조회")
    void shortestPath_서초역_강남역() {
        //when
        ExtractableResponse<Response> response = 경로_조회_요청(서초역.getId(), 강남역.getId());

        PathResponse pathResponse = response.as(PathResponse.class);
        //then
        // 최단 경로 지하철 역 목록 조회됨
        최단_경로_지하철역_순서_정렬됨(pathResponse, Arrays.asList(서초역, 교대역, 강남역));
        // 총겨리 조회됨
        총거리_조회됨(pathResponse, 25);
        // 총요금 조회됨 (이호선 환승없음, 추가운임 0)
        총요금_조회됨(pathResponse, 1550);
    }

    @Test
    @DisplayName("15세 로그인 사용자 서초역-강남역 최단 거리 경로, 요금 조회")
    void loginMemberShortestPath_서초역_강남역_15세() {
        // Given
        // And: 회원 등록되어 있음
        회원_생성을_요청(EMAIL, PASSWORD, 15);
        // And: 로그인 되어 있음
        String accessToken = 로그인_되어_있음(EMAIL, PASSWORD);

        //when
        ExtractableResponse<Response> response = 로그인_사용자_경로_조회_요청(accessToken, 서초역.getId(), 강남역.getId());

        PathResponse pathResponse = response.as(PathResponse.class);
        //then
        // 최단 경로 지하철 역 목록 조회됨
        최단_경로_지하철역_순서_정렬됨(pathResponse, Arrays.asList(서초역, 교대역, 강남역));
        // 총겨리 조회됨
        총거리_조회됨(pathResponse, 25);
        // 총요금 조회됨 (이호선 환승없음, 추가운임 0)
        총요금_조회됨(pathResponse, 980);
    }

    @Test
    @DisplayName("8세 로그인 사용자 서초역-강남역 최단 거리 경로, 요금 조회")
    void loginMemberShortestPath_서초역_강남역_8세() {
        // Given
        // And: 회원 등록되어 있음
        회원_생성을_요청(EMAIL, PASSWORD, 8);
        // And: 로그인 되어 있음
        String accessToken = 로그인_되어_있음(EMAIL, PASSWORD);

        //when
        ExtractableResponse<Response> response = 로그인_사용자_경로_조회_요청(accessToken, 서초역.getId(), 강남역.getId());

        PathResponse pathResponse = response.as(PathResponse.class);
        //then
        // 최단 경로 지하철 역 목록 조회됨
        최단_경로_지하철역_순서_정렬됨(pathResponse, Arrays.asList(서초역, 교대역, 강남역));
        // 총겨리 조회됨
        총거리_조회됨(pathResponse, 25);
        // 총요금 조회됨 (이호선 환승없음, 추가운임 0)
        총요금_조회됨(pathResponse, 600);
    }

    private void 최단_경로_지하철역_순서_정렬됨(PathResponse pathResponse, List<StationResponse> expectedStations) {
        List<Long> stationIds = getIdsByStationResponses(pathResponse.getStations());
        List<Long> expectedStationIds = getIdsByStationResponses(expectedStations);
        // 역 순서 정렬
        assertThat(stationIds).containsExactlyElementsOf(expectedStationIds);
    }

    private void 총거리_조회됨(PathResponse pathResponse, int distance){
        // 최단 경로
        assertThat(pathResponse.getDistance()).isEqualTo(distance);
    }

    private void 총요금_조회됨(PathResponse pathResponse, int fare){
        // 총 요금
        assertThat(pathResponse.getFare()).isEqualTo(fare);
    }

    private ExtractableResponse<Response> 경로_조회_요청(Long source, Long target) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .param("source", source)
            .param("target", target)
            .when().get("/paths")
            .then().log().all()
            .extract();
    }

    private ExtractableResponse<Response> 로그인_사용자_경로_조회_요청(String accessToken, Long source, Long target) {
        return RestAssured
            .given().log().all()
            .auth().oauth2(accessToken)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .param("source", source)
            .param("target", target)
            .when().get("/paths")
            .then().log().all()
            .extract();
    }

}

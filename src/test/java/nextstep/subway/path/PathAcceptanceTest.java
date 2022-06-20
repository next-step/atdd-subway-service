package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 력삼역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;


    /**
     * 교대역    --- *2호선* ---   강남역   --- *2호선* ---  력삼역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        력삼역 = StationAcceptanceTest.지하철역_등록되어_있음("력삼역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest(
                "신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10, 1000)).as(LineResponse.class);
        이호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest(
                "이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 1000)).as(LineResponse.class);
        삼호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest(
                "삼호선", "bg-red-600", 교대역.getId(), 남부터미널역.getId(), 5)).as(LineResponse.class);

        지하철_노선에_지하철역_등록_요청(이호선, 강남역, 력삼역, 3);
        지하철_노선에_지하철역_등록_요청(삼호선, 남부터미널역, 양재역, 3);
    }

    /**
     * Given 지하철역, 노선, 구간이 등록 되어 있고
     * And  로그인이 되어 있지 않고
     * When 교대역에서 강남역을 최단거리 노선을 조회하면
     * Then 교대역-남부터미널역-양재역-강남역이 경로가 조회된다
     * and  거리가 조회된다
     * And  일반요금이 조회된다
     */
    @Test
    @DisplayName("최단거리 노선과 경로, 일반요금을 조회한다")
    void findPathWithNoMember() {
        //when
        ExtractableResponse<Response> response = 지하철_노선_최단거리_노선_조회(교대역.getId(), 강남역.getId());
        PathResponse pathResponse = response.as(PathResponse.class);

        //then
        assertThat(pathResponse.getStations()).hasSize(4);
        Assertions.assertAll(
                () -> assertThat(pathResponse.getStations().get(0).getId()).isEqualTo(교대역.getId()),
                () -> assertThat(pathResponse.getStations().get(1).getId()).isEqualTo(남부터미널역.getId()),
                () -> assertThat(pathResponse.getStations().get(2).getId()).isEqualTo(양재역.getId()),
                () -> assertThat(pathResponse.getStations().get(3).getId()).isEqualTo(강남역.getId()),
                () -> assertThat(pathResponse.getDistance()).isEqualTo(18),
                () -> assertThat(pathResponse.getFare()).isEqualTo(2450));
    }

    /**
     * Given 지하철역, 노선, 구간이 등록 되어있고
     * And  청소년 나이의 회원이 로그인 되어있고
     * When 교대역에서 강남역을 최단거리 노선을 조회하면
     * Then 교대역-남부터미널역-양재역-강남역이 경로가 조회된다
     * and  거리가 조회된다
     * And  청소년 요금이 조회된다
     */
    @Test
    @DisplayName("최단거리 노선과 경로, 청소년 요금을 조회한다")
    void findPathWithLogin() {
        //given
        MemberAcceptanceTest.회원_생성을_요청("minho@mino.com", "민호", 18);
        String accessToken = AuthAcceptanceTest.토큰_가져오기("minho@mino.com", "민호");

        //when
        ExtractableResponse<Response> response = 지하철_노선_최단거리_노선_조회(교대역.getId(), 강남역.getId(), accessToken);
        PathResponse pathResponse = response.as(PathResponse.class);

        //then
        assertThat(pathResponse.getStations()).hasSize(4);
        Assertions.assertAll(
                () -> assertThat(pathResponse.getStations().get(0).getId()).isEqualTo(교대역.getId()),
                () -> assertThat(pathResponse.getStations().get(1).getId()).isEqualTo(남부터미널역.getId()),
                () -> assertThat(pathResponse.getStations().get(2).getId()).isEqualTo(양재역.getId()),
                () -> assertThat(pathResponse.getStations().get(3).getId()).isEqualTo(강남역.getId()),
                () -> assertThat(pathResponse.getDistance()).isEqualTo(18),
                () -> assertThat(pathResponse.getFare()).isEqualTo(880));
    }

    /**
     * Given 지하철역, 노선, 구간이 등록되어 있고
     * When 등록되지 않은 지하철 역으로 경로를 조회하면
     * Then 에러가 발생한다
     */
    @Test
    @DisplayName("등록되지 않은 지하철역으로 경로를 조회하면 에러가 발생한다")
    void wrongStationError() {
        // when
        ExtractableResponse<Response> response = 지하철_노선_최단거리_노선_조회(교대역.getId(), -1L);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.asString()).isEqualTo("존재하지 않는 id 입니다. id=-1");
    }

    private ExtractableResponse<Response> 지하철_노선_최단거리_노선_조회(Long source, Long target) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().log().all()
                .get("/paths?source=" + source + "&target=" + target)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 지하철_노선_최단거리_노선_조회(Long source, Long target, String accessToken) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().log().all()
                .get("/paths?source=" + source + "&target=" + target)
                .then().log().all()
                .extract();
    }
}

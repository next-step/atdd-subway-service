package nextstep.subway.path;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.ApiRequest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.assertj.core.api.ListAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;


@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선, 이호선, 삼호선, 사호선;
    private StationResponse 강남역, 양재역, 교대역, 남부터미널역, 이촌역, 삼각지역, 새로운역1;
    private String email;
    private String password;
    private TokenResponse token;

    @BeforeEach
    public void setUp() {
        super.setUp();
        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        이촌역 = StationAcceptanceTest.지하철역_등록되어_있음("이촌역").as(StationResponse.class);
        삼각지역 = StationAcceptanceTest.지하철역_등록되어_있음("삼각지역").as(StationResponse.class);
        새로운역1 = StationAcceptanceTest.지하철역_등록되어_있음("새로운역1").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 10, 900);
        이호선 = 지하철_노선_등록되어_있음("이호선", "bg-red-600", 교대역, 강남역, 10, 0);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-red-600", 교대역, 양재역, 5, 0);
        사호선 = 지하철_노선_등록되어_있음("사호선", "bg-red-600", 이촌역, 삼각지역, 5, 500);

        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);

        email = "test@emila.com";
        password = "pass";
        MemberAcceptanceTest.회원_생성되어있음(email, password, 8);
        token = AuthAcceptanceTest.로그인을_요청한다(email, password).as(TokenResponse.class);
    }

    @DisplayName("지하철 경로를 조회한다.")
    @Test
    void searchPath() {
        LineAcceptanceTest.지하철_노선_조회_요청(신분당선);

        ExtractableResponse<Response> searchResponse = 지하철_경로를_지하철_출발역_도착역으로_경로_조회_요청_비로그인(강남역, 남부터미널역);
        최단_경로를_반영한_역들_거리_요금이_조회됨(searchResponse, 12, 2250, 강남역, 양재역, 남부터미널역);

        ExtractableResponse<Response> searchResponse2 = 지하철_경로를_지하철_출발역_도착역으로_경로_조회_요청_로그인(강남역, 남부터미널역);
        최단_경로를_반영한_역들_거리_요금이_조회됨(searchResponse2, 12, 950, 강남역, 양재역, 남부터미널역);

        ExtractableResponse<Response> searchResponseSameStation = 지하철_경로를_지하철_출발역_도착역으로_경로_조회_요청(강남역, 강남역);
        최단_경로_조회_실패(searchResponseSameStation);

        ExtractableResponse<Response> searchResponseUnConnectedStation = 지하철_경로를_지하철_출발역_도착역으로_경로_조회_요청(강남역, 이촌역);
        최단_경로_조회_실패(searchResponseUnConnectedStation);

        ExtractableResponse<Response> searchResponseUnRelatedStation = 지하철_경로를_지하철_출발역_도착역으로_경로_조회_요청(강남역, 새로운역1);
        최단_경로_조회_실패(searchResponseUnRelatedStation);
    }

    private void 최단_경로_조회_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    private void 최단_경로를_반영한_역들_거리_요금이_조회됨(ExtractableResponse<Response> searchResponse, int distance, int fare, StationResponse... stationResponses) {
        PathResponse pathResponse = searchResponse.as(PathResponse.class);
        assertAll(
                () -> assertThat(pathResponse.getDistance()).isEqualTo(distance),
                () -> assertThat(pathResponse.getFare()).isEqualTo(fare),
                () -> assertThat(pathResponse.getStations()).containsExactly(stationResponses)
        );
    }

    private ExtractableResponse<Response> 지하철_경로를_지하철_출발역_도착역으로_경로_조회_요청_비로그인(StationResponse source, StationResponse target) {
        return ApiRequest.get("/paths?source=" + source.getId() + "&target=" + target.getId());
    }

    private ExtractableResponse<Response> 지하철_경로를_지하철_출발역_도착역으로_경로_조회_요청_로그인(StationResponse source, StationResponse target) {
        return ApiRequest.get("/paths?source=" + source.getId() + "&target=" + target.getId(), token.getAccessToken());
    }

    private ExtractableResponse<Response> 지하철_경로를_지하철_출발역_도착역으로_경로_조회_요청(StationResponse source, StationResponse target) {
        return ApiRequest.get("/paths?source=" + source.getId() + "&target=" + target.getId(), token.getAccessToken());
    }

    public static LineResponse 지하철_노선_등록되어_있음(String name, String color, StationResponse upStation, StationResponse downStation, int distance, int extraFee) {
        return LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest(name, color, upStation.getId(), downStation.getId(), distance, extraFee))
                .as(LineResponse.class);
    }

    public static void 지하철_노선에_지하철역_결과_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }


}

package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private LineResponse 일호선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 청량리역;
    private StationResponse 신도림역;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;

    /**
     * 교대역    --- *2호선* ---   강남역          청량리역
     * |                        |               ㅣ
     * *3호선*                   *신분당선*       *1호선*
     * |                        |               ㅣ
     * 남부터미널역  --- *3호선* --- 양재역          신도림역
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        청량리역 = StationAcceptanceTest.지하철역_등록되어_있음("청량리역").as(StationResponse.class);
        신도림역 = StationAcceptanceTest.지하철역_등록되어_있음("신도림역").as(StationResponse.class);
        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 10, 300);
        일호선 = 지하철_노선_등록되어_있음("일호선", "bg-blue-600", 청량리역, 신도림역, 7, 400);
        이호선 = 지하철_노선_등록되어_있음("이호선", "bg-red-600", 교대역, 강남역, 10, 500);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-red-600", 교대역, 양재역, 5, 600);

        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);
    }

    @DisplayName("로그인 시 경로 조회 시 최단거리와 함께 기준 지하철 이용 요금이 응답됨.")
    @Test
    void findShortestPathWithFee() {
        // given
        String email = "yohan@test.com";
        String password = "password";
        MemberAcceptanceTest.회원_등록되어_있음(email, password, 10);
        String token = AuthAcceptanceTest.회원_로그인되어_있음(email, password);

        // when
        ExtractableResponse<Response> response = 지하철_경로_조회_요청(강남역.getId(), 남부터미널역.getId(), token);

        // then 1250원(12Km) + 600원(3호선) -  750원(어린이할인) = 1100원
        지하철_최단경로_응답됨(response, 강남역.getId(), 양재역.getId(), 남부터미널역.getId());
        지하철_이용요금_응답됨(response,1100);
    }

    @DisplayName("비로그인 시 경로 조회 시 최단거리 기준 지하철 이용 요금이 응답되지 않음.")
    @Test
    void findShortestPathWithoutFee() {
        // when
        ExtractableResponse<Response> response = 지하철_경로_조회_요청(강남역.getId(), 남부터미널역.getId());

        // then
        지하철_최단경로_응답됨(response, 강남역.getId(), 양재역.getId(), 남부터미널역.getId());
        지하철_이용요금_응답되지않음(response);
    }

    @DisplayName("지하철 구간의 최단 경로를 찾는다.")
    @Test
    void findShortestPath() {
        // when
        ExtractableResponse<Response> response = 지하철_경로_조회_요청(강남역.getId(), 남부터미널역.getId());

        // then
        지하철_최단경로_응답됨(response, 강남역.getId(), 양재역.getId(), 남부터미널역.getId());
    }

    @DisplayName("출발역과 도착역이 같은 경우")
    @Test
    void sameStation() {
        // when
        ExtractableResponse<Response> response = 지하철_경로_조회_요청(강남역.getId(), 강남역.getId());

        // then
        지하철_최단경로_응답_실패됨(response);
    }

    @DisplayName("출발역과 도착역이 연결이 되어 있지 않은 경우")
    @Test
    void notConnectedStation() {
        // when
        ExtractableResponse<Response> response = 지하철_경로_조회_요청(강남역.getId(), 신도림역.getId());

        // then
        지하철_최단경로_응답_실패됨(response);
    }

    @DisplayName("존재하지 않은 출발역이나 도착역을 조회 할 경우 예외처리 한다.")
    @Test
    void notExistedStation() {
        // given
        StationResponse 당정역 = StationAcceptanceTest.지하철역_등록되어_있음("당정역").as(StationResponse.class);
        StationResponse 금정역 = StationAcceptanceTest.지하철역_등록되어_있음("금정역").as(StationResponse.class);

        // when
        ExtractableResponse<Response> response1 = 지하철_경로_조회_요청(당정역.getId(), 강남역.getId());
        ExtractableResponse<Response> response2 = 지하철_경로_조회_요청(강남역.getId(), 당정역.getId());
        ExtractableResponse<Response> response3 = 지하철_경로_조회_요청(금정역.getId(), 당정역.getId());

        // then
        지하철_최단경로_응답_실패됨(response1);
        지하철_최단경로_응답_실패됨(response2);
        지하철_최단경로_응답_실패됨(response3);
    }


    private LineResponse 지하철_노선_등록되어_있음(final String name, final String color, final StationResponse upStation,
                                        final StationResponse downStation, final int distance, final int surcharge) {
        LineRequest lineRequest = new LineRequest(name, color, upStation.getId(), downStation.getId(), distance, surcharge);
        return LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);
    }

    private ExtractableResponse<Response> 지하철_경로_조회_요청(final Long source, final Long target) {
        String path = String.format("/paths?source=%d&target=%d", source, target);
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get(path)
                .then()
                .log().all()
                .extract();
        return response;
    }

    private ExtractableResponse<Response> 지하철_경로_조회_요청(final Long source, final Long target, final String token) {
        String path = String.format("/paths?source=%d&target=%d", source, target);
        ExtractableResponse<Response> response = RestAssured
                .given().log().all().auth().oauth2(token)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get(path)
                .then()
                .log().all()
                .extract();
        return response;
    }

    private void 지하철_최단경로_응답됨(final ExtractableResponse<Response> response, final Long... expectedStationIds) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<Long> actualStationIds = response.jsonPath()
                .getList("stations", PathResponse.StationResponse.class).stream()
                .map(PathResponse.StationResponse::getId)
                .collect(Collectors.toList());
        assertThat(actualStationIds).containsExactly(expectedStationIds);
    }

    private void 지하철_최단경로_응답_실패됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private void 지하철_이용요금_응답됨(final ExtractableResponse<Response> response, final int expectedFee) {
        Integer fee = response.jsonPath().get("fee");
        assertThat(fee).isNotNull();
        assertThat(fee).isEqualTo(expectedFee);
    }

    private void 지하철_이용요금_응답되지않음(final ExtractableResponse<Response> response) {
        Long fee = response.jsonPath().get("fee");
        assertThat(fee).isNull();
    }
}

package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_요청;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.member.MemberAcceptanceTest.*;
import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private LineResponse 칠호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남강역;
    private StationResponse 대교역;
    private StationResponse 널미터부남역;
    private StationResponse 남부터미널역;
    private StationResponse 노원역;
    private StationResponse 군자역;
    private final int 이호선_추가요금 = 1000;
    private final int 신분당선_추가요금 = 800;
    private final int 삼호선_추가요금 = 1200;
    private final int 대교역에서_널미터부남역_최단거리 = 59;
    private final int 성인과_비로그인유저_요금 = 3450;
    private final int 청소년_요금 = 2480;
    private final int 어린이_요금 = 1550;
//    private int distanceBetween강남역and양재역 = 27;
//    private int distanceBetween교대역and강남역 = 30;
//    private int distanceBetween교대역and양재역 = 5;

    /**
     * 요금 관련 인수테스트는 ( 비로그인+성인 / 청소년 / 어린이 ) 셋 다 같은 경로를 조회한다.
     * 대교역 ~ 널미터부남역을 조회하여,
     * 거리는: 11+17+10+21 = 59
     * 금액은
     *   비로그인/성인 : 3450 원 = 1250(기본요금) + 800(10~ 50km 5km당요금) + 200(50km 이상 8km당 요금) + 1200(노선별 요금 중 최고금액)
     *   청소년       : 2480 원 = 3450 - 350 * 0.8
     *   어린이       : 1550 원 = 3450 - 350 * 0.5
     *
     * 2호선 추가요금 1000원
     * 신분당선 추가요금 800원
     * 3호선 추가요금 1200원
     * 7호선 추가요금 0원
     * 
     * 교대역    --- *2호선* ---     강남역 --- *2호선* ---   대교역
     * |            30                |         11
     * |                         *신분당선* 17
     * |                              |
     * |                            남강역
     * |                              |
     * *3호선* 43                 *신분당선* 10
     * |                              |
     * 남부터미널역  --- *3호선* ---   양재   --- *3호선* --- 널미터부남역
     *                  2                       21
     *
     *          (연결안됨)
     * 노원역 --- *7호선* --- 군자역
     *             77
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남강역 = StationAcceptanceTest.지하철역_등록되어_있음("남강역").as(StationResponse.class);
        대교역 = StationAcceptanceTest.지하철역_등록되어_있음("대교역").as(StationResponse.class);
        노원역 = StationAcceptanceTest.지하철역_등록되어_있음("노원역").as(StationResponse.class);
        군자역 = StationAcceptanceTest.지하철역_등록되어_있음("군자역").as(StationResponse.class);
        널미터부남역 = StationAcceptanceTest.지하철역_등록되어_있음("널미터부남역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음(
                new LineRequest("신분당선", "bg-red-600", 신분당선_추가요금, 강남역.getId(), 양재역.getId(), 27))
                .as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음(
                new LineRequest("이호선", "bg-red-600", 이호선_추가요금, 교대역.getId(), 강남역.getId(), 30))
                .as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(
                new LineRequest("삼호선", "bg-red-600", 삼호선_추가요금, 교대역.getId(), 양재역.getId(), 5))
                .as(LineResponse.class);
        칠호선 = 지하철_노선_등록되어_있음(
                new LineRequest("칠호선", "bg-red-600", 노원역.getId(), 군자역.getId(), 77))
                .as(LineResponse.class);

        지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 43);
        지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 남강역, 17);
        지하철_노선에_지하철역_등록_요청(이호선, 강남역, 대교역, 11);
        지하철_노선에_지하철역_등록_요청(삼호선, 양재역, 널미터부남역, 21);

    }

    @Test
    void 두_역의_최단거리요금을_조회하는데_어린이_로그인유저_이다() {
        // Given 로그인 어린이 나이
        어린이_회원_등록되어_있음(EMAIL, PASSWORD);
        ExtractableResponse<Response> tokenResponse = 로그인_요청(EMAIL, PASSWORD);
        String token = tokenResponse.as(TokenResponse.class).getAccessToken();
        
        // When 로그인유저가 출발역~ 도착역 최단거리 경로조회한다.
        ExtractableResponse<Response> pathResponse = 로그인유저가_출발역과_도착역으로_최단경로를_찾는다(
                token,
                PathRequest.of(대교역.getId(), 널미터부남역.getId()));
        // Then 최단 거리 경로를 정상 응답한다.
        assertThat(pathResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        역_사이의_최단경로_역들을_확인한다(pathResponse, Arrays.asList(대교역, 강남역, 남강역, 양재역, 널미터부남역));

        // And 요금이 정상이다.
        역_사이의_요금을_확인한다(pathResponse, 어린이_요금);
    }

    @Test
    void 두_역의_최단거리요금을_조회하는데_청소년_로그인유저_이다() {
        // Given 로그인 청소년 나이
        청소년_회원_등록되어_있음(EMAIL, PASSWORD);
        ExtractableResponse<Response> tokenResponse = 로그인_요청(EMAIL, PASSWORD);
        String token = tokenResponse.as(TokenResponse.class).getAccessToken();
        
        // When 로그인유저가 출발역~ 도착역 최단거리 경로조회한다.
        ExtractableResponse<Response> pathResponse = 로그인유저가_출발역과_도착역으로_최단경로를_찾는다(
                token,
                PathRequest.of(대교역.getId(), 널미터부남역.getId()));

        // Then 최단 거리 경로를 정상 응답한다.
        assertThat(pathResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        역_사이의_최단경로_역들을_확인한다(pathResponse, Arrays.asList(대교역, 강남역, 남강역, 양재역, 널미터부남역));

        // And 요금이 정상이다.
        역_사이의_요금을_확인한다(pathResponse, 청소년_요금);
    }

    @Test
    void 두_역의_최단거리와_요금을_조회하는데_로그인을_안했거나_성인이다() {
//        When 출발역에서 도착역까지의 최단 거리 경로 조회를 요청
        PathRequest pathRequest = PathRequest.of(대교역.getId(), 널미터부남역.getId());
        ExtractableResponse<Response> pathResponse = 출발역과_도착역으로_최단경로를_찾는다(pathRequest);
//        Then 최단 거리 경로를 응답
        assertThat(pathResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        역_사이의_최단경로_역들을_확인한다(pathResponse, Arrays.asList(대교역, 강남역, 남강역, 양재역, 널미터부남역));

//        And 총 거리도 함께 응답함
        역_사이의_최단거리를_확인한다(pathResponse, 대교역에서_널미터부남역_최단거리);

//        And ** 지하철 이용 요금도 함께 응답함 **
        역_사이의_요금을_확인한다(pathResponse, 성인과_비로그인유저_요금);
    }

    @Test
    void 없는_역의_경로를_확인해서_실패한다() {
        Long 등록안된역의_ID = 111L;
        // When
        ExtractableResponse<Response> pathsResponse = 출발역과_도착역으로_최단경로를_찾는다(PathRequest.of(강남역.getId(), 등록안된역의_ID));

        // Then
        assertThat(pathsResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void 연결되지_않은_출발역과_도착역의_최단거리를_확인해서_실패한다() {
        // When
        ExtractableResponse<Response> pathsResponse = 출발역과_도착역으로_최단경로를_찾는다(PathRequest.of(노원역.getId(), 강남역.getId()));

        // Then
        assertThat(pathsResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void 출발역과_도착역이_같은_경우의_최단거리를_확인해서_실패한다() {
        // When
        ExtractableResponse<Response> pathsResponse = 출발역과_도착역으로_최단경로를_찾는다(PathRequest.of(강남역.getId(), 강남역.getId()));

        // Then
        assertThat(pathsResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void 등록된_노선들에서_출발역과_도착역의_최단거리를_확인한다() {
        // Given (setUp())

        // When
        ExtractableResponse<Response> pathResponse = 출발역과_도착역으로_최단경로를_찾는다(PathRequest.of(대교역.getId(), 널미터부남역.getId()));

        // Then
        assertThat(pathResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        역_사이의_최단경로_역들을_확인한다(pathResponse, Arrays.asList(대교역, 강남역, 남강역, 양재역, 널미터부남역));
        역_사이의_최단거리를_확인한다(pathResponse, 59);
    }

    private ExtractableResponse<Response> 출발역과_도착역으로_최단경로를_찾는다(PathRequest pathRequest) {
        return RestAssured
                .given().log().all()
                .when().get("/paths?source={sourceId}&target={targetId}", pathRequest.getSourceStationId(), pathRequest.getTargetStationId())
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 로그인유저가_출발역과_도착역으로_최단경로를_찾는다(String token, PathRequest pathRequest) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .when().get("/paths?source={sourceId}&target={targetId}", pathRequest.getSourceStationId(), pathRequest.getTargetStationId())
                .then().log().all()
                .extract();
    }

    private void 역_사이의_최단경로_역들을_확인한다(ExtractableResponse<Response> pathResponse,
                                     List<StationResponse> expectedStations) {
        PathResponse path = pathResponse.as(PathResponse.class);

        List<Long> stationIds = path.getStations().stream()
                .map(it -> it.getId())
                .collect(Collectors.toList());
        List<Long> expectedStationIds = expectedStations.stream()
                .map(it -> it.getId())
                .collect(Collectors.toList());

        assertThat(stationIds).containsExactlyElementsOf(expectedStationIds);
    }

    private void 역_사이의_최단거리를_확인한다(ExtractableResponse<Response> pathResponse, int distance) {
        PathResponse path = pathResponse.as(PathResponse.class);
        assertThat(path.getDistance()).isEqualTo(distance);
    }

    private void 역_사이의_요금을_확인한다(ExtractableResponse<Response> pathResponse, int fare) {
        PathResponse path = pathResponse.as(PathResponse.class);
        assertThat(path.getFare()).isEqualTo(fare);
    }
}

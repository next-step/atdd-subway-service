package nextstep.subway.path.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.assertj.core.api.AbstractIntegerAssert;
import org.assertj.core.api.ListAssert;
import org.assertj.core.api.ObjectAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.*;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_요청;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.member.MemberAcceptanceTest.*;
import static nextstep.subway.member.MemberAcceptanceTest.AGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;


@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {

    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private String 토큰;

    /**                 10
     * 교대역  ---   *2호선*   ---   강남역
     *   |                             |
     * *3호선* 3                  *신분당선* 10
     *   |                  2          |
     * 남부터미널역  --- *3호선* ---  양재
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 10, 0).as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음("이호선", "bg-red-600", 교대역, 강남역, 10, 500).as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-red-600", 교대역, 양재역, 5, 900).as(LineResponse.class);

        지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 남부터미널역, 3);

        회원_생성을_요청(EMAIL, PASSWORD, TEENAGER_AGE);
        토큰 = 로그인_요청(EMAIL, PASSWORD).as(TokenResponse.class).getAccessToken();
    }

    @Test
    void 최단_경로_조회() {
        // when
        ExtractableResponse<Response> response = 최단_경로_조회_요청(강남역, 양재역);
        PathResponse pathResponse = response.as(PathResponse.class);

        // then
        assertAll(
                () -> 최단_경로_거리_조회_응답됨(response),
                () -> 최단_경로_거리_확인됨(pathResponse, 10),
                () -> 최단_경로_지하철역_순서_정렬됨(pathResponse, Arrays.asList(강남역, 양재역)),
                () -> 최단_경로_요금_확인됨(pathResponse, 1250)
        );
    }

    @Test
    void 청소년_최단_경로_조회() {
        // when
        ExtractableResponse<Response> response = 토큰포함_최단_경로_조회_요청(강남역, 양재역, 토큰);
        PathResponse pathResponse = response.as(PathResponse.class);

        // then
        assertAll(
                () -> 최단_경로_거리_조회_응답됨(response),
                () -> 최단_경로_거리_확인됨(pathResponse, 10),
                () -> 최단_경로_지하철역_순서_정렬됨(pathResponse, Arrays.asList(강남역, 양재역)),
                () -> 최단_경로_요금_확인됨(pathResponse, 720)
        );
    }

    @Test
    void 환승을_포함한_최단_경로_조회() {
        // when
        ExtractableResponse<Response> response = 최단_경로_조회_요청(강남역, 남부터미널역);
        PathResponse pathResponse = response.as(PathResponse.class);

        // then
        assertAll(
                () -> 최단_경로_거리_조회_응답됨(response),
                () -> 최단_경로_거리_확인됨(pathResponse, 12),
                () -> 최단_경로_지하철역_순서_정렬됨(pathResponse, Arrays.asList(강남역, 양재역, 남부터미널역)),
                () -> 최단_경로_요금_확인됨(pathResponse, 2250)
        );
    }

    @Test
    void 환승을_포함하지_않는_최단_경로_조회() {
        // when
        ExtractableResponse<Response> response = 최단_경로_조회_요청(교대역, 양재역);
        PathResponse pathResponse = response.as(PathResponse.class);

        // then
        assertAll(
                () -> 최단_경로_거리_조회_응답됨(response),
                () -> 최단_경로_거리_확인됨(pathResponse, 5),
                () -> 최단_경로_지하철역_순서_정렬됨(pathResponse, Arrays.asList(교대역, 남부터미널역, 양재역)),
                () -> 최단_경로_요금_확인됨(pathResponse, 2150)
        );
    }

    @Test
    void 존재하지_않은_출발역이나_도착역을_조회_할_경우() {
        // given
        StationResponse 존재하지_않는_역 = new StationResponse(99L, "없는역", null, null);

        // when
        ExtractableResponse<Response> response = 최단_경로_조회_요청(교대역, 존재하지_않는_역);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private AbstractIntegerAssert<?> 최단_경로_거리_조회_응답됨(ExtractableResponse<Response> response) {
        return assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private ObjectAssert<Distance> 최단_경로_거리_확인됨(PathResponse pathResponse, int distance) {
        return assertThat(pathResponse.getDistance()).isEqualTo(new Distance(distance));
    }

    private ListAssert<String> 최단_경로_지하철역_순서_정렬됨(PathResponse pathResponse, List<StationResponse> stations) {
        List<String> actual = pathResponse.getStations().stream()
                .map(StationResponse::getName)
                .collect(toList());

        List<String> expected = stations.stream()
                .map(StationResponse::getName)
                .collect(toList());

        return assertThat(actual).containsExactlyElementsOf(expected);
    }

    private ExtractableResponse<Response> 최단_경로_조회_요청(StationResponse sourceStation, StationResponse targetStation) {
         return RestAssured.given().log().all()
                .when().get("paths?source={source}&target={target}", sourceStation.getId(), targetStation.getId())
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 토큰포함_최단_경로_조회_요청(StationResponse sourceStation, StationResponse targetStation, String accessToken) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .when().get("paths?source={source}&target={target}", sourceStation.getId(), targetStation.getId())
                .then().log().all()
                .extract();
    }

    private AbstractIntegerAssert<?> 최단_경로_요금_확인됨(PathResponse pathResponse, int fare) {
        return assertThat(pathResponse.getFare()).isEqualTo(fare);
    }

}

package nextstep.subway.path;

import static java.time.LocalDateTime.*;
import static java.util.stream.Collectors.*;
import static nextstep.subway.exception.ExceptionMessage.*;
import static nextstep.subway.member.MemberAcceptanceTest.*;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;

@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {

    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private String 성인토큰;
    private String 청소년토큰;
    private String 어린이토큰;

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

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 10);
        이호선 = 지하철_노선_등록되어_있음("이호선", "bg-red-600", 교대역, 강남역, 10);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-red-600", 교대역, 양재역, 5);

        지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);

        성인토큰 = 회원생성_후_로그인(20);
        청소년토큰 = 회원생성_후_로그인(13);
        어린이토큰 = 회원생성_후_로그인(6);
    }

    private String 회원생성_후_로그인(int age) {
        String email = age + "@email.com";
        회원_생성을_요청(email, PASSWORD, age);
        return 로그인_정상_토큰_발급(email, PASSWORD);
    }

    @DisplayName("로그인하지 않고 최단 경로를 조회한다")
    @Test
    void paths1() {
        // when
        ExtractableResponse<Response> pathsResponse = 최단_경로_조회_요청(null, 강남역, 남부터미널역);

        // then
        최단_경로_및_요금_응답(pathsResponse, Arrays.asList(강남역, 양재역, 남부터미널역), 12, 1250 + 100);
    }

    @DisplayName("존재하지 않은 출발역이나 도착역을 조회할 경우")
    @Test
    void paths2() {
        // when
        ExtractableResponse<Response> pathsResponse = 최단_경로_조회_요청(null, new StationResponse(98L, "왕십리역", now(), now()),
            new StationResponse(99L, "강남역", now(), now()));

        // then
        assertThat(pathsResponse.jsonPath().getString("message")).isEqualTo(NO_STATION);
    }

    @DisplayName("성인으로 로그인 후 최단 경로를 조회한다")
    @Test
    void paths3() {
        // when
        ExtractableResponse<Response> pathsResponse = 최단_경로_조회_요청(성인토큰, 강남역, 남부터미널역);

        // then
        최단_경로_및_요금_응답(pathsResponse, Arrays.asList(강남역, 양재역, 남부터미널역), 12, 1250 + 100);
    }

    @DisplayName("청소년으로 로그인 후 최단 경로를 조회한다")
    @Test
    void paths4() {
        // when
        ExtractableResponse<Response> pathsResponse = 최단_경로_조회_요청(청소년토큰, 강남역, 남부터미널역);

        // then
        최단_경로_및_요금_응답(pathsResponse, Arrays.asList(강남역, 양재역, 남부터미널역), 12, (int)((1250 + 100 - 350) * 0.8));
    }

    @DisplayName("어린이로 로그인 후 최단 경로를 조회한다")
    @Test
    void paths5() {
        // when
        ExtractableResponse<Response> pathsResponse = 최단_경로_조회_요청(어린이토큰, 강남역, 남부터미널역);

        // then
        최단_경로_및_요금_응답(pathsResponse, Arrays.asList(강남역, 양재역, 남부터미널역), 12, (int)((1250 + 100 - 350) * 0.5));
    }

    private ExtractableResponse<Response> 최단_경로_조회_요청(String token, StationResponse source, StationResponse target) {
        RequestSpecification requestSpec = RestAssured
            .given().log().all();
        if (!Objects.isNull(token)) {
            requestSpec = requestSpec.auth().oauth2(token);
        }
        return requestSpec.accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get(String.format("/paths?source=%d&target=%d", source.getId(), target.getId()))
            .then().log().all()
            .extract();

    }

    private void 최단_경로_및_요금_응답(ExtractableResponse<Response> response, List<StationResponse> stationResponses,
        int distance, int fare) {
        PathResponse path = response.as(PathResponse.class);

        List<PathResponse.PathStationResponse> stations = path.getStations();
        List<Long> stationIds = stations.stream()
            .map(PathResponse.PathStationResponse::getId)
            .collect(toList());
        List<String> stationNames = stations.stream()
            .map(PathResponse.PathStationResponse::getName)
            .collect(toList());

        assertThat(stationIds).containsExactlyElementsOf(
            stationResponses.stream().map(StationResponse::getId).collect(toList()));
        assertThat(stationNames).containsExactlyElementsOf(
            stationResponses.stream().map(StationResponse::getName).collect(toList()));
        List<LocalDateTime> createAtList = stations.stream()
            .map(PathResponse.PathStationResponse::getCreatedAt)
            .collect(toList());
        assertThat(createAtList).hasSize(stationResponses.size());
        assertThat(createAtList).doesNotContainNull();
        assertThat(path.getDistance()).isEqualTo(distance);
        assertThat(path.getFare()).isEqualTo(fare);
    }

    public static LineResponse 지하철_노선_등록되어_있음(String name, String color,
        StationResponse upStationResponse, StationResponse downStationResponse, int distance) {
        return 지하철_노선_생성_요청(
            new LineRequest(name, color, upStationResponse.getId(), downStationResponse.getId(), distance))
            .as(LineResponse.class);
    }

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(LineRequest params) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(params)
            .when().post("/lines")
            .then().log().all().
                extract();
    }

    public static void 지하철_노선에_지하철역_등록되어_있음(LineResponse line, StationResponse upStation, StationResponse downStation,
        int distance) {
        SectionRequest sectionRequest = new SectionRequest(upStation.getId(), downStation.getId(),
            new Distance(distance));

        RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(sectionRequest)
            .when().post("/lines/{lineId}/sections", line.getId())
            .then().log().all()
            .extract();
    }
}

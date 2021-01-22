package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTestSupport.getAccessToken;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTestSupport.토큰_발급_요청;
import static nextstep.subway.member.acceptance.MemberAcceptanceTestSupport.*;
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
    private StationResponse 인천역;
    private String 토큰;

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
        인천역 = StationAcceptanceTest.지하철역_등록되어_있음("인천역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 50);
        이호선 = 지하철_노선_등록되어_있음("이호선", "bg-green-200", 교대역.getId(), 강남역.getId(), 60);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-yellow-300", 교대역.getId(), 양재역.getId(), 60);

        지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 30);

        회원_생성을_요청(EMAIL, PASSWORD, AGE);
        토큰 = getAccessToken(토큰_발급_요청(EMAIL, PASSWORD));
    }

    @DisplayName("최단경로 조회 - 성인 요금할인 없음")
    @Test
    void findShortPathForAdult() {
        // When
        ExtractableResponse<Response> response = 지하철_노선_경로탐색_요청(토큰, 교대역, 양재역);
        // Then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.as(PathResponse.class).getStations()).hasSize(3),
                () -> assertThat(response.as(PathResponse.class).getDistance()).isEqualTo(60),
                () -> assertThat(response.as(PathResponse.class).getFare()).isEqualTo(1250 + 800 + 200)
        );
    }

    @DisplayName("최단경로 조회 - 어린이(6세~12세) 요금할인 350원을 공제한 금액의 50% 할인")
    @Test
    void findShortPathForChildren() {
        // Given
        내_정보_수정_요청(토큰, EMAIL, PASSWORD, 6);
        // When
        ExtractableResponse<Response> response = 지하철_노선_경로탐색_요청(토큰, 교대역, 양재역);
        // Then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.as(PathResponse.class).getStations()).hasSize(3),
                () -> assertThat(response.as(PathResponse.class).getDistance()).isEqualTo(60),
                () -> assertThat(response.as(PathResponse.class).getFare()).isEqualTo((int) ((1250 - 350 + 800 + 200) * 0.5))
        );
    }

    @DisplayName("최단경로 조회 - 청소년(13세~18세) 요금할인 없음 350원을 공제한 금액의 20% 할인")
    @Test
    void findShortPathTestForYouth() {
        // Given
        내_정보_수정_요청(토큰, EMAIL, PASSWORD, 18);
        // When
        ExtractableResponse<Response> response = 지하철_노선_경로탐색_요청(토큰, 교대역, 양재역);
        // Then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.as(PathResponse.class).getStations()).hasSize(3),
                () -> assertThat(response.as(PathResponse.class).getDistance()).isEqualTo(60),
                () -> assertThat(response.as(PathResponse.class).getFare()).isEqualTo((int) ((1250 - 350 + 800 + 200) * 0.8))
        );
    }

    @DisplayName("예외 상황 - 출발역과 도착역이 같은 경우")
    @Test
    void exceptionToSearchPathOfSameStation() {
        // When
        ExtractableResponse<Response> response = 지하철_노선_경로탐색_요청(토큰, 교대역, 교대역);
        // Then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("예외 상황 - 출발역과 도착역이 연결이 되어 있지 않은 경우")
    @Test
    void exceptionToSearchPathOfUnconnectedStation() {
        // When
        ExtractableResponse<Response> response = 지하철_노선_경로탐색_요청(토큰, 교대역, 인천역);
        // Then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("예외 상황 - 존재하지 않은 출발역이나 도착역을 조회 할 경우")
    @Test
    void exceptionToSearchPathOfNotExistedStation() {
        // Given
        StationResponse 홍대역 = new StationResponse(5L, "홍대역", LocalDateTime.now(), LocalDateTime.now());
        // When
        ExtractableResponse<Response> response = 지하철_노선_경로탐색_요청(토큰, 교대역, 홍대역);
        // Then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private ExtractableResponse<Response> 지하철_노선_경로탐색_요청(String accessToken, StationResponse source, StationResponse target) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("sourceId", source.getId())
                .pathParam("targetId", target.getId())
                .when().get("/paths?source={sourceId}&target={targetId}")
                .then().log().all().extract();
    }

    private void 지하철_노선에_지하철역_등록되어_있음(LineResponse line, StationResponse upStation, StationResponse downStation, int distance) {
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(line, upStation, downStation, distance);
    }

    private LineResponse 지하철_노선_등록되어_있음(String line, String color, Long upStationId, Long downStationId, int distance) {
        return LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest(line, color, upStationId, downStationId, distance))
                .as(LineResponse.class);
    }
}

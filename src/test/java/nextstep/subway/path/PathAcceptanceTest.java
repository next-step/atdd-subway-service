package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.ui.dto.PathResponse;
import nextstep.subway.path.ui.dto.StationInPathResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_됨;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록되어_있음;
import static nextstep.subway.member.MemberAcceptanceTest.회원_등록되어_있음;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    // given
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private LineResponse srt;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 용산역;
    private StationResponse 천안역;
    private String 청소년;
    private String 아동;
    private String 성인;

    /*
     * 교대역    --- *2호선*(10m) ---   강남역 --- *SRT*(90m) ---  용산역
     * |                               |                       |
     * *3호선(3m)*                      *신분당선*(15m)           *SRT*(100m)
     * |                               |                       |
     * 남부터미널역  --- *3호선*(7m) ---   양재                     천안역
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        용산역 = 지하철역_등록되어_있음("용산역").as(StationResponse.class);
        천안역 = 지하철역_등록되어_있음("천안역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음(
                new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 15, BigDecimal.valueOf(400)))
                .as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음(
                new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 10, BigDecimal.valueOf(500)))
                .as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(
                new LineRequest("삼호선", "bg-red-600", 교대역.getId(), 양재역.getId(), 10, BigDecimal.valueOf(300)))
                .as(LineResponse.class);
        srt = 지하철_노선_등록되어_있음(
                new LineRequest("srt", "bg-red-600", 용산역.getId(), 천안역.getId(), 100, BigDecimal.valueOf(1000)))
                .as(LineResponse.class);

        지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);
        지하철_노선에_지하철역_등록되어_있음(srt, 강남역, 용산역, 90);

        String teenEmail = "test@nextstep.com";
        String teenPassword = "password";
        Integer teenAge = 15;

        String kidEmail = "test2@nextstep.com";
        String kidPassword = "password";
        Integer kidAge = 8;

        String adultEmail = "test3@nextstep.com";
        String adultPassword = "password";
        Integer adultAge = 32;

        회원_등록되어_있음(teenEmail, teenPassword, teenAge);
        청소년 = 로그인_됨(teenEmail, teenPassword);

        회원_등록되어_있음(kidEmail, kidPassword, kidAge);
        아동 = 로그인_됨(kidEmail, kidPassword);

        회원_등록되어_있음(adultEmail, adultPassword, adultAge);
        성인 = 로그인_됨(adultEmail, adultPassword);
    }

    @DisplayName("시나리오1: 최단 경로를 조회할 수 있다.")
    @Test
    void findShortestPathTest() {
        // when
        ExtractableResponse<Response> response = 최단_경로_조회_요청(교대역, 양재역, 청소년);

        // then
        최단_경로_조회_성공(response, Arrays.asList(교대역, 남부터미널역, 양재역), 10);
        요금이_정상적으로_계산됨(response, BigDecimal.valueOf(720));
    }

    @DisplayName("시나리오2: 출발역과 도착역이 같은 최단 경로 조회")
    @Test
    void findShortestPathFailBySameSourceDestinationTest() {
        // when
        ExtractableResponse<Response> response = 최단_경로_조회_요청(교대역, 교대역, 청소년);

        // then
        최단_경로_조회_실패(response);
    }

    @DisplayName("시나리오3: 경로에 없는 역의 최단 경로를 조회")
    @Test
    void findShortestPathWithNotInLineStation() {
        // given
        StationResponse 공사중인역 = 지하철역_등록되어_있음("공사중인역").as(StationResponse.class);

        // when
        ExtractableResponse<Response> response = 최단_경로_조회_요청(교대역, 공사중인역, 청소년);

        // then
        최단_경로_조회_실패(response);
    }

    @DisplayName("시나리오4: 갈 수 없는 경로의 최단 경로 조회")
    @Test
    void findShortestPathWithoutConnection() {
        // given
        /*
         * 교대역    --- *2호선* ---   강남역               용인역
         * |                        |                    |
         * *3호선*                   *신분당선*              *경강선*
         * |                        |                    |
         * 남부터미널역  --- *3호선* ---   양재             에버랜드역
        */
        StationResponse 못가는역 = 기존_노선과_접점이_없는_지하철_노선_등록됨();

        // when
        ExtractableResponse<Response> response = 최단_경로_조회_요청(교대역, 못가는역, 청소년);

        // then
        최단_경로_조회_실패(response);
    }

    @DisplayName("시나리오5: 연령별로 요금 할인이 다르게 적용된다.")
    @Test
    void discountByAgeTest() {
        // when
        ExtractableResponse<Response> adultResponse = 최단_경로_조회_요청(교대역, 양재역, 성인);
        // then
        최단_경로_조회_성공(adultResponse, Arrays.asList(교대역, 남부터미널역, 양재역), 10);
        요금이_정상적으로_계산됨(adultResponse, BigDecimal.valueOf(1250));

        // when
        ExtractableResponse<Response> teenResponse = 최단_경로_조회_요청(교대역, 양재역, 청소년);
        // then
        최단_경로_조회_성공(teenResponse, Arrays.asList(교대역, 남부터미널역, 양재역), 10);
        요금이_정상적으로_계산됨(teenResponse, BigDecimal.valueOf(720));

        // when
        ExtractableResponse<Response> kidResponse = 최단_경로_조회_요청(교대역, 양재역, 아동);
        // then
        최단_경로_조회_성공(kidResponse, Arrays.asList(교대역, 남부터미널역, 양재역), 10);
        요금이_정상적으로_계산됨(kidResponse, BigDecimal.valueOf(450));
    }

    @DisplayName("시나리오6: 거리에 따라 요금이 차등 부과된다.")
    @Test
    void chargeByDistanceTest() {
        // when
        ExtractableResponse<Response> shortDistanceResponse = 최단_경로_조회_요청(교대역, 양재역, 성인);
        // then
        요금이_정상적으로_계산됨(shortDistanceResponse, BigDecimal.valueOf(1250));

        // when
        ExtractableResponse<Response> longDistanceResponse = 최단_경로_조회_요청(강남역, 양재역, 성인);
        // then
        요금이_정상적으로_계산됨(longDistanceResponse, BigDecimal.valueOf(1350));

        // when
        ExtractableResponse<Response> tooLongDistanceResponse = 최단_경로_조회_요청(용산역, 천안역, 성인);
        // then
        요금이_정상적으로_계산됨(tooLongDistanceResponse, BigDecimal.valueOf(2750));
    }

    @DisplayName("시나리오7: 같은 거리라도 환승 추가금에 따라 요금이 다르다.")
    @Test
    void chargeByTransferTest() {
        // when
        ExtractableResponse<Response> notTransferResponse = 최단_경로_조회_요청(천안역, 용산역, 성인);
        ExtractableResponse<Response> transferResponse = 최단_경로_조회_요청(교대역, 용산역, 성인);

        // then
        두_경로의_이동거리가_같다(notTransferResponse, transferResponse);
        요금이_정상적으로_계산됨(notTransferResponse, BigDecimal.valueOf(2750));
        요금이_정상적으로_계산됨(transferResponse, BigDecimal.valueOf(3750));
    }

    public static void 두_경로의_이동거리가_같다(ExtractableResponse<Response> response1, ExtractableResponse<Response> response2) {
        PathResponse path1 = response1.as(PathResponse.class);
        PathResponse path2 = response2.as(PathResponse.class);
        assertThat(path1.getDistance()).isEqualTo(path2.getDistance());
    }

    public static void 요금이_정상적으로_계산됨(ExtractableResponse<Response> response, BigDecimal expected) {
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getFee()).isEqualTo(expected);
    }

    public static void 최단_경로_조회_성공(
            ExtractableResponse<Response> shortestPathResponse, List<StationResponse> expectedStationPath, int expectedDistance) {
        assertThat(shortestPathResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        PathResponse pathResponse = shortestPathResponse.as(PathResponse.class);

        assertThat(pathResponse.getDistance()).isEqualTo(expectedDistance);

        List<Long> stationIds = pathResponse.getStations().stream()
                .map(StationInPathResponse::getId)
                .collect(Collectors.toList());
        List<Long> expectedStationPathIds = expectedStationPath.stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());
        assertThat(stationIds).containsAll(expectedStationPathIds);
    }

    public static void 최단_경로_조회_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static StationResponse 기존_노선과_접점이_없는_지하철_노선_등록됨() {
        StationResponse 용인역 = 지하철역_등록되어_있음("용인역").as(StationResponse.class);
        StationResponse 에버랜드역 = 지하철역_등록되어_있음("에버랜드역").as(StationResponse.class);
        지하철_노선_등록되어_있음(
                new LineRequest("경강선", "bg-red-600", 용인역.getId(), 에버랜드역.getId(), 10, BigDecimal.ZERO))
                .as(LineResponse.class);

        return 용인역;
    }

    public static ExtractableResponse<Response> 최단_경로_조회_요청(StationResponse source, StationResponse destination, String token) {
        return RestAssured.given().log().all()
                .auth().oauth2(token)
                .when().get("/paths?source=" + source.getId() + "&target=" + destination.getId())
                .then().log().all()
                .extract();
    }
}

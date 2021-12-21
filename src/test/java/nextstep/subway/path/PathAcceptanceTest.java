package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.path.domain.DiscountPolicy;
import nextstep.subway.path.domain.Fare;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private MemberRequest 회원;
    private TokenResponse tokenResponse;

    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10));
        이호선 = 지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 5));
        삼호선 = 지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-red-600", 교대역.getId(), 양재역.getId(), 5, 1000));

        지하철_노선에_구간_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);

        회원 = new MemberRequest("jdragon@woo.com", "12345", 15);
        AuthAcceptanceTest.회원등록됨(회원);
        ExtractableResponse<Response> response = AuthAcceptanceTest.로그인_요청(회원);
        AuthAcceptanceTest.로그인_성공(response);
        tokenResponse = AuthAcceptanceTest.토큰_생성됨(response);
    }

    /**
     * 교대역    --- *2호선* 거리5 ---     강남역
     * |                                  |
     * *3호선* 거리3                    *신분당선* 거리10
     * |                                  |
     * 남부터미널역  --- *3호선* 거리2 ---   양재
     */

    @DisplayName("최단경로조회")
    @Test
    void getShortestPath() {
        ExtractableResponse<Response> response = 최단경로_조회_요청(교대역, 양재역);

        // 교대 , 남부터미널, 양재, distance = 5
        최단경로_조회_성공(5, response, 교대역, 남부터미널역, 양재역);
    }

    private void 최단경로_조회_성공(int totalDistance, ExtractableResponse<Response> response, StationResponse... stations) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getDistance()).isEqualTo(totalDistance);
        assertThat(pathResponse.getStationIds()).containsExactlyElementsOf(지하철역_아이디_조회(stations));
        assertThat(pathResponse.getFare()).isEqualTo((Fare.plusFareFromDefaultFare(삼호선.getAdditionalFare())).deduct(350, DiscountPolicy.TEENAGER)  );
    }

    private List<Long> 지하철역_아이디_조회(StationResponse... stations) {
        return Arrays.stream(stations)
                .map(stationResponse -> stationResponse.getId())
                .collect(Collectors.toList());
    }

    private ExtractableResponse<Response> 최단경로_조회_요청(StationResponse source, StationResponse target) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .param("source", source.getId())
                .param("target", target.getId())
                .when().get("/paths")
                .then().log().all()
                .extract();
    }

    private LineResponse 지하철_노선_등록되어_있음(LineRequest request) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/lines")
                .then().log().all()
                .extract()
                .as(LineResponse.class);
    }

    private ExtractableResponse<Response> 지하철_노선에_구간_등록되어_있음(LineResponse line, StationResponse upStation, StationResponse downStation, int distance) {
        SectionRequest sectionRequest = new SectionRequest(upStation.getId(), downStation.getId(), distance);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionRequest)
                .when().post("/lines/{lineId}/sections", line.getId())
                .then().log().all()
                .extract();
    }
}

package nextstep.subway.path.acceptance;

import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.dto.PathStationDto;
import nextstep.subway.path.infrastructure.PathAnalysis;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;

import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private StationResponse 양재역;
    private StationResponse 강남역;
    private StationResponse 역삼역;
    private StationResponse 교대역;
    private StationResponse 우성역;

    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;

    @BeforeEach
    public void setUp() {
        super.setUp();

        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        역삼역 = StationAcceptanceTest.지하철역_등록되어_있음("역삼역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);

        LineRequest 신분당선_노선등록 = new LineRequest("신분당선", "bg-blue-600", 강남역.getId(), 양재역.getId(), 30);
        LineRequest 이호선_노선등록 = new LineRequest("이호선", "bg-green-600", 교대역.getId(), 강남역.getId(), 10);
        LineRequest 삼호선_노선등록 = new LineRequest("삼호선", "bg-orange-600", 교대역.getId(), 양재역.getId(), 50);

        신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(신분당선_노선등록).as(LineResponse.class);
        이호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(이호선_노선등록).as(LineResponse.class);
        삼호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(삼호선_노선등록).as(LineResponse.class);

        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(이호선, 강남역, 역삼역, 100);
    }

    @DisplayName("지하철 최단 경로가 조회된다.")
    @Test
    void acceptance_search_shortestPath() {
        // when
        ExtractableResponse<Response> response = 출발역_도착역_검색(교대역, 양재역);

        // then
        최단거리_조회됨(response);
    }

    @DisplayName("기등록된 구간이 삭제된 후 최단 경로가 조회된다.")
    @Test
    void acceptance_search_shortestPathAfterDeleteSection() {
        // when
        ExtractableResponse<Response> response = 출발역_도착역_검색(교대역, 양재역);

        // then
        최단거리_조회됨(response);

        // when
        LineSectionAcceptanceTest.지하철_노선에_지하철역_제외_요청(이호선, 강남역);
        ExtractableResponse<Response> responseAfterDeleteSection = 출발역_도착역_검색(교대역, 양재역);

        // then
        기등록구간삭제후_최단거리_조회됨(responseAfterDeleteSection);
    }

    @DisplayName("기등록된 구간에 신규 구간이 추가된 후 최단 경로가 조회된다.")
    @Test
    void acceptance_search_shortestPathAfterAddSection() {
        // when
        ExtractableResponse<Response> response = 출발역_도착역_검색(교대역, 양재역);
        // then
        최단거리_조회됨(response);

        // when
        우성역 = StationAcceptanceTest.지하철역_등록되어_있음("우성역").as(StationResponse.class);
        LineRequest 구호선_노선등록 = new LineRequest("구호선", "bg-black-600", 우성역.getId(), 교대역.getId(), 20);
        // then
        LineResponse 구호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(구호선_노선등록).as(LineResponse.class);

        // when
        ExtractableResponse<Response> addSectionResponse = LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(신분당선, 우성역, 양재역, 15);
        // then
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록됨(addSectionResponse);

        // when
        ExtractableResponse<Response> responseAfterDeleteSection = 출발역_도착역_검색(교대역, 양재역);
        // then
        신규구간추가후_최단거리_조회됨(responseAfterDeleteSection);
    }

    @DisplayName("어린이 나이로 로그인 후 지하철 최단 경로가 조회된다.")
    @Test
    void acceptance_search_shortestPath_afterLogin() {
        // given
        MemberRequest 테스트계정 = new MemberRequest("probitanima11@gmail.com", "11", 10);
        MemberAcceptanceTest.회원_생성을_요청(테스트계정.getEmail(), 테스트계정.getPassword(), 테스트계정.getAge());

        // when
        ExtractableResponse<Response> correctAccountResponse = AuthAcceptanceTest.JWT_요청(테스트계정);
        // then
        String accessJwt = MemberAcceptanceTest.JWT_받음(correctAccountResponse);

        // when
        ExtractableResponse<Response> response = 로그인후_출발역_도착역_검색(교대역, 양재역, accessJwt);

        // then
        로그인후_최단거리_조회됨(response);
    }

    private void 신규구간추가후_최단거리_조회됨(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        PathStationDto 양재역_pathStaion = new PathStationDto(양재역.getId(), 양재역.getName(), 양재역.getCreatedDate());
        PathStationDto 우성역_pathStaion = new PathStationDto(우성역.getId(), 우성역.getName(), 우성역.getCreatedDate());
        PathStationDto 교대역_pathStaion = new PathStationDto(교대역.getId(), 교대역.getName(), 교대역.getCreatedDate());

        assertAll(
            () -> Assertions.assertThat(response.as(PathResponse.class).getStations()).isEqualTo(List.of(교대역_pathStaion, 우성역_pathStaion, 양재역_pathStaion)),
            () ->  Assertions.assertThat(response.as(PathResponse.class).getDistance()).isEqualTo(35),
            () ->  Assertions.assertThat(response.as(PathResponse.class).getFare()).isEqualTo(1750)
        );
    }

    private void 기등록구간삭제후_최단거리_조회됨(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        PathStationDto 양재역_pathStaion = new PathStationDto(양재역.getId(), 양재역.getName(), 양재역.getCreatedDate());
        PathStationDto 교대역_pathStaion = new PathStationDto(교대역.getId(), 교대역.getName(), 교대역.getCreatedDate());

        assertAll(
            () -> Assertions.assertThat(response.as(PathResponse.class).getStations()).isEqualTo(List.of(교대역_pathStaion, 양재역_pathStaion)),
            () ->  Assertions.assertThat(response.as(PathResponse.class).getDistance()).isEqualTo(50),
            () ->  Assertions.assertThat(response.as(PathResponse.class).getFare()).isEqualTo(2050)
        );
    }

    private void 로그인후_최단거리_조회됨(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        PathStationDto 양재역_pathStaion = new PathStationDto(양재역.getId(), 양재역.getName(), 양재역.getCreatedDate());
        PathStationDto 강남역_pathStaion = new PathStationDto(강남역.getId(), 강남역.getName(), 강남역.getCreatedDate());
        PathStationDto 교대역_pathStaion = new PathStationDto(교대역.getId(), 교대역.getName(), 교대역.getCreatedDate());

        assertAll(
            () -> Assertions.assertThat(response.as(PathResponse.class).getStations()).isEqualTo(List.of(교대역_pathStaion, 강남역_pathStaion, 양재역_pathStaion)),
            () ->  Assertions.assertThat(response.as(PathResponse.class).getDistance()).isEqualTo(40),
            () ->  Assertions.assertThat(response.as(PathResponse.class).getFare()).isEqualTo(750)
        );
    }

    private void 최단거리_조회됨(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        PathStationDto 양재역_pathStaion = new PathStationDto(양재역.getId(), 양재역.getName(), 양재역.getCreatedDate());
        PathStationDto 강남역_pathStaion = new PathStationDto(강남역.getId(), 강남역.getName(), 강남역.getCreatedDate());
        PathStationDto 교대역_pathStaion = new PathStationDto(교대역.getId(), 교대역.getName(), 교대역.getCreatedDate());

        assertAll(
            () -> Assertions.assertThat(response.as(PathResponse.class).getStations()).isEqualTo(List.of(교대역_pathStaion, 강남역_pathStaion, 양재역_pathStaion)),
            () ->  Assertions.assertThat(response.as(PathResponse.class).getDistance()).isEqualTo(40),
            () ->  Assertions.assertThat(response.as(PathResponse.class).getFare()).isEqualTo(1850)
        );
    }

    private ExtractableResponse<Response> 출발역_도착역_검색(StationResponse sourceStation, StationResponse targetStation) {
        return RestAssured.given().log().all()
                            
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .when().get("/paths?source={sourceStationId}&target={targetStationId}", sourceStation.getId(), targetStation.getId())
                            .then().log().all()
                            .extract();
    }

    private ExtractableResponse<Response> 로그인후_출발역_도착역_검색(StationResponse sourceStation, StationResponse targetStation, String accessToken) {
        return RestAssured.given().log().all()
                            .auth().oauth2(accessToken)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .when().get("/paths?source={sourceStationId}&target={targetStationId}", sourceStation.getId(), targetStation.getId())
                            .then().log().all()
                            .extract();
    }
}

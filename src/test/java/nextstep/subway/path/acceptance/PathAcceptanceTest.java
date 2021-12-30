package nextstep.subway.path.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.fixture.*;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;


@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    /**
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     */
    @DisplayName("비로그인 사용자가 요청한 최적의 경로를 찾으며, 거리와 요금도 같이 계산한다.")
    @Test
    void pathfind() {
        // given
        StationResponse 강남역 = TestStationFactory.지하철_역_생성_요청("강남역").as(StationResponse.class);
        StationResponse 양재역 = TestStationFactory.지하철_역_생성_요청("양재역").as(StationResponse.class);
        StationResponse 교대역 = TestStationFactory.지하철_역_생성_요청("교대역").as(StationResponse.class);
        StationResponse 남부터미널역 = TestStationFactory.지하철_역_생성_요청("남부터미널역").as(StationResponse.class);
        LineResponse 신분당선 = TestLineFactory.지하철_노선_생성_요청(LineRequest.of("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 20,100)).as(LineResponse.class);
        LineResponse 이호선 = TestLineFactory.지하철_노선_생성_요청(LineRequest.of("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 30, 200)).as(LineResponse.class);
        LineResponse 삼호선 = TestLineFactory.지하철_노선_생성_요청(LineRequest.of("삼호선", "bg-red-600", 남부터미널역.getId(), 양재역.getId(), 10, 300)).as(LineResponse.class);
        TestSectionFactory.지하철_노선에_구간_등록_요청(삼호선, 교대역, 남부터미널역, 10);

        // when
        ExtractableResponse<Response> response = TestPathFactory.비로그인_지하철_최적_경로_요청(교대역.getId(), 양재역.getId());

        // then
        TestPathFactory.지하철_최적_경로_목록_응답됨(response);
        TestPathFactory.지하철_최적_경로_목록_포함됨(response, 3, 20, new BigDecimal("1750.00"), 교대역, 남부터미널역, 양재역);
    }

    @DisplayName("로그인한 사용자로 최적의 경로를 찾으며, 거리와 요금도 같이 계산한다.")
    @ParameterizedTest
    @CsvSource(value = {
            "6:950.00",
            "13:1220.00",
            "19:1750.00",
            "20:1750.00"}, delimiter = ':')
    void pathfind_login(final int age, final String fare) {
        // given
        ExtractableResponse<Response> createResponse = TestMemberFactory.회원_등록_요청("email", "password", age);
        TestMemberFactory.회원_생성됨(createResponse);
        ExtractableResponse<Response> loginResponse = TestLoginFactory.로그인_요청("email", "password");
        String 토큰 = TestLoginFactory.로그인_되어_있음(loginResponse);

        StationResponse 강남역 = TestStationFactory.지하철_역_생성_요청("강남역").as(StationResponse.class);
        StationResponse 양재역 = TestStationFactory.지하철_역_생성_요청("양재역").as(StationResponse.class);
        StationResponse 교대역 = TestStationFactory.지하철_역_생성_요청("교대역").as(StationResponse.class);
        StationResponse 남부터미널역 = TestStationFactory.지하철_역_생성_요청("남부터미널역").as(StationResponse.class);
        LineResponse 신분당선 = TestLineFactory.지하철_노선_생성_요청(LineRequest.of("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 20, 100)).as(LineResponse.class);
        LineResponse 이호선 = TestLineFactory.지하철_노선_생성_요청(LineRequest.of("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 30, 200)).as(LineResponse.class);
        LineResponse 삼호선 = TestLineFactory.지하철_노선_생성_요청(LineRequest.of("삼호선", "bg-red-600", 남부터미널역.getId(), 양재역.getId(), 10, 300)).as(LineResponse.class);
        TestSectionFactory.지하철_노선에_구간_등록_요청(삼호선, 교대역, 남부터미널역, 10);

        // when
        ExtractableResponse<Response> response = TestPathFactory.로그인_지하철_최적_경로_요청(토큰, 교대역.getId(), 양재역.getId());

        // then
        TestPathFactory.지하철_최적_경로_목록_응답됨(response);
        TestPathFactory.지하철_최적_경로_목록_포함됨(response, 3, 20, new BigDecimal(fare), 교대역, 남부터미널역, 양재역);
    }

    @DisplayName("출발역과 도착역이 같은 경우 예외처리한다.")
    @Test
    void pathfind_예외() {
        // given
        StationResponse 강남역 = TestStationFactory.지하철_역_생성_요청("강남역").as(StationResponse.class);
        StationResponse 양재역 = TestStationFactory.지하철_역_생성_요청("양재역").as(StationResponse.class);
        StationResponse 교대역 = TestStationFactory.지하철_역_생성_요청("교대역").as(StationResponse.class);
        StationResponse 남부터미널역 = TestStationFactory.지하철_역_생성_요청("남부터미널역").as(StationResponse.class);
        LineResponse 신분당선 = TestLineFactory.지하철_노선_생성_요청(LineRequest.of("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 20, 100)).as(LineResponse.class);
        LineResponse 이호선 = TestLineFactory.지하철_노선_생성_요청(LineRequest.of("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 30, 200)).as(LineResponse.class);
        LineResponse 삼호선 = TestLineFactory.지하철_노선_생성_요청(LineRequest.of("삼호선", "bg-red-600", 남부터미널역.getId(), 양재역.getId(), 10, 300)).as(LineResponse.class);
        TestSectionFactory.지하철_노선에_구간_등록_요청(삼호선, 교대역, 남부터미널역, 10);

        // when
        ExtractableResponse<Response> response = TestPathFactory.비로그인_지하철_최적_경로_요청(교대역.getId(), 교대역.getId());

        // then
        TestPathFactory.지하철_최적_경로_응답_실패됨(response);
    }

    @DisplayName("출발역과 도착역이 연결이 되어 있지 않은 경우 예외처리한다.")
    @Test
    void pathfind_예외2() {
        // given
        StationResponse 강남역 = TestStationFactory.지하철_역_생성_요청("강남역").as(StationResponse.class);
        StationResponse 양재역 = TestStationFactory.지하철_역_생성_요청("양재역").as(StationResponse.class);
        StationResponse 교대역 = TestStationFactory.지하철_역_생성_요청("교대역").as(StationResponse.class);
        StationResponse 남부터미널역 = TestStationFactory.지하철_역_생성_요청("남부터미널역").as(StationResponse.class);
        LineResponse 신분당선 = TestLineFactory.지하철_노선_생성_요청(LineRequest.of("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 20, 100)).as(LineResponse.class);
        LineResponse 이호선 = TestLineFactory.지하철_노선_생성_요청(LineRequest.of("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 30, 200)).as(LineResponse.class);
        LineResponse 삼호선 = TestLineFactory.지하철_노선_생성_요청(LineRequest.of("삼호선", "bg-red-600", 남부터미널역.getId(), 양재역.getId(), 10, 300)).as(LineResponse.class);
        TestSectionFactory.지하철_노선에_구간_등록_요청(삼호선, 교대역, 남부터미널역, 10);

        StationResponse 부평역 = TestStationFactory.지하철_역_생성_요청("부평역").as(StationResponse.class);
        StationResponse 송내역 = TestStationFactory.지하철_역_생성_요청("송내역").as(StationResponse.class);
        LineResponse 일호선 = TestLineFactory.지하철_노선_생성_요청(LineRequest.of("일호선", "bg-red-600", 부평역.getId(), 송내역.getId(), 10, 900)).as(LineResponse.class);
        TestSectionFactory.지하철_노선에_구간_등록_요청(일호선, 부평역, 송내역, 3);

        // when
        ExtractableResponse<Response> response = TestPathFactory.비로그인_지하철_최적_경로_요청(교대역.getId(), 부평역.getId());

        // then
        TestPathFactory.지하철_최적_경로_응답_실패됨(response);
    }

    @DisplayName("존재하지 않은 출발역이나 도착역을 조회 할 경우 예외처리한다.")
    @Test
    void pathfind_예외3() {
        // given
        StationResponse 강남역 = TestStationFactory.지하철_역_생성_요청("강남역").as(StationResponse.class);
        StationResponse 양재역 = TestStationFactory.지하철_역_생성_요청("양재역").as(StationResponse.class);
        StationResponse 교대역 = TestStationFactory.지하철_역_생성_요청("교대역").as(StationResponse.class);
        StationResponse 남부터미널역 = TestStationFactory.지하철_역_생성_요청("남부터미널역").as(StationResponse.class);
        LineResponse 신분당선 = TestLineFactory.지하철_노선_생성_요청(LineRequest.of("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 20,100)).as(LineResponse.class);
        LineResponse 이호선 = TestLineFactory.지하철_노선_생성_요청(LineRequest.of("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 30, 200)).as(LineResponse.class);
        LineResponse 삼호선 = TestLineFactory.지하철_노선_생성_요청(LineRequest.of("삼호선", "bg-red-600", 남부터미널역.getId(), 양재역.getId(), 10, 300)).as(LineResponse.class);
        TestSectionFactory.지하철_노선에_구간_등록_요청(삼호선, 교대역, 남부터미널역, 10);

        // when
        StationResponse 등록되지않는역 = StationResponse.of(Station.from("등록되지않는역"));
        ExtractableResponse<Response> response = TestPathFactory.비로그인_지하철_최적_경로_요청(교대역.getId(), 등록되지않는역.getId());

        // then
        TestPathFactory.지하철_최적_경로_응답_실패됨(response);
    }
}

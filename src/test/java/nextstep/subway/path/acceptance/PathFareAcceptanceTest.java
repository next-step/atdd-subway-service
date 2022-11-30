package nextstep.subway.path.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_요청;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptance.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.member.MemberRestAssured.회원_생성을_요청;
import static nextstep.subway.path.acceptance.PathRestAssured.로그인_상태로_최단경로_조회_요청;
import static nextstep.subway.path.acceptance.PathRestAssured.최단경로_조회_요청;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@DisplayName("지하철 요금 조회")
class PathFareAcceptanceTest extends AcceptanceTest {
    private static final int BASIC_PRICE = 1_250;

    /**
     * 일호선 추가요금: 0
     * 이호선 추가요금: 1000
     * 삼호선 추가요금: 2000
     *
     * 강남역 --- *3호선(4)*--- 양재역 ---*3호선(16)*--- 교대역
     *   |
     *  3호선 (5)
     *   |
     * 인천  --- *1호선(8)* --- 부평 --- *2호선(70)* --- 판교역
     */
    @DisplayName("지하철 요금에 대한 다양한 case 테스트")
    @TestFactory
    Stream<DynamicTest> subwayFare() {
        // given
        int 이호선_추가요금 = 1_000;
        int 삼호선_추가요금 = 2_000;

        StationResponse 강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        StationResponse 양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        StationResponse 교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        StationResponse 인천역 = 지하철역_등록되어_있음("인천역").as(StationResponse.class);
        StationResponse 부평역 = 지하철역_등록되어_있음("부평역").as(StationResponse.class);
        StationResponse 판교역 = 지하철역_등록되어_있음("판교역").as(StationResponse.class);

        지하철_노선_등록되어_있음("일호선", "bg-yellow-600", 인천역.getId(), 부평역.getId(), 8, 0);
        지하철_노선_등록되어_있음("이호선", "bg-blue-600", 판교역.getId(), 부평역.getId(), 70, 1_000);
        LineResponse 삼호선 =
                지하철_노선_등록되어_있음("삼호선", "bg-red-600", 강남역.getId(), 양재역.getId(), 4, 삼호선_추가요금);
        지하철_노선에_지하철역_등록_요청(삼호선, 양재역, 교대역, 16);
        지하철_노선에_지하철역_등록_요청(삼호선, 인천역, 강남역, 5);


        return Stream.of(
                dynamicTest("기본요금은 1250원이다", () -> {
                    // when
                    ExtractableResponse<Response> response = 최단경로_조회_요청(인천역, 부평역);

                    // then
                    지하철_이용금액_확인됨(response, BASIC_PRICE);
                }),
                dynamicTest("노선에 추가요금이 있을 경우 지하철 이용요금은 기본요금 + 추가요금이다.", () -> {
                    // when
                    ExtractableResponse<Response> response = 최단경로_조회_요청(강남역, 양재역);

                    // then
                    지하철_이용금액_확인됨(response, BASIC_PRICE + 삼호선_추가요금);
                }),
                dynamicTest("여러 노선을 거칠 경우(환승), 가장 높은 추가 요금이 선택된다.", () -> {
                    // when
                    ExtractableResponse<Response> response = 최단경로_조회_요청(인천역, 양재역);

                    // then
                    지하철_이용금액_확인됨(response, BASIC_PRICE + 삼호선_추가요금);

                }),
                dynamicTest("거리가 10km 넘을 경우 5km 마다 100원의 추가요금이 발생한다", () -> {
                    // given (33km에 대한 추가요금)
                    int distanceExtraFare = 500;

                    // when
                    ExtractableResponse<Response> response = 최단경로_조회_요청(부평역, 교대역);

                    // then
                    지하철_이용금액_확인됨(response, BASIC_PRICE + 삼호선_추가요금 + distanceExtraFare);
                }),
                dynamicTest("거리가 50km 넘을 경우 8km 마다 100원의 추가요금이 발생한다", () -> {
                    // given (70km에 대한 추가요금)
                    int distanceExtraFare = 1100;

                    // when
                    ExtractableResponse<Response> response = 최단경로_조회_요청(부평역, 판교역);

                    // then
                    지하철_이용금액_확인됨(response, BASIC_PRICE + 이호선_추가요금 + distanceExtraFare);
                }),
                dynamicTest("어린이는 50% 할인을 받는다", () -> {
                    // given
                    회원_생성을_요청("child@naver.com", "password", 8);
                    String token = 로그인_요청("child@naver.com", "password").as(TokenResponse.class)
                            .getAccessToken();
                    int discount = 450;

                    // when
                    ExtractableResponse<Response> response = 로그인_상태로_최단경로_조회_요청(token, 인천역, 부평역);

                    // then
                    지하철_이용금액_확인됨(response, BASIC_PRICE - discount);
                }),
                dynamicTest("청소년은 20% 할인을 받는다", () -> {
                    // given
                    회원_생성을_요청("teen@email.com", "password", 18);
                    String token = 로그인_요청("teen@email.com", "password").as(TokenResponse.class)
                            .getAccessToken();
                    int discount = 180;

                    // when
                    ExtractableResponse<Response> response = 로그인_상태로_최단경로_조회_요청(token, 인천역, 부평역);

                    // then
                    지하철_이용금액_확인됨(response, BASIC_PRICE - discount);
                })
        );
    }

    private void 지하철_이용금액_확인됨(ExtractableResponse<Response> response, int fare) {
        PathResponse pathResponse = response.as(PathResponse.class);
        assertThat(pathResponse.getFare()).isEqualTo(fare);
    }
}

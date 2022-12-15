package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_요청을_한다;
import static nextstep.subway.auth.application.AuthServiceTest.EMAIL;
import static nextstep.subway.auth.application.AuthServiceTest.PASSWORD;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록되어_있음;
import static nextstep.subway.member.MemberAcceptanceTest.AGE;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private LineResponse 일호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 역삼역;
    private StationResponse 선릉역;
    private StationResponse 수원역;
    private StationResponse 화서역;
    private StationResponse 고속터미널역;
    private StationResponse 남부터미널역;
    private StationResponse 양재시민의숲;

    /**
     * 지하철 노선도
     *
     * 고속터미널역
     * |
     * *3호선*
     * |
     * 교대역    --- *2호선* ---   강남역 --- *2호선* --- 역삼역 --- *2호선* ---선릉역
     * |                        |
     * *3호선*                   *신분당선*
     * |                        |
     * 남부터미널역  --- *3호선* ---   양재
     *                          |
     *                          *신분당선*
     *                          |
     *                          양재시민의 숲
     *
     * 화서역 --- *1호선* --- 수원역
     */

    @BeforeEach
    public void setUp() {
        super.setUp();
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        역삼역 = 지하철역_등록되어_있음("역삼역").as(StationResponse.class);
        선릉역 = 지하철역_등록되어_있음("선릉역").as(StationResponse.class);
        양재시민의숲 = 지하철역_등록되어_있음("양재시민의숲").as(StationResponse.class);
        고속터미널역 = 지하철역_등록되어_있음("고속터미널역").as(StationResponse.class);
        수원역 = 지하철역_등록되어_있음("수원역").as(StationResponse.class);
        화서역 = 지하철역_등록되어_있음("화서역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재시민의숲.getId(), 40, 300)).as(LineResponse.class);
        일호선 = 지하철_노선_등록되어_있음(new LineRequest("일호선", "blue", 수원역.getId(), 화서역.getId(), 10, 500)).as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-red-600", 교대역.getId(), 선릉역.getId(), 100, 0)).as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(new LineRequest("삼호선", "bg-red-600", 고속터미널역.getId(), 양재역.getId(), 30, 1000)).as(LineResponse.class);

        지하철_노선에_지하철역_등록되어_있음(삼호선, 고속터미널역, 교대역, 10);
        지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 5);
        지하철_노선에_지하철역_등록되어_있음(이호선, 교대역, 강남역, 10);
        지하철_노선에_지하철역_등록되어_있음(이호선, 강남역, 역삼역, 10);
        지하철_노선에_지하철역_등록되어_있음(신분당선, 강남역, 양재역, 20);
    }

    @Test
    @DisplayName("비로그인 후 성공적으로 최단 경로를 조회한다")
    void getShortPathByNoLogin() {
        // when
        ExtractableResponse<Response> 결과 = 지하철_경로_조회_요청(교대역.getId(), 양재시민의숲.getId());

        // then
        상태값이_기대값과_일치하는지_체크한다(결과, HttpStatus.OK);
        최단경로를_맞게_조회했는지_체크한다(결과, Arrays.asList(교대역.getId(), 남부터미널역.getId(), 양재역.getId(), 양재시민의숲.getId()), 40);
    }

    @Test
    @DisplayName("로그인 후 성공적으로 최단 경로를 조회한다 ")
    void getShortPathByLogin() {
        // given
        회원_생성을_요청(EMAIL, PASSWORD, AGE);
        ExtractableResponse<Response> loginResponse = 로그인_요청을_한다(EMAIL, PASSWORD);
        String accessToken = loginResponse.as(TokenResponse.class).getAccessToken();

        // when
        ExtractableResponse<Response> 결과 = 지하철_경로_조회_요청_로그인시(교대역.getId(), 양재시민의숲.getId(), accessToken);

        // then
        상태값이_기대값과_일치하는지_체크한다(결과, HttpStatus.OK);
        최단경로를_맞게_조회했는지_체크한다(결과, Arrays.asList(교대역.getId(), 남부터미널역.getId(), 양재역.getId(), 양재시민의숲.getId()), 40);
    }


    @Test
    @DisplayName("출발지와 도착지가 같은 경우 예외를 발생한다")
    void isSameStationException() {
        // when
        ExtractableResponse<Response> 결과 = 지하철_경로_조회_요청(교대역.getId(), 교대역.getId());

        // then
        상태값이_기대값과_일치하는지_체크한다(결과, HttpStatus.BAD_REQUEST);
    }


    @Test
    @DisplayName("존재하지 않는 역이 출발지일 경우 예외가 발생한다")
    void isNotExistStartStationException() {
        // when
        ExtractableResponse<Response> 결과 = 지하철_경로_조회_요청(Station.from("성균관대학교역").getId(), 역삼역.getId());

        // then
        상태값이_기대값과_일치하는지_체크한다(결과, HttpStatus.BAD_REQUEST);
    }


    @Test
    @DisplayName("존재하지 않는 역이 도착지일 경우 예외가 발생한다")
    void isNotExistEndStationException() {
        // when
        ExtractableResponse<Response> 결과 = 지하철_경로_조회_요청(교대역.getId(), Station.from("성균관대학교역").getId());

        // then
        상태값이_기대값과_일치하는지_체크한다(결과, HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("시작역과 도착역이 연결되어 있지 않으면 예외가 발생한다")
    void isNotConnectStation() {
        // when
        ExtractableResponse<Response> 결과 = 지하철_경로_조회_요청(수원역.getId(), 교대역.getId());

        // then
        상태값이_기대값과_일치하는지_체크한다(결과, HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("1. 로그인 하지 않는 유저가 " +
            "2. 추가 요금 부담구간이 없는 곳에서" +
            "3. 거리는 10km 이하로 지하철을 탄다")
    void notExtraFeeAndNoLoginAndNotPerKmFee() {
        // when
        ExtractableResponse<Response> 결과 = 지하철_경로_조회_요청(교대역.getId(), 강남역.getId());

        // then
        상태값이_기대값과_일치하는지_체크한다(결과, HttpStatus.OK);
        요금이_일치하는지_체크한다(결과, 1250);
    }

    @Test
    @DisplayName("1. 로그인 하지 않는 유저가 " +
            "2. 추가 요금 부담구간이 없는 곳에서" +
            "3. 거리는 20km 지하철을 탄다")
    void notExtraFeeAndNoLoginAnd20PerKmFee() {
        // when
        ExtractableResponse<Response> 결과 = 지하철_경로_조회_요청(교대역.getId(), 역삼역.getId());

        // then
        상태값이_기대값과_일치하는지_체크한다(결과, HttpStatus.OK);
        요금이_일치하는지_체크한다(결과, 1450);
    }

    @Test
    @DisplayName("1. 로그인 하지 않는 유저가 " +
            "2. 추가 요금 부담구간이 없는 곳에서" +
            "3. 거리는 50km 이상 지하철을 탄다")
    void notExtraFeeAndNoLoginAnd50PerKmFee() {
        // when
        ExtractableResponse<Response> 결과 = 지하철_경로_조회_요청(교대역.getId(), 선릉역.getId());

        // then
        상태값이_기대값과_일치하는지_체크한다(결과, HttpStatus.OK);
        요금이_일치하는지_체크한다(결과, 2750);
    }

    @Test
    @DisplayName("1. 로그인 하지 않는 유저가 " +
            "2. 추가 요금이 있는 구간에서" +
            "3. 거리는 10km 이하로 지하철을 탄다")
    void addExtraFeeAndNoLoginAndNotPerKmFee() {
        // when
        ExtractableResponse<Response> 결과 = 지하철_경로_조회_요청(수원역.getId(), 화서역.getId());

        // then
        상태값이_기대값과_일치하는지_체크한다(결과, HttpStatus.OK);
        요금이_일치하는지_체크한다(결과, 1750);
    }

    @Test
    @DisplayName("1. 로그인 한 10살의 유저가 " +
            "2. 추가 요금이 있는 구간에서" +
            "3. 거리는 10km 이하로 지하철을 탄다")
    void addExtraFeeAndLoginChildrenAndNotPerKmFee() {
        // given
        회원_생성을_요청(EMAIL, PASSWORD, 10);
        ExtractableResponse<Response> loginResponse = 로그인_요청을_한다(EMAIL, PASSWORD);
        String accessToken = loginResponse.as(TokenResponse.class).getAccessToken();

        // when
        ExtractableResponse<Response> 결과 = 지하철_경로_조회_요청_로그인시(수원역.getId(), 화서역.getId(), accessToken);

        // then
        상태값이_기대값과_일치하는지_체크한다(결과, HttpStatus.OK);
        요금이_일치하는지_체크한다(결과, 700);
    }

    @Test
    @DisplayName("1. 로그인 한 15살의 유저가 " +
            "2. 추가 요금이 있는 구간에서" +
            "3. 거리는 10km 이하로 지하철을 탄다")
    void addExtraFeeAndLoginTeenagerAndNotPerKmFee() {
        // given
        회원_생성을_요청(EMAIL, PASSWORD, 15);
        ExtractableResponse<Response> loginResponse = 로그인_요청을_한다(EMAIL, PASSWORD);
        String accessToken = loginResponse.as(TokenResponse.class).getAccessToken();

        // when
        ExtractableResponse<Response> 결과 = 지하철_경로_조회_요청_로그인시(수원역.getId(), 화서역.getId(), accessToken);

        // then
        상태값이_기대값과_일치하는지_체크한다(결과, HttpStatus.OK);
        요금이_일치하는지_체크한다(결과, 1120);
    }

    @Test
    @DisplayName("1. 로그인 한 30살의 유저가 " +
            "2. 추가 요금이 있는 구간에서" +
            "3. 거리는 10km 이하로 지하철을 탄다")
    void addExtraFeeAndLoginAdultAndNotPerKmFee() {
        // given
        회원_생성을_요청(EMAIL, PASSWORD, 30);
        ExtractableResponse<Response> loginResponse = 로그인_요청을_한다(EMAIL, PASSWORD);
        String accessToken = loginResponse.as(TokenResponse.class).getAccessToken();

        // when
        ExtractableResponse<Response> 결과 = 지하철_경로_조회_요청_로그인시(수원역.getId(), 화서역.getId(), accessToken);

        // then
        상태값이_기대값과_일치하는지_체크한다(결과, HttpStatus.OK);
        요금이_일치하는지_체크한다(결과, 1750);
    }

    @Test
    @DisplayName("1. 로그인 하지 않는 유저가 " +
            "2. 추가 요금 부담구간이 있는 두곳의 구간에서" +
            "3. 거리는 10km 이하로 지하철을 탄다")
    void addTwoExtraFeeAndNoLoginAndNotPerKmFee() {
        // when
        ExtractableResponse<Response> 결과 = 지하철_경로_조회_요청(고속터미널역.getId(), 양재시민의숲.getId());

        // then
        상태값이_기대값과_일치하는지_체크한다(결과, HttpStatus.OK);
        요금이_일치하는지_체크한다(결과, 3050);
    }

    private void 최단경로를_맞게_조회했는지_체크한다(ExtractableResponse<Response> response, List<Long> expectIds, int expectDistance) {
        List<Long> stationIds = response.jsonPath().getList("stations", StationResponse.class)
                .stream().map(StationResponse::getId)
                .collect(Collectors.toList());

        int distance = response.jsonPath().getInt("distance");

        assertThat(stationIds).containsAll(expectIds);
        assertThat(distance).isEqualTo(expectDistance);
    }


    private static ExtractableResponse<Response> 지하철_경로_조회_요청(Long sourceId, Long targetId) {
        Map<String, Long> params = new HashMap<>();
        params.put("source", sourceId);
        params.put("target", targetId);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .params(params)
                .when().get("/paths")
                .then().log().all()
                .extract();
    }

    private void 상태값이_기대값과_일치하는지_체크한다(ExtractableResponse<Response> response, HttpStatus status) {
        assertThat(response.statusCode()).isEqualTo(status.value());
    }

    private void 요금이_일치하는지_체크한다(ExtractableResponse<Response> response, int fee) {
        assertThat(response.jsonPath().getInt("fee")).isEqualTo(fee);
    }

    private ExtractableResponse<Response> 지하철_경로_조회_요청_로그인시(Long sourceId, Long targetId, String token) {
        Map<String, Long> params = new HashMap<>();
        params.put("source", sourceId);
        params.put("target", targetId);

        return RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .params(params)
                .when().get("/paths")
                .then().log().all()
                .extract();
    }
}

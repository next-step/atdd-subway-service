package nextstep.subway.path.acceptance;

import com.google.common.collect.Lists;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.StationTestUtils;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static nextstep.subway.auth.acceptance.AuthTestUtils.로그인_요청;
import static nextstep.subway.member.MemberTestUtils.회원_생성을_요청;
import static nextstep.subway.path.acceptance.PathTestUtils.*;

@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private LineResponse 경강선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private StationResponse 세종대왕릉역;
    private StationResponse 여주역;
    private String email;
    private String password;

    /**
     * 교대역    --- *2호선* ---   강남역      세종대왕릉역
     * |                          |            |
     * *3호선*                   *신분당선*     *경강선*
     * |                          |            |
     * 남부터미널역  --- *3호선* ---  양재        여주역
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        email = "email@email.com";
        password = "1234";

        강남역 = StationTestUtils.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationTestUtils.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationTestUtils.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationTestUtils.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        세종대왕릉역 = StationTestUtils.지하철역_등록되어_있음("세종대왕릉역").as(StationResponse.class);
        여주역 = StationTestUtils.지하철역_등록되어_있음("여주역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 4, 0);
        이호선 = 지하철_노선_등록되어_있음("이호선", "bg-red-600", 교대역, 강남역, 6, 0);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-red-600", 교대역, 양재역, 50, 900);
        경강선 = 지하철_노선_등록되어_있음("경강선", "bg-red-600", 세종대왕릉역, 여주역, 80, 1200);

        지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);
    }

    @DisplayName("두 역의 최단 거리 경로를 조회한다.")
    @Test
    void findPath() {
        // when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(교대역, 양재역);

        // then
        최단경로_조회_성공(response);
        최단경로_역순서_확인(response, Lists.newArrayList(교대역, 강남역, 양재역));
        경로_길이_확인(response, 10);
    }

    @DisplayName("두 역의 최단 거리 경로를 조회하면 연령별 할인이 적용된 요금이 조회된다.")
    @ParameterizedTest
    @CsvSource(value = {"6:800", "13:1070", "19:1250"}, delimiter = ':')
    void findPathWithFare(int age, int expected) {
        // given
        회원_생성을_요청(email, password, age);
        TokenResponse 로그인_토큰 = 로그인_요청(email, password).as(TokenResponse.class);

        // when
        ExtractableResponse<Response> response = 로그인회원_거리_경로_조회_요청(로그인_토큰, 교대역, 양재역);

        // then
        최단경로_조회_성공(response);
        최단경로_역순서_확인(response, Lists.newArrayList(교대역, 강남역, 양재역));
        경로_길이_확인(response, 10);
        요금_확인(response, expected);
    }

    @DisplayName("두 역의 최단 거리 경로를 조회한다. (추가요금이 있는 노선 경로)")
    @Test
    void findPathWithFare_LineEaxtraFare() {
        // when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(교대역, 남부터미널역);

        // then
        최단경로_조회_성공(response);
        최단경로_역순서_확인(response, Lists.newArrayList(교대역, 남부터미널역));
        경로_길이_확인(response, 3);
        요금_확인(response, 1250 + 900);
    }

    @DisplayName("두 역의 최단 거리 경로를 조회한다. (10KM 초과, 50KM 미만인 경로)")
    @Test
    void findPathWithFare_10KM_50KM() {
        // when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(남부터미널역, 양재역);

        // then
        최단경로_조회_성공(response);
        최단경로_역순서_확인(response, Lists.newArrayList(남부터미널역, 교대역, 강남역, 양재역));
        경로_길이_확인(response, 13);
        요금_확인(response, 1250 + 100 + 900);
    }

    @DisplayName("두 역의 최단 거리 경로를 조회한다. (50KM 초과인 경로)")
    @Test
    void findPathWithFare_50KM() {
        // when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(세종대왕릉역, 여주역);

        // then
        최단경로_조회_성공(response);
        최단경로_역순서_확인(response, Lists.newArrayList(세종대왕릉역, 여주역));
        경로_길이_확인(response, 80);
        요금_확인(response, 1250 + 1200 + 1200);
    }

    @DisplayName("최단 거리 경로 조회 실패 (출발역과 도착역이 같은 경우)")
    @Test
    void findPathFail_equalStation() {
        // when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(교대역, 교대역);

        // then
        최단경로_조회_실패(response);
    }

    @DisplayName("최단 거리 경로 조회 실패 (출발역과 도착역이 연결이 되어 있지 않은 경우)")
    @Test
    void findPathFail_notConnected() {
        // when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(교대역, 세종대왕릉역);

        // then
        최단경로_조회_실패(response);
    }

    @DisplayName("최단 거리 경로 조회 실패 (존재하지 않은 출발역이나 도착역을 조회 할 경우)")
    @Test
    void findPathFail_notExist() {
        //when
        ExtractableResponse<Response> response = 거리_경로_조회_요청(교대역, new StationResponse());

        //then
        최단경로_조회_실패(response);
    }
}

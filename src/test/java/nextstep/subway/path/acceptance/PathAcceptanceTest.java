package nextstep.subway.path.acceptance;

import static nextstep.subway.auth.application.AuthServiceTest.*;
import static nextstep.subway.member.MemberAcceptanceTest.BEARER;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;

@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 정자역;
    private StationResponse 광교역;
    private StationResponse 선릉역;
    private StationResponse 교대역;
    private StationResponse 천호역;
    private StationResponse 군자역;

    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        정자역 = StationAcceptanceTest.지하철역_등록되어_있음("정자역").as(StationResponse.class);
        광교역 = StationAcceptanceTest.지하철역_등록되어_있음("광교역").as(StationResponse.class);
        선릉역 = StationAcceptanceTest.지하철역_등록되어_있음("선릉역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        천호역 = StationAcceptanceTest.지하철역_등록되어_있음("천호역").as(StationResponse.class);
        군자역 = StationAcceptanceTest.지하철역_등록되어_있음("군자역").as(StationResponse.class);

        LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10, 0);
        신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);
        lineRequest = new LineRequest("이호선", "bg-red-600", 강남역.getId(), 선릉역.getId(), 10, 0);
        LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);
        lineRequest = new LineRequest("삼호선", "bg-red-600", 양재역.getId(), 교대역.getId(), 10, 0);
        LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);
        lineRequest = new LineRequest("오호선", "bg-red-600", 천호역.getId(), 군자역.getId(), 10, 0);
        LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);

        지하철_노선에_지하철역_등록_요청(신분당선, 양재역, 광교역, 10);
        지하철_노선에_지하철역_등록_요청(신분당선, 광교역, 정자역, 10);
    }

    /********************************
     * 선릉역    --- *2호선* ---   강남역
     *                          |
     *                     *신분당선*
     *                          |
     * 교대역  --- *3호선* ---     양재역
     *                          |
     *                           광교역
     *                          |
     *                           정자역
     ***********************************/

    @DisplayName("경로 찾기 요청 정상")
    @Test
    void findRequest() {
        //given
        PathRequest 경로_조회_요청_내용 = 경로_조회_요청_내용(강남역, 교대역);
        //when
        ExtractableResponse<Response> 지하철_경로_조회_요청_응답 = 지하철_경로_조회_요청(경로_조회_요청_내용);
        //then
        //지하철 경로 조회 응답 확인
        지하철_노선에_지하철역_경로_결과(지하철_경로_조회_요청_응답, 20, 강남역, 양재역, 교대역);
    }

    @DisplayName("경로 찾기 요청 정상 - 다른 값")
    @Test
    void findRequestOtherStation() {
        //given
        PathRequest 경로_조회_요청_내용 = 경로_조회_요청_내용(교대역, 선릉역);
        //when
        ExtractableResponse<Response> 지하철_경로_조회_요청_응답 = 지하철_경로_조회_요청(경로_조회_요청_내용);
        //then
        //지하철 경로 조회 응답 확인
        지하철_노선에_지하철역_경로_결과(지하철_경로_조회_요청_응답, 30, 교대역, 양재역, 강남역, 선릉역);
    }

    @DisplayName("경로 찾기 실패")
    @Test
    void findRequestFailed() {
        //given
        PathRequest 없는역_조회_요청 = 경로_조회_요청_내용(정자역, new StationResponse());
        PathRequest 같은역_조회_요청 = 경로_조회_요청_내용(정자역, 정자역);
        PathRequest 연결_되지_않은_구간_요청 = 경로_조회_요청_내용(정자역, 천호역);
        //when
        ExtractableResponse<Response> 없는역_조회_요청_응답 = 지하철_경로_조회_요청(없는역_조회_요청);
        ExtractableResponse<Response> 같은역_조회_요청_응답 = 지하철_경로_조회_요청(같은역_조회_요청);
        ExtractableResponse<Response> 연결_되지_않은_구간_요청_응답 = 지하철_경로_조회_요청(연결_되지_않은_구간_요청);
        //then
        //지하철 경로 조회 응답 확인
        //실패 체크
        역_생성_실패_체크(없는역_조회_요청_응답);
        역_생성_실패_체크(같은역_조회_요청_응답);
        역_생성_실패_체크(연결_되지_않은_구간_요청_응답);
    }

    @DisplayName("로그인 후 할인 요금으로 측정 - 청소년")
    @Test
    void findPathWithTeenager() {
        // given
        회원_생성을_요청(16);
        String 토큰 = 회원_로그인_요청();
        // when
        PathRequest 경로_조회_요청_내용 = 경로_조회_요청_내용(강남역, 양재역);
        ExtractableResponse<Response> 지하철_경로_조회_요청_응답 = 지하철_경로_조회_요청_로그인_됨(토큰, 경로_조회_요청_내용);
        // then
        경로_조회_요금_확인(지하철_경로_조회_요청_응답, 720);
    }

    @DisplayName("로그인 후 할인 요금으로 측정 - 어린이")
    @Test
    void findPathWithKidsAndLineFare() {
        // given
        회원_생성을_요청(8);
        String 토큰 = 회원_로그인_요청();
        // when
        PathRequest 경로_조회_요청_내용 = 경로_조회_요청_내용(강남역, 양재역);
        ExtractableResponse<Response> 지하철_경로_조회_요청_응답 = 지하철_경로_조회_요청_로그인_됨(토큰, 경로_조회_요청_내용);
        // then
        경로_조회_요금_확인(지하철_경로_조회_요청_응답, 450);
    }

    @DisplayName("로그인 후 할인 요금으로 측정 - 어린이, 환승 구간 요금이 있는 경우")
    @Test
    void findPathWithKids() {
        // given
        StationResponse 복정역 = StationAcceptanceTest.지하철역_등록되어_있음("복정역").as(StationResponse.class);
        LineRequest lineRequest = new LineRequest("분당선", "yellog", 선릉역.getId(), 복정역.getId(), 10, 900);
        LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);
        회원_생성을_요청(8);
        String 토큰 = 회원_로그인_요청();
        // when
        PathRequest 경로_조회_요청_내용 = 경로_조회_요청_내용(선릉역, 복정역);
        ExtractableResponse<Response> 지하철_경로_조회_요청_응답 = 지하철_경로_조회_요청_로그인_됨(토큰, 경로_조회_요청_내용);
        // then
        경로_조회_요금_확인(지하철_경로_조회_요청_응답, 900);
    }

    private void 경로_조회_요금_확인(ExtractableResponse<Response> 경로_조회_요청_내용, int 요금) {
        PathResponse response = 경로_조회_요청_내용.as(PathResponse.class);
        assertThat(response.getFare()).isEqualTo(요금);
    }

    private static Long 역_번호_추출(StationResponse 역) {
        return 역.getId();
    }

    private static ExtractableResponse<Response> 지하철_경로_조회_요청(PathRequest params) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().get("/paths")
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> 지하철_경로_조회_요청_로그인_됨(String 토큰, PathRequest params) {
        return RestAssured
                .given().header("authorization", BEARER + 토큰).log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().get("/paths")
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> 지하철_노선에_지하철역_등록_요청(LineResponse line, StationResponse upStation, StationResponse downStation, int distance) {
        SectionRequest sectionRequest = new SectionRequest(upStation.getId(), downStation.getId(), distance);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionRequest)
                .when().post("/lines/{lineId}/sections", line.getId())
                .then().log().all()
                .extract();
    }

    private PathRequest 경로_조회_요청_내용(StationResponse 출발역, StationResponse 도착역) {
        return new PathRequest(역_번호_추출(출발역), 역_번호_추출(도착역));
    }

    private void 지하철_노선에_지하철역_경로_결과(ExtractableResponse<Response> 지하철_경로_조회_요청_응답, int 거리, StationResponse... 역_경로) {
        List<StationResponse> stationResponses = Arrays.asList(역_경로);
        PathResponse path = 지하철_경로_조회_요청_응답.as(PathResponse.class);
        List<Long> stationIds = path.getStations().stream()
                .map(it -> it.getId())
                .collect(Collectors.toList());

        List<Long> expectedStationIds = stationResponses.stream()
                .map(it -> it.getId())
                .collect(Collectors.toList());

        assertThat(지하철_경로_조회_요청_응답.statusCode()).isEqualTo(HttpStatus.OK.value());
        for (int i = 0; i < stationResponses.size(); i++) {
            assertThat(stationIds.get(i)).isEqualTo(expectedStationIds.get(i));
        }
        assertThat(path.getDistance()).isEqualTo(거리);
    }

    private void 역_생성_실패_체크(ExtractableResponse<Response> 없는역_조회_요청_응답) {
        assertThat(없는역_조회_요청_응답.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private static ExtractableResponse<Response> 회원_생성을_요청(int age) {
        MemberRequest memberRequest = new MemberRequest(EMAIL, PASSWORD, age);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberRequest)
                .when().post("/members")
                .then().log().all()
                .extract();
    }

    private static String 회원_로그인_요청() {
        TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);

        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tokenRequest)
                .when().post("/login/token")
                .then().log().all()
                .extract();
        return response.as(TokenResponse.class).getAccessToken();
    }
}

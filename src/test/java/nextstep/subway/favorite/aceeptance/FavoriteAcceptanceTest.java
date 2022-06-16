package nextstep.subway.favorite.aceeptance;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_되어_있음;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.member.MemberAcceptanceTest.*;
import static nextstep.subway.member.MemberAcceptanceTest.AGE;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    private StationResponse 공덕역;
    private StationResponse 영등포구청역;
    private StationResponse 합정역;
    private StationResponse 당산역;
    private StationResponse 동래역;
    private LineResponse 오호선;
    private LineResponse 이호선;
    private LineResponse 육호선;
    private String 사용자_인증_토큰;

    /**
     * 합정역 --- *6호선(4)* --- 공덕역
     * |                                |
     * *2호선(3)*                 *5호선(6)*
     * |                                |
     * 당산역 --- *2호선(5)* --- 영등포구청역
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        //given
        공덕역 = 지하철역_등록되어_있음("공덕역").as(StationResponse.class);
        영등포구청역 = 지하철역_등록되어_있음("영등포구청역").as(StationResponse.class);
        당산역 = 지하철역_등록되어_있음("당산역").as(StationResponse.class);
        합정역 = 지하철역_등록되어_있음("합정역").as(StationResponse.class);
        동래역 = 지하철역_등록되어_있음("동래역").as(StationResponse.class);

        오호선 = 지하철_노선_등록되어_있음(new LineRequest("오호선", "bg-red-600", 공덕역.getId(), 영등포구청역.getId(), 6)).as(LineResponse.class);
        육호선 = 지하철_노선_등록되어_있음(new LineRequest("육호선", "bg-red-600", 합정역.getId(), 공덕역.getId(), 4)).as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음(new LineRequest("이호선", "bg-red-600", 합정역.getId(), 영등포구청역.getId(), 8)).as(LineResponse.class);

        지하철_노선에_지하철역_등록_요청(이호선, 합정역, 당산역, 3);

        회원_생성을_요청(EMAIL, PASSWORD, AGE);
        사용자_인증_토큰 = 로그인_되어_있음(EMAIL, PASSWORD);
    }

    /**
     * Feature: 즐겨찾기 관련 기능
     *
     * Background
     * Given 지하철역 등록되어 있음
     * And 지하철 노선 등록되어 있음
     * And 지하철 노선에 지하철역 등록되어 있음
     * And 회원 등록되어 있음
     * And 로그인 되어있음
     *
     * Scenario: 즐겨찾기를 관리
     * When 즐겨찾기 생성을 요청
     * Then 즐겨찾기 생성됨
     * When 즐겨찾기 목록 조회 요청
     * Then 즐겨찾기 목록 조회됨
     * When 즐겨찾기 삭제 요청
     * Then 즐겨찾기 삭제됨
     */
    @Test
    @DisplayName("즐겨찾기 관련 기능 인수통합테스트")
    void favoriteNormalScenario() {
        //when
        ExtractableResponse<Response> 즐겨찾기_생성_응답_결과 = 즐겨찾기_생성_요청(사용자_인증_토큰, 합정역, 당산역);
        //then
        즐겨찾기_생성됨(즐겨찾기_생성_응답_결과);

        //when
        ExtractableResponse<Response> 즐겨찾기_조회_응답_결과 = 즐겨찾기_목록_조회_요청(사용자_인증_토큰);
        //then
        즐겨찾기_목록_조회됨(즐겨찾기_조회_응답_결과, 합정역, 당산역);

        //when
        ExtractableResponse<Response> 즐겨찾기_삭제_응답_결과 = 즐겨찾기_목록_삭제_요청(사용자_인증_토큰, 즐겨찾기_생성_응답_결과);
        //then
        즐겨찾기_삭제됨(즐겨찾기_삭제_응답_결과);
    }

    /**
     * Feature: 즐겨찾기 관련 기능
     *
     * Background
     * Given 지하철역 등록되어 있음
     * And 지하철 노선 등록되어 있음
     * And 지하철 노선에 지하철역 등록되어 있음
     * And 회원 등록되어 있음
     * And 로그인 되어있음
     *
     * Scenario: 즐겨찾기 기능 관련 예외
     * When 동일한 지하철 출발역과 도착역으로 즐겨찾기 생성 요청
     * Then 즐겨찾기 생성 실패됨
     * When 연결되어 있지 않은 지하철 출발역과 도착역으로 즐겨찾기 생성 요청
     * Then 즐겨찾기 생성 실패됨
     * When 존재하지 않는 지하철 출발역이나 도착역으로 즐겨찾기 생성 요청
     * Then 즐겨찾기 생성 실패됨
     */
    @Test
    @DisplayName("즐겨찾기 관련 기능 비정상 시나리오")
    void favoriteExceptionScenario() {
        //when
        ExtractableResponse<Response> 동일한_역_즐겨찾기_생성_응답_결과 = 즐겨찾기_생성_요청(사용자_인증_토큰, 합정역, 합정역);
        //then
        즐겨찾기_생성_실패됨(동일한_역_즐겨찾기_생성_응답_결과);

        //when
        ExtractableResponse<Response> 연결되지_않은_역_즐겨찾기_생성_응답_결과 = 즐겨찾기_생성_요청(사용자_인증_토큰, 합정역, 동래역);
        //then
        즐겨찾기_생성_실패됨(연결되지_않은_역_즐겨찾기_생성_응답_결과);

        //when
        StationResponse 존재하지_않는_역 = new StationResponse(99L, "존재하지 않는 역", LocalDateTime.now(), LocalDateTime.now());
        ExtractableResponse<Response> 존재하지_않는_역_즐겨찾기_생성_응답_결과 = 즐겨찾기_생성_요청(사용자_인증_토큰, 합정역, 존재하지_않는_역);
        //then
        즐겨찾기_생성_실패됨(존재하지_않는_역_즐겨찾기_생성_응답_결과);
    }

    private ExtractableResponse<Response> 즐겨찾기_목록_삭제_요청(String accessToken, ExtractableResponse<Response> createResponse) {
        String uri = createResponse.header("Location");
        return sendDeleteWithAuth(accessToken, uri);
    }

    private ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(String accessToken) {
        return sendGetWithAuth(accessToken, "/favorites");
    }

    private ExtractableResponse<Response> 즐겨찾기_생성_요청(String accessToken, StationResponse source, StationResponse target) {
        FavoriteRequest favoriteRequest = new FavoriteRequest(source.getId(), target.getId());
        return sendPostWithAuth(accessToken, "/favorites", favoriteRequest);
    }

    private void 즐겨찾기_생성_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    private void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> response, StationResponse source, StationResponse target) {
        FavoriteResponse favoriteResponse = response.as(FavoriteResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        즐겨찾기_역_이름_동일함(source, target, favoriteResponse);
    }

    private void 즐겨찾기_역_이름_동일함(StationResponse source, StationResponse target, FavoriteResponse favoriteResponse) {
        List<String> favoriteStationNames = favoriteResponse.getStations().stream()
                .map(StationResponse::getName)
                .collect(Collectors.toList());

        List<String> names = Arrays.asList(source.getName(), target.getName());

        assertThat(favoriteStationNames).containsAll(names);
    }

    private void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }
}
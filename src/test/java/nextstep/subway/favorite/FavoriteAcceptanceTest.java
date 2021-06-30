package nextstep.subway.favorite;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.auth.acceptance.step.AuthAcceptanceStep.로그인_되어_있음;
import static nextstep.subway.favorite.acceptance.step.FavoriteAcceptanceStep.즐겨찾기_등록되어_있음;
import static nextstep.subway.favorite.acceptance.step.FavoriteAcceptanceStep.즐겨찾기_목록_조회_요청;
import static nextstep.subway.favorite.acceptance.step.FavoriteAcceptanceStep.즐겨찾기_목록_조회됨;
import static nextstep.subway.favorite.acceptance.step.FavoriteAcceptanceStep.즐겨찾기_삭제_요청;
import static nextstep.subway.favorite.acceptance.step.FavoriteAcceptanceStep.즐겨찾기_삭제됨;
import static nextstep.subway.favorite.acceptance.step.FavoriteAcceptanceStep.즐겨찾기_생성됨;
import static nextstep.subway.favorite.acceptance.step.FavoriteAcceptanceStep.즐겨찾기_생성을_요청;
import static nextstep.subway.line.acceptance.step.LineAcceptanceStep.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.step.LineSectionAcceptanceStep.지하철_노선에_지하철역_등록되어_있음;
import static nextstep.subway.member.acceptance.step.MemberAcceptanceStep.회원_등록되어_있음;
import static nextstep.subway.station.step.StationAcceptanceStep.지하철역_등록되어_있음;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";

    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private TokenResponse tokenResponse;

    /**
     * Background
     * Given 지하철역 등록되어 있음
     * And 지하철 노선 등록되어 있음
     * And 지하철 노선에 지하철역 등록되어 있음
     * And 회원 등록되어 있음
     * And 로그인 되어있음
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        final LineRequest 노선_등록_요청1 = LineRequest.builder()
                .name("이호선")
                .color("green")
                .upStationId(강남역.getId())
                .downStationId(교대역.getId())
                .distance(10)
                .build();

        final LineRequest 노선_등록_요청2 = LineRequest.builder()
                .name("삼호선")
                .color("orange")
                .upStationId(교대역.getId())
                .downStationId(남부터미널역.getId())
                .distance(10)
                .build();

        이호선 = 지하철_노선_등록되어_있음(노선_등록_요청1).as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(노선_등록_요청2).as(LineResponse.class);
        지하철_노선에_지하철역_등록되어_있음(삼호선, 남부터미널역, 양재역, 5);

        회원_등록되어_있음(EMAIL, PASSWORD, 20);

        tokenResponse = 로그인_되어_있음(EMAIL, PASSWORD);
    }

    /**
     * Scenario: 즐겨찾기를 관리
     * When 즐겨찾기 생성을 요청
     * Then 즐겨찾기 생성됨
     * When 즐겨찾기 생성을 요청
     * Then 즐겨찾기 생성됨
     * When 즐겨찾기 목록 조회 요청
     * Then 즐겨찾기 목록 조회됨
     * When 즐겨찾기 삭제 요청
     * Then 즐겨찾기 삭제됨
     */
    @Test
    void mangeFavorite() {
        // when
        ExtractableResponse<Response> 즐겨찾기_생성_응답1 = 즐겨찾기_생성을_요청(tokenResponse, 강남역.getId(), 남부터미널역.getId());

        // then
        즐겨찾기_생성됨(즐겨찾기_생성_응답1);

        // when
        ExtractableResponse<Response> 즐겨찾기_생성_응답2 = 즐겨찾기_등록되어_있음(tokenResponse, 강남역.getId(), 교대역.getId());
        ExtractableResponse<Response> 즐겨찾기_목록_조회_응답 = 즐겨찾기_목록_조회_요청(tokenResponse);

        // then
        즐겨찾기_목록_조회됨(즐겨찾기_목록_조회_응답);

        // when
        ExtractableResponse<Response> 즐겨찾기_삭제_응답 = 즐겨찾기_삭제_요청(tokenResponse, 즐겨찾기_생성_응답2);

        // then
        즐겨찾기_삭제됨(즐겨찾기_삭제_응답);
    }
}

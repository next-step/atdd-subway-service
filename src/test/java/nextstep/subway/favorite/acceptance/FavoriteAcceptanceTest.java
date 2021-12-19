package nextstep.subway.favorite.acceptance;

import static nextstep.subway.favorite.acceptance.FavoriteAcceptanceTestHelper.*;
import static nextstep.subway.line.acceptance.LineAcceptanceTestHelper.*;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTestHelper.*;
import static nextstep.subway.member.MemberAcceptanceTestHelper.*;
import static nextstep.subway.station.StationAcceptanceTestHelper.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    private static String EMAIL = "changchang743@gmail.com";
    private static String PASSWORD = "1234";
    private static int AGE = 29;

    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private String 사용자;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        // given
        // 지하철역_등록되어_있음
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        // 지하철_노선_등록되어_있음
        신분당선 = 지하철_노선_등록되어_있음("신분당선", "bg-red-600", 강남역, 양재역, 10);
        이호선 = 지하철_노선_등록되어_있음("이호선", "bg-red-600", 교대역, 강남역, 10);
        삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-red-600", 교대역, 양재역, 5);

        // 지하철_노선에_지하철역_등록되어_있음
        지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);

        // 회원_등록되어_있음
        회원_등록되어_있음(EMAIL, PASSWORD, AGE);

        // 로그인_되어있음
        사용자 = 로그인되어_있음(EMAIL, PASSWORD);
    }

    @DisplayName("즐겨찾기를 관리")
    @Test
    void manageFavorite() {
        // when
        ExtractableResponse<Response> 즐겨찾기_생성_응답 = 즐겨찾기_생성_요청(사용자, 강남역, 남부터미널역);
        // then
        즐겨찾기_생성됨(즐겨찾기_생성_응답);
        // when
        ExtractableResponse<Response> 즐겨찾기_목록_조회_응답 = 즐겨찾기_목록_조회_요청(사용자);
        // then
        즐겨찾기_목록_조회됨(즐겨찾기_목록_조회_응답);
        // when
        ExtractableResponse<Response> 즐겨찾기_삭제_응답 = 즐겨찾기_삭제_요청(사용자, 1L);
        // then
        즐겨찾기_삭제됨(즐겨찾기_삭제_응답);
    }

    @DisplayName("즐겨찾기 생성")
    @Test
    void saveFavorite() {
        // when
        ExtractableResponse<Response> 즐겨찾기_생성_응답 = 즐겨찾기_생성_요청(사용자, 강남역, 남부터미널역);
        // then
        즐겨찾기_생성됨(즐겨찾기_생성_응답);
    }

    @DisplayName("즐겨찾기 중복생성 오류")
    @Test
    void invalidSaveFavorite() {
        // when
        ExtractableResponse<Response> 즐겨찾기_생성_응답 = 즐겨찾기_생성_요청(사용자, 강남역, 남부터미널역);
        ExtractableResponse<Response> 중복_즐겨찾기_생성_응답 = 즐겨찾기_생성_요청(사용자, 강남역, 남부터미널역);

        // then
        즐겨찾기_생성_실패됨(중복_즐겨찾기_생성_응답);
    }



    @DisplayName("즐겨찾기 목록 조회")
    @Test
    void findAllMemberFavorites() {
        // given
        즐겨찾기_생성_요청(사용자, 강남역, 남부터미널역);
        즐겨찾기_생성_요청(사용자, 양재역, 교대역);

        // when
        ExtractableResponse<Response> 즐겨찾기_목록_조회_응답 = 즐겨찾기_목록_조회_요청(사용자);
        // then
        즐겨찾기_목록_조회됨(즐겨찾기_목록_조회_응답);
    }
}

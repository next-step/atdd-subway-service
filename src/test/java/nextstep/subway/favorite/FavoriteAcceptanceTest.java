package nextstep.subway.favorite;

import static nextstep.subway.auth.acceptance.AuthAcceptanceMethods.*;
import static nextstep.subway.favorite.FavoriteAcceptanceMethods.*;
import static nextstep.subway.line.acceptance.LineAcceptanceMethods.*;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceMethods.*;
import static nextstep.subway.member.MemberAcceptanceMethods.*;
import static nextstep.subway.station.StationAcceptanceMethods.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoritePathRequest;
import nextstep.subway.favorite.dto.FavoritePathResponse;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.station.dto.StationRequest;
import nextstep.subway.station.dto.StationResponse;

@DisplayName("즐겨찾기 관련 기능")
class FavoriteAcceptanceTest extends AcceptanceTest {
    private static final int DISTANCE_10 = 10;
    private static final int DISTANCE_5 = 5;
    private static final int DISTANCE_3 = 3;
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;

    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private TokenResponse token;

    @BeforeEach
    void beforeEach() {
        super.setUp();

        강남역 = 지하철역_등록되어_있음(StationRequest.of("강남역")).as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음(StationRequest.of("양재역")).as(StationResponse.class);
        교대역 = 지하철역_등록되어_있음(StationRequest.of("교대역")).as(StationResponse.class);
        남부터미널역 = 지하철역_등록되어_있음(StationRequest.of("남부터미널역")).as(StationResponse.class);

        LineRequest 신분당선_Request = LineRequest.of("신분당선", "RED", 강남역.getId(), 양재역.getId(), DISTANCE_10);
        LineRequest 이호선_Request = LineRequest.of("이호선", "GREED", 교대역.getId(), 강남역.getId(), DISTANCE_10);
        LineRequest 삼호선_Request = LineRequest.of("삼호선", "ORANGE", 남부터미널역.getId(), 양재역.getId(), DISTANCE_5);

        신분당선 = 지하철_노선_등록되어_있음(신분당선_Request).as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음(이호선_Request).as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(삼호선_Request).as(LineResponse.class);

        지하철_노선에_지하철역_등록되어_있음(삼호선.getId(), SectionRequest.of(교대역.getId(), 남부터미널역.getId(), DISTANCE_3));

        회원_생성을_요청(MemberRequest.of(EMAIL, PASSWORD, AGE));
        token = 회원_로그인_됨(TokenRequest.of(EMAIL, PASSWORD)).as(TokenResponse.class);
    }

    @DisplayName("즐겨찾기를 생성한다.")
    @Test
    void createFavorite1() {
        // given
        FavoritePathRequest request = FavoritePathRequest.of(교대역.getId(), 양재역.getId());

        // when
        ExtractableResponse<Response> response = 즐겨찾기_생성_요청(token, request);

        // then
        즐겨찾기_생성됨(response);
    }

    @DisplayName("존재하지 않는 출발역으로 즐겨찾기를 생성한다.")
    @Test
    void createFavorite2() {
        // given
        StationResponse 서울역 = 지하철역_등록되어_있음(StationRequest.of("서울역")).as(StationResponse.class);
        FavoritePathRequest request = FavoritePathRequest.of(서울역.getId(), 양재역.getId());

        // when
        ExtractableResponse<Response> response = 즐겨찾기_생성_요청(token, request);

        // then
        즐겨찾기_생성_실패(response);
    }

    @DisplayName("존재하지 않는 도착역으로 즐겨찾기를 생성한다.")
    @Test
    void createFavorite3() {
        // given
        StationResponse 서울역 = 지하철역_등록되어_있음(StationRequest.of("서울역")).as(StationResponse.class);

        // when
        FavoritePathRequest request = FavoritePathRequest.of(양재역.getId(), 서울역.getId());
        ExtractableResponse<Response> response = 즐겨찾기_생성_요청(token, request);

        // then
        즐겨찾기_생성_실패(response);
    }

    @DisplayName("서로 연결되지 않은 출발역, 도착역으로 즐겨찾기를 생성한다.")
    @Test
    void createFavorite4() {
        // given
        StationResponse 서울역 = 지하철역_등록되어_있음(StationRequest.of("서울역")).as(StationResponse.class);
        StationResponse 시청역 = 지하철역_등록되어_있음(StationRequest.of("시청역")).as(StationResponse.class);

        LineRequest 일호선_Request = LineRequest.of("일호선", "BLUE", 서울역.getId(), 시청역.getId(), DISTANCE_5);
        지하철_노선_등록되어_있음(일호선_Request).as(LineResponse.class);

        // when
        FavoritePathRequest request = FavoritePathRequest.of(교대역.getId(), 서울역.getId());
        ExtractableResponse<Response> response = 즐겨찾기_생성_요청(token, request);

        // then
        즐겨찾기_생성_실패(response);
    }

    @DisplayName("즐겨찾기에 저장된 목록을 조회한다.")
    @Test
    void getFavorites() {
        // given
        FavoritePathRequest 교대역_양재역_즐겨찾기_Request = FavoritePathRequest.of(교대역.getId(), 양재역.getId());
        FavoritePathRequest 교대역_강남역_즐겨찾기_Request = FavoritePathRequest.of(교대역.getId(), 강남역.getId());
        ExtractableResponse<Response> 교대역_양재역_즐겨찾기_Response = 즐겨찾기_등록됨(token, 교대역_양재역_즐겨찾기_Request);
        ExtractableResponse<Response> 교대역_강남역_즐겨찾기_Response = 즐겨찾기_등록됨(token, 교대역_강남역_즐겨찾기_Request);

        // when
        ExtractableResponse<Response> response = 즐겨찾기_목록_조회_요청(token);

        // then
        FavoritePathResponse 교대역_양재역_즐겨찾기 = 즐겨찾기_조회_요청(교대역_양재역_즐겨찾기_Response, token).as(FavoritePathResponse.class);
        FavoritePathResponse 교대역_강남역_즐겨찾기 = 즐겨찾기_조회_요청(교대역_강남역_즐겨찾기_Response, token).as(FavoritePathResponse.class);;
        즐겨찾기_목록_포함됨(response, Arrays.asList(교대역_양재역_즐겨찾기, 교대역_강남역_즐겨찾기));
    }

    @DisplayName("즐겨찾기를 삭제한다.")
    @Test
    void deleteFavorite1() {
        // given
        FavoritePathRequest 교대역_양재역_즐겨찾기_Request = FavoritePathRequest.of(교대역.getId(), 양재역.getId());
        ExtractableResponse<Response> 교대역_양재역_즐겨찾기_Response = 즐겨찾기_등록됨(token, 교대역_양재역_즐겨찾기_Request);
        FavoritePathResponse 교대역_양재역_즐겨찾기 = 즐겨찾기_조회_요청(교대역_양재역_즐겨찾기_Response, token).as(FavoritePathResponse.class);

        // when
        ExtractableResponse<Response> response = 즐겨찾기_삭제_요청(교대역_양재역_즐겨찾기.getId(), token);

        // then
        즐겨찾기_삭제됨(response);
    }

    @DisplayName("저장되지 않은 즐겨찾기를 삭제한다.")
    @Test
    void deleteFavorite2() {
        // when
        ExtractableResponse<Response> response = 즐겨찾기_삭제_요청(1L, token);

        // then
        즐겨찾기_삭제_실패됨(response);
    }
}
package nextstep.subway.favorite;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";


    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 남부터미널역;
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;

    private TokenResponse 로그인된_회원;

    @BeforeEach
    public void setUp() {
        super.setUp();
        /*
        Background
        Given 지하철역 등록되어 있음
        And 지하철 노선 등록되어 있음
        And 지하철 노선에 지하철역 등록되어 있음
        And 회원 등록되어 있음
        And 로그인 되어있음
        */
        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = StationAcceptanceTest.지하철역_등록되어_있음("교대역").as(StationResponse.class);
        남부터미널역 = StationAcceptanceTest.지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);

        신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10))
                .as(LineResponse.class);

        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(신분당선, 양재역, 교대역,10);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(신분당선, 교대역, 남부터미널역,5);

        AuthAcceptanceTest.회원이_등록되어_있음(EMAIL, PASSWORD, 20);

        로그인된_회원 = AuthAcceptanceTest.로그인_되어_있음(EMAIL, PASSWORD);
    }

/*
Scenario: 즐겨찾기를 관리
        When 즐겨찾기 생성을 요청
        Then 즐겨찾기 생성됨
        When 즐겨찾기 목록 조회 요청
        Then 즐겨찾기 목록 조회됨
        When 즐겨찾기 삭제 요청
        Then 즐겨찾기 삭제됨
*/
    @DisplayName("즐겨찾기를 관리한다.")
    @Test
    void manageFavorite() {
        //즐겨찾기_생성을_요청(로그인된_회원)


    }

    @DisplayName("즐겨찾기를 생성한다.")
    @Test
    void createFavorite() {
        //given
        FavoriteRequest favoriteRequest = new FavoriteRequest(강남역.getId(), 양재역.getId());

        //when
        final ExtractableResponse<Response> response = 즐겨찾기_생성_요청(로그인된_회원, favoriteRequest);

        //then
        즐겨찾기가_생성됨(response);
    }

    private void 즐겨찾기가_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header(HttpHeaders.LOCATION)).isNotEmpty();
    }


    private ExtractableResponse<Response> 즐겨찾기_생성_요청(TokenResponse tokenResponse, FavoriteRequest favoriteReqeust) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(tokenResponse.getAccessToken())
                .body(favoriteReqeust)
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }



}
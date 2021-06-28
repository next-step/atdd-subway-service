package nextstep.subway.favorite.acceptance;

import static nextstep.subway.member.MemberAcceptanceTest.BEARER;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoritesResponse;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;


@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final int AGE = 20;

    private String 토큰;
    private Long 강남역;
    private Long 광교역;

    @BeforeEach
    void setup() {
        회원_생성을_요청();

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class).getId();
        광교역 = StationAcceptanceTest.지하철역_등록되어_있음("광교역").as(StationResponse.class).getId();
        new LineRequest("신분당선", "bg-red-600", 강남역, 광교역, 10);

        토큰 = 회원_로그인_요청();
    }

    @DisplayName("즐겨찾기 관리 - 생성 조회")
    @Test
    void favoriteAdd() {
        //given

        //when
        ExtractableResponse<Response> 즐겨찾기_생성_응답 = 즐겨찾기_생성(토큰, 강남역, 광교역);
        //then
        즐겨찾기_생성_응답_확인(즐겨찾기_생성_응답);

        //when
        ExtractableResponse<Response> 즐겨찾기_조회_응답 = 즐겨찾기_조회(토큰);
        //then
        즐겨찾기_조회_응답_확인(즐겨찾기_조회_응답, 강남역, 광교역);

        //when
        즐겨찾기_제거(토큰);
        ExtractableResponse<Response> 즐겨찾기_제거_후_조회_응답 = 즐겨찾기_조회(토큰);
        //then
        즐겨찾기_제거_후_조회_응답_확인(즐겨찾기_제거_후_조회_응답);
    }

    @DisplayName("즐겨찾기 관리 - 생성 후 삭제 조회")
    @Test
    void favoriteRemove() {
        //given

        //when
        ExtractableResponse<Response> 즐겨찾기_생성_응답 = 즐겨찾기_생성(토큰, 강남역, 광교역);
        //then
        즐겨찾기_생성_응답_확인(즐겨찾기_생성_응답);

        //given
        즐겨찾기_제거(토큰);
        //when
        즐겨찾기_생성(토큰, 강남역, 광교역);
        ExtractableResponse<Response> 즐겨찾기_조회_응답 = 즐겨찾기_조회(토큰);
        //then
        즐겨찾기_조회_응답_확인(즐겨찾기_조회_응답, 강남역, 광교역);
    }

    @DisplayName("즐겨찾기 생성 실패 - 중복")
    @Test
    void favoriteFailedByDuplication() {
        //given

        //when
        ExtractableResponse<Response> 즐겨찾기_생성_응답 = 즐겨찾기_생성(토큰, 강남역, 광교역);
        //then
        즐겨찾기_생성_응답_확인(즐겨찾기_생성_응답);

        //when
        ExtractableResponse<Response> 중복_즐겨찾기_생성_응답 = 즐겨찾기_생성(토큰, 강남역, 광교역);
        //then
        중복_즐겨찾기_조회_응답_확인(중복_즐겨찾기_생성_응답);
    }

    @DisplayName("즐겨찾기 추가 실패 - 등록되지 않은 역")
    @Test
    void favoriteFailedByInvalidStation() {
        //given

        //when
        ExtractableResponse<Response> 즐겨찾기_생성_응답 = 즐겨찾기_생성(토큰, 0L, 0L);
        //then
        즐겨찾기_생성_실패_응답_확인(즐겨찾기_생성_응답);

        //when
        즐겨찾기_생성_응답 = 즐겨찾기_생성(토큰, 100L, 101L);
        //then
        즐겨찾기_생성_실패_응답_확인(즐겨찾기_생성_응답);
    }

    @DisplayName("즐겨찾기 구간 2개 추가")
    @Test
    void addFavorites() {
        // given
        Long 판교역 = StationAcceptanceTest.지하철역_등록되어_있음("판교역").as(StationResponse.class).getId();
        Long 정자역 = StationAcceptanceTest.지하철역_등록되어_있음("정자역").as(StationResponse.class).getId();
        즐겨찾기_생성(토큰, 강남역, 광교역);
        즐겨찾기_생성(토큰, 판교역, 정자역);
        // then
        ExtractableResponse<Response> 즐겨찾기_조회_응답 = 즐겨찾기_조회(토큰);
        // when
        즐겨찾기_목록_조회_응답_확인(즐겨찾기_조회_응답, 판교역, 정자역);
    }

    private void 즐겨찾기_목록_조회_응답_확인(ExtractableResponse<Response> 즐겨찾기_조회_응답, Long 두번째_출발역, Long 두번째_도착역) {
        FavoritesResponse favoritesResponse = 즐겨찾기_조회_응답.as(FavoritesResponse.class);
        List<FavoriteResponse> favoriteResponses = favoritesResponse.getFavoriteResponses();
        assertThat(favoriteResponses.size()).isEqualTo(2);
        assertThat(favoriteResponses.get(0).getSource().getId()).isEqualTo(강남역);
        assertThat(favoriteResponses.get(0).getTarget().getId()).isEqualTo(광교역);

        assertThat(favoriteResponses.get(1).getSource().getId()).isEqualTo(두번째_출발역);
        assertThat(favoriteResponses.get(1).getTarget().getId()).isEqualTo(두번째_도착역);
    }

    private ExtractableResponse<Response> 즐겨찾기_조회(String 토큰) {
        FavoriteRequest favoriteRequest = new FavoriteRequest(강남역, 광교역);

        return RestAssured
                .given().header("authorization", BEARER + 토큰).log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(favoriteRequest)
                .when().get("/favorites")
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> 회원_생성을_요청() {
        MemberRequest memberRequest = new MemberRequest(EMAIL, PASSWORD, AGE);

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

    private ExtractableResponse<Response> 즐겨찾기_생성(String 토큰, Long 출발역, Long 도착역) {
        FavoriteRequest favoriteRequest = new FavoriteRequest(출발역, 도착역);

        return RestAssured
                .given().header("authorization", BEARER + 토큰).log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(favoriteRequest)
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }

    private void 즐겨찾기_생성_응답_확인(ExtractableResponse<Response> 즐겨찾기_생성_응답) {
        assertThat(즐겨찾기_생성_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 즐겨찾기_조회_응답_확인(ExtractableResponse<Response> 즐겨찾기_조회_응답, Long 출발역_번호, Long 도착역_번호) {
        FavoritesResponse favoritesResponse = 즐겨찾기_조회_응답.as(FavoritesResponse.class);
        List<FavoriteResponse> favoriteResponses = favoritesResponse.getFavoriteResponses();
        assertThat(favoriteResponses.get(0).getSource().getId()).isEqualTo(출발역_번호);
        assertThat(favoriteResponses.get(0).getTarget().getId()).isEqualTo(도착역_번호);
    }

    private void 즐겨찾기_제거(String 토큰) {
        RestAssured
                .given().header("authorization", BEARER + 토큰).log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/favorites")
                .then().log().all()
                .extract();
    }

    private void 즐겨찾기_제거_후_조회_응답_확인(ExtractableResponse<Response> 즐겨찾기_조회_응답) {
        FavoritesResponse favoritesResponse = 즐겨찾기_조회_응답.as(FavoritesResponse.class);
        List<FavoriteResponse> favoriteResponses = favoritesResponse.getFavoriteResponses();
        assertThat(favoriteResponses.size()).isEqualTo(0);
    }

    private void 중복_즐겨찾기_조회_응답_확인(ExtractableResponse<Response> 즐겨찾기_조회_응답) {
        즐겨찾기_조회_실패(즐겨찾기_조회_응답.statusCode());
    }

    private void 즐겨찾기_생성_실패_응답_확인(ExtractableResponse<Response> 즐겨찾기_생성_응답) {
        즐겨찾기_조회_실패(즐겨찾기_생성_응답.statusCode());
    }

    private void 즐겨찾기_조회_실패(int 조회_응답_코드) {
        assertThat(조회_응답_코드).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
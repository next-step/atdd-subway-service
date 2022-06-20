package nextstep.subway.favorite;

import com.google.common.collect.Lists;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.회원_로그인_성공확인;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.회원_로그인을_시도한다;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_생성_요청;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_생성됨;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성됨;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    public static final String INVALID_ACCESS_TOKEN = "Invalid AccessToken";

    private TokenResponse loginToken;
    private StationResponse 청담역;
    private StationResponse 뚝섬유원지역;
    private StationResponse 건대입구역;
    private StationResponse 군자역;
    private LineRequest 칠호선Request;

    /**
     * Feature: 즐겨찾기를 관리한다.
     *
     *   Background
     *     Given 지하철역 등록되어 있음
     *     And 지하철 노선 등록되어 있음
     *     And 지하철 노선에 지하철역 등록되어 있음
     *     And 회원 등록되어 있음
     *     And 로그인 되어있음
     *
     *   Scenario: 즐겨찾기를 관리
     *     When 즐겨찾기 생성을 요청
     *     Then 즐겨찾기 생성됨
     *     When 즐겨찾기 목록 조회 요청
     *     Then 즐겨찾기 목록 조회됨
     *     When 즐겨찾기 삭제 요청
     *     Then 즐겨찾기 삭제됨
     */

    @BeforeEach
    void init() {
        super.setUp();

        //given 지하철역이 등록되어 있음
        청담역 = StationAcceptanceTest.지하철역_등록되어_있음("청담역").as(StationResponse.class);
        뚝섬유원지역 = StationAcceptanceTest.지하철역_등록되어_있음("뚝섬유원지역").as(StationResponse.class);
        건대입구역 = StationAcceptanceTest.지하철역_등록되어_있음("건대입구역").as(StationResponse.class);
        군자역 = new StationResponse(10L, "군자역");

        칠호선Request = new LineRequest("신분당선", "bg-red-600", 청담역.getId(), 뚝섬유원지역.getId(), 20);

        ExtractableResponse<Response> 칠호선Response = 지하철_노선_생성_요청(칠호선Request);
        지하철_노선_생성됨(칠호선Response);

        지하철_노선에_지하철역_등록_요청(칠호선Response.as(LineResponse.class), 뚝섬유원지역, 건대입구역, 10);

        //given 회원 등록되어 있음
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(MemberAcceptanceTest.EMAIL,
                                                                    MemberAcceptanceTest.PASSWORD,
                                                                    MemberAcceptanceTest.AGE);
        회원_생성됨(createResponse);

        //given 로그인 되어있음
        ExtractableResponse<Response> loginResponse = 회원_로그인을_시도한다(MemberAcceptanceTest.EMAIL, MemberAcceptanceTest.PASSWORD);
        회원_로그인_성공확인(loginResponse);

        loginToken = loginResponse.as(TokenResponse.class);
    }

    @Test
    @DisplayName("등록되어있는_출발역_도착역으로_즐겨찾기를_등록하면_등록된다(HappyPath)")
    void 즐겨찾기를_등록하면_즐겨찾기가_정상적으로_등록된다() {
        ExtractableResponse<Response> response = 즐겨찾기_등록_요청(loginToken, 청담역.getId(), 건대입구역.getId());

        즐겨찾기가_정상적으로_등록(response, 청담역.getId(), 건대입구역.getId());
    }

    @Test
    void 존재하지_않는_역을_즐겨찾기하면_등록되지_않는다() {
        ExtractableResponse<Response> response = 즐겨찾기_등록_요청(loginToken, 청담역.getId(), 군자역.getId());

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void 유효하지_않는_로그인으로_즐겨찾기를_등록하면_등록되지_않는다() {
        ExtractableResponse<Response> response = 즐겨찾기_등록_요청(new TokenResponse(INVALID_ACCESS_TOKEN), 청담역.getId(), 건대입구역.getId());

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    @DisplayName("등록되어있는_출발역_도착역으로_즐겨찾기를_등록하면_등록된다(HappyPath)")
    void 즐겨찾기를_조회하면_내_즐겨찾기가_정상적으로_조회된다() {
        즐겨찾기_등록_요청(loginToken, 청담역.getId(), 건대입구역.getId());
        즐겨찾기_등록_요청(loginToken, 뚝섬유원지역.getId(), 건대입구역.getId());

        ExtractableResponse<Response> response = 내_즐겨찾기_목록을_조회한다(loginToken);

        즐겨찾기가_정상적으로_조회(response, Arrays.asList(청담역, 뚝섬유원지역), Arrays.asList(건대입구역, 건대입구역));
    }

    @Test
    void 유효하지_않는_로그인으로_즐겨찾기를_조회하면_조회되지_않는다() {
        ExtractableResponse<Response> response = 내_즐겨찾기_목록을_조회한다(new TokenResponse(INVALID_ACCESS_TOKEN));

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    void 내_즐겨찾기를_삭제요청하면_즐겨찾기가_삭제된다() {
        즐겨찾기_등록_요청(loginToken, 청담역.getId(), 건대입구역.getId());
        즐겨찾기_등록_요청(loginToken, 뚝섬유원지역.getId(), 건대입구역.getId());

        ExtractableResponse<Response> response = 내_즐겨찾기_목록을_조회한다(loginToken);
        List<FavoriteResponse> resultFavorites = response.jsonPath().getList(".", FavoriteResponse.class).stream().collect(Collectors.toList());

        FavoriteResponse favoriteResponse = resultFavorites.get(0);
        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(loginToken, favoriteResponse.getId());
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        ExtractableResponse<Response> newResponse = 내_즐겨찾기_목록을_조회한다(loginToken);
        즐겨찾기가_정상적으로_조회(newResponse, Arrays.asList(뚝섬유원지역), Arrays.asList(건대입구역));
    }

    @Test
    void 유효하지_않는_로그인으로_즐겨찾기를_삭제하면_삭제되지_않는다() {
        즐겨찾기_등록_요청(loginToken, 청담역.getId(), 건대입구역.getId());
        즐겨찾기_등록_요청(loginToken, 뚝섬유원지역.getId(), 건대입구역.getId());

        ExtractableResponse<Response> response = 내_즐겨찾기_목록을_조회한다(loginToken);
        List<FavoriteResponse> resultFavorites = response.jsonPath().getList(".", FavoriteResponse.class).stream().collect(Collectors.toList());

        FavoriteResponse favoriteResponse = resultFavorites.get(0);
        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(new TokenResponse(INVALID_ACCESS_TOKEN), favoriteResponse.getId());
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    public static ExtractableResponse<Response> 즐겨찾기_등록_요청(TokenResponse tokenResponse, Long source, Long target) {
        FavoriteRequest favoriteRequest = new FavoriteRequest(source, target);
        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(favoriteRequest)
                .when().post("/favorites")
                .then().log().all().
                extract();
    }

    public static void 즐겨찾기가_정상적으로_등록(ExtractableResponse<Response> response, Long source, Long target) {
        assertThat(response).isNotNull()
                .satisfies(response1 -> {
                    assertThat(response1.statusCode()).isEqualTo(HttpStatus.CREATED.value());
                    assertThat(response1.as(FavoriteResponse.class).getSource().getId()).isEqualTo(source);
                    assertThat(response1.as(FavoriteResponse.class).getTarget().getId()).isEqualTo(target);
                });
    }

    public static ExtractableResponse<Response> 내_즐겨찾기_목록을_조회한다(TokenResponse tokenResponse) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/favorites")
                .then().log().all().
                extract();
    }

    public static void 즐겨찾기가_정상적으로_조회(ExtractableResponse<Response> response, List<StationResponse> sources, List<StationResponse> targets) {
        assertThat(response).isNotNull()
                .satisfies(response1 -> {
                    assertThat(response1.statusCode()).isEqualTo(HttpStatus.OK.value());
                    List<FavoriteResponse> resultFavorites = response1.jsonPath().getList(".", FavoriteResponse.class).stream().collect(Collectors.toList());
                    assertThat(resultFavorites.stream().map(FavoriteResponse::getSource).collect(Collectors.toList())).containsExactlyInAnyOrderElementsOf(sources);
                    assertThat(resultFavorites.stream().map(FavoriteResponse::getTarget).collect(Collectors.toList())).containsExactlyInAnyOrderElementsOf(targets);
                });
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(TokenResponse tokenResponse, Long favoriteId) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/favorites/{favoriteId}", favoriteId)
                .then().log().all().
                extract();
    }
}
package nextstep.subway.favorite;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_요청;
import static nextstep.subway.member.MemberAcceptanceTest.EMAIL;
import static nextstep.subway.member.MemberAcceptanceTest.PASSWORD;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private LineResponse 신분당선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 정자역;
    private StationResponse 광교역;
    private String accessToken;

    @BeforeEach
    public void setUp() {
        super.setUp();

        회원_생성을_요청(EMAIL, PASSWORD, 20);
        accessToken = 로그인_요청(EMAIL, PASSWORD).as(TokenResponse.class).getAccessToken();

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        정자역 = StationAcceptanceTest.지하철역_등록되어_있음("정자역").as(StationResponse.class);
        광교역 = StationAcceptanceTest.지하철역_등록되어_있음("광교역").as(StationResponse.class);

        LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(),
            10);
        신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);
    }

    /**
     * Given 지하철역 등록되어 있음
     * And 지하철 노선 등록되어 있음
     * And 지하철 노선에 지하철역 등록되어 있음
     * And 회원 등록되어 있음
     * And 로그인 되어있음
     * when 즐겨찾기 생성 요청을 하면
     * then 즐겨찾기가 생성된다.
     */
    @DisplayName("즐겨찾기 생성 요청을 한다.")
    @Test
    public void createFavorite() {
        //when
        ExtractableResponse<Response> 즐겨찾기_생성_응답 = 즐겨찾기_생성_요청(accessToken, 강남역.getId(), 광교역.getId());
        //then
        즐겨찾기_생성됨(즐겨찾기_생성_응답);
    }

    /**
     * Given 지하철역 등록되어 있음
     * And 지하철 노선 등록되어 있음
     * And 지하철 노선에 지하철역 등록되어 있음
     * And 회원 등록되어 있음
     * And 로그인 되어있음
     * And 즐겨찾기 생성 되어있음
     * when 즐겨찾기 목록 조회 요청을 하면
     * then 즐겨찾기 목록이 조회된다.
     */
    @DisplayName("즐겨찾기 목록 조회 요청을 한다.")
    @Test
    public void findFavorites() {
        //given
        즐겨찾기_생성_요청(accessToken, 강남역.getId(), 광교역.getId());
        //when
        ExtractableResponse<Response> 즐겨찾기_조회_요청 = 즐겨찾기_조회_요청(accessToken);
        //then
        즐겨찾기_정보_조회됨(즐겨찾기_조회_요청, 강남역, 광교역);
    }

    public static void 즐겨찾기_정보_조회됨(ExtractableResponse<Response> response, StationResponse source, StationResponse target) {
        List<FavoriteResponse> favoriteResponses = response.jsonPath().getList(".", FavoriteResponse.class);

        List<StationResponse> favoriteSources = favoriteResponses.stream().map(favoriteResponse -> favoriteResponse.getSource()).collect(Collectors.toList());

        List<StationResponse> favoriteTargets = favoriteResponses.stream().map(favoriteResponse -> favoriteResponse.getTarget()).collect(Collectors.toList());

        assertThat(favoriteSources).contains(source);
        assertThat(favoriteTargets).contains(target);
    }

    public static ExtractableResponse<Response> 즐겨찾기_생성_요청(String accessToken, Long sourceId, Long targetId) {
        FavoriteRequest favoriteRequest = new FavoriteRequest(sourceId, targetId);

        return RestAssured
            .given().log().all()
            .auth().oauth2(accessToken)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(favoriteRequest)
            .when().post("/favorites")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_조회_요청(String accessToken) {

        return RestAssured
            .given().log().all()
            .auth().oauth2(accessToken)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/favorites")
            .then().log().all()
            .extract();
    }

    public static void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

}
package nextstep.subway.favorite;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.stream.Stream;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_요청;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private LineResponse 신분당선;
    private StationResponse 강남역;
    private StationResponse 양재역;

    String accessToken;
    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);

        LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 양재역.getId(), 10);
        신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);

        String email = "test@email.com";
        String password = "1234";

        회원_생성을_요청(email, password, 12);
        ExtractableResponse<Response> response = 로그인_요청(email, password);
        accessToken = response.as(TokenResponse.class).getAccessToken();
    }

    @DisplayName("나의 즐겨찾기를 관리한다.")
    @TestFactory
    Stream<DynamicNode> manageMyFavorite() {
        return Stream.of(
            dynamicTest("즐겨찾기 생성을 요청",this::create_favorite),
            dynamicTest("즐겨찾기 목록 조회 요청", this::view_favorite)
        );
    }

    private void create_favorite() {
        ExtractableResponse<Response> response = 즐겨찾기_생성_요청(accessToken, 강남역, 양재역);

        즐겨찾기_생성됨(response);
    }


    private void view_favorite() {
        ExtractableResponse<Response> response = 즐겨찾기_조회_요청(accessToken);

        즐겨찾기_조회됨(response, 강남역, 양재역);
    }

    private ExtractableResponse<Response> 즐겨찾기_생성_요청(String accessToken, StationResponse sourceStation,
        StationResponse targetStation) {
        FavoriteRequest request = new FavoriteRequest(sourceStation.getId(), targetStation.getId());
        return RestAssured
            .given().log().all()
            .auth().oauth2(accessToken)
            .body(request)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/favorites")
            .then().log().all()
            .extract();
    }

    private void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private ExtractableResponse<Response> 즐겨찾기_조회_요청(String accessToken) {
        return RestAssured
            .given().log().all()
            .auth().oauth2(accessToken)
            .when().get("/favorites")
            .then().log().all()
            .extract();
    }

    private void 즐겨찾기_조회됨(ExtractableResponse<Response> response,
        StationResponse source, StationResponse target) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        List<FavoriteResponse> favoriteResponses = response.jsonPath().getList(".", FavoriteResponse.class);
        assertThat(favoriteResponses.size()).isEqualTo(1);

        FavoriteResponse favoriteResponse = favoriteResponses.get(0);
        assertThat(favoriteResponse.getId()).isNotNull();
        assertThat(favoriteResponse.getSource().getName()).isEqualTo(source.getName());
        assertThat(favoriteResponse.getTarget().getName()).isEqualTo(target.getName());
    }
}

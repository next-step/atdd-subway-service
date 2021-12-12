package nextstep.subway.favorite;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private TokenResponse tokenResponse;
    private StationResponse 당산역;
    private StationResponse 선유도역;
    private LineResponse 구호선;

    @Autowired
    private LineService lineService;

    @BeforeEach
    void setup() {
        createToken();
        // given
        당산역 = StationAcceptanceTest.지하철역_등록되어_있음("당산역").as(StationResponse.class);
        선유도역 = StationAcceptanceTest.지하철역_등록되어_있음("선유도역").as(StationResponse.class);

        구호선 = 지하철_노선_등록되어_있음("구호선", "bg-red-600", 당산역, 선유도역, 20);
    }

    @Test
    @DisplayName("Favorite 생성")
    public void createFavoriteTest() {
        ExtractableResponse<Response> favoriteCreateResponse = createFavorite(당산역.getId(), 선유도역.getId());
        즐겨찾기_생성됨(favoriteCreateResponse);
    }

    @Test
    @DisplayName("Favorite 조회")
    public void searchFavoriteTest() {
        ExtractableResponse<Response> favoriteCreateResponse = createFavorite(당산역.getId(), 선유도역.getId());
        ExtractableResponse<Response> searchFavoriteResponse = searchFavorite();
        즐겨찾기_조회됨(searchFavoriteResponse);
    }

    @Test
    @DisplayName("Favorite 삭제")
    public void deleteFavoriteTest() {
        ExtractableResponse<Response> favoriteCreateResponse = createFavorite(당산역.getId(), 선유도역.getId());
        ExtractableResponse<Response> deleteResponse = deleteFavorite(favoriteCreateResponse);

        즐겨찾기_삭제됨(deleteResponse);
    }

    private ExtractableResponse<Response> createFavorite(Long sourceId, Long targetId) {
        FavoriteRequest favoriteRequest = new FavoriteRequest(sourceId, targetId);
        return RestAssured.given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(tokenResponse.getAccessToken())
                .body(favoriteRequest)
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> searchFavorite() {
        return RestAssured.given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(tokenResponse.getAccessToken())
                .when().get("/favorites")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> deleteFavorite(ExtractableResponse<Response> response) {
        String uri = response.header("Location");

        return RestAssured.given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(tokenResponse.getAccessToken())
                .when().delete(uri)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> signUpMember(String email, String password, Integer age) {
        MemberRequest memberRequest = new MemberRequest(email, password, age);
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberRequest)
                .when().post("/members")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> login(String email, String password) {
        TokenRequest tokenRequest = new TokenRequest(email, password);
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tokenRequest)
                .when().post("/login/token")
                .then().log().all().
                extract();
    }

    private void createToken() {
        String email = "abc@test.com";
        String password = "1234";
        Integer age = 12;
        signUpMember(email, password, age);
        ExtractableResponse<Response> loginResponse = login(email, password);
        tokenResponse = loginResponse.as(TokenResponse.class);

    }

    private LineResponse 지하철_노선_등록되어_있음(String lineName, String color, StationResponse upStation, StationResponse downStation, int distance) {
        LineRequest lineRequest = new LineRequest(lineName, color, upStation.getId(), downStation.getId(), distance);
        return lineService.saveLine(lineRequest);
    }

    private void 즐겨찾기_조회됨(ExtractableResponse<Response> searchFavoriteResponse) {
        List<FavoriteResponse> favoriteResponseList = new ArrayList<>(searchFavoriteResponse
                .jsonPath()
                .getList(".", FavoriteResponse.class));

        List<Long> sourceStationIds = favoriteResponseList.stream()
                .map(it -> it.getSource().getId())
                .collect(Collectors.toList());

        List<Long> targetStationIds = favoriteResponseList.stream()
                .map(it -> it.getTarget().getId())
                .collect(Collectors.toList());

        List<Long> stationIds = new ArrayList<>();
        stationIds.addAll(sourceStationIds);
        stationIds.addAll(targetStationIds);

        assertThat(favoriteResponseList.size()).isEqualTo(1);
        assertThat(stationIds).contains(당산역.getId(), 선유도역.getId());

    }

    private void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 즐겨찾기_삭제됨(ExtractableResponse<Response> deleteResponse) {
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

}

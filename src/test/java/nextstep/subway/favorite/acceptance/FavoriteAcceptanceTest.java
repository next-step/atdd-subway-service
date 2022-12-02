package nextstep.subway.favorite.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_성공;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_요청;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록됨;
import static nextstep.subway.member.MemberAcceptanceTest.*;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private LineResponse 이호선;
    private StationResponse 강남역;
    private StationResponse 잠실역;
    private StationResponse 삼성역;
    private String accessToken;

    @BeforeEach
    public void setUp() {
        super.setUp();
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        잠실역 = 지하철역_등록되어_있음("잠실역").as(StationResponse.class);
        삼성역 = 지하철역_등록되어_있음("삼성역").as(StationResponse.class);

        LineRequest lineRequest = new LineRequest("이호선", "green", 강남역.getId(), 삼성역.getId(), 10);
        이호선 = 지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);

        ExtractableResponse<Response> response = 지하철_노선에_지하철역_등록_요청(이호선, 강남역, 잠실역, 5);
        지하철_노선에_지하철역_등록됨(response);

        ExtractableResponse<Response> createResponse = 회원_생성을_요청(EMAIL, PASSWORD, AGE);
        회원_생성됨(createResponse);

        ExtractableResponse<Response> loginResponse = 로그인_요청(new TokenRequest(EMAIL, PASSWORD));
        로그인_성공(loginResponse);

        accessToken = loginResponse.as(TokenResponse.class).getAccessToken();
    }

    @DisplayName("즐겨찾기를 관리")
    @Test
    void manageFavorites() {
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(accessToken, 강남역.getId(), 삼성역.getId());

        즐겨찾기_생성_성공(createResponse);

        ExtractableResponse<Response> readResponse = 즐겨찾기_목록_조회_요청(accessToken);

        즐겨찾기_목록_조회_성공(readResponse, 강남역.getId(), 삼성역.getId());

        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(accessToken, readResponse.as(FavoriteResponse[].class)[0].getId());

        즐겨찾기_삭제_성공(deleteResponse);
    }

    public ExtractableResponse<Response> 즐겨찾기_생성_요청(String accessToken, Long sourceStationId, Long targetStationId) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new FavoriteRequest(sourceStationId, targetStationId))
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }

    public ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(String accessToken) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/favorites")
                .then().log().all()
                .extract();
    }

    public ExtractableResponse<Response> 즐겨찾기_삭제_요청(String accessToken, Long favoriteId) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .when().delete("/favorites/{favoriteId}", favoriteId)
                .then().log().all()
                .extract();
    }

    public void 즐겨찾기_생성_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public void 즐겨찾기_목록_조회_성공(ExtractableResponse<Response> response, Long sourceStationId, Long targetStationId) {
        List<FavoriteResponse> favorites = Arrays.asList(response.as(FavoriteResponse[].class));
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(favorites).hasSize(1),
                () -> assertThat(favorites.get(0).getSource().getId()).isEqualTo(sourceStationId),
                () -> assertThat(favorites.get(0).getTarget().getId()).isEqualTo(targetStationId)
        );
    }

    public void 즐겨찾기_삭제_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }


}
package nextstep.subway.favorite;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.*;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.*;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.*;
import static nextstep.subway.member.MemberAcceptanceTest.*;
import static nextstep.subway.station.StationAcceptanceTest.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private StationResponse 강남역;
    private StationResponse 교대역;
    private StationResponse 시청역;
    private RequestSpecification given;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        // given
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        시청역 = 지하철역_등록되어_있음("시청역").as(StationResponse.class);
        final LineRequest lineRequest = new LineRequest("2호선", "green", 강남역.getId(), 교대역.getId(), 10);
        final LineResponse lineResponse = 지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);
        // when
        지하철_노선에_지하철역_등록_요청(lineResponse, 교대역, 시청역, 20);

        // given
        final User user = new User("test@test.com", "password", 20);
        // when
        final ExtractableResponse<Response> createResponse = 회원_생성을_요청(user);
        // then
        회원_생성됨(createResponse);

        // when
        final ExtractableResponse<Response> loginResponse = 로그인_요청(user.getEmail(), user.getPassword());
        // then
        로그인_됨(loginResponse);

        given = given(loginResponse.as(TokenResponse.class));
    }

    private RequestSpecification given(final TokenResponse tokenResponse) {
        return RestAssured.given()
            .log()
            .all()
            .auth()
            .oauth2(tokenResponse.getAccessToken());
    }

    @DisplayName("즐겨찾기를 관리한다")
    @Test
    void manageFavorite() {
        // when
        final ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(given, 강남역.getId(), 교대역.getId());
        // then
        즐겨찾기_생성됨(createResponse);

        // when
        final ExtractableResponse<Response> findResponse = 즐겨찾기_목록_조회_요청(given);
        // // then
        즐겨찾기_목록_조회됨(findResponse);

        // given
        final FavoriteResponse favoriteResponse = favoriteResponses(findResponse).get(0);
        // when
        final ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(given, favoriteResponse.getId());
        // then
        회원_삭제됨(deleteResponse);
    }

    public ExtractableResponse<Response> 즐겨찾기_생성_요청(final RequestSpecification given,
        final long sourceId, final long targetId) {

        return given
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(new FavoriteRequest(sourceId, targetId))
            .when().post("/favorites")
            .then().log().all()
            .extract();
    }

    public ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(final RequestSpecification given) {
        return given
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/favorites")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(final RequestSpecification given, final Long id) {
        return given
            .when().delete("/favorites/" + id)
            .then().log().all()
            .extract();
    }

    public void 즐겨찾기_생성됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public void 즐겨찾기_목록_조회됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        final List<FavoriteResponse> favoriteResponses = favoriteResponses(response);

        assertThat(favoriteResponses.size()).isEqualTo(1);
        final FavoriteResponse favorite = favoriteResponses.get(0);

        assertAll(
            () -> assertThat(favorite.getId()).isEqualTo(1),
            () -> assertThat(favorite.getSource().getName()).isEqualTo(강남역.getName()),
            () -> assertThat(favorite.getTarget().getName()).isEqualTo(교대역.getName())
        );
    }

    private List<FavoriteResponse> favoriteResponses(final ExtractableResponse<Response> response) {
        return response.jsonPath()
            .getList(".", FavoriteResponse.class);
    }

    public static void 회원_삭제됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}

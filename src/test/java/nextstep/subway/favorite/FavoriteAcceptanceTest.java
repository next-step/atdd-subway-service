package nextstep.subway.favorite;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    private static final int DEFAULT_DISTANCE = 10;
    private static final String EMAIL = "example@sample.com";
    private static final String PASSWORD = "example";
    private static final int AGE = 18;

    private StationResponse 신도림;
    private StationResponse 도림천;
    private StationResponse 양천구청;

    private LineResponse 이호선;

    private TokenResponse tokenResponse;

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();
        신도림 = StationAcceptanceTest.지하철역_등록되어_있음("신도림").as(StationResponse.class);
        도림천 = StationAcceptanceTest.지하철역_등록되어_있음("도림천").as(StationResponse.class);
        양천구청 = StationAcceptanceTest.지하철역_등록되어_있음("양천구청").as(StationResponse.class);

        이호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("2호선", "green", 신도림.getId(), 도림천.getId(), DEFAULT_DISTANCE)).as(LineResponse.class);

        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(이호선, 도림천, 양천구청, DEFAULT_DISTANCE);

        MemberAcceptanceTest.회원_생성을_요청(EMAIL, PASSWORD, AGE);

        tokenResponse = AuthAcceptanceTest.로그인_요청(EMAIL, PASSWORD).as(TokenResponse.class);
    }

    @DisplayName("즐겨찾기 정보를 관리한다")
    @Test
    public void manageFavorite() {
        ExtractableResponse<Response> createdResponse = 즐겨찾기_생성을_요청();
        즐겨찾기_생성됨(createdResponse);
        ExtractableResponse<Response> selectedResponse = 즐겨찾기_목록_조회_요청();
        즐겨찾기_목록_조회됨(selectedResponse);
        ExtractableResponse<Response> deletedResponse = 즐겨찾기_삭제_요청();
        즐겨찾기_삭제됨(deletedResponse);
    }

    public static ExtractableResponse<Response> 즐겨찾기_생성을_요청(String token, Station source, Station target) {
        return RestAssured.given().log().all()
                .auth().oauth2(token)
                .param("source", source.getId())
                .param("target", target.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().log().all()
                .post("/favorites")
                .then().log().all()
                .extract();
    }

    public static void 즐겨찾기_생성됨(ExtractableResponse<Response> createdResponse) {
        assertThat(createdResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 즐겨찾기_생성_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(String token) {
        return RestAssured.given().log().all()
                .auth().oauth2(token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().log().all()
                .get("/favorites")
                .then().log().all()
                .extract();
    }

    public static void 즐겨찾기_조회됨(ExtractableResponse<Response> selectedResponse, StationResponse source, StationResponse target) {
        assertThat(selectedResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        FavoriteResponse favoriteResponse = selectedResponse.as(FavoriteResponse.class);
        assertThat(favoriteResponse.getSource()).isEquals(source);
        assertThat(favoriteResponse.getTarget()).isEquals(target);
    }

    public static void 즐겨찾기_조회_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(String token, FavoriteResponse favorite) {
        return RestAssured.given().log().all()
                .auth().oauth2(token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().log().all()
                .delete("/favorites" + favorite.getId())
                .then().log().all()
                .extract();
    }

    public static void 즐겨찾기_삭제됨(ExtractableResponse<Response> deletedResponse) {
        assertThat(deletedResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static void 즐겨찾기_삭제_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
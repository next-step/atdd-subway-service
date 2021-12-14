package nextstep.subway.favorite;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
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

import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인을_실행한다;
import static nextstep.subway.member.MemberAcceptanceTest.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 정자역;
    private StationResponse 광교역;
    private StationResponse 송파역;
    private TokenResponse 사용자;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        ExtractableResponse<Response> createResponse = MemberAcceptanceTest.회원_생성을_요청(EMAIL, PASSWORD, AGE);
        TokenRequest param = new TokenRequest(EMAIL, PASSWORD);
        사용자 = 로그인을_실행한다(param);
        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        정자역 = StationAcceptanceTest.지하철역_등록되어_있음("정자역").as(StationResponse.class);
        광교역 = StationAcceptanceTest.지하철역_등록되어_있음("광교역").as(StationResponse.class);
        송파역 = StationAcceptanceTest.지하철역_등록되어_있음("송파역").as(StationResponse.class);

        LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 10);
        신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);
    }

    @DisplayName("즐겨찾기를 관리한다.")
    @Test
    void manageFavorites() {
        // when
        ExtractableResponse<Response> createResponse1 = 즐겨찾기_생성을_요청(사용자, 강남역, 양재역);
        // then
        즐겨찾기_생성됨(createResponse1);

        // when
        ExtractableResponse<Response> findResponse1 = 즐겨찾기_목록_조회_요청(사용자, createResponse1);
        // then
        즐겨찾기_조회됨(findResponse1, 강남역, 양재역);

        // when
        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(사용자, createResponse1);
        // then
        즐겨찾기_삭제됨(deleteResponse);
        // when
        ExtractableResponse<Response> findResponse2 = 즐겨찾기_목록_조회_요청(사용자, createResponse1);
        // then
        즐겨찾기_없음(findResponse2);
    }

    private void 즐겨찾기_없음(ExtractableResponse<Response> findResponse) {
    }

    private void 즐겨찾기_삭제됨(ExtractableResponse<Response> deleteResponse) {
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private void 즐겨찾기_조회됨(ExtractableResponse<Response> findResponse, StationResponse sourceStation, StationResponse targetStation) {
        List<FavoriteResponse> favoriteResponses = findResponse.jsonPath().getList(".", FavoriteResponse.class);
        FavoriteResponse favoriteResponse1 = favoriteResponses.get(0);
        assertThat(favoriteResponse1.getSourceStation().getName()).isEqualTo(sourceStation.getName());
        assertThat(favoriteResponse1.getTargetStation().getName()).isEqualTo(targetStation.getName());
    }

    private ExtractableResponse<Response> 즐겨찾기_삭제_요청(TokenResponse 사용자, ExtractableResponse<Response> createResponse) {
        String uri = createResponse.header("Location");
        return RestAssured
                .given().log().all()
                .auth().oauth2(사용자.getAccessToken())
                .when().delete(uri)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(TokenResponse 사용자, ExtractableResponse<Response> createResponse) {

        return RestAssured
                .given().log().all()
                .auth().oauth2(사용자.getAccessToken())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/favorites")
                .then().log().all()
                .extract();
    }

    private void 즐겨찾기_생성됨(ExtractableResponse<Response> createResponse) {
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private ExtractableResponse<Response> 즐겨찾기_생성을_요청(TokenResponse 사용자, StationResponse sourceStation, StationResponse targetStation) {
        FavoriteRequest favoriteRequest = new FavoriteRequest(sourceStation.getId(), targetStation.getId());

        return RestAssured
                .given().log().all()
                .auth().oauth2(사용자.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(favoriteRequest)
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }
}

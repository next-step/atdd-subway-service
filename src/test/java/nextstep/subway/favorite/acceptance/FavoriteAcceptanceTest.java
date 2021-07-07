package nextstep.subway.favorite.acceptance;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.*;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.*;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.*;
import static nextstep.subway.member.MemberAcceptanceTest.*;
import static nextstep.subway.station.StationAcceptanceTest.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;

@DisplayName("즐겨찾기 관련 기능")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private StationResponse 강남역;
    private StationResponse 판교역;
    private StationResponse 광교역;
    private LineResponse 신분당선;

    private String accessToken;

    @BeforeEach
    void favoriteSetUp() {
        String 이메일 = "ehdgml3206@gmail.com";
        String 비밀번호 = "1234";
        Integer 나이 = 31;
        회원_등록되어_있음(이메일, 비밀번호, 나이);
        accessToken = 로그인_되어_있음(이메일, 비밀번호);

        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        판교역 = 지하철역_등록되어_있음("판교역").as(StationResponse.class);
        광교역 = 지하철역_등록되어_있음("광교역").as(StationResponse.class);
        LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 10);
        신분당선 = 지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);
        지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 판교역, 5);
    }

    @DisplayName("즐겨찾기를 관리한다.")
    @Test
    void manageFavorite() {
        // when
        ExtractableResponse<Response> createdResponse = 즐겨찾기_생성_요청(accessToken, 강남역.getId(), 판교역.getId());
        // then
        즐겨찾기_생성됨(createdResponse);

        // when
        ExtractableResponse<Response> createdResponse2 = 즐겨찾기_생성_요청(accessToken, 판교역.getId(), 광교역.getId());
        // then
        즐겨찾기_생성됨(createdResponse2);

        // when
        ExtractableResponse<Response> findResponse = 즐겨찾기_목록_조회_요청(accessToken);
        // then
        즐겨찾기_목록_조회됨(findResponse);
        즐겨찾기_목록에_포함됨(findResponse,  Arrays.asList(createdResponse, createdResponse2));

        // when
        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(accessToken, createdResponse);
        // then
        즐겨찾기_삭제됨(deleteResponse);

        // when
        ExtractableResponse<Response> findResponseAfterDelete = 즐겨찾기_목록_조회_요청(accessToken);
        // then
        즐겨찾기_목록_조회됨(findResponseAfterDelete);
        즐겨찾기_목록에_포함됨(findResponseAfterDelete,  Arrays.asList(createdResponse2));
    }

    @DisplayName("다른 회원의 즐겨찾기는 삭제할수 없다")
    @Test
    void deleteNotMineFavorite() {
        // given
        String 다른_회원_이메일 = "test@gmail.com";
        String 다른_회원_비밀번호 = "1234";
        Integer 다른_회원_나이 = 31;
        회원_등록되어_있음(다른_회원_이메일, 다른_회원_비밀번호, 다른_회원_나이);

        // when
        ExtractableResponse<Response> createdResponse = 즐겨찾기_생성_요청(accessToken, 강남역.getId(), 판교역.getId());
        // then
        즐겨찾기_생성됨(createdResponse);

        // when
        ExtractableResponse<Response> findResponse = 즐겨찾기_목록_조회_요청(accessToken);
        // then
        즐겨찾기_목록_조회됨(findResponse);
        즐겨찾기_목록에_포함됨(findResponse,  Arrays.asList(createdResponse));

        // when
        accessToken = 로그인_되어_있음(다른_회원_이메일, 다른_회원_비밀번호);
        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(accessToken, createdResponse);
        // then
        즐겨찾기_삭제_실패함(deleteResponse);
    }

    private ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(String accessToken) {
        return RestAssured
            .given().log().all()
            .auth().oauth2(accessToken)
            .when().get("/favorites")
            .then()
            .log().all()
            .extract();
    }

    private ExtractableResponse<Response> 즐겨찾기_생성_요청(String accessToken, Long source, Long target) {
        FavoriteRequest favoriteRequest = new FavoriteRequest(source, target);

        return RestAssured.given().log().all()
            .auth().oauth2(accessToken)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(favoriteRequest)
            .when().post("/favorites")
            .then().log().all()
            .extract();
    }

    private ExtractableResponse<Response> 즐겨찾기_삭제_요청(String accessToken, ExtractableResponse<Response> response) {
        String uri = response.header("Location");

        return RestAssured
            .given().log().all()
            .auth().oauth2(accessToken)
            .when().delete(uri)
            .then()
            .log().all()
            .extract();
    }

    private void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        assertThat(HttpStatus.CREATED.value()).isEqualTo(response.statusCode());
    }

    private void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(HttpStatus.OK.value()).isEqualTo(response.statusCode());
    }

    private void 즐겨찾기_목록에_포함됨(ExtractableResponse<Response> response, List<ExtractableResponse<Response>> createdResponses) {
        List<Long> expectedLineIds = createdResponses.stream()
            .map(it -> Long.parseLong(it.header("Location").split("/")[2]))
            .collect(Collectors.toList());

        List<Long> resultLineIds = response.jsonPath().getList(".", FavoriteResponse.class).stream()
            .map(FavoriteResponse::getId)
            .collect(Collectors.toList());

        assertThat(resultLineIds).containsAll(expectedLineIds);
    }

    private void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
        assertThat(HttpStatus.NO_CONTENT.value()).isEqualTo(response.statusCode());
    }

    private void 즐겨찾기_삭제_실패함(ExtractableResponse<Response> response) {
        assertThat(HttpStatus.INTERNAL_SERVER_ERROR.value()).isEqualTo(response.statusCode());
    }
}

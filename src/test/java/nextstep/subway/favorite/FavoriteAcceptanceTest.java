package nextstep.subway.favorite;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
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

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private StationResponse 청량리역;
    private StationResponse 신도림역;
    private StationResponse 관악역;
    private StationResponse 금정역;
    private StationResponse 당정역;

    private String token;

    @BeforeEach
    void given() {
        청량리역 = StationAcceptanceTest.지하철역_등록되어_있음("청량리역").as(StationResponse.class);
        신도림역 = StationAcceptanceTest.지하철역_등록되어_있음("신도림역").as(StationResponse.class);
        관악역 = StationAcceptanceTest.지하철역_등록되어_있음("관악역").as(StationResponse.class);
        금정역 = StationAcceptanceTest.지하철역_등록되어_있음("금정역").as(StationResponse.class);
        당정역 = StationAcceptanceTest.지하철역_등록되어_있음("당정역").as(StationResponse.class);

        LineRequest lineRequest = new LineRequest("일호선", "bg-blue-600", 청량리역.getId(), 신도림역.getId(), 10);
        LineResponse 일호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);

        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(일호선, 신도림역, 관악역, 3);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(일호선, 관악역, 금정역, 6);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(일호선, 금정역, 당정역, 9);

        String email = "yohan@test.com";
        String password = "password";
        MemberAcceptanceTest.회원_등록되어_있음(email, password, 29);
        token = AuthAcceptanceTest.회원_로그인되어_있음(email, password);
    }

    /**
     * - 1호선 -
     * 청량리역 - 신도림역 - 관악역 - 금정역 - 당정역
     */
    @DisplayName("즐겨찾기를 관리한다.")
    @Test
    void mangeFavorite() {
        // given
        FavoriteRequest request = new FavoriteRequest(청량리역.getId(), 당정역.getId());
        ExtractableResponse<Response> createdResponse = 즐겨찾기_생성_요청(token, request);

        // when
        즐겨찾기_생성됨(createdResponse);

        // when
        ExtractableResponse<Response> findedResponse = 즐겨찾기_목록_조회_요청(token);

        // then
        즐겨찾기_목록_조회됨(findedResponse, Arrays.asList(createdResponse));

        // when
        ExtractableResponse<Response> deletedResponse = 즐겨찾기_삭제_요청(token, createdResponse);

        //then
        즐겨찾기_삭제됨(deletedResponse);
    }

    private ExtractableResponse<Response> 즐겨찾기_생성_요청(final String token, final FavoriteRequest request) {
        return RestAssured
                .given().log().all().auth().oauth2(token)
                .body(request)
                .accept(MediaType.ALL_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/favorites")
                .then()
                .log().all()
                .extract();
    }

    private ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(final String token) {
        return RestAssured
                .given().log().all().auth().oauth2(token)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/favorites")
                .then()
                .log().all()
                .extract();
    }

    private ExtractableResponse<Response> 즐겨찾기_삭제_요청(final String token, final ExtractableResponse<Response> response) {
        String uri = response.header("Location");

        return RestAssured
                .given().log().all().auth().oauth2(token)
                .accept(MediaType.ALL_VALUE)
                .when()
                .delete(uri)
                .then()
                .log().all()
                .extract();
    }

    private void 즐겨찾기_생성됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 즐겨찾기_목록_조회됨(final ExtractableResponse<Response> response,
                             final List<ExtractableResponse<Response>> createResponses) {
        List<Long> expectedIds = createResponses.stream()
                .map(it -> Long.parseLong(it.header("Location").split("/")[2]))
                .collect(Collectors.toList());

        List<Long> resultIds = response.jsonPath().getList(".", FavoriteResponse.class).stream()
                .map(FavoriteResponse::getId)
                .collect(Collectors.toList());

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(resultIds).containsAll(expectedIds);
    }

    private void 즐겨찾기_삭제됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}

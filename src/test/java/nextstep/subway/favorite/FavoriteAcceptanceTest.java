package nextstep.subway.favorite;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.domain.FavoriteResponse;
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
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 정자역;
    private StationResponse 광교역;

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final int AGE = 20;

    private TokenResponse token;

    @BeforeEach
    public void setUp() {
        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        정자역 = StationAcceptanceTest.지하철역_등록되어_있음("정자역").as(StationResponse.class);
        광교역 = StationAcceptanceTest.지하철역_등록되어_있음("광교역").as(StationResponse.class);

        LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 10);
        신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);

        // when
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 양재역, 2);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(신분당선, 정자역, 강남역, 5);

        // then
        ExtractableResponse<Response> response = LineAcceptanceTest.지하철_노선_조회_요청(신분당선);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록됨(response);
        LineSectionAcceptanceTest.지하철_노선에_지하철역_순서_정렬됨(response, Arrays.asList(정자역, 강남역, 양재역, 광교역));

        // when
        ExtractableResponse<Response> createResponse = MemberAcceptanceTest.회원_생성을_요청(EMAIL, PASSWORD, AGE);
        // then
        MemberAcceptanceTest.회원_생성됨(createResponse);

        // when
        ExtractableResponse<Response> tokenResponse = AuthAcceptanceTest.토큰_발급_요청(EMAIL, PASSWORD, AGE);
        // then
        assertThat(tokenResponse.statusCode()).isEqualTo(HttpStatus.OK.value());

        token = tokenResponse.jsonPath().getObject(".", TokenResponse.class);
    }

    @DisplayName("즐겨찾기를 관리한다.")
    @Test
    void manageMember() {
        // when
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성을_요청(token, 강남역, 양재역);
        // then
        즐겨찾기_생성됨(createResponse);

        // when
        ExtractableResponse<Response> createResponse2 = 즐겨찾기_생성을_요청(token, 정자역, 광교역);
        // then
        즐겨찾기_생성됨(createResponse);

        // when
        ExtractableResponse<Response> findResponse = 즐겨찾기_목록을_조회(token);
        // then
        즐겨찾기_목록_조회됨(findResponse);

        FavoriteResponse favorite = findResponse.jsonPath().getList(".", FavoriteResponse.class).get(0);
        // when
        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제(token, favorite.getId());
        // then
        즐겨찾기_삭제됨(deleteResponse);

    }

    private ExtractableResponse<Response> 즐겨찾기_삭제(TokenResponse token, Long favoriteId) {
        return RestAssured
                .given().log().all()
                .header("Authorization", "Bearer " + token.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/favorites/{id}", favoriteId.toString())
                .then().log().all().extract();
    }

    private void 즐겨찾기_삭제됨(ExtractableResponse<Response> deleteResponse) {
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private ExtractableResponse<Response> 즐겨찾기_목록을_조회(TokenResponse token) {
        return RestAssured
                .given().log().all()
                .header("Authorization", "Bearer " + token.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/favorites")
                .then().log().all().extract();
    }

    private void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> findResponse) {
        assertThat(findResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(findResponse.jsonPath().getList(".", FavoriteResponse.class)).hasSize(2);
    }

    private ExtractableResponse<Response> 즐겨찾기_생성을_요청(TokenResponse token, StationResponse source, StationResponse target) {
        // when
        Map<String, String> params = new HashMap<>();
        params.put("source", source.getId().toString());
        params.put("target", target.getId().toString());

        return RestAssured
                .given().log().all()
                .header("Authorization", "Bearer " + token.getAccessToken())
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/favorites")
                .then().log().all().extract();
    }

    private void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }
}
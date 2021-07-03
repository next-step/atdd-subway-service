package nextstep.subway.favorite;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.dto.MemberResponse;
import nextstep.subway.station.dto.StationResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_되어_있음;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.member.MemberAcceptanceTest.*;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;
import static org.apache.http.HttpHeaders.AUTHORIZATION;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 정자역;
    private StationResponse 광교역;
    private LineResponse 신분당선;
    private TokenResponse 사용자;

    @BeforeEach
    void setup() {
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        정자역 = 지하철역_등록되어_있음("정자역").as(StationResponse.class);
        광교역 = 지하철역_등록되어_있음("광교역").as(StationResponse.class);

        신분당선 = 지하철_노선_등록되어_있음(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 10)).as(LineResponse.class);

        회원_생성을_요청(EMAIL, PASSWORD, AGE);
        사용자 = 로그인_되어_있음(EMAIL, PASSWORD).as(TokenResponse.class);
    }

    @Test
    @DisplayName("즐겨찾기를 관리한다")
    void manageFavoriteTest() {

        // when
        ExtractableResponse<Response> createResponse = 즐겨찾기_추가_요청(사용자.getAccessToken(), new FavoriteRequest(강남역.getId(), 광교역.getId()));

        // then
        즐겨찾기_추가됨(createResponse);

        // when
        ExtractableResponse<Response> findAllResponse = 즐겨찾기_목록_조회_요청(사용자.getAccessToken());

        // then
        즐겨찾기_목록_조회됨(findAllResponse);
    }

    private ExtractableResponse<Response> 즐겨찾기_추가_요청(String token, FavoriteRequest request) {
        return RestAssured
                .given().log().all()
                .header(AUTHORIZATION, "Bearer " + token)
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/favorites")
                .then().log().all().extract();
    }

    private ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(String token) {
        return RestAssured
                .given().log().all()
                .header(AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/favorites")
                .then().log().all().extract();
    }

    private void 즐겨찾기_추가됨(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> findAllResponse) {
        Assertions.assertThat(findAllResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
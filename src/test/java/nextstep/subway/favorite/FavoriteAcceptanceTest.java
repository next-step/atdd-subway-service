package nextstep.subway.favorite;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.member.dto.FavoriteRequest;
import nextstep.subway.member.dto.FavoriteResponse;
import nextstep.subway.station.dto.StationResponse;

import static java.util.Arrays.*;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.*;
import static nextstep.subway.member.MemberAcceptanceTest.*;
import static nextstep.subway.station.StationAcceptanceTest.*;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.http.HttpStatus.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    @DisplayName("즐겨찾기를 관리한다.")
    @Test
    void manageFavorite() {
        StationResponse 강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        StationResponse 양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        StationResponse 수서역 = 지하철역_등록되어_있음("수서역").as(StationResponse.class);
        StationResponse 선릉역 = 지하철역_등록되어_있음("선릉역").as(StationResponse.class);

        회원_생성을_요청("baek@github.com", "white", AGE);
        ExtractableResponse<Response> response = 로그인_요청(new TokenRequest("baek@github.com", "white"));
        String token = response.as(TokenResponse.class).getAccessToken();

        ExtractableResponse<Response> created1 = 즐겨찾기_생성_요청("Bearer", token, new FavoriteRequest(강남역.getId(), 양재역.getId()));
        ExtractableResponse<Response> created2 = 즐겨찾기_생성_요청("Bearer", token, new FavoriteRequest(수서역.getId(), 선릉역.getId()));
        assertThat(asList(created1, created2)).extracting(ExtractableResponse::statusCode).allMatch(c -> c == CREATED.value());

        ExtractableResponse<Response> found = 즐겨찾기_조회_요청("Bearer", token);
        List<FavoriteResponse> result = found.jsonPath().getList(".", FavoriteResponse.class);
        assertThat(result).extracting(f -> f.getSource().getName()).containsExactly("강남역", "수서역");
        assertThat(result).extracting(f -> f.getTarget().getName()).containsExactly("양재역", "선릉역");
    }

    public static ExtractableResponse<Response> 즐겨찾기_생성_요청(String bearerType, String token, FavoriteRequest request) {
        return RestAssured
                .given().log().all()
                .header("Authorization", bearerType + token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_조회_요청(String bearerType, String token) {
        return RestAssured
                .given().log().all()
                .header("Authorization", bearerType + token)
                .when().get("/favorites")
                .then().log().all()
                .extract();
    }
}

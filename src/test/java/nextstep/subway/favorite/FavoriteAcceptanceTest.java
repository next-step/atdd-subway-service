package nextstep.subway.favorite;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.*;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.*;
import static nextstep.subway.member.MemberAcceptanceTest.*;
import static nextstep.subway.station.StationAcceptanceTest.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.station.dto.StationResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    TokenResponse 사용자;

    StationResponse 강남역;
    StationResponse 광교역;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        광교역 = 지하철역_등록되어_있음("광교역").as(StationResponse.class);
        지하철_노선_등록되어_있음(new LineRequest("신분당선", "레드", 강남역.getId(), 광교역.getId(), 10));

        회원_생성을_요청(EMAIL, PASSWORD, AGE);
        사용자 = 회원_로그인_되어_있음(EMAIL, PASSWORD);
    }

    @Test
    @DisplayName("즐겨찾기를 관리 한다")
    void manageFavorite() {
        //     - When 즐겨찾기 생성을 요청
        // when
        FavoriteRequest request = new FavoriteRequest(강남역.getId(), 광교역.getId());
        ExtractableResponse<Response> createResponse = RestAssured
            .given().log().all()
            .auth().oauth2(사용자.getAccessToken())
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(request)
            .when().post("/favorites")
            .then().log().all().extract();

        //       - Then 즐겨찾기 생성됨
        // then
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(createResponse.header("Location")).isNotBlank();

        //     - When 즐겨찾기 목록 조회 요청
        // when
        ExtractableResponse<Response> queryResponse = RestAssured
            .given().log().all()
            .auth().oauth2(사용자.getAccessToken())
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/favorites")
            .then().log().all().extract();

//       - Then 즐겨찾기 목록 조회됨
        // then
        assertThat(queryResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<FavoriteResponse> favorite
            = queryResponse.jsonPath().getList(".", FavoriteResponse.class);
        assertThat(favorite.get(0).getId()).isNotNull();
        assertThat(favorite.get(0).getSource()).isEqualTo(강남역);
        assertThat(favorite.get(0).getTarget()).isEqualTo(광교역);

        //     - When 즐겨찾기 삭제 요청
        // when
        String id = createResponse.header("Location").split("/")[2];
        ExtractableResponse<Response> deleteResponse = RestAssured
            .given().log().all()
            .auth().oauth2(사용자.getAccessToken())
            .when().delete("/favorites/{id}", id)
            .then().log().all().extract();

//       - Then 즐겨찾기 삭제됨
        // then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        //     - When 즐겨찾기 목록 조회 요청
        // when
        ExtractableResponse<Response> emptyResponse = RestAssured
            .given().log().all()
            .auth().oauth2(사용자.getAccessToken())
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/favorites")
            .then().log().all().extract();

        //       - Then 즐겨찾기 빈 목록 조회됨
        // then
        assertThat(emptyResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(emptyResponse.as(List.class).isEmpty()).isTrue();
    }
}

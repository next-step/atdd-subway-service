package nextstep.subway.favorite;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.favorite.dto.FavoriteRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.회원_로그인_됨;
import static nextstep.subway.member.MemberAcceptanceTest.*;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private String token;
    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        회원_생성을_요청(EMAIL, PASSWORD,28);
        token = 회원_로그인_됨(EMAIL, PASSWORD);
    }

    @DisplayName("즐겨찾기를 추가한다.")
    @Test
    void createFavorite() {
        // when
        FavoriteRequest request = new FavoriteRequest(1L, 5L);

        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .body(request)
                .header(HttpHeaders.AUTHORIZATION, makeAccessToken(token))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/favorites")
                .then().log().all().extract();

        // then
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }
}

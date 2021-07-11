package nextstep.subway.favorite;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.station.dto.StationResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.회원_로그인_됨;
import static nextstep.subway.member.MemberAcceptanceTest.*;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private String token;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        지하철역_등록되어_있음("강남역").as(StationResponse.class);
        지하철역_등록되어_있음("광교역").as(StationResponse.class);

        회원_생성을_요청(EMAIL, PASSWORD,28);
        token = 회원_로그인_됨(EMAIL, PASSWORD);
    }

    @DisplayName("즐겨찾기를 추가한다.")
    @Test
    void createFavorite() {
        // when
        FavoriteRequest request = new FavoriteRequest(1L, 5L);

        //when
        ExtractableResponse<Response> response = 즐겨찾기_생성_요청(request);

        // then
        즐겨찾기_생성됨(response);
    }

    private void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private ExtractableResponse<Response> 즐겨찾기_생성_요청(FavoriteRequest request) {
        return RestAssured
                    .given().log().all()
                    .body(request)
                    .header(HttpHeaders.AUTHORIZATION, makeAccessToken(token))
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when().post("/favorites")
                    .then().log().all().extract();
    }
}

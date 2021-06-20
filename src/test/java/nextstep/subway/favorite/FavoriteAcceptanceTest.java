package nextstep.subway.favorite;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.subway.auth.AuthHelper.로그인_요청;
import static nextstep.subway.line.acceptance.LineHelper.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineHelper.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.member.MemberHelper.회원_생성됨;
import static nextstep.subway.member.MemberHelper.회원_생성을_요청;
import static nextstep.subway.station.StationHelper.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final int AGE = 20;

    private StationResponse 강남역;
    private StationResponse 역삼역;
    private StationResponse 선릉역;

    @BeforeEach
    void 미리_생성() {
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        역삼역 = 지하철역_등록되어_있음("역삼역").as(StationResponse.class);
        선릉역 = 지하철역_등록되어_있음("선릉역").as(StationResponse.class);

        LineResponse 이호선 = 지하철_노선_등록되어_있음(new LineRequest("2호선", "green", 강남역.getId(), 역삼역.getId(), 5)).as(LineResponse.class);
        지하철_노선에_지하철역_등록_요청(이호선, 역삼역, 선릉역, 5);
    }

    @DisplayName("즐겨찾기 생성")
    @Test
    void 즐겨찾기_생성() {
        //given
        회원_생성을_요청(EMAIL, PASSWORD, AGE);
        TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
        ExtractableResponse<Response> loginResponse = 로그인_요청(tokenRequest);
        String accessToken = loginResponse.jsonPath().getObject(".", TokenResponse.class).getAccessToken();

        //given
        FavoriteRequest favoriteRequest = new FavoriteRequest(강남역.getId(), 선릉역.getId());

        //when
        ExtractableResponse createResponse = RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(favoriteRequest)
                .when().post("/favorites")
                .then().log().all().extract();

        //then
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }
}
package nextstep.subway.favorite;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_요청을_한다;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.토큰을_얻는다;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_생성_요청;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private StationResponse 강남역;
    private StationResponse 역삼역;
    private LineResponse 이호선;
    private final String EMAIL = "testl@test.com";
    private final String PASSWORD = "test";
    private final int AGE = 10;

    private String TOKEN;

    @Override
    public void setUp() {
        super.setUp();
        회원_생성을_요청(EMAIL, PASSWORD, AGE);
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        역삼역 = 지하철역_등록되어_있음("광교역").as(StationResponse.class);
        이호선 = 지하철_노선_생성_요청(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 역삼역.getId(), 10))
                .as(LineResponse.class);
        TOKEN = 토큰을_얻는다(로그인_요청을_한다(EMAIL, PASSWORD));
    }

    @Test
    @DisplayName("즐겨 찾기를 생성한다")
    void creatFavorite() {
        // when
        ExtractableResponse<Response> response = 즐겨찾기_생성_요청(TOKEN, 강남역, 역삼역);

        // then
        상태코드가_기대값과_일치하는지_검증한다(response, HttpStatus.CREATED);
    }

    public static ExtractableResponse<Response> 즐겨찾기_생성_요청(String accessToken, StationResponse 강남역, StationResponse 역삼역) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new FavoriteRequest(강남역.getId(), 역삼역.getId()))
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }

    private static void 상태코드가_기대값과_일치하는지_검증한다(ExtractableResponse<Response> response, HttpStatus status) {
        assertThat(response.statusCode()).isEqualTo(status.value());
    }
}
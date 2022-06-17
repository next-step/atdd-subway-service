package nextstep.subway.favorite;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_요청;
import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.암호_이메일_입력;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_생성_요청;
import static nextstep.subway.member.MemberAcceptanceTest.토큰정보_획득;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private ExtractableResponse<Response> 사용자정보;
    private StationResponse 강남역;
    private StationResponse 광교역;

    @BeforeEach
    public void setUp() {
        super.setUp();
        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        광교역 = StationAcceptanceTest.지하철역_등록되어_있음("광교역").as(StationResponse.class);

        지하철_노선_생성_요청(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 10));
        지하철_노선_생성_요청(new LineRequest("구신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 15));

        final String PASSWORD = "password";
        final String EMAIL = "email@email.com";

        회원_생성을_요청(EMAIL, PASSWORD, 20);
        사용자정보 = 로그인_요청(암호_이메일_입력(PASSWORD, EMAIL));
    }

    /**
     * When 즐겨찾기 생성을 요청
     * Then 즐겨찾기 생성됨
     */
    @DisplayName("즐겨찾기 생성 하기")
    @Test
    void favoritesScenarioTest() {
        // When
        ExtractableResponse<Response> createFavoritesResponse = 즐겨찾기_생성을_요청(사용자정보, 강남역, 광교역);

        // Then
        즐겨찾기_생성됨(createFavoritesResponse);
    }

    /**
     * When 저장되지 않는 지하철역 정보로 즐겨찾기 생성을 요청
     * Then 즐겨찾기 생성이 안됨
     */
    @DisplayName("저장되지 않는 지하철역 정보로 즐겨찾기 생성시 생성이 되지 않는다.")
    @Test
    void favoriteSaveTestWhenNoSavedStation() {
        // When
        ExtractableResponse<Response> invalidResponse = 즐겨찾기_생성을_요청(사용자정보, new StationResponse(100L, "모르는역", LocalDateTime.now(), LocalDateTime.now()), 광교역);

        // Them
        즐겨찾기_생성이_안됨(invalidResponse);
    }

    public static void 즐겨찾기_생성이_안됨(ExtractableResponse<Response> invalidResponse) {
        assertThat(HttpStatus.valueOf(invalidResponse.statusCode())).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    public static void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        String uri = response.header("Location");
        assertThat(response.header("Location")).isNotBlank();
        assertThat(HttpStatus.valueOf(response.statusCode())).isEqualTo(HttpStatus.CREATED);
    }


    public static ExtractableResponse<Response> 즐겨찾기_생성을_요청(ExtractableResponse<Response> response, StationResponse startStation, StationResponse endStation)
    {
        return RestAssured.given().log().all()
                .auth().oauth2(토큰정보_획득(response).getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new FavoriteRequest(startStation.getId(), endStation.getId()))
                .when()
                .post("/favorites")
                .then().log().all()
                .extract();
    }
}
package nextstep.subway.favorite;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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

    /**
     * Given : 즐겨찾기가 추가 되어 있고
     * When: 즐겨찾기 목록 조회 요청을 하면
     * Then : 즐겨찾기 목록을 정상적으로 가져온다.
     */
    @DisplayName("즐겨찾기가 추가 되어 있을경우 목록을 조회 요청시 정상적으로 가져온다.")
    @Test
    void getFavoriteMyList() {
        // Given
        ExtractableResponse<Response> createFavoritesResponse = 즐겨찾기_생성을_요청(사용자정보, 강남역, 광교역);

        // When
        ExtractableResponse<Response> myFavoriteListResponse = 즐겨찾기_목록_조회(사용자정보);

        // Then
        final long id = 즐겨찾기_등록번호_찾기(createFavoritesResponse);
        즐겨찾기_목록_정상_확인(myFavoriteListResponse, Arrays.asList(new FavoriteResponse(id, 강남역, 광교역)));
    }

    /**
     * Given : 즐겨찾기에 추가 되어 있고
     * When : 즐겨 찾기 삭제를 요청 하면
     * Then : 즐겨찾기 목록에서 정상적으로 삭제 된다.
     */
    @DisplayName("즐겨찾기에 추가 되어 있고 삭제 요청시 정상적으로 삭제된다.")
    @Test
    void deleteFavorite() {
        // Given
        ExtractableResponse<Response> 생성_요청_결과 = 즐겨찾기_생성을_요청(사용자정보, 강남역, 광교역);

        // When
        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(사용자정보, 즐겨찾기_등록번호_찾기(생성_요청_결과));

        // Then
        즐겨찾기_정상_삭제(deleteResponse);
        즐겨찾기_목록_정상_확인(즐겨찾기_목록_조회(사용자정보), Collections.emptyList());
    }

    /**
     * When : 잘못된 즐겨찾기 id 로 요청 시
     * Then : 에러가 발생한다.
     */
    @DisplayName("잘못된 즐겨찾기 id 로 요청 하기")
    @Test
    void invalidFavoriteDeleteWhenFavoriteIdIsInvalid() {
        // When
        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(사용자정보, 2);

        // Then
        즐겨찾기_삭제_실패(deleteResponse);
    }

    public  static void 즐겨찾기_삭제_실패(ExtractableResponse<Response> deleteResponse) {
        assertThat(HttpStatus.valueOf(deleteResponse.statusCode())).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    public static void 즐겨찾기_정상_삭제(ExtractableResponse<Response> deleteResponse) {
        assertThat(HttpStatus.valueOf(deleteResponse.statusCode())).isEqualTo(HttpStatus.NO_CONTENT);
    }

    public static void 즐겨찾기_목록_정상_확인(ExtractableResponse<Response> myFavoriteListResponse, List<FavoriteResponse> expectedResult) {
        assertThat(Arrays.asList(myFavoriteListResponse.as(FavoriteResponse[].class))).containsExactlyElementsOf(expectedResult);
    }

    public static Long 즐겨찾기_등록번호_찾기(ExtractableResponse<Response> createFavoritesResponse) {
        String location = createFavoritesResponse.header("location");
        return Long.valueOf(location.substring(location.lastIndexOf("/")+1));
    }

    public static void 즐겨찾기_생성이_안됨(ExtractableResponse<Response> invalidResponse) {
        assertThat(HttpStatus.valueOf(invalidResponse.statusCode())).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    public static void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
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

    public static ExtractableResponse<Response> 즐겨찾기_목록_조회(ExtractableResponse<Response> response) {
        return RestAssured.given().log().all()
                .auth().oauth2(토큰정보_획득(response).getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .get("/favorites")
                .then()
                .log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(ExtractableResponse<Response> 사용자정보, long id) {
        return RestAssured.given().log().all()
                .auth().oauth2(토큰정보_획득(사용자정보).getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .delete("/favorites/"+id)
                .then().log().all()
                .extract();
    }
}
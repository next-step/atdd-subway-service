package nextstep.subway.favorite;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.station.domain.Station;
import nextstep.subway.utils.RestAssuredRequest;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.신규_회원가입_후_로그인_토큰_추출;
import static nextstep.subway.station.acceptance.StationAcceptanceTest.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    private static final String PATH = "/favorites";
    private static final String MY_EMAIL = "test@test.com";
    private static final String OTHER_EMAIL = "other@test.com";
    private static final String MY_PASSWORD = "1234";
    private static final String OTHER_PASSWORD = "5678";
    private static final int MY_AGE = 20;
    private static final int OTHER_AGE = 30;

    private Station 봉천역, 삼성역, 잠실역, 오남역;
    private String myToken, otherToken;

    /**
     * Feature: 즐겨찾기를 관리한다.
     *
     *   Background
     *     Given 지하철역 등록되어 있음
     *     And 회원 등록되어 있음
     *     And 로그인 되어있음
     *
     *   Scenario: 즐겨찾기를 관리
     *     When 즐겨찾기 생성을 요청
     *     Then 즐겨찾기 생성됨
     *     When 즐겨찾기 목록 조회 요청
     *     Then 즐겨찾기 목록 조회됨
     *     When 즐겨찾기 삭제 요청
     *     Then 즐겨찾기 삭제됨
     */

    @BeforeEach
    public void setUp() {
        super.setUp();

        봉천역 = 지하철역_생성_요청("봉천역").as(Station.class);
        삼성역 = 지하철역_생성_요청("삼성역").as(Station.class);
        잠실역 = 지하철역_생성_요청("잠실역").as(Station.class);
        오남역 = 지하철역_생성_요청("오남역").as(Station.class);

        myToken = 신규_회원가입_후_로그인_토큰_추출(MY_EMAIL, MY_PASSWORD, MY_AGE);
        otherToken = 신규_회원가입_후_로그인_토큰_추출(OTHER_EMAIL, OTHER_PASSWORD, OTHER_AGE);
    }

    @DisplayName("즐겨찾기 생성시 해당 계정에 즐겨찾기가 추가되어야 한다")
    @Test
    void createFavoriteTest() {
        // when
        ExtractableResponse<Response> result = 즐겨찾기_생성_요청(봉천역.getId(), 삼성역.getId(), myToken);

        // then
        즐겨찾기_생성_성공됨(result, myToken);
    }

    @DisplayName("즐겨찾기 목록 조회 시 조회한 계정에 속한 즐겨찾기만 조회되어야 한다")
    @Test
    void getAllFavoriteTest() {
        // given
        List<Long> myFavoriteIds = Arrays.asList(
                extractIdByHeaderLocation(즐겨찾기_생성_요청(봉천역.getId(), 삼성역.getId(), myToken)),
                extractIdByHeaderLocation(즐겨찾기_생성_요청(봉천역.getId(), 잠실역.getId(), myToken)),
                extractIdByHeaderLocation(즐겨찾기_생성_요청(봉천역.getId(), 오남역.getId(), myToken))
        );
        Long otherFavoritesId = extractIdByHeaderLocation(즐겨찾기_생성_요청(봉천역.getId(), 오남역.getId(), otherToken));

        // when
        ExtractableResponse<Response> result = 즐겨찾기_목록_조회_요청(myToken);

        // then
        즐겨찾기_목록_조회_성공됨(result, myFavoriteIds, otherFavoritesId, myToken);
    }

    @DisplayName("즐겨찾기 제거 시 해당 즐겨찾기가 제거되어야 한다")
    @Test
    void deleteFavoriteTest() {
        // given
        Long removeFavoriteId = extractIdByHeaderLocation(즐겨찾기_생성_요청(봉천역.getId(), 삼성역.getId(), myToken));
        List<Long> myFavoriteIds = Arrays.asList(
                extractIdByHeaderLocation(즐겨찾기_생성_요청(봉천역.getId(), 잠실역.getId(), myToken)),
                extractIdByHeaderLocation(즐겨찾기_생성_요청(봉천역.getId(), 오남역.getId(), myToken))
        );

        // when
        ExtractableResponse<Response> result = 즐겨찾기_삭제_요청(removeFavoriteId, myToken);

        // then
        즐겨찾기_삭제_성공됨(result, myFavoriteIds, removeFavoriteId, myToken);
    }

    @DisplayName("내가 생성한 즐겨찾기가 아닌 즐겨찾기를 제거 시 예외가 발생해야 한다")
    @Test
    void deleteFavoriteNotMineTest() {
        // given
        Long otherFavoriteId = extractIdByHeaderLocation(즐겨찾기_생성_요청(봉천역.getId(), 삼성역.getId(), otherToken));

        // when
        ExtractableResponse<Response> result = 즐겨찾기_삭제_요청(otherFavoriteId, myToken);

        // then
        즐겨찾기_삭제_실패됨(result);
    }

    public static ExtractableResponse<Response> 즐겨찾기_생성_요청(Long source, Long target, String token) {
        Map<String, Object> body = new HashMap<>();
        body.put("source", source);
        body.put("target", target);

        return RestAssuredRequest.postRequest(PATH, Collections.emptyMap(), body, token);
    }

    public static ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(String token) {
        return RestAssuredRequest.getRequest(PATH, Collections.emptyMap(), token);
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(Long id, String token) {
        return RestAssuredRequest.deleteRequest(PATH + "/" + id, Collections.emptyMap(), token);
    }

    public static void 즐겨찾기_생성_성공됨(ExtractableResponse<Response> response, String token) {
        Long createdId = extractIdByHeaderLocation(response);
        List<Long> favoriteIds = 즐겨찾기_목록_조회_요청(token).jsonPath().getList("id", Long.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_CREATED);
        assertThat(favoriteIds).containsAnyOf(createdId);
    }

    public static void 즐겨찾기_목록_조회_성공됨(
            ExtractableResponse<Response> response, List<Long> containIds, Long notContainId, String token
    ) {
        List<Long> favoriteIds = 즐겨찾기_목록_조회_요청(token).jsonPath().getList("id", Long.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_OK);
        assertThat(favoriteIds).containsAll(containIds);
        assertThat(favoriteIds).doesNotContain(notContainId);
    }

    public static void 즐겨찾기_삭제_성공됨(
            ExtractableResponse<Response> response, List<Long> containIds, Long notContainId, String token
    ) {
        List<Long> favoriteIds = 즐겨찾기_목록_조회_요청(token).jsonPath().getList("id", Long.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_NO_CONTENT);
        assertThat(favoriteIds).containsAll(containIds);
        assertThat(favoriteIds).doesNotContain(notContainId);
    }

    public static void 즐겨찾기_삭제_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_UNAUTHORIZED);
    }

    private static Long extractIdByHeaderLocation(ExtractableResponse<Response> response) {
        String location = response.header("Location");
        return Long.parseLong(location.substring(location.lastIndexOf("/") + 1));
    }
}

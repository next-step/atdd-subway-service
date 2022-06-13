package nextstep.subway.favorite.acceptance;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTestMethod.로그인_요청;
import static nextstep.subway.line.acceptance.LineAcceptanceTestMethod.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTestMethod.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.member.MemberAcceptanceTestMethod.로그인_성공_이후_토큰;
import static nextstep.subway.member.MemberAcceptanceTestMethod.회원_생성을_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    /**
     * Given 지하철역 등록되어 있음
     * And 지하철 노선 등록되어 있음
     * And 지하철 노선에 지하철역 등록되어 있음
     * And 회원 등록되어 있음
     * And 로그인 되어있음
     */

    private StationResponse 가락시장역;
    private StationResponse 경찰병원역;
    private StationResponse 오금역;

    private String 로그인_토큰;

    @BeforeEach
    void before() {
        //given: 지하철역 등록되어 있음
        가락시장역 = StationAcceptanceTest.지하철역_등록되어_있음("가락시장역").as(StationResponse.class);
        경찰병원역 = StationAcceptanceTest.지하철역_등록되어_있음("경찰병원역").as(StationResponse.class);
        오금역 = StationAcceptanceTest.지하철역_등록되어_있음("오금역").as(StationResponse.class);

        // 3호선 등록 지하철역 : 가락시장역 - 경찰병원역 - 오금역
        LineRequest lineRequest = new LineRequest("삼호선", "bg-orange-600", 가락시장역.getId(), 경찰병원역.getId(), 10);
        LineResponse 삼호선 = 지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);
        지하철_노선에_지하철역_등록_요청(삼호선, 경찰병원역, 오금역, 3);

        String EMAIL = "email@email.com";
        String PASSWORD = "password";
        int AGE = 30;
        // 회원 등록
        회원_생성을_요청(EMAIL, PASSWORD, AGE);

        // 로그인됨
        ExtractableResponse<Response> 로그인_요청_응답 = 로그인_요청(EMAIL, PASSWORD);
        로그인_토큰 = 로그인_성공_이후_토큰(로그인_요청_응답);
    }

    /**
     * Scenario: 즐겨찾기를 관리
     *     When 즐겨찾기 생성을 요청
     *     Then 즐겨찾기 생성됨
     *     When 즐겨찾기 목록 조회 요청
     *     Then 즐겨찾기 목록 조회됨
     *     When 즐겨찾기 삭제 요청
     *     Then 즐겨찾기 삭제됨
     */

    @Test
    @DisplayName("즐겨찾기 생성")
    void favoriteTest01() {
        // when: 즐겨찾기 생성을 요청
        ExtractableResponse<Response> 즐겨찾기_생성_요청_응답 = 즐겨찾기_생성_요청(로그인_토큰, 가락시장역.getId(), 오금역.getId());

        // then 즐겨찾기 생성됨
        즐겨찾기_생성됨(즐겨찾기_생성_요청_응답);
    }

    @Test
    @DisplayName("즐겨찾기 목록 조회")
    void favoriteTest02() {
        // when: 즐겨찾기 목록 조회을 요청
        ExtractableResponse<Response> 즐겨찾기_목록_조회_요청_응답 = 즐겨찾기_목록_조회_요청(로그인_토큰);

        // then 즐겨찾기 생성됨
        즐겨찾기_목록_조회됨(즐겨찾기_목록_조회_요청_응답);
    }

    @Test
    @DisplayName("즐겨찾기 삭제")
    void favoriteTest03() {
        // when: 즐겨찾기 생성을 요청
        ExtractableResponse<Response> 즐겨찾기_생성_요청_응답 = 즐겨찾기_생성_요청(로그인_토큰, 가락시장역.getId(), 오금역.getId());

        // then 즐겨찾기 생성됨
        즐겨찾기_생성됨(즐겨찾기_생성_요청_응답);

        // when: 즐겨찾기 삭제를 요청
        ExtractableResponse<Response> 즐겨찾기_삭제_요청_응답 = 즐겨찾기_삭제_요청(로그인_토큰, 즐겨찾기_생성_요청_응답);

        // then 즐겨찾기 삭제됨
        즐겨찾기_목록_삭제됨(즐겨찾기_삭제_요청_응답);
    }

    private void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED);
    }

    public static ExtractableResponse<Response> 즐겨찾기_생성_요청(String token, Long sourceStationId, Long targetStationId) {
        FavoriteRequest favoriteRequest = new FavoriteRequest(sourceStationId, targetStationId);
        return RestAssured.given().log().all().
                auth().oauth2(token).
                body(favoriteRequest).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                when().post("/favorites").
                then().log().all().extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(String token) {
        return RestAssured.given().log().all().
                auth().oauth2(token).
                when().get("/favorites").
                then().log().all().extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(String token, ExtractableResponse<Response> response) {
        String uri = response.header("Location");
        return RestAssured.given().log().all().
                auth().oauth2(token).
                when().delete(uri).
                then().log().all().extract();
    }

    private void 즐겨찾기_목록_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    private void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> response) {
        List<FavoriteResponse> favoriteResponses = response.jsonPath().getList(".", FavoriteResponse.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(favoriteResponses).hasSize(1)
        );
    }

}

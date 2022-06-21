package nextstep.subway.favorite;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    private StationResponse 강남역;
    private StationResponse 광교역;
    private LineResponse 신분당선;
    private String 로그인_토큰;

    @BeforeEach
    void beforeEach() {
        setUp();

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        광교역 = StationAcceptanceTest.지하철역_등록되어_있음("광교역").as(StationResponse.class);
        신분당선 = LineAcceptanceTest.지하철_노선_생성_요청(new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 10)).as(LineResponse.class);

        AuthAcceptanceTest.회원가입_요청(new MemberRequest(MemberAcceptanceTest.EMAIL, MemberAcceptanceTest.PASSWORD, MemberAcceptanceTest.AGE));
        로그인_토큰 = AuthAcceptanceTest.로그인_토큰(AuthAcceptanceTest.로그인_요청(new TokenRequest(MemberAcceptanceTest.EMAIL, MemberAcceptanceTest.PASSWORD)));
    }

    @DisplayName("즐겨찾기 생성")
    @Test
    void create() {
        // when
        ExtractableResponse<Response> 즐겨찾기_결과 = 즐겨찾기_요청(로그인_토큰, new FavoriteRequest(강남역.getId(), 광교역.getId()));

        // then
        즐겨찾기_성공(즐겨찾기_결과);
    }

    @DisplayName("즐겨찾기 목록 조회")
    @Test
    void inquiry() {
        // given
        즐겨찾기_요청(로그인_토큰, new FavoriteRequest(강남역.getId(), 광교역.getId()));

        // when
        ExtractableResponse<Response> 즐겨찾기 = 즐겨찾기_조회(로그인_토큰);

        // then
        즐겨찾기_조회_성공(즐겨찾기);
    }

    @DisplayName("즐겨찾기 삭제")
    @Test
    void delete() {
        // given
        즐겨찾기_요청(로그인_토큰, new FavoriteRequest(강남역.getId(), 광교역.getId()));
        ExtractableResponse<Response> 즐겨찾기 = 즐겨찾기_조회(로그인_토큰);

        // when
        ExtractableResponse<Response> 즐겨찾기_삭제 = 즐겨찾기_삭제(로그인_토큰, 즐겨찾기);

        // then
        즐겨찾기_삭제_성공(즐겨찾기_삭제);
    }

    @DisplayName("로그인 하지 않은 경우 즐겨찾기 생성 불가")
    @Test
    void create_throwsException_ifMemberNotLoggedIn() {
        // when
        ExtractableResponse<Response> 즐겨찾기_결과 = 즐겨찾기_요청(로그인_토큰 + "disable", new FavoriteRequest(강남역.getId(), 광교역.getId()));

        // then
        즐겨찾기_실패(즐겨찾기_결과);
    }

    @DisplayName("같은 출발역과 도착역으로 중복 생성 불가")
    @Test
    void create_throwsException_ifFavoriteExist() {
        // when
        ExtractableResponse<Response> 즐겨찾기_결과 = 즐겨찾기_요청(로그인_토큰 + "disable", new FavoriteRequest(강남역.getId(), 강남역.getId()));

        // then
        즐겨찾기_실패(즐겨찾기_결과);
    }

    @DisplayName("존재하지 않는 역으로 중복 생성 불가")
    @Test
    void create_throwsException_ifStationNotExist() {
        // when
        ExtractableResponse<Response> 즐겨찾기_결과 = 즐겨찾기_요청(로그인_토큰 + "disable", new FavoriteRequest(2L, 3L));

        // then
        즐겨찾기_실패(즐겨찾기_결과);
    }

    public static ExtractableResponse<Response> 즐겨찾기_요청(String accessToken, FavoriteRequest request) {
        return RestAssured.given().header("Authorization", "Bearer " + accessToken).log().all()
                          .contentType(MediaType.APPLICATION_JSON_VALUE)
                          .body(request)
                          .when().post("/favorites")
                          .then().log().all()
                          .extract();
    }

    public static void 즐겨찾기_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 즐겨찾기_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제(String accessToken, ExtractableResponse<Response> response) {
        List<Long> ids = response.jsonPath().get("id");

        return RestAssured.given().header("Authorization", "Bearer " + accessToken).log().all()
                          .contentType(MediaType.APPLICATION_JSON_VALUE)
                          .when().delete("/favorites/{id}", ids.get(0))
                          .then().log().all()
                          .extract();
    }

    public static void 즐겨찾기_삭제_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static ExtractableResponse<Response> 즐겨찾기_조회(String accessToken) {
        return RestAssured.given().header("Authorization", "Bearer " + accessToken).log().all()
                          .accept(MediaType.APPLICATION_JSON_VALUE)
                          .when().get("/favorites")
                          .then().log().all()
                          .extract();
    }

    public static void 즐겨찾기_조회_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<FavoriteResponse> favorites = new ArrayList<>(response.jsonPath().getList(".", FavoriteResponse.class));
        assertThat(favorites).hasSize(1);

        List<String> names = new LinkedList<>();
        names.addAll(response.jsonPath().get("source.name"));
        names.addAll(response.jsonPath().get("target.name"));
        assertThat(names).containsExactlyInAnyOrder("강남역", "광교역");
    }
}
package nextstep.subway.favorite.acceptance;

import static nextstep.subway.member.MemberAcceptanceTest.AGE;
import static nextstep.subway.member.MemberAcceptanceTest.EMAIL;
import static nextstep.subway.member.MemberAcceptanceTest.PASSWORD;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
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

    private String token;
    private LineResponse 신분당선;
    private StationResponse 강남역;
    private StationResponse 양재역;
    private StationResponse 광교역;

    @BeforeEach
    public void setUp() {
        super.setUp();
        // 지하철역 등록되어 있음
        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);
        광교역 = StationAcceptanceTest.지하철역_등록되어_있음("광교역").as(StationResponse.class);

        // 지하철 노선 등록되어 있음
        LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", 강남역.getId(), 광교역.getId(), 10, 0);
        신분당선 = LineAcceptanceTest.지하철_노선_등록되어_있음(lineRequest).as(LineResponse.class);

        // 지하철 노선에 지하철역 등록되어 있음
        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 양재역, 3);

        // 회원 등록되어 있음
        회원_생성을_요청(EMAIL, PASSWORD, AGE);

        // 로그인 되어있음
        token = AuthAcceptanceTest.회원_토큰_요청(EMAIL, PASSWORD).jsonPath().getString("accessToken");
    }

    @DisplayName("즐겨찾기를 관리한다.")
    @Test
    void manage_favorite() {
        // when
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(강남역.getId(), 양재역.getId());
        // then
        즐겨찾기_생성됨(createResponse);

        // when
        ExtractableResponse<Response> favorites = 즐겨찾기_목록_조회_요청();
        // then
        즐겨찾기_목록_조회됨(favorites, 강남역, 양재역);

        // when
        Long id = favorites.jsonPath().getList(".", FavoriteResponse.class).get(0).getId();
        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(id);
        // then
        즐겨찾기_삭제됨(deleteResponse);

    }

    private ExtractableResponse<Response> 즐겨찾기_생성_요청(Long source, Long target) {
        FavoriteRequest request = new FavoriteRequest(source, target);

        return RestAssured
            .given().log().all()
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(request)
            .when().post("/favorites")
            .then().log().all()
            .extract();
    }

    private void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private ExtractableResponse<Response> 즐겨찾기_목록_조회_요청() {
        return RestAssured
            .given().log().all()
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/favorites")
            .then().log().all()
            .extract();
    }

    private void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> response, StationResponse source, StationResponse target) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        FavoriteResponse favorite = response.jsonPath().getList(".", FavoriteResponse.class).get(0);
        checkStationResponseEquals(favorite.getSource(), source);
        checkStationResponseEquals(favorite.getTarget(), target);
    }

    private void checkStationResponseEquals(StationResponse response, StationResponse expected) {
        assertThat(response.getId()).isEqualTo(expected.getId());
        assertThat(response.getName()).isEqualTo(expected.getName());
        assertThat(response.getCreatedDate()).isEqualTo(expected.getCreatedDate());
        assertThat(response.getModifiedDate()).isEqualTo(expected.getModifiedDate());
    }

    private ExtractableResponse<Response> 즐겨찾기_삭제_요청(Long id) {
        return RestAssured
            .given().log().all()
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().delete("/favorites/" + id)
            .then().log().all()
            .extract();
    }

    private void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
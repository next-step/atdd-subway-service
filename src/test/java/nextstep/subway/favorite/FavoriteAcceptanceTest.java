package nextstep.subway.favorite;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_토큰_얻기;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.생성된_지하철노선;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_생성_요청;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성됨;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static nextstep.subway.station.StationAcceptanceTest.생성된_지하철역;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private StationResponse 강남역;
    private StationResponse 서초역;
    private StationResponse 역삼역;
    private String token;

    @BeforeEach
    void setup() {
        super.setUp();
        강남역 = 생성된_지하철역(지하철역_생성_요청("강남역"));
        서초역 = 생성된_지하철역(지하철역_생성_요청("서초역"));
        역삼역 = 생성된_지하철역(지하철역_생성_요청("역삼역"));
        LineResponse 신분당선 = 생성된_지하철노선(지하철_노선_생성_요청(new LineRequest("신분당선", "bg-red-600",
            강남역.getId(), 역삼역.getId(), 10, 900)));
        지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 서초역, 5);

        String email = "cyr9210@gmail.com";
        String password = "password";
        회원_생성됨(회원_생성을_요청(email, password, 32));
        token = 로그인_토큰_얻기(email, password);
    }

    @DisplayName("즐겨찾기를 생성한다.")
    @Test
    void createFavorite() {
        // when
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(token, 강남역.getId(), 서초역.getId());
        //then
        즐겨찾기_생성됨(createResponse);
    }

    @DisplayName("즐겨찾기를 생성 시, 등록되지 않은 역이면 notFound.")
    @Test
    void createFavorite_fail_stationNotFound() {
        //given
        Long notExistStationId = 123L;
        // when
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(token, 강남역.getId(), notExistStationId);
        //then
        즐겨찾기_기능_실패_nofFound(createResponse);
    }

    @DisplayName("즐겨찾기 목록을 조회한다.")
    @Test
    void getFavorite() {
        //given
        즐겨찾기_생성_요청(token, 강남역.getId(), 서초역.getId());
        //when
        ExtractableResponse<Response> getResponse = 즐겨찾기_목록조회_요청(token);
        //then
        즐겨찾기_목록조회_확인(getResponse, 1);
    }

    @DisplayName("즐겨찾기를 삭제한다.")
    @Test
    void deleteFavorite() {
        //given
        즐겨찾기_생성_요청(token, 강남역.getId(), 서초역.getId());
        List<FavoriteResponse> 즐겨찾기_목록 = 즐겨찾기_목록조회_확인(즐겨찾기_목록조회_요청(token), 1);
        //when
        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(token, 즐겨찾기_목록.get(0).getId());
        //then
        즐겨찾기_삭제됨(deleteResponse);
    }

    @DisplayName("즐겨찾기 삭제 시, 없는 즐겨찾기라면 NotFound")
    @Test
    void deleteFavorite_fail_favoriteNotFound() {
        //when
        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(token, 1L);
        //then
        즐겨찾기_기능_실패_nofFound(deleteResponse);
    }

    private ExtractableResponse<Response> 즐겨찾기_생성_요청(String token, Long source, Long target) {
        return RestAssured.given().log().all()
                .auth().oauth2(token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new FavoriteRequest(source, target))
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }

    private void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        assertAll(
                () -> assertEquals(HttpStatus.CREATED.value(), response.statusCode()),
                () -> assertThat(response.header("Location")).isNotBlank()
        );
    }

    private ExtractableResponse<Response> 즐겨찾기_목록조회_요청(String token) {
        return RestAssured.given().log().all()
                .auth().oauth2(token)
                .when().get("/favorites")
                .then().log().all()
                .extract();
    }

    private List<FavoriteResponse> 즐겨찾기_목록조회_확인(ExtractableResponse<Response> response, int expectedSize) {
        List<FavoriteResponse> list = response.jsonPath().getList(".", FavoriteResponse.class);
        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.statusCode()),
                () -> assertEquals(expectedSize, list.size())
        );
        return list;
    }

    private ExtractableResponse<Response> 즐겨찾기_삭제_요청(String token, Long favoriteId) {
        return RestAssured.given().log().all()
                .auth().oauth2(token)
                .when().delete("/favorites/{id}", favoriteId)
                .then().log().all()
                .extract();
    }

    private void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
        assertEquals(HttpStatus.NO_CONTENT.value(), response.statusCode());
    }

    private void 즐겨찾기_기능_실패_nofFound(ExtractableResponse<Response> response) {
        assertEquals(HttpStatus.NOT_FOUND.value(), response.statusCode());
    }
}
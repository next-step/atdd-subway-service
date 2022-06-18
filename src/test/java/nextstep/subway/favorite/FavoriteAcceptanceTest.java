package nextstep.subway.favorite;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptanceTest;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.station.StationAcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    private static final String FAVORITE_URI = "/favorites";

    private Long sourceId;
    private Long targetId;
    private String accessToken;

    @BeforeEach
    void setup() {
        sourceId = StationAcceptanceTest.지하철역_생성_요청("강남역").jsonPath().getLong("id");
        targetId = StationAcceptanceTest.지하철역_생성_요청("력삼역").jsonPath().getLong("id");

        MemberAcceptanceTest.회원_생성을_요청("minho@mino.com", "민호", 19);
        accessToken = AuthAcceptanceTest.토큰_가져오기("minho@mino.com", "민호");
    }

    /**
     * Given 지하철역, 회원등록, 로그인이 되어 있음
     *
     * When 즐겨찾기를 생성 요청하면
     * Then 즐겨찾기가 생성된다
     *
     * When 즐겨찾기 조회를 요청하면
     * Then 즐겨찾기 정보가 조회된다
     *
     * When 즐겨찾기를 삭제 요청하면
     * Then 즐겨찾기 정보가 삭제된다
     *
     * When 즐겨찾기 조회를 요청하면
     * Then 즐겨찾기 정보는 0개이다
     */
    @DisplayName("즐겨찾기를 관리한다")
    @Test
    void manageFavorite() {
        //when
        ExtractableResponse<Response> response = 즐겨찾기_추가_요청(sourceId, targetId);
        //then
        즐겨찾기_생성됨(response);

        //when
        ExtractableResponse<Response> findResponse = 즐겨찾기_조회_요청();
        //then
        즐겨찾기_정보_조회됨(findResponse);

        //when
        List<FavoriteResponse> favoriteResponse = getResponse(findResponse);
        ExtractableResponse<Response> deleteResponse = 즐겨찾기_정보_삭제_요청(favoriteResponse.get(0).getId());
        //then
        즐겨찾기_삭제됨(deleteResponse);

        //when
        ExtractableResponse<Response> findResponse2 = 즐겨찾기_조회_요청();
        //then
        즐겨찾기_목록_사이즈_검증(findResponse2, 0);
    }

    private ExtractableResponse<Response> 즐겨찾기_추가_요청(Long source, Long target) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new FavoriteRequest(source, target))
                .when().post(FAVORITE_URI).then().extract();
    }

    private void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private ExtractableResponse<Response> 즐겨찾기_조회_요청() {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get(FAVORITE_URI)
                .then().log().all()
                .extract();
    }

    private void 즐겨찾기_정보_조회됨(ExtractableResponse<Response> findResponse) {
        List<FavoriteResponse> favoriteResponse = getResponse(findResponse);
        assertAll(
                () -> assertThat(findResponse.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(favoriteResponse.get(0).getId()).isNotNull(),
                () -> assertThat(favoriteResponse.get(0).getSource().getId()).isEqualTo(sourceId),
                () -> assertThat(favoriteResponse.get(0).getTarget().getId()).isEqualTo(targetId));
    }

    private ExtractableResponse<Response> 즐겨찾기_정보_삭제_요청(Long favoriteId) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().delete(FAVORITE_URI + "/" + favoriteId)
                .then().log().all()
                .extract();
    }

    private void 즐겨찾기_삭제됨(ExtractableResponse<Response> deleteResponse) {
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private void 즐겨찾기_목록_사이즈_검증(ExtractableResponse<Response> findResponse2, int size) {
        List<FavoriteResponse> favoriteResponse2 = getResponse(findResponse2);
        assertThat(favoriteResponse2).hasSize(size);
    }

    private List<FavoriteResponse> getResponse(ExtractableResponse<Response> findResponse) {
        return findResponse.jsonPath().getList(".", FavoriteResponse.class);
    }
}

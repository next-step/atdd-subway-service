package nextstep.subway.favorite.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.acceptance.LineAcceptanceTest;
import nextstep.subway.line.acceptance.LineSectionAcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private LineResponse 팔호선;
    private StationResponse 천호역;
    private StationResponse 잠실역;
    private StationResponse 문정역;
    private String accessTokenMember1;
    private String accessTokenMember2;

    @BeforeAll
    public void setUp() {
        super.setUp();

        천호역 = StationAcceptanceTest.지하철역_등록되어_있음("천호역").as(StationResponse.class);
        잠실역 = StationAcceptanceTest.지하철역_등록되어_있음("잠실역").as(StationResponse.class);
        문정역 = StationAcceptanceTest.지하철역_등록되어_있음("문정역").as(StationResponse.class);

        팔호선 = LineAcceptanceTest.지하철_노선_등록되어_있음(new LineRequest("팔호선", "bg-pink-600", 천호역.getId(), 문정역.getId(),10, 0))
                .as(LineResponse.class);

        LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청(팔호선, 천호역, 잠실역, 5);

        MemberAcceptanceTest.회원_생성을_요청("member1@email.com", "1a2b3c", 20);
        accessTokenMember1 = MemberAcceptanceTest.회원_로그인_요청("member1@email.com", "1a2b3c");

        MemberAcceptanceTest.회원_생성을_요청("member2@email.com", "4d5e6f", 30);
        accessTokenMember2 = MemberAcceptanceTest.회원_로그인_요청("member2@email.com", "4d5e6f");
    }

    @DisplayName("즐겨찾기 관리")
    @Test
    void manageFavorite() {
        // 즐겨찾기 생성
        ExtractableResponse<Response> 즐겨찾기_천호역_잠실역_생성_결과 = 즐겨찾기_생성_요청(accessTokenMember1, 천호역, 잠실역);
        즐겨찾기_생성됨(즐겨찾기_천호역_잠실역_생성_결과);

        ExtractableResponse<Response> 즐겨찾기_잠실역_문정역_생성_결과 = 즐겨찾기_생성_요청(accessTokenMember1, 잠실역, 문정역);
        즐겨찾기_생성됨(즐겨찾기_잠실역_문정역_생성_결과);

        // 즐겨찾기 조회
        ExtractableResponse<Response> 즐겨찾기_목록_조회_요청_결과 = 즐겨찾기_목록_조회_요청(accessTokenMember1);
        즐겨찾기_목록_확인(즐겨찾기_목록_조회_요청_결과, Arrays.asList("천호역", "잠실역"));

        // 즐겨찾기 삭제
        ExtractableResponse<Response> 즐겨찾기_삭제_요청_결과 = 즐겨찾기_삭제_요청(accessTokenMember1, 즐겨찾기_천호역_잠실역_생성_결과);
        즐겨찾기_삭제됨(즐겨찾기_삭제_요청_결과);
        즐겨찾기_목록_확인(즐겨찾기_목록_조회_요청(accessTokenMember1), Arrays.asList("잠실역"));
    }

    @DisplayName("다른 회원의 즐겨찾기는 삭제할수 없음")
    @Test
    void notDeleteOtherMemberFavorite() {
        // 즐겨찾기 생성
        ExtractableResponse<Response> 즐겨찾기_잠실역_문정역_생성_결과 = 즐겨찾기_생성_요청(accessTokenMember2, 잠실역, 문정역);
        즐겨찾기_생성됨(즐겨찾기_잠실역_문정역_생성_결과);

        // 즐겨찾기 삭제
        ExtractableResponse<Response> 즐겨찾기_삭제_결과 = 즐겨찾기_삭제_요청(accessTokenMember1, 즐겨찾기_잠실역_문정역_생성_결과);
        즐겨찾기_삭제_실패(즐겨찾기_삭제_결과);
    }

    private ExtractableResponse<Response> 즐겨찾기_생성_요청(String accessToken, StationResponse 천호역, StationResponse 잠실역) {
        FavoriteRequest favoriteRequest = FavoriteRequest.builder()
                .source(천호역.getId())
                .target(잠실역.getId())
                .build();

        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(favoriteRequest)
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }

    private void 즐겨찾기_생성됨(ExtractableResponse<Response> 즐겨찾기_생성_요청_결과) {
        assertThat(즐겨찾기_생성_요청_결과.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(String accessToken) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(accessToken)
                .when().get("/favorites")
                .then().log().all()
                .extract();
    }

    private void 즐겨찾기_목록_확인(ExtractableResponse<Response> 즐겨찾기_목록_조회_요청_결과, List<String> favoriteSources) {
        List<FavoriteResponse> favoriteResponses = 즐겨찾기_목록_조회_요청_결과.body().jsonPath().getList("", FavoriteResponse.class);
        assertThat(favoriteResponses)
                .map(favoriteResponse -> favoriteResponse.getSource().getName())
                .asList()
                .containsExactly(favoriteSources.toArray());
    }

    private ExtractableResponse<Response> 즐겨찾기_삭제_요청(String accessToken, ExtractableResponse<Response> 즐겨찾기_생성_요청_결과) {
        String uri = 즐겨찾기_생성_요청_결과.header("Location");

        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().delete(uri)
                .then().log().all()
                .extract();
    }

    private void 즐겨찾기_삭제됨(ExtractableResponse<Response> 즐겨찾기_삭제_요청_결과) {
        assertThat(즐겨찾기_삭제_요청_결과.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private void 즐겨찾기_삭제_실패(ExtractableResponse<Response> 즐겨찾기_삭제_결과) {
        assertThat(즐겨찾기_삭제_결과.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
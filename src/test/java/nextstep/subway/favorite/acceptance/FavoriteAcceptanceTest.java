package nextstep.subway.favorite.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_요청;
import static nextstep.subway.favorite.acceptance.FavoriteRestAssured.*;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptance.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.member.MemberRestAssured.회원_생성을_요청;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "programmer-sjk@email.com";
    private static final String PASSWORD = "password";
    private static final int AGE = 33;

    private LineResponse 삼호선;
    private StationResponse 양재역;
    private StationResponse 교대역;
    private StationResponse 연신내역;
    private String token;

    @BeforeEach
    void setup() {
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        연신내역 = 지하철역_등록되어_있음("연신내역").as(StationResponse.class);

        삼호선 = 지하철_노선_등록되어_있음("삼호선", "bg-red-400", 양재역.getId(), 교대역.getId(), 5, 0);
        지하철_노선에_지하철역_등록_요청(삼호선, 교대역, 연신내역, 15);

        회원_생성을_요청(EMAIL, PASSWORD, AGE);
        token = 로그인_요청(EMAIL, PASSWORD).as(TokenResponse.class)
                .getAccessToken();
    }

    @DisplayName("즐겨찾기를 생성할 수 있다")
    @Test
    void createFavorite() {
        // given
        FavoriteRequest request = new FavoriteRequest(양재역.getId(), 연신내역.getId());

        // when
        ExtractableResponse<Response> response = 즐겨찾기_생성_요청(token, request);

        // then
        즐겨찾기_등록됨(response);
    }

    @DisplayName("즐겨찾기 목록을 조회할 수 있다")
    @Test
    void findFavorites() {
        // given
        즐겨찾기_생성_요청(token, new FavoriteRequest(교대역.getId(), 연신내역.getId()));
        즐겨찾기_생성_요청(token, new FavoriteRequest(양재역.getId(), 연신내역.getId()));

        // when
        ExtractableResponse<Response> response = 즐겨찾기_목록_조회_요청(token);

        // then
        즐겨찾기_목록_조회됨(response);
    }

    @DisplayName("즐겨찾기를 삭제할 수 있다")
    @Test
    void deleteFavorite() {
        // given
        즐겨찾기_생성_요청(token, new FavoriteRequest(교대역.getId(), 연신내역.getId()));
        즐겨찾기_생성_요청(token, new FavoriteRequest(양재역.getId(), 연신내역.getId()));

        // when
        ExtractableResponse<Response> response = 즐겨찾기_삭제_요청(token, 1L);

        // then
        즐겨찾기_삭제됨(response);
    }

    @DisplayName("타인의 즐겨찾기를 삭제할 수 없다")
    @Test
    void deleteOtherFavoriteException() {
        // given
        즐겨찾기_생성_요청(token, new FavoriteRequest(교대역.getId(), 연신내역.getId()));
        String otherMemberEmail = "other@email.com";
        회원_생성을_요청(otherMemberEmail, PASSWORD, AGE);
        String otherToken = 로그인_요청(otherMemberEmail, PASSWORD).as(TokenResponse.class)
                .getAccessToken();

        // when
        ExtractableResponse<Response> response = 즐겨찾기_삭제_요청(otherToken, 1L);

        // then
        즐겨찾기_삭제_실패(response);
    }

    private void 즐겨찾기_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private void 즐겨찾기_삭제_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}

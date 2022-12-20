package nextstep.subway.favorite;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import nextstep.subway.favorite.dto.FavoriteCreateRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends FavoriteAcceptanceTestFixture {

    /**
     * Feature: 즐겨찾기를 관리한다.
     *
     *   Background
     *     Given 지하철역 등록되어 있음
     *     And 지하철 노선 등록되어 있음
     *     And 지하철 노선에 지하철역 등록되어 있음
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
    @DisplayName("즐겨찾기를 관리한다")
    @Test
    void manageFavorite() {
        // When 즐겨찾기 생성을 요청
        FavoriteCreateRequest favoriteCreateRequest = new FavoriteCreateRequest(강남역.getId(), 정자역.getId());
        ExtractableResponse<Response> response = 즐겨찾기_생성_요청(myAccessToken, favoriteCreateRequest);
        // Then 즐겨찾기 생성됨
        즐겨찾기_생성됨(response);

        // When 즐겨찾기 목록 조회 요청
        즐겨찾기_등록되어_있음(myAccessToken, new FavoriteCreateRequest(광교역.getId(), 양재역.getId()));
        response = 즐겨찾기_정보_조회_요청(myAccessToken);
        // Then 즐겨찾기 목록 조회됨
        즐겨찾기_정보_조회됨(response);
        List<FavoriteResponse> 조회된_즐겨찾기_목록 = 즐겨찾기_목록(response);
        assertThat(조회된_즐겨찾기_목록).hasSize(2);

        // When 즐겨찾기 삭제 요청
        response = 즐겨찾기_삭제_요청(myAccessToken, 조회된_즐겨찾기_목록.get(0).getId());
        // Then 즐겨찾기 삭제됨
        즐겨찾기_삭제됨(response);
    }

    @DisplayName("즐겨찾기를 생성한다")
    @Test
    void createFavorite() {
        // When 즐겨찾기 생성을 요청
        FavoriteCreateRequest favoriteCreateRequest = new FavoriteCreateRequest(강남역.getId(), 정자역.getId());
        ExtractableResponse<Response> response = 즐겨찾기_생성_요청(myAccessToken, favoriteCreateRequest);
        // Then 즐겨찾기 생성됨
        즐겨찾기_생성됨(response);
    }

    @DisplayName("즐겨찾기를 조회한다")
    @Test
    void findFavorites() {
        // Given
        즐겨찾기_등록되어_있음(myAccessToken, new FavoriteCreateRequest(강남역.getId(), 정자역.getId()));
        즐겨찾기_등록되어_있음(myAccessToken, new FavoriteCreateRequest(광교역.getId(), 양재역.getId()));

        // When 즐겨찾기 목록 조회 요청
        ExtractableResponse<Response> response = 즐겨찾기_정보_조회_요청(myAccessToken);
        // Then 즐겨찾기 목록 조회됨
        즐겨찾기_정보_조회됨(response);
        List<FavoriteResponse> 조회된_즐겨찾기_목록 = 즐겨찾기_목록(response);
        assertThat(조회된_즐겨찾기_목록).hasSize(2);
    }

    @DisplayName("즐겨찾기를 삭제한다")
    @Test
    void deleteFavorite() {
        // Given
        즐겨찾기_등록되어_있음(myAccessToken, new FavoriteCreateRequest(강남역.getId(), 정자역.getId()));
        List<FavoriteResponse> 조회된_즐겨찾기_목록 = 즐겨찾기_목록(즐겨찾기_정보_조회_요청(myAccessToken));

        // When 즐겨찾기 삭제 요청
        ExtractableResponse<Response> response = 즐겨찾기_삭제_요청(myAccessToken, 조회된_즐겨찾기_목록.get(0).getId());
        // Then 즐겨찾기 삭제됨
        즐겨찾기_삭제됨(response);
    }
}
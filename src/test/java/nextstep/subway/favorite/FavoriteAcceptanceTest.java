package nextstep.subway.favorite;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import nextstep.subway.utils.RestAssuredCRUD;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_요청;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.member.MemberAcceptanceTest.*;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private TokenResponse 사용자;
    private LineResponse 이호선;
    private StationResponse 강남역;
    private StationResponse 교대역;
    private StationResponse 대교역;

    /**
     * 교대역    --- *2호선* ---     강남역 --- *2호선* ---   대교역
     *               30                        11
     */

    @BeforeEach
    public void setUp() {
        super.setUp();

        // Given 지하철역 등록되어 있음
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        대교역 = 지하철역_등록되어_있음("대교역").as(StationResponse.class);

        // And 지하철 노선 등록되어 있음
        이호선 = 지하철_노선_등록되어_있음(
                new LineRequest("이호선", "bg-red-600", 교대역.getId(), 강남역.getId(), 30))
                .as(LineResponse.class);

        // And 지하철 노선에 지하철역 등록되어 있음
        지하철_노선에_지하철역_등록_요청(이호선, 강남역, 대교역, 11);

        // And 회원 등록되어 있음
        회원_등록되어_있음(EMAIL, PASSWORD, AGE);

        // And 로그인 되어있음
        사용자 = 로그인_요청(EMAIL, PASSWORD).as(TokenResponse.class);
    }

    @DisplayName("즐겨찾기를 관리한다.")
    @Test
    void manageMember() {
        // When
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성을_요청(사용자, 강남역, 교대역);
        // Then
        즐겨찾기_생성됨(createResponse);

        // When
        ExtractableResponse<Response> selectResponse = 즐겨찾기_목록_조회_요청(사용자);
        // Then
        즐겨찾기_목록_조회됨(selectResponse);

        // When
        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제_요청(사용자, createResponse.as(FavoriteResponse.class));
        // Then
        즐겨찾기_삭제됨(deleteResponse);

        // When 즐겨찾기 3개 추가
        createResponse = 즐겨찾기_생성을_요청(사용자, 강남역, 대교역);
        ExtractableResponse<Response> createResponse2 = 즐겨찾기_생성을_요청(사용자, 교대역, 강남역);
        ExtractableResponse<Response> createResponse3 = 즐겨찾기_생성을_요청(사용자, 대교역, 강남역);
        // Then 즐겨찾기 목록을 조회 3개 확인
        selectResponse = 즐겨찾기_목록_조회_요청(사용자);
        List<Favorite> favorites = selectResponse.body().jsonPath().getList(".", Favorite.class);
        assertThat(favorites).hasSize(3);

        // When 즐겨찾기 2개 삭제
        즐겨찾기_삭제_요청(사용자, createResponse3.as(FavoriteResponse.class));
        즐겨찾기_삭제_요청(사용자, createResponse2.as(FavoriteResponse.class));
        // Then 즐겨찾기 목록 조회 1개 확인
        selectResponse = 즐겨찾기_목록_조회_요청(사용자);
        favorites = selectResponse.body().jsonPath().getList(".", Favorite.class);
        assertThat(favorites).hasSize(1);

    }

    private ExtractableResponse<Response> 즐겨찾기_생성을_요청(TokenResponse tokenResponse, StationResponse sourceStation, StationResponse targetStation) {
        return RestAssuredCRUD.postRequestWithOAuth("/favorites",
                new FavoriteRequest(sourceStation.getId(), targetStation.getId()), tokenResponse.getAccessToken());
    }

    private void 즐겨찾기_생성됨(ExtractableResponse<Response> createResponse) {
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(TokenResponse tokenResponse) {
        return RestAssuredCRUD.getWithOAuth("/favorites", tokenResponse.getAccessToken());
    }

    private void 즐겨찾기_목록_조회됨(ExtractableResponse<Response> selectResponse) {
        assertThat(selectResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private ExtractableResponse<Response> 즐겨찾기_삭제_요청(TokenResponse tokenResponse, FavoriteResponse favoriteResponse) {
        return RestAssuredCRUD.deleteWithOAuth("/favorites/" + favoriteResponse.getId(), tokenResponse.getAccessToken());
    }

    private void 즐겨찾기_삭제됨(ExtractableResponse<Response> deleteResponse) {
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
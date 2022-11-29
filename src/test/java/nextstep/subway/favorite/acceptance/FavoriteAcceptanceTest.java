package nextstep.subway.favorite.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.acceptance.AuthAcceptance;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.acceptance.LineAcceptance;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.acceptance.MemberAcceptance;
import nextstep.subway.station.acceptance.StationAcceptance;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private LineResponse 이호선;
    private StationResponse 강남역;
    private StationResponse 잠실역;

    private TokenResponse tokenResponse;
    private TokenResponse tokenResponseOfWrongMember;

    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationAcceptance.create_station("강남역").as(StationResponse.class);
        잠실역 = StationAcceptance.create_station("잠실역").as(StationResponse.class);
        이호선 = LineAcceptance.create_line("2호선", "bg-green-600", 강남역.getId(),
                잠실역.getId(), 30).as(LineResponse.class);

        MemberAcceptance.create_member("testuser@test.com", "password157#", 20);
        MemberAcceptance.create_member("testuser-12@test.com", "password157#", 20);
        tokenResponse = AuthAcceptance.member_token_is_issued("testuser@test.com", "password157#");
        tokenResponseOfWrongMember = AuthAcceptance.member_token_is_issued("testuser-12@test.com", "password157#");
    }

    /**
     * Given 지하철역이 등록되어 있고
     * And 지하철 노선이 등록되어 있고
     * And 지하철 노선에 지하철 구간이 등록되어 있고
     * And 회원이 등록되어 있고
     * And 로그인 되어 있고
     * When 즐겨찾기 생성을 요청하면
     * Then 즐겨찾기를 생성할 수 있다.
     */
    @DisplayName("즐겨찾기를 생성한다.")
    @Test
    void createFavorite() {
        // when
        ExtractableResponse<Response> response = FavoriteAcceptance.create_favorite(tokenResponse,
                강남역.getId(), 잠실역.getId());

        // then
        assertEquals(HttpStatus.CREATED.value(), response.statusCode());
    }

    /**
     * Given 지하철역이 등록되어 있고
     * And 지하철 노선이 등록되어 있고
     * And 지하철 노선에 지하철 구간이 등록되어 있고
     * And 회원이 등록되어 있고
     * When 유효하지 않은 토큰으로 즐겨찾기 생성을 요청하면
     * Then 즐겨찾기를 생성할 수 없다.
     */
    @DisplayName("유효하지 않은 토큰으로 즐겨찾기를 생성한다.")
    @Test
    void createFavoriteWithInvalidToken() {
        // given
        TokenResponse invalidToken = new TokenResponse("Invalid_Token");

        // when
        ExtractableResponse<Response> response = FavoriteAcceptance.create_favorite(invalidToken,
                강남역.getId(), 잠실역.getId());

        // then
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
    }

    /**
     * Given 지하철역이 등록되어 있고
     * And 지하철 노선이 등록되어 있고
     * And 지하철 노선에 지하철 구간이 등록되어 있고
     * And 회원이 등록되어 있고
     * And 로그인 되어 있고
     * When 같은 출발역과 도착역을 입력하여 즐겨찾기 생성을 요청하면
     * Then 즐겨찾기를 생성할 수 없다.
     */
    @DisplayName("같은 출발역과 도착역을 입력하여 즐겨찾기를 생성한다.")
    @Test
    void createFavoriteWithSameSourceAndTarget() {
        // when
        ExtractableResponse<Response> response = FavoriteAcceptance.create_favorite(tokenResponse,
                강남역.getId(), 강남역.getId());

        // then
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
    }

    /**
     * Given 지하철역이 등록되어 있고
     * And 지하철 노선이 등록되어 있고
     * And 지하철 노선에 지하철 구간이 등록되어 있고
     * And 회원이 등록되어 있고
     * And 로그인 되어 있고
     * And 즐겨찾기가 생성되어 있고
     * When 즐겨찾기 목록을 조회하면
     * Then 즐겨찾기 목록을 조회할 수 있다.
     */
    @DisplayName("즐겨찾기 목록을 조회한다.")
    @Test
    void selectFavorites() {
        // given
        FavoriteAcceptance.create_favorite(tokenResponse, 강남역.getId(), 잠실역.getId());

        // when
        List<FavoriteResponse> favorites = FavoriteAcceptance.getFavoriteResponses(
                FavoriteAcceptance.favorite_list_was_queried(tokenResponse));

        // then
        assertThat(favorites).hasSize(1);
    }

    /**
     * Given 지하철역이 등록되어 있고
     * And 지하철 노선이 등록되어 있고
     * And 지하철 노선에 지하철 구간이 등록되어 있고
     * And 회원이 등록되어 있고
     * And 즐겨찾기가 생성되어 있고
     * When 유효하지 않은 토큰으로 즐겨찾기 목록을 조회하면
     * Then 즐겨찾기 목록을 조회할 수 없다.
     */
    @DisplayName("유효하지 않은 토큰으로 즐겨찾기를 조회한다.")
    @Test
    void selectFavoritesWithInvalidToken() {
        // given
        TokenResponse invalidToken = new TokenResponse("Invalid_Token");

        // when
        ExtractableResponse<Response> response = FavoriteAcceptance.favorite_list_was_queried(invalidToken);

        // then
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
    }

    /**
     * Given 지하철역이 등록되어 있고
     * And 지하철 노선이 등록되어 있고
     * And 지하철 노선에 지하철 구간이 등록되어 있고
     * And 회원이 등록되어 있고
     * And 로그인 되어 있고
     * And 즐겨찾기가 생성되어 있고
     * When 즐겨찾기를 삭제하면
     * Then 즐겨찾기를 삭제할 수 있다.
     */
    @DisplayName("즐겨찾기를 삭제한다.")
    @Test
    void deleteFavorite() {
        // given
        FavoriteResponse favoriteResponse = FavoriteAcceptance.create_favorite(tokenResponse, 강남역.getId(),
                잠실역.getId()).as(FavoriteResponse.class);

        // when
        ExtractableResponse<Response> response =
                FavoriteAcceptance.delete_favorite(tokenResponse, favoriteResponse.getId());

        // then
        assertEquals(HttpStatus.NO_CONTENT.value(), response.statusCode());
    }

    /**
     * Given 지하철역이 등록되어 있고
     * And 지하철 노선이 등록되어 있고
     * And 지하철 노선에 지하철 구간이 등록되어 있고
     * And 회원이 등록되어 있고
     * And 즐겨찾기가 생성되어 있고
     * When 유효하지 않은 토큰으로 즐겨찾기를 삭제하면
     * Then 즐겨찾기를 삭제할 수 없다.
     */
    @DisplayName("유효하지 않은 토큰으로 즐겨찾기를 삭제한다.")
    @Test
    void deleteFavoriteWithInvalidToken() {
        // given
        TokenResponse invalidToken = new TokenResponse("Invalid_Token");
        FavoriteResponse favoriteResponse = FavoriteAcceptance.create_favorite(tokenResponse, 강남역.getId(),
                잠실역.getId()).as(FavoriteResponse.class);

        // when
        ExtractableResponse<Response> response =
                FavoriteAcceptance.delete_favorite(invalidToken, favoriteResponse.getId());

        // then
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
    }

    /**
     * Given 지하철역이 등록되어 있고
     * And 지하철 노선이 등록되어 있고
     * And 지하철 노선에 지하철 구간이 등록되어 있고
     * And 회원이 등록되어 있고
     * And 즐겨찾기가 생성되어 있고
     * When 다른 회원으로 로그인하여 즐겨찾기를 삭제하면
     * Then 즐겨찾기를 삭제할 수 없다.
     */
    @DisplayName("즐겨찾기를 등록한 회원과 다른 회원으로 로그인하여 즐겨찾기를 삭제한다.")
    @Test
    void deleteFavoriteWithWrongMember() {
        // given
        FavoriteResponse favoriteResponse = FavoriteAcceptance.create_favorite(tokenResponse, 강남역.getId(),
                잠실역.getId()).as(FavoriteResponse.class);

        // when
        ExtractableResponse<Response> response =
                FavoriteAcceptance.delete_favorite(tokenResponseOfWrongMember, favoriteResponse.getId());

        // then
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
    }
}
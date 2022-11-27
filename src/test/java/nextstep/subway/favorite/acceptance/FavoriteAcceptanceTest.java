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

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private LineResponse 이호선;
    private StationResponse 강남역;
    private StationResponse 잠실역;

    private TokenResponse tokenResponse;

    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = StationAcceptance.create_station("강남역").as(StationResponse.class);
        잠실역 = StationAcceptance.create_station("잠실역").as(StationResponse.class);
        이호선 = LineAcceptance.create_line("2호선", "bg-green-600", 강남역.getId(),
                잠실역.getId(), 30).as(LineResponse.class);

        MemberAcceptance.create_member("testuser@test.com", "password157#", 20);
        tokenResponse = AuthAcceptance.member_token_is_issued("testuser@test.com", "password157#");
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
        FavoriteResponse favoriteResponse = FavoriteAcceptance.create_favorite(tokenResponse,
                강남역.getId(), 잠실역.getId()).as(FavoriteResponse.class);

        // then
        assertThat(favoriteResponse.getId()).isNotNull();
    }
}
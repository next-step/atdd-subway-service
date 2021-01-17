package nextstep.subway.favorite.application;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.member.application.FavoriteService;
import nextstep.subway.member.dto.FavoriteRequest;
import nextstep.subway.member.dto.FavoriteResponse;
import nextstep.subway.member.dto.MemberResponse;
import nextstep.subway.station.StationAcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTestSupport.로그인_되어_있음;
import static nextstep.subway.member.MemberAcceptanceTestSupport.나의_정보_요청;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
public class FavoriteServiceTest extends AcceptanceTest {

    private StationResponse 강남역;
    private StationResponse 양재역;
    private Long id;

    @Autowired
    FavoriteService favoriteService;

    @BeforeEach
    void setup() {

        강남역 = StationAcceptanceTest.지하철역_등록되어_있음("강남역").as(StationResponse.class);
        양재역 = StationAcceptanceTest.지하철역_등록되어_있음("양재역").as(StationResponse.class);

        // 회원 등록되어 있음
        String accessToken = 로그인_되어_있음();
        ExtractableResponse<Response> myInfoResponse = 나의_정보_요청(accessToken);
        MemberResponse memberResponse = myInfoResponse.as(MemberResponse.class);
        this.id = memberResponse.getId();
    }

    @Test
    @DisplayName("즐겨찾기를 추가합니다.")
    void addFavorites() {
        // when
        Long favoriteId = this.favoriteService.addFavorite(this.id, new FavoriteRequest(강남역.getId(), 양재역.getId()));

        // then
        assertThat(favoriteId).isNotNull();
    }

    @Test
    @DisplayName("즐겨찾기를 조회합니다.")
    void findFavorites() {
        // given
        Long favoriteId = this.favoriteService.addFavorite(this.id, new FavoriteRequest(강남역.getId(), 양재역.getId()));

        // when
        FavoriteResponse favoriteResponse = this.favoriteService.findFavorites(this.id);

        // then
        assertThat(favoriteResponse.getFavorites()).hasSize(1);
        assertThat(favoriteResponse.getFavorites().get(0).getId()).isEqualTo(favoriteId);
    }

    @Test
    @DisplayName("즐겨찾기를 삭제합니다.")
    void deleteFavorite() {
        // given
        Long favoriteId = this.favoriteService.addFavorite(this.id, new FavoriteRequest(강남역.getId(), 양재역.getId()));

        // when
        this.favoriteService.deleteFavorite(this.id, favoriteId);
        FavoriteResponse favoriteResponse = this.favoriteService.findFavorites(this.id);

        // then
        assertThat(favoriteResponse.getFavorites()).hasSize(0);
    }
}

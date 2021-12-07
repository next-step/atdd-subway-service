package nextstep.subway.favorite;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.application.FavoriteService;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static nextstep.subway.member.MemberAcceptanceTest.*;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

public class FavoriteServiceTest extends AcceptanceTest {
    @Autowired
    FavoriteService favoriteService;
    private StationResponse 강남역;
    private StationResponse 정자역;
    private Favorite favorite;
    private LoginMember loginMember;

    @BeforeEach
    public void setUp() {
        super.setUp();
        // Given 지하철역 등록되어 있음
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        정자역 = 지하철역_등록되어_있음("정자역").as(StationResponse.class);

        // And 회원 등록되어 있음
        ExtractableResponse<Response> createResponse = 회원_생성을_요청(MY_EMAIL, MY_PASSWORD, MY_AGE);
        회원_생성됨(createResponse);

        String location = createResponse.header("Location");
        Long id = Long.valueOf(location.substring(location.lastIndexOf('/') + 1));
        loginMember = new LoginMember(id, MY_EMAIL, MY_AGE);

        // And 즐겨찾기 추가
        favorite = favoriteService.saveFavorite(loginMember, FavoriteRequest.of(강남역.getId(), 정자역.getId()));
    }

    @DisplayName("즐겨찾기를 저장 서비스를 호출하면, DB에 해당 데이터가 저장된다.")
    @Test
    void saveFavoriteTest() {
        assertThat(favorite.getId()).isNotNull();
        assertThat(favorite.getSourceStation().getName()).isEqualTo(강남역.getName());
        assertThat(favorite.getTargetStation().getName()).isEqualTo(정자역.getName());
    }

    @DisplayName("사용자 정보를 입력 받아 즐겨찾기 목록을 조회하면 즐겨찾기 콜렉션을 반환한다.")
    @Test
    void findFavoritesTest() {
        assertThat(favoriteService.findFavorites(loginMember).size()).isEqualTo(1);
    }

    @DisplayName("사용자 id를 입력 받아 즐겨찾기 제거 함수를 호출하면 즐겨 찾기 삭제한다.")
    @Test
    void deleteFavoriteTest() {
        favoriteService.deleteFavorite(loginMember.getId());
        assertThat(favoriteService.findFavorites(loginMember).size()).isEqualTo(0);
    }

}

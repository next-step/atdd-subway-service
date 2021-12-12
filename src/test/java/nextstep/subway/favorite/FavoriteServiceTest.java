package nextstep.subway.favorite;

import static nextstep.subway.member.MemberAcceptanceTest.*;
import static nextstep.subway.station.StationAcceptanceTest.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;

import io.restassured.response.*;
import nextstep.subway.*;
import nextstep.subway.auth.domain.*;
import nextstep.subway.common.exception.*;
import nextstep.subway.favorite.application.*;
import nextstep.subway.favorite.dto.*;
import nextstep.subway.station.dto.*;

public class FavoriteServiceTest extends BaseServiceTest {
    @Autowired
    FavoriteService favoriteService;

    @Autowired
    FavoriteReadService favoriteReadService;

    private StationResponse 강남역;
    private StationResponse 정자역;
    private FavoriteResponse favoriteResponse;
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
        favoriteResponse = favoriteService.saveFavorite(loginMember, FavoriteRequest.of(강남역.getId(), 정자역.getId()));
    }

    @DisplayName("즐겨찾기를 저장 서비스를 호출하면, DB에 해당 데이터가 저장된다.")
    @Test
    void saveFavoriteTest() {
        assertThat(favoriteResponse.getId()).isNotNull();
        assertThat(favoriteResponse.getSourceStation().getName()).isEqualTo(강남역.getName());
        assertThat(favoriteResponse.getTargetStation().getName()).isEqualTo(정자역.getName());
    }

    @DisplayName("사용자 정보를 입력 받아 즐겨찾기 목록을 조회하면 즐겨찾기 콜렉션을 반환한다.")
    @Test
    void findFavoritesTest() {
        assertThat(favoriteReadService.findFavorites(loginMember).size()).isEqualTo(1);
    }

    @DisplayName("사용자 id를 입력 받아 즐겨찾기 제거 함수를 호출하면 즐겨 찾기 삭제한다.")
    @Test
    void deleteFavoriteTest() {
        favoriteService.deleteFavorite(favoriteResponse.getId(), loginMember.getId());
        assertThat(favoriteReadService.findFavorites(loginMember).size()).isEqualTo(0);
    }

    @DisplayName("출발역과 도착역이 같은 즐겨찾기를 저장하려고 한다면, 예외를 던진다")
    @Test
    void exceptionTest() {
        assertThatThrownBy(() -> favoriteService.saveFavorite(loginMember,
            FavoriteRequest.of(강남역.getId(),
                강남역.getId()))
        ).isInstanceOf(CannotAddException.class);
    }
}

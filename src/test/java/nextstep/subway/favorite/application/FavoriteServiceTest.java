package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteFixtures;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.domain.adapters.SafeStationAdapter;
import nextstep.subway.favorite.domain.excpetions.FavoriteCreationException;
import nextstep.subway.favorite.ui.dto.FavoriteRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {
    private FavoriteService favoriteService;

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private SafeStationAdapter safeStationAdapter;

    @BeforeEach
    void setup() {
        favoriteService = new FavoriteService(favoriteRepository, safeStationAdapter);
    }

    @DisplayName("새로운 즐겨찾기를 추가할 수 있다.")
    @Test
    void createFavoriteTest() {
        Long memberId = 1L;
        Long sourceId = 1L;
        Long targetId = 2L;
        LoginMember loginMember = new LoginMember(memberId, "hello", 32);
        FavoriteRequest favoriteRequest = new FavoriteRequest(sourceId, targetId);
        given(safeStationAdapter.isAllExists(sourceId, targetId)).willReturn(true);
        given(favoriteRepository.save(any())).willReturn(FavoriteFixtures.FAVORITE_1);

        Long savedId = favoriteService.saveFavorite(loginMember, favoriteRequest);

        assertThat(savedId).isEqualTo(FavoriteFixtures.FAVORITE_1.getId());
    }

    @DisplayName("존재하지 않는 역이 포함된 즐겨찾기를 추가할 수 없다.")
    @Test
    void saveFavoriteFailTest() {
        Long memberId = 1L;
        Long sourceId = 1L;
        Long targetId = 2L;
        LoginMember loginMember = new LoginMember(memberId, "hello", 32);
        FavoriteRequest favoriteRequest = new FavoriteRequest(sourceId, targetId);
        given(safeStationAdapter.isAllExists(sourceId, targetId)).willReturn(false);

        assertThatThrownBy(() -> favoriteService.saveFavorite(loginMember, favoriteRequest))
                .isInstanceOf(FavoriteCreationException.class);
    }
}
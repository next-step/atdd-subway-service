package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.application.exceptions.FavoriteEntityNotFoundException;
import nextstep.subway.favorite.domain.FavoriteFixtures;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.domain.adapters.SafeStationForFavoriteAdapter;
import nextstep.subway.favorite.domain.excpetions.FavoriteCreationException;
import nextstep.subway.favorite.ui.dto.FavoriteRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {
    private FavoriteService favoriteService;

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private SafeStationForFavoriteAdapter safeStationAdapter;

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

    @DisplayName("등록된 즐겨찾기를 제거할 수 있다.")
    @Test
    void deleteFavoriteTest() {
        Long deleteTarget = 1L;

        favoriteService.deleteFavorite(deleteTarget);

        verify(favoriteRepository).deleteById(deleteTarget);
    }

    @DisplayName("등록되지 않는 즐겨찾기 제거 시도 시 예외 발생")
    @Test
    void deleteFavoriteFailTest() {
        Long deleteTarget = 1L;

        doThrow(new EmptyResultDataAccessException(10)).when(favoriteRepository).deleteById(deleteTarget);

        assertThatThrownBy(() -> favoriteService.deleteFavorite(deleteTarget))
                .isInstanceOf(FavoriteEntityNotFoundException.class);
    }
}
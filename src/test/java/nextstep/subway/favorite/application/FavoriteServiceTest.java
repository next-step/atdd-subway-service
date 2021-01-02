package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.application.exceptions.FavoriteEntityNotFoundException;
import nextstep.subway.favorite.application.exceptions.NotMyFavoriteException;
import nextstep.subway.favorite.domain.FavoriteFixtures;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.domain.adapters.SafeStationForFavoriteAdapter;
import nextstep.subway.favorite.domain.adapters.SafeStationInFavorite;
import nextstep.subway.favorite.domain.excpetions.FavoriteCreationException;
import nextstep.subway.favorite.ui.dto.FavoriteRequest;
import nextstep.subway.favorite.ui.dto.FavoriteResponse;
import nextstep.subway.favorite.ui.dto.StationInFavoriteResponse;
import nextstep.subway.station.domain.StationFixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
        LoginMember loginMember = new LoginMember(1L, "test@nextstep.com", 30);
        Long deleteTarget = 1L;
        given(favoriteRepository.findById(deleteTarget)).willReturn(Optional.of(FavoriteFixtures.FAVORITE_1));

        favoriteService.deleteFavorite(loginMember, deleteTarget);

        verify(favoriteRepository).deleteById(deleteTarget);
    }

    @DisplayName("본인의 즐겨찾기가 아닌 즐겨찾기 항목을 제거할 수 없다.")
    @Test
    void deleteFavoriteFailWhenNotMineTest() {
        LoginMember loginMember = new LoginMember(2L, "test@nextstep.com", 30);
        Long deleteTarget = 1L;
        given(favoriteRepository.findById(deleteTarget)).willReturn(Optional.of(FavoriteFixtures.FAVORITE_1));

        assertThatThrownBy(() -> favoriteService.deleteFavorite(loginMember, deleteTarget))
                .isInstanceOf(NotMyFavoriteException.class);
    }

    @DisplayName("등록되지 않는 즐겨찾기 제거 시도 시 예외 발생")
    @Test
    void deleteFavoriteFailTest() {
        LoginMember loginMember = new LoginMember(1L, "test@nextstep.com", 30);
        Long deleteTarget = 1L;

        assertThatThrownBy(() -> favoriteService.deleteFavorite(loginMember, deleteTarget))
                .isInstanceOf(FavoriteEntityNotFoundException.class);
    }

    @DisplayName("등록된 즐겨찾기 정보들을 받아올 수 있다.")
    @Test
    void getFavoritesTest() {
        LoginMember loginMember = new LoginMember(1L, "test", 30);
        given(favoriteRepository.findAllByMemberId(loginMember.getId()))
                .willReturn(Collections.singletonList(FavoriteFixtures.FAVORITE_1));
        given(safeStationAdapter.getSafeStationInFavorite(1L)).willReturn(
                new SafeStationInFavorite(StationFixtures.강남역));
        given(safeStationAdapter.getSafeStationInFavorite(2L)).willReturn(
                new SafeStationInFavorite(StationFixtures.역삼역));

        List<FavoriteResponse> favoriteResponses = favoriteService.getFavorites(loginMember);

        assertThat(favoriteResponses).hasSize(1);
        assertThat(favoriteResponses).contains(new FavoriteResponse(
                1L,
                new StationInFavoriteResponse(1L, "강남역", null, null),
                new StationInFavoriteResponse(2L, "역삼역", null, null)
        ));
    }
}
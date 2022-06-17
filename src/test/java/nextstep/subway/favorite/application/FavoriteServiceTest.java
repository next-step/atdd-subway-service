package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private StationRepository stationRepository;

    /**
     * Given 정상적인 LoginMember 와 FavoriteRequest 를 이용하여 Favorite 를 생성하고
     * When  Favorite 를 저장하면
     * Then 정상적으로 저장이 되고 FavoriteResponse 를 반환한다.
     */
    @DisplayName("즐겨찾기 생성 하기")
    @Test
    void createTest() {
        // Given
        final Station 수원역 = new Station(1L, "수원역");
        final Station 병점역 = new Station(2L, "병점역");
        final LoginMember loginMember = new LoginMember(1L, "email@email.com", 20);
        when(stationRepository.findById(1L)).thenReturn(Optional.of(수원역));
        when(stationRepository.findById(2L)).thenReturn(Optional.of(병점역));
        when(favoriteRepository.save(any())).thenReturn(new Favorite(loginMember, 수원역, 병점역));
        FavoriteService favoriteService = new FavoriteService(stationRepository, favoriteRepository);
        FavoriteRequest favoriteRequest = new FavoriteRequest(1L, 2L);

        // When
        FavoriteResponse favoriteResponse = favoriteService.saveFavorite(loginMember, favoriteRequest);

        // Then
        assertThat(favoriteResponse.getSource().getId()).isEqualTo(1L);
        assertThat(favoriteResponse.getTarget().getId()).isEqualTo(2L);
        verify(favoriteRepository, times(1)).save(any());
    }
}
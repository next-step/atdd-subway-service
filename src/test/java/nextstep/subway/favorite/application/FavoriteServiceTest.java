package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("즐겨찾기 관련 기능 Mock 테스트")
@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {

    @Mock
    private FavoriteRepository favoriteRepository;
    @Mock
    private StationService stationService;

    @InjectMocks
    private FavoriteService favoriteService;

    @DisplayName("즐겨찾기 목록 조회 테스트")
    @Test
    void findFavoritesTest() {
        // given
        Mockito.when(favoriteRepository.findByMemberId(1l)).thenReturn(Arrays.asList(new Favorite(1l, 2l, 1l)));

        // when
        List<FavoriteResponse> 조회_결과 = favoriteService.findFavorites(1l);

        // then
        assertThat(조회_결과).isNotEmpty();
        Mockito.verify(favoriteRepository).findByMemberId(Mockito.any());
    }

    @DisplayName("즐겨찾기 저장 테스트")
    @Test
    void saveFavoriteTest() {
        // given
        Mockito.when(stationService.findStationById(1l)).thenReturn(new Station("강남역"));
        Mockito.when(stationService.findStationById(2l)).thenReturn(new Station("역삼역"));
        Mockito.when(favoriteRepository.save(Mockito.any())).thenReturn(new Favorite(1l, 2l, 1l));

        // when
        FavoriteResponse favoriteResponse = favoriteService.saveFavorite(1l, new FavoriteRequest(1l, 2l));

        // then
        assertAll(() -> {
            assertThat(favoriteResponse).isNotNull();
            assertThat(favoriteResponse.getSource()).isEqualTo(1l);
            assertThat(favoriteResponse.getTarget()).isEqualTo(2l);
        });

        Mockito.verify(stationService, Mockito.times(2)).findStationById(Mockito.any());
        Mockito.verify(favoriteRepository).save(Mockito.any());
    }

    @DisplayName("즐겨찾기 삭제 테스트")
    @Test
    void removeFavoriteTest() {
        // when
        favoriteService.removeFavorite(1l);

        // then
        Mockito.verify(favoriteRepository).deleteById(Mockito.any());
    }

}

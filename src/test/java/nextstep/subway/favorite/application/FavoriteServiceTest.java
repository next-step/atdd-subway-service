package nextstep.subway.favorite.application;

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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static nextstep.subway.station.domain.Station.stationStaticFactoryForTestCode;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {

  @Mock
  private StationRepository stationRepository;

  @Mock
  private FavoriteRepository favoriteRepository;

  @DisplayName("즐겨찾기 생성 요청을 한다.")
  @Test
  void saveFavoriteTest() {
    //given
    Long userId = 1L;
    Station sourceStation = stationStaticFactoryForTestCode(1L, "교대역");
    Station targetStation = stationStaticFactoryForTestCode(2L, "양재역");
    when(stationRepository.findById(sourceStation.getId())).thenReturn(Optional.of(sourceStation));
    when(stationRepository.findById(targetStation.getId())).thenReturn(Optional.of(targetStation));
    Favorite favorite = new Favorite(userId, sourceStation, targetStation);
    when(favoriteRepository.save(any())).thenReturn(favorite);
    FavoriteService favoriteService = new FavoriteService(stationRepository, favoriteRepository);

    //when
    FavoriteResponse result = favoriteService.saveFavorite(0L, new FavoriteRequest(sourceStation.getId(), targetStation.getId()));

    //then
    assertThat(result).isEqualTo(FavoriteResponse.of(favorite));
  }

  @DisplayName("즐겨찾기 목록 조회 요청을 한다.")
  @Test
  void findFavoritesTest() {
    //given
    Long userId = 1L;
    Station sourceStation = stationStaticFactoryForTestCode(1L, "교대역");
    Station targetStation = stationStaticFactoryForTestCode(2L, "양재역");
    Favorite favorite = new Favorite(userId, sourceStation, targetStation);
    when(favoriteRepository.findAllByMemberId(anyLong())).thenReturn(Arrays.asList(favorite));
    FavoriteService favoriteService = new FavoriteService(stationRepository, favoriteRepository);

    //when
    List<FavoriteResponse> result = favoriteService.findFavorites(0L);

    //then
    assertThat(result).containsExactly(FavoriteResponse.of(favorite));
  }

}

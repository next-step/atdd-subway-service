package nextstep.subway.favorite.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.exception.CannotAddException;
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
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("즐겨찾기 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {

    @Mock
    FavoriteRepository favoriteRepository;

    @Mock
    StationService stationService;

    @InjectMocks
    FavoriteService favoriteService;

    final Station 서울역 = new Station("서울역");
    final Station 용산역 = new Station("용산역");

    @Test
    @DisplayName("즐겨찾기 저장")
    void saveFavorite() {

        when(stationService.findStation(1L)).thenReturn(서울역);
        when(stationService.findStation(2L)).thenReturn(용산역);
        when(favoriteRepository.findAllByOwnerId(1L)).thenReturn(Arrays.asList());
        when(favoriteRepository.save(any(Favorite.class))).thenReturn(Favorite.of(서울역, 용산역, 1L));

        FavoriteRequest favoriteRequest = new FavoriteRequest(1L, 2L);
        FavoriteResponse favoriteResponse = favoriteService.saveFavorite(1L, favoriteRequest);

        assertAll(() -> {
            assertThat(favoriteResponse.getSource().getName()).isEqualTo("서울역");
            assertThat(favoriteResponse.getTarget().getName()).isEqualTo("용산역");
        });
    }

    @Test
    @DisplayName("즐겨찾기 목록 조회")
    void findFavoriteResponseList() {
        Station 남영역 = new Station("남영역");

        when(favoriteRepository.findAllByOwnerId(1L))
            .thenReturn(Arrays.asList(Favorite.of(서울역, 용산역, 1L), Favorite.of(남영역, 용산역, 1L)));

        List<FavoriteResponse> favoriteResponseList = favoriteService.findFavoriteResponseList(1L);
        List<String> favoriteNames = favoriteResponseList.stream()
            .map(it -> it.getSource().getName() + "-" + it.getTarget().getName())
            .collect(Collectors.toList());

        assertAll(() -> {
            assertThat(favoriteResponseList.size()).isEqualTo(2);
            assertThat(favoriteNames).containsExactlyElementsOf(Arrays.asList("서울역-용산역", "남영역-용산역"));
        });
    }

    @Test
    @DisplayName("즐겨찾기 중보인 경우 CannotAddException")
    void validateSaveFail() {
        when(stationService.findStation(1L)).thenReturn(서울역);
        when(stationService.findStation(2L)).thenReturn(용산역);
        when(favoriteRepository.findAllByOwnerId(1L)).thenReturn(Arrays.asList(Favorite.of(서울역, 용산역, 1L)));

        FavoriteRequest favoriteRequest = new FavoriteRequest(1L, 2L);
        assertThatThrownBy(() -> favoriteService.saveFavorite(1L, favoriteRequest))
            .isInstanceOf(CannotAddException.class)
            .hasMessage("이미 등록된 즐겨찾기 입니다.");
    }
}
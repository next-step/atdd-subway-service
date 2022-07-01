package nextstep.subway.favorites.application;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.Collections;
import nextstep.subway.favorites.domain.Favorites;
import nextstep.subway.favorites.domain.FavoritesRepository;
import nextstep.subway.favorites.dto.FavoritesRequest;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("Service:Favorites")
class FavoritesServiceTest {

    @Mock
    private FavoritesRepository favoritesRepository;

    @Mock
    private StationService stationService;

    @InjectMocks
    private FavoritesService favoritesService;

    private Station 선정릉역;
    private Station 선릉역;
    private Long 선정릉역_번호;
    private Long 선릉역_번호;

    @BeforeEach
    void setUp() {
        선정릉역 = new Station("선정릉역");
        선릉역 = new Station("선릉역");

        선정릉역_번호 = 1L;
        선릉역_번호 = 2L;
    }

    @Test
    @DisplayName("즐겨찾기를 저장한다.")
    public void saveFavorites() {
        // Given
        given(stationService.findStationById(선정릉역_번호)).willReturn(선정릉역);
        given(stationService.findStationById(선릉역_번호)).willReturn(선릉역);
        given(favoritesRepository.save(any(Favorites.class))).will(AdditionalAnswers.returnsFirstArg());

        FavoritesRequest favoritesRequest = new FavoritesRequest(선정릉역_번호, 선릉역_번호);

        // When
        favoritesService.saveFavorites(favoritesRequest);

        // Then
        verify(stationService, times(2)).findStationById(anyLong());
        verify(favoritesRepository).save(any(Favorites.class));
    }

    @Test
    @DisplayName("즐겨찾기 목록을 조회한다.")
    public void findAllFavorites() {
        // Given
        Favorites favorites = new Favorites(선정릉역, 선릉역);
        given(favoritesRepository.findAll()).willReturn(Collections.unmodifiableList(Arrays.asList(favorites)));

        // When
        favoritesService.findAllFavorites();

        // Then
        verify(favoritesRepository).findAll();
    }
}

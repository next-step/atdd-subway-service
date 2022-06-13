package nextstep.subway.favorite.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

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

@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {

    @Mock
    StationService stationService;

    @InjectMocks
    FavoriteService favoriteService;

    @Test
    @DisplayName("즐겨찾기를 등록 한다.")
    void save() {
        // 즐겨찾기를 db에 저장
        // FavoriteResponse를 응답한다.

        //given
        Station 강남역 = new Station(1L, "강남역");
        Station 잠실역 = new Station(2L, "잠실역");

        when(stationService.findStationById(1L)).thenReturn(강남역);
        when(stationService.findStationById(2L)).thenReturn(잠실역);
        FavoriteRequest favoriteRequest = new FavoriteRequest(강남역.getId(), 잠실역.getId());

        FavoriteResponse favoriteResponse = favoriteService.save(favoriteRequest);

        assertThat(favoriteResponse).isNotNull();
        assertThat(favoriteResponse.getId()).isNotNull();
    }

}

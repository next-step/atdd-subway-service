package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FavoriteServiceTest {
    public static final long MEMBER_ID = 1L;
    public static final long 강남역_ID = 1L;
    public static final long 광교역_ID = 2L;
    public static final long 교대역_ID = 3L;
    public static final long 양재역_ID = 4L;
    public static final String MEMBER_EMAIL = "me@mail.com";
    public static final int MEMBER_AGE = 20;
    @Mock
    private FavoriteRepository favoriteRepository;
    @Mock
    private StationRepository stationRepository;

    @Test
    void findFavorite() {
        // given
        when(favoriteRepository.findByMemberId(MEMBER_ID))
                .thenReturn(Arrays.asList(new Favorite(MEMBER_ID, 강남역_ID, 광교역_ID)
                                        , new Favorite(MEMBER_ID, 교대역_ID, 양재역_ID)));
        when(stationRepository.findById(강남역_ID)).thenReturn(Optional.of(new Station("강남역")));
        when(stationRepository.findById(광교역_ID)).thenReturn(Optional.of(new Station("광교역")));
        when(stationRepository.findById(교대역_ID)).thenReturn(Optional.of(new Station("교대역")));
        when(stationRepository.findById(양재역_ID)).thenReturn(Optional.of(new Station("양재역")));
        FavoriteService favoriteService = new FavoriteService(favoriteRepository, stationRepository);

        // when
        List<FavoriteResponse> favorite = favoriteService.findFavorite(new LoginMember(MEMBER_ID, MEMBER_EMAIL, MEMBER_AGE));

        // then
        assertThat(favorite).hasSize(2);
        verify(favoriteRepository).findByMemberId(MEMBER_ID);
        verify(stationRepository).findById(강남역_ID);
        verify(stationRepository).findById(광교역_ID);
        verify(stationRepository).findById(교대역_ID);
        verify(stationRepository).findById(양재역_ID);
    }
}

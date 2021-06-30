package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {
    private static final Long TARGET = 2L;
    private static final Long SOURCE = 1L;
    private static final Long TARGET2 = 3L;
    private static final Long LOGIN_MEMBER_ID = 1L;

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private StationRepository stationRepository;

    private FavoriteService favoriteService;

    private Favorite favorite1;
    private Favorite favorite2;
    private Station station1;
    private Station station2;
    private Station station3;


    @BeforeEach
    void setUp() {
        favoriteService = new FavoriteService(favoriteRepository, stationRepository);
        favorite1 = new Favorite(1L, LOGIN_MEMBER_ID, SOURCE, TARGET);
        favorite2 = new Favorite(2L, LOGIN_MEMBER_ID, SOURCE, TARGET2);
        station1 = new Station("강남역");
        ReflectionTestUtils.setField(station1, "id", 1L);
        station2 = new Station("교대역");
        ReflectionTestUtils.setField(station2, "id", 2L);
        station3 = new Station("양재역");
        ReflectionTestUtils.setField(station3, "id", 3L);
    }

    @Test
    void saveFavorite() {
        // given
        when(favoriteRepository.save(any(Favorite.class)))
                .thenReturn(favorite1);
        // when
        Favorite actual = favoriteService.saveFavorite(LOGIN_MEMBER_ID, SOURCE, TARGET);
        // then
        assertThat(actual).isNotNull();
    }

    @Test
    void getFavorite() {
        // given
        when(favoriteRepository.findAllByMemberId(anyLong()))
                .thenReturn(Arrays.asList(favorite1, favorite2));
        when(stationRepository.findAllById(anySet()))
                .thenReturn(Arrays.asList(station1, station2, station3));
        // when
        List<FavoriteResponse> actual = favoriteService.getFavorites(LOGIN_MEMBER_ID);
        // then
        assertThat(actual.size()).isNotZero();
    }
}

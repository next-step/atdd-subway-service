package nextstep.subway.favorite.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoritesResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;


@DisplayName("FavoriteService 협력객체 이용 테스트")
@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {

    FavoriteService favoriteService;

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private StationRepository stationRepository;

    @BeforeEach
    void setup() {
        favoriteService = new FavoriteService(favoriteRepository, memberRepository, stationRepository);
    }

    @DisplayName("즐겨찾기 추가")
    @Test
    void add() {
        //given
        //when
        when(favoriteRepository.save(any())).thenReturn(new Favorite());
        when(memberRepository.findById(any())).thenReturn(Optional.of(new Member()));
        when(stationRepository.findById(any())).thenReturn(Optional.of(new Station()));
        favoriteService.add(new LoginMember(), new FavoriteRequest());
        //then
        verify(favoriteRepository, times(1)).save(any());
    }

    @DisplayName("즐겨찾기 조회")
    @Test
    void search() {
        //given
        //when
        when(memberRepository.findById(any())).thenReturn(Optional.of(new Member()));
        when(favoriteRepository.findByMember(any())).thenReturn(new ArrayList<>());
        FavoritesResponse favoritesResponse = favoriteService.search(new LoginMember());
        //then
        assertThat(favoritesResponse).isNotNull();
    }

    @DisplayName("즐겨찾기 제거")
    @Test
    void remove() {
        //given
        //when
        when(memberRepository.findById(any())).thenReturn(Optional.of(new Member()));
        when(favoriteRepository.findByMember(any())).thenReturn(new ArrayList<>(Arrays.asList(new Favorite())));

        favoriteService.remove(new LoginMember());
        //then
        verify(favoriteRepository, times(1)).delete(any());
    }
}

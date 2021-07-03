package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.application.LineService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private StationRepository stationRepository;

    @Mock
    private FavoriteRepository favoriteRepository;

    private FavoriteService favoriteService;

    @BeforeEach
    void setUp() {
        favoriteService = new FavoriteService(favoriteRepository, memberRepository, stationRepository);
    }

    @Test
    void createFavoriteTest() {
        when(memberRepository.findById(1L)).thenReturn(Optional.of(new Member("", "", 1)));
        when(stationRepository.findById(1L)).thenReturn(Optional.of(new Station("강남역")));
        when(stationRepository.findById(2L)).thenReturn(Optional.of(new Station("역삼역")));
        when(favoriteRepository.save(any(Favorite.class))).thenReturn(new Favorite(any(Member.class), new Station("강남역"), new Station("역삼역")));

        FavoriteResponse favorite = favoriteService.createFavorite(1L, new FavoriteRequest(1L, 2L));

        assertThat(favorite.getSource().getName()).isEqualTo("강남역");
        assertThat(favorite.getTarget().getName()).isEqualTo("역삼역");
    }

    @Test
    void findAllTest() {
        when(favoriteRepository.findByMemberId(1L)).thenReturn(Optional.of(Arrays.asList(
                new Favorite(new Member("", "", 1), new Station("강남역"), new Station("서초역"))
                , new Favorite(new Member("", "", 1), new Station("강남역"), new Station("광교역")))));

        List<FavoriteResponse> favorites = favoriteService.findAll(1L);
        assertThat(favorites).hasSize(2);
    }
}
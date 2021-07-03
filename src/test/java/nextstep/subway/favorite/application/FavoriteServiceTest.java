package nextstep.subway.favorite.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {
    @Mock
    private StationService stationService;
    @Mock
    private MemberService memberService;
    @Mock
    private FavoriteRepository favoriteRepository;

    @InjectMocks
    private FavoriteService favoriteService;

    private Station station1;
    private Station station2;
    private Member member;
    private Favorite favorite;

    @BeforeEach
    void setUp() {
        station1 = new Station("강남역");
        station2 = new Station("교대역");
        member = new Member(MemberAcceptanceTest.EMAIL, MemberAcceptanceTest.PASSWORD, MemberAcceptanceTest.AGE);
        favorite = new Favorite(member, station1, station2);
    }

    @Test
    @DisplayName("즐겨찾기 저장")
    void save() {
        // mocking
        when(stationService.findById(1L)).thenReturn(station1);
        when(stationService.findById(2L)).thenReturn(station2);
        when(favoriteRepository.save(any(Favorite.class))).thenReturn(favorite);
        when(memberService.findMemberById(1L)).thenReturn(member);

        // when
        FavoriteResponse favoriteResponse = favoriteService.saveFavorite(1L, new FavoriteRequest(1L, 2L));

        // then
        assertAll(
                () -> assertThat(favoriteResponse.getSource().getName()).isEqualTo(station1.getName()),
                () -> assertThat(favoriteResponse.getTarget().getName()).isEqualTo(station2.getName())
        );
    }

    @Test
    @DisplayName("즐겨찾기 목록 조회")
    void find_all_favorite() {
        when(favoriteRepository.findByMemberId(anyLong())).thenReturn(new ArrayList<>(Arrays.asList(favorite)));

        List<FavoriteResponse> favorites = favoriteService.findAllFavoriteByMemberId(1L);

        assertThat(favorites.size()).isNotZero();
    }
}

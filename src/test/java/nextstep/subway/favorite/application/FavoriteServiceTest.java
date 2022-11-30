package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("FavoriteService 테스트")
@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {

    private static final long MEMBER_ID = 1L;
    private static final long SOURCE_ID = 1L;
    private static final long TARGET_ID = 2L;
    private static final long FAVORITE_ID = 1L;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private StationRepository stationRepository;

    @InjectMocks
    private FavoriteService favoriteService;

    private Member member;
    private Station source;
    private Station target;

    @BeforeEach
    void setUp() {
        member = new Member("testuser@test.com", "password157#", 20);
        source = new Station("강남역");
        target = new Station("잠실역");
    }

    @Test
    void 즐겨찾기_생성() {
        FavoriteRequest request = new FavoriteRequest(SOURCE_ID, TARGET_ID);
        Favorite favorite = mock(Favorite.class);

        when(favorite.getId()).thenReturn(FAVORITE_ID);
        when(favorite.getSource()).thenReturn(source);
        when(favorite.getTarget()).thenReturn(target);

        when(memberRepository.findById(MEMBER_ID)).thenReturn(Optional.of(member));
        when(stationRepository.findById(SOURCE_ID)).thenReturn(Optional.of(source));
        when(stationRepository.findById(TARGET_ID)).thenReturn(Optional.of(target));
        when(favoriteRepository.save(any())).thenReturn(favorite);

        FavoriteResponse response = favoriteService.createFavorite(MEMBER_ID, request);

        assertThat(response).satisfies(item -> {
            assertEquals(item.getSource().getName(), source.getName());
            assertEquals(item.getTarget().getName(), target.getName());
        });
    }

    @Test
    void 즐겨찾기_목록_조회() {
        Favorite favorite = mock(Favorite.class);

        when(favorite.getId()).thenReturn(FAVORITE_ID);
        when(favorite.getSource()).thenReturn(source);
        when(favorite.getTarget()).thenReturn(target);

        when(favoriteRepository.findAllByMemberId(MEMBER_ID))
                .thenReturn(Arrays.asList(favorite));

        List<FavoriteResponse> favorites = favoriteService.findAllFavorites(MEMBER_ID);

        assertThat(favorites).hasSize(1);
    }
}

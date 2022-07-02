package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {
    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private StationService stationService;

    @Mock
    private MemberService memberService;

    @InjectMocks
    private FavoriteService favoriteService;

    private Member member;
    private LoginMember loginMember;
    private Station 강남역;
    private Station 역삼역;
    private Station 삼성역;
    private List<Favorite> favorites;

    @BeforeEach
    void setUp() {
        member = new Member(1L,"weno@nextstep.com", "password", 20);
        loginMember = new LoginMember(1L, "weno@nextstep.com", 20);
        강남역 = new Station(1L, "강남역");
        역삼역 = new Station(2L, "역삼역");
        삼성역 = new Station(3L, "삼성역");
        favorites = Arrays.asList(new Favorite(member.getId(), 강남역, 역삼역), new Favorite(member.getId(), 강남역, 삼성역));
    }

    @Test
    void register() {
        FavoriteRequest request = new FavoriteRequest(1L, 2L);

        given(memberService.findById(anyLong())).willReturn(member);
        given(stationService.findById(anyLong())).willReturn(강남역).willReturn(역삼역);

        FavoriteResponse response = favoriteService.register(loginMember, request);

        assertThat(response.getSource().getId()).isEqualTo(강남역.getId());
        assertThat(response.getTarget().getId()).isEqualTo(역삼역.getId());
    }

    @Test
    void findAll() {
        given(favoriteRepository.findAllByMemberId(any())).willReturn(favorites);

        List<FavoriteResponse> result = favoriteService.findAll(loginMember);

        assertThat(result.size()).isEqualTo(favorites.size());
    }
}

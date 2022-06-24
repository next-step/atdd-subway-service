package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
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

    @Test
    void register() {
        FavoriteRequest request = new FavoriteRequest(1L, 2L);
        LoginMember loginMember = new LoginMember(1L, "weno@nextstep.com", 20);
        Member member = new Member("weno@nextstep.com", "password", 20);
        Station 강남역 = new Station(1L, "강남역");
        Station 역삼역 = new Station(2L, "역삼역");

        given(memberService.findById(anyLong())).willReturn(member);
        given(stationService.findById(anyLong())).willReturn(강남역).willReturn(역삼역);

        FavoriteResponse response = favoriteService.register(loginMember, request);

        assertThat(response.getSource().getId()).isEqualTo(강남역.getId());
        assertThat(response.getTarget().getId()).isEqualTo(역삼역.getId());
    }
}

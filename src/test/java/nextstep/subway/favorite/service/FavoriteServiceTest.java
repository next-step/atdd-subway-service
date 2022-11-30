package nextstep.subway.favorite.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.when;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {
    @Mock
    private MemberService memberService;
    @Mock
    private StationService stationService;
    @Mock
    private FavoriteRepository favoriteRepository;

    @InjectMocks
    private FavoriteService favoriteService;

    private Member member;
    private LoginMember loginMember;
    private Station departureStation;
    private Station arrivalStation;
    private Favorite favorite;

    @BeforeEach
    void setUp() {
        member = Member.of("email@email.com", "password", 20);
        loginMember = new LoginMember(1L, member.emailValue(), member.ageValue());
        departureStation = Station.from("강남역");
        arrivalStation = Station.from("잠실역");
        favorite = Favorite.of(member, departureStation, arrivalStation);
    }

    @Test
    @DisplayName("즐겨찾기를 생성한다")
    void createFavorite() {
        // given
        FavoriteRequest favoriteRequest = new FavoriteRequest(1L, 2L);
        when(memberService.findMemberById(1L)).thenReturn(member);
        when(stationService.findStationById(1L)).thenReturn(departureStation);
        when(stationService.findStationById(2L)).thenReturn(arrivalStation);
        given(favoriteRepository.save(any())).willReturn(favorite);

        // when
        Favorite actual = favoriteService.createFavorite(loginMember, favoriteRequest);

        // then
        assertNotNull(actual);
        assertThat(actual).isInstanceOf(Favorite.class);
    }
}

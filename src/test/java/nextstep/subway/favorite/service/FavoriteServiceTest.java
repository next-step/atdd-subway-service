package nextstep.subway.favorite.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.when;

import java.util.List;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.common.exception.InvalidParameterException;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
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
    @DisplayName("존재하지 않는 역을 출발역으로 즐겨찾기를 생성할 수 없다.")
    void createFavoriteByEmptyDepartureStation() {
        // given
        FavoriteRequest favoriteRequest = new FavoriteRequest(1L, 2L);
        when(memberService.findMemberById(1L)).thenReturn(member);
        when(stationService.findStationById(1L)).thenReturn(null);
        when(stationService.findStationById(2L)).thenReturn(arrivalStation);

        // when
        assertThatThrownBy(() -> favoriteService.createFavorite(loginMember, favoriteRequest))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("출발역을 확인해주세요.");
    }

    @Test
    @DisplayName("존재하지 않는 역을 도착역으로 즐겨찾기를 생성할 수 없다.")
    void createFavoriteByEmptyArrivalStation() {
        // given
        FavoriteRequest favoriteRequest = new FavoriteRequest(1L, 2L);
        when(memberService.findMemberById(1L)).thenReturn(member);
        when(stationService.findStationById(1L)).thenReturn(departureStation);
        when(stationService.findStationById(2L)).thenReturn(null);

        // when
        assertThatThrownBy(() -> favoriteService.createFavorite(loginMember, favoriteRequest))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("도착역을 확인해주세요.");
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
    
    @Test
    @DisplayName("즐겨찾기 목록을 조회한다")
    void findFavorites() {
        // given
        Favorite favorite2 = Favorite.of(member, Station.from("잠실역"), Station.from("몽촌토성역"));
        member.addFavorite(favorite);
        member.addFavorite(favorite2);
        when(memberService.findMemberById(1L)).thenReturn(member);

        // when
        List<FavoriteResponse> favoriteResponses = favoriteService.findFavoritesByMemberId(loginMember);

        assertAll(
                () -> assertThat(favoriteResponses).hasSize(2)
        );
    }
}

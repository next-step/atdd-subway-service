package nextstep.subway.favorite.application;

import static nextstep.subway.DomainFixtureFactory.createFavorite;
import static nextstep.subway.DomainFixtureFactory.createLoginMember;
import static nextstep.subway.DomainFixtureFactory.createMember;
import static nextstep.subway.DomainFixtureFactory.createStation;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import java.util.List;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    @DisplayName("즐겨찾기 저장 테스트")
    @Test
    void saveFavorite() {
        LoginMember loginMember = createLoginMember(1L, "email@email.com", 20);
        Member member = createMember(1L, "email@email.com", "password", 20);
        Station 강남역 = createStation(1L, "강남역");
        Station 정자역 = createStation(2L, "정자역");

        when(memberService.findMemberById(loginMember.getId())).thenReturn(member);
        when(stationService.findById(강남역.id())).thenReturn(강남역);
        when(stationService.findById(정자역.id())).thenReturn(정자역);
        when(favoriteRepository.save(createFavorite(member, 강남역, 정자역))).thenReturn(
                createFavorite(1L, member, 강남역, 정자역));

        FavoriteResponse favoriteResponse = favoriteService.saveFavorite(loginMember, 강남역.id(), 정자역.id());
        assertAll(
                () -> assertThat(favoriteResponse.getId()).isNotNull(),
                () -> assertThat(favoriteResponse.getSource()).isEqualTo(StationResponse.of(강남역)),
                () -> assertThat(favoriteResponse.getTarget()).isEqualTo(StationResponse.of(정자역))
        );
    }

    @DisplayName("즐겨찾기 목록 조회 테스트")
    @Test
    void findFavorites() {
        LoginMember loginMember = createLoginMember(1L, "email@email.com", 20);
        Member member = createMember(1L, "email@email.com", "password", 20);
        Station 강남역 = createStation(1L, "강남역");
        Station 정자역 = createStation(2L, "정자역");
        Favorite favorite = createFavorite(1L, member, 강남역, 정자역);
        when(favoriteRepository.findByMemberId(loginMember.getId())).thenReturn(
                Lists.newArrayList(favorite));

        List<FavoriteResponse> favorites = favoriteService.findFavorites(loginMember);
        assertThat(favorites).containsExactly(FavoriteResponse.of(favorite));
    }
}

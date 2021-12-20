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
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 10;

    private FavoriteService favoriteService;

    @Mock
    private MemberService memberService;

    @Mock
    private StationService stationService;

    @Mock
    private FavoriteRepository favoriteRepository;

    @BeforeEach
    void setUp() {
        favoriteService = new FavoriteService(stationService, favoriteRepository);
    }

    @Test
    void saveFavorite() {
        final LoginMember loginMember = LoginMember.of(EMAIL, AGE);
        lenient().when(memberService.findMemberById(anyLong())).thenReturn(Member.of(EMAIL, PASSWORD, AGE));
        when(stationService.findStationById(anyLong())).thenReturn(Station.from("잠실역"));
        when(stationService.findStationById(anyLong())).thenReturn(Station.from("잠실새내역"));
        when(favoriteRepository.save(any())).thenReturn(Favorite.of(loginMember.getId(), Station.from("잠실역").getId(), Station.from("잠실새내역").getId()));

        FavoriteResponse favoriteResponse  = favoriteService.saveFavorite(loginMember, FavoriteRequest.of(1L, 2L));

        assertThat(favoriteResponse).isNotNull();
    }

    @Test
    void findLineResponseById() {
        final LoginMember loginMember = LoginMember.of(EMAIL, AGE);
        lenient().when(memberService.findMemberById(anyLong())).thenReturn(Member.of(EMAIL, PASSWORD, AGE));
        when(favoriteRepository.findByIdAndMemberId(anyLong(), any())).thenReturn(Optional.of(Favorite.of(loginMember.getId(), Station.from(1L, "잠실역").getId(), Station.from(2L, "잠실새내역").getId())));
        when(stationService.findStationById(anyLong())).thenReturn(Station.from(1L, "잠실역"));
        when(stationService.findStationById(anyLong())).thenReturn(Station.from(2L, "잠실새내역"));
        FavoriteResponse favoriteResponse = favoriteService.findLineResponseById(loginMember, 1L);

        assertThat(favoriteResponse).isNotNull();
    }
}

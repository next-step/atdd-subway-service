package nextstep.subway.favorite.application;

import nextstep.subway.auth.dto.TokenRequest;
import nextstep.subway.auth.dto.TokenResponse;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
        favoriteService = new FavoriteService(memberService, stationService, favoriteRepository);
    }

    @Test
    void saveFavorite() {
        when(memberService.findMemberById(anyLong())).thenReturn(Member.of(EMAIL, PASSWORD, AGE));
        when(stationService.findStationById(anyLong())).thenReturn(Station.from("잠실역"));
        when(stationService.findStationById(anyLong())).thenReturn(Station.from("잠실새내역"));
        when(favoriteRepository.save(any())).thenReturn(Favorite.of(Member.of(EMAIL, PASSWORD, AGE), Station.from("잠실역"), Station.from("잠실새내역")));

        FavoriteResponse favoriteResponse  = favoriteService.saveFavorite(1L, FavoriteRequest.of(1L, 2L));

        assertThat(favoriteResponse).isNotNull();
    }

    @Test
    void findLineResponseById() {
        when(memberService.findMemberById(anyLong())).thenReturn(Member.of(EMAIL, PASSWORD, AGE));
        when(favoriteRepository.findByIdAndMember(anyLong(), any())).thenReturn(Favorite.of(Member.of(EMAIL, PASSWORD, AGE), Station.from("잠실역"), Station.from("잠실새내역")));

        FavoriteResponse favoriteResponse = favoriteService.findLineResponseById(1L, 1L);

        assertThat(favoriteResponse).isNotNull();
    }
}

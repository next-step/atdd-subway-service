package nextstep.subway.favorite.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("즐겨찾기 서비스")
@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {
    public static final Station SOURCE_STATION = Station.of(1L, "강남역");
    public static final Station TARGET_STATION = Station.of(2L, "정자역");
    public static final Member MEMBER = new Member("email@email.com", "password", 20);

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

    @DisplayName("즐겨찾기 저장")
    @Test
    void saveFavorite() {
        when(favoriteRepository.save(any(Favorite.class))).thenReturn(
                new Favorite.Builder().id(1L).source(SOURCE_STATION).target(TARGET_STATION).member(MEMBER).build());

        FavoriteResponse favoriteResponse = favoriteService.saveFavorite(
                new LoginMember(MEMBER.getId(), MEMBER.getEmail(), MEMBER.getAge()),
                new FavoriteRequest(SOURCE_STATION.getId(), TARGET_STATION.getId()));

        assertAll(() -> assertThat(favoriteResponse).isNotNull(),
                () -> assertThat(favoriteResponse.getId()).isNotNull());
    }
}

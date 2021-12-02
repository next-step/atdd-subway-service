package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class FavoriteServiceTest {

    private static final Station 강남역 = new Station("강남역");
    private static final Station 역삼역 = new Station("역삼역");
    private static final Member 회원 = new Member("email@email.com", "password", 20);

    private StationService stationService;
    private MemberService memberService;
    private FavoriteRepository favoriteRepository;
    private FavoriteService favoriteService;

    @BeforeEach
    void setUp() {
        stationService = mock(StationService.class);
        memberService = mock(MemberService.class);
        favoriteRepository = mock(FavoriteRepository.class);
        favoriteService = new FavoriteService(stationService, memberService, favoriteRepository);
    }

    @ParameterizedTest
    @CsvSource(value = "1,2,3")
    void saveFavorite(Long 회원_아이디, Long 출발역_아이디, Long 도착역_아이디) {
        // given
        given(stationService.findById(출발역_아이디)).willReturn(강남역);
        given(stationService.findById(도착역_아이디)).willReturn(역삼역);
        given(memberService.findById(회원_아이디)).willReturn(회원);
        given(favoriteRepository.save(any(Favorite.class))).willReturn(new Favorite(회원, 강남역, 역삼역));

        // when
        FavoriteResponse response = favoriteService.saveFavorite(회원_아이디, 출발역_아이디, 도착역_아이디);

        // then
        assertAll(
                () -> assertThat(response.getSource()).isEqualTo(StationResponse.of(강남역)),
                () -> assertThat(response.getTarget()).isEqualTo(StationResponse.of(역삼역))
        );
    }
}
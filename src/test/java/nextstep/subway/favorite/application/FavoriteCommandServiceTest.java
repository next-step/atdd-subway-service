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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FavoriteCommandServiceTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final int AGE = 20;

    private FavoriteCommandService favoriteCommandService;

    @Mock
    private MemberService memberService;

    @Mock
    private StationService stationService;

    @Mock
    private FavoriteRepository favoriteRepository;

    @BeforeEach
    void setUp() {
        favoriteCommandService = new FavoriteCommandService(favoriteRepository, memberService, stationService);
    }

    @DisplayName("즐겨찾기를 등록한다.")
    @Test
    void createFavorite() {
        // given
        LoginMember loginMember = new LoginMember(1L, EMAIL, AGE);
        Member member = new Member(EMAIL, PASSWORD, AGE);
        ReflectionTestUtils.setField(member, "id", 1L);
        Station 강남역 = new Station("강남역");
        ReflectionTestUtils.setField(member, "id", 1L);
        Station 삼성역 = new Station("삼성역");
        ReflectionTestUtils.setField(member, "id", 2L);
        Favorite favorite = new Favorite(강남역, 삼성역, member);
        ReflectionTestUtils.setField(favorite, "id", 1L);
        FavoriteRequest request = new FavoriteRequest("1", "2");
        when(memberService.findByEmail(EMAIL)).thenReturn(member);
        when(stationService.findById(1L)).thenReturn(강남역);
        when(stationService.findById(2L)).thenReturn(삼성역);
        when(favoriteRepository.save(any())).thenReturn(favorite);

        // when
        FavoriteResponse favoriteResponse = favoriteCommandService.createFavorite(loginMember, request);

        // then
        assertThat(favoriteResponse).isNotNull();
        assertThat(favoriteResponse.getId()).isNotNull();
        assertThat(favoriteResponse.getSource().getName()).isEqualTo(강남역.getName());
        assertThat(favoriteResponse.getTarget().getName()).isEqualTo(삼성역.getName());
    }
}

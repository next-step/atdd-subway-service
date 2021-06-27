package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FavoriteQueryServiceTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final int AGE = 20;

    private FavoriteQueryService favoriteQueryService;

    @Mock
    private FavoriteRepository favoriteRepository;

    private Station 강남역;
    private Station 삼성역;
    private Member member;
    private LoginMember loginMember;

    @BeforeEach
    void setUp() {
        favoriteQueryService = new FavoriteQueryService(favoriteRepository);
        강남역 = new Station("강남역");
        ReflectionTestUtils.setField(강남역, "id", 1L);
        삼성역 = new Station("삼성역");
        ReflectionTestUtils.setField(삼성역, "id", 2L);
        member = new Member(EMAIL, PASSWORD, AGE);
        ReflectionTestUtils.setField(member, "id", 1L);
        loginMember = new LoginMember(1L, EMAIL, AGE);
    }

    @Test
    void findFavoriteResponsesByMemberId() {
        //given
        when(favoriteRepository.findByMemberId(any())).thenReturn(Arrays.asList(new Favorite(강남역, 삼성역, member)));

        //when
        List<FavoriteResponse> actual = favoriteQueryService.findFavoriteResponsesByMemberId(loginMember);

        //then
        assertThat(actual.size()).isEqualTo(1);
        assertThat(actual.get(0).getSource().getName()).isEqualTo(강남역.getName());
        assertThat(actual.get(0).getTarget().getName()).isEqualTo(삼성역.getName());
    }
}

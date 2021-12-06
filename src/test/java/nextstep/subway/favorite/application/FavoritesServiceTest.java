package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.common.exception.ServiceException;
import nextstep.subway.favorites.application.FavoritesService;
import nextstep.subway.favorites.domain.Favorites;
import nextstep.subway.favorites.domain.FavoritesRepository;
import nextstep.subway.favorites.dto.FavoritesResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.assertj.core.api.AbstractThrowableAssert;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
public class FavoritesServiceTest {

    @Mock
    private FavoritesRepository favoritesRepository;
    @Mock
    private StationService stationService;
    @Mock
    private MemberRepository memberRepository;

    private FavoritesService favoritesService;

    @BeforeEach
    void setUp() {
        favoritesService = new FavoritesService(favoritesRepository, stationService, memberRepository);
    }

    @DisplayName("소유자가 다른 즐겨찾기는 조회 할 수 없다")
    @Test
    void testCantReadOthersFavorites() {
        // given
        Long favoritesId = 1L;
        LoginMember loginMember = new LoginMember(1L, "test", 22);
        Member owner = new Member(2L, "test", "test", 22);
        Mockito.when(favoritesRepository.findById(anyLong())).thenReturn(Optional.of(new Favorites(favoritesId, owner, new Station(), new Station())));
        // when
        ThrowableAssert.ThrowingCallable throwingCallable = () -> favoritesService.findOne(loginMember, favoritesId);
        // then
        assertThatThrownBy(throwingCallable).isInstanceOf(ServiceException.class);
    }

    @DisplayName("소유자가 다른 즐겨찾기는 삭제 할 수 없다")
    @Test
    void testCantDeleteReadOthersFavorites() {
        // given
        Long favoritesId = 1L;
        LoginMember loginMember = new LoginMember(1L, "test", 22);
        Member owner = new Member(2L, "test", "test", 22);
        Mockito.when(favoritesRepository.findById(anyLong())).thenReturn(Optional.of(new Favorites(favoritesId, owner, new Station(), new Station())));
        // when
        ThrowableAssert.ThrowingCallable throwingCallable = () -> favoritesService.deleteOne(loginMember, favoritesId);
        // then
        assertThatThrownBy(throwingCallable).isInstanceOf(ServiceException.class);
    }
}

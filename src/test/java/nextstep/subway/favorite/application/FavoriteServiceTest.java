package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {
    private static final Long TARGET = 2L;
    private static final Long SOURCE = 1L;
    private static final Long LOGIN_MEMBER_ID = 1L;

    @Mock
    private FavoriteRepository favoriteRepository;

    private FavoriteService favoriteService;

    private Favorite favorite;

    @BeforeEach
    void setUp() {
        favoriteService = new FavoriteService(favoriteRepository);
        favorite = new Favorite(1L, LOGIN_MEMBER_ID, SOURCE, TARGET);
    }

    @Test
    void saveFavorite() {
        // given
        when(favoriteRepository.save(any(Favorite.class)))
                .thenReturn(favorite);

        // when
        Favorite actual = favoriteService.saveFavorite(LOGIN_MEMBER_ID, SOURCE, TARGET);
        // then
        assertThat(actual).isNotNull();
    }
}

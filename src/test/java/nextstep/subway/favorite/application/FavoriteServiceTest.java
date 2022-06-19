package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.infrastructure.InMemoryFavoriteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FavoriteServiceTest {
    private FavoriteService favoriteService;
    private Long source;
    private Long target;

    @BeforeEach
    void setUp() {
        source = 1L;
        target = 2L;

        FavoriteRepository favoriteRepository = new InMemoryFavoriteRepository();
        favoriteService = new FavoriteService(favoriteRepository);
    }

    @Test
    void 즐겨찾기를_생성한다() {
        // given
        FavoriteRequest request = new FavoriteRequest(source, target);

        // when
        Long result = favoriteService.createFavorite(request);

        // then
        assertThat(result).isNotNull();
    }
}

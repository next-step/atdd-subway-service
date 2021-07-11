package nextstep.subway.favorite.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.station.domain.Station;

public class FavoriteServiceTest {
	@Test
	void createFavoriteTest() {
		// given
		FavoriteRepository favoriteRepository = mock(FavoriteRepository.class);
		when(favoriteRepository.save(any())).thenReturn(new Favorite(new Station("신촌역"), new Station("홍대입구역")));
		FavoriteService favoriteService = new FavoriteService(favoriteRepository);
		// when
		FavoriteResponse favoriteResponse = favoriteService.save(new FavoriteRequest(1L, 2L));
		// then
		assertThat(favoriteResponse.getSourceStationResponse().getName()).isEqualTo("신촌역");
		assertThat(favoriteResponse.getTargetStationResponse().getName()).isEqualTo("홍대입구역");
	}
}

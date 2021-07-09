package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.station.application.StationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {
	@Mock
	private FavoriteRepository favoriteRepository;
	@Mock
	private StationService stationService;

	private FavoriteService favoriteService;

	@BeforeEach
	void setUp() {
		favoriteService = new FavoriteService(favoriteRepository, stationService);
	}

	@Test
	@DisplayName("존재하지 않는 역을 등록하면 익셉션 발생")
	void createFavoriteTest() {
		when(stationService.findStationById(any())).thenThrow(RuntimeException.class);

		assertThatThrownBy(() -> favoriteService.createFavorite(new FavoriteRequest(), 1L))
				.isInstanceOf(RuntimeException.class);
	}

	@Test
	@DisplayName("자신의 계정의 id나 해당 즐겨찾기의 id가 존재하지 않으면 익셉션 발생")
	void deleteFavoriteTest() {
		when(favoriteRepository.findFavoriteByIdAndMemberId(any(), any())).thenThrow(RuntimeException.class);

		assertThatThrownBy(() -> favoriteService.deleteFavorite(1L, 1L))
				.isInstanceOf(RuntimeException.class);
	}
}
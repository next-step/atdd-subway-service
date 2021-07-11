package nextstep.subway.favorite.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

public class FavoriteServiceTest {
	@Test
	void createFavoriteTest() {
		// given
		FavoriteRepository favoriteRepository = mock(FavoriteRepository.class);
		StationService stationService = mock(StationService.class);
		when(favoriteRepository.save(any())).thenReturn(new Favorite(new Station("신촌역"), new Station("홍대입구역")));
		when(stationService.findStationById(1L)).thenReturn(new Station("신촌역"));
		when(stationService.findStationById(2L)).thenReturn(new Station("홍대입구역"));
		FavoriteService favoriteService = new FavoriteService(favoriteRepository, stationService);
		// when
		FavoriteResponse favoriteResponse = favoriteService.save(new FavoriteRequest(1L, 2L));
		// then
		assertThat(favoriteResponse.getSourceStationResponse().getName()).isEqualTo("신촌역");
		assertThat(favoriteResponse.getTargetStationResponse().getName()).isEqualTo("홍대입구역");
	}

	@Test
	void findFavoriteTest() {
		// given
		FavoriteRepository favoriteRepository = mock(FavoriteRepository.class);
		MemberRepository memberRepository = mock(MemberRepository.class);
		StationService stationService = mock(StationService.class);
		Member member = new Member("email@email.com", "password", 30);
		member.addFavorite(new Favorite(new Station("신촌역"), new Station("홍대입구역")));
		when(memberRepository.findById(any())).thenReturn(Optional.of(member));
		FavoriteService favoriteService = new FavoriteService(favoriteRepository, memberRepository, stationService);
		// when
		List<FavoriteResponse> favoriteResponses = favoriteService.findFavorites(1L);
		// then
		assertThat(favoriteResponses.get(0).getSourceStationResponse().getName()).isEqualTo("신촌역");
		assertThat(favoriteResponses.get(0).getTargetStationResponse().getName()).isEqualTo("홍대입구역");
	}
}

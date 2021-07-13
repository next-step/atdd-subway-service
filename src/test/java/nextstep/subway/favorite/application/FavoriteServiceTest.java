package nextstep.subway.favorite.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import nextstep.subway.favorite.domain.Favorite;
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
		MemberRepository memberRepository = mock(MemberRepository.class);
		StationService stationService = mock(StationService.class);
		when(memberRepository.findById(1L)).thenReturn(Optional.of(new Member("email@email.com", "password", 30)));
		Member member = new Member();
		member.addFavorite(new Favorite(new Station("신촌역"), new Station("홍대입구역")));
		when(memberRepository.save(any())).thenReturn(member);
		when(stationService.findStationById(1L)).thenReturn(new Station("신촌역"));
		when(stationService.findStationById(2L)).thenReturn(new Station("홍대입구역"));
		FavoriteService favoriteService = new FavoriteService(memberRepository, stationService);
		// when
		FavoriteResponse favoriteResponse = favoriteService.save(1L, new FavoriteRequest(1L, 2L));
		// then
		assertThat(favoriteResponse.getSourceStationResponse().getName()).isEqualTo("신촌역");
		assertThat(favoriteResponse.getTargetStationResponse().getName()).isEqualTo("홍대입구역");
	}

	@Test
	void findFavoriteTest() {
		// given
		MemberRepository memberRepository = mock(MemberRepository.class);
		StationService stationService = mock(StationService.class);
		Member member = new Member("email@email.com", "password", 30);
		member.addFavorite(new Favorite(new Station("신촌역"), new Station("홍대입구역")));
		when(memberRepository.findById(any())).thenReturn(Optional.of(member));
		FavoriteService favoriteService = new FavoriteService(memberRepository, stationService);
		// when
		List<FavoriteResponse> favoriteResponses = favoriteService.find(1L);
		// then
		assertThat(favoriteResponses.get(0).getSourceStationResponse().getName()).isEqualTo("신촌역");
		assertThat(favoriteResponses.get(0).getTargetStationResponse().getName()).isEqualTo("홍대입구역");
	}
}

package nextstep.subway.favorite.application;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

@ExtendWith(SpringExtension.class)
class FavoriteServiceTest {

	@MockBean
	private StationService stationService;
	@MockBean
	private MemberRepository memberRepository;
	@MockBean
	private FavoriteRepository favoriteRepository;

	@DisplayName("즐겨찾기 추가")
	@Test
	void testCreateFavorite() {
		FavoriteService service = new FavoriteService(stationService, memberRepository, favoriteRepository);
		Member member = new Member("email@email.com", "password", 20);
		Mockito.when(this.memberRepository.findById(Mockito.eq(1L)))
			.thenReturn(Optional.of(member));
		Station 강남역 = new Station("강남역");
		Mockito.when(this.stationService.findById(Mockito.eq(1L))).thenReturn(강남역);
		Station 광교역 = new Station("광교역");
		Mockito.when(this.stationService.findById(Mockito.eq(2L))).thenReturn(광교역);

		Favorite favorite = new Favorite(member, 강남역, 광교역);
		Mockito.when(this.favoriteRepository.save(Mockito.any())).thenReturn(favorite);

		Favorite actual = service.createFavorite(1L, new FavoriteRequest(1L, 2L));
		Assertions.assertThat(actual.getSource()).isEqualTo(강남역);
		Assertions.assertThat(actual.getTarget()).isEqualTo(광교역);
	}

	@DisplayName("즐겨찾기 목록 반환")
	@Test
	void testFindFavorites() {
		FavoriteService service = new FavoriteService(stationService, memberRepository, favoriteRepository);
		Member member = new Member("email@email.com", "password", 20);
		Station 광교역 = new Station("광교역");
		Station 강남역 = new Station("강남역");
		Favorite favorite = new Favorite(member, 강남역, 광교역);
		ReflectionTestUtils.setField(favorite, "id", 99L);
		member.addFavorite(favorite);

		Mockito.when(this.memberRepository.findById(Mockito.eq(1L)))
			.thenReturn(Optional.of(member));

		List<FavoriteResponse> favorites = service.findFavorites(1L);
		Assertions.assertThat(favorites.stream().map(FavoriteResponse::getId).collect(Collectors.toList()))
			.containsExactly(99L);
	}
}
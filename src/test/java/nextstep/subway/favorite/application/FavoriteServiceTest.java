package nextstep.subway.favorite.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

/**
 * @author : byungkyu
 * @date : 2021/01/18
 * @description :
 **/
@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {
	@Mock
	private FavoriteRepository favoriteRepository;
	@Mock
	private StationService stationService;
	@Mock
	private MemberService memberService;

	@DisplayName("즐겨찾기 생성")
	@Test
	void save() {
		// given
		LoginMember loginMember = mock(LoginMember.class);
		when(loginMember.getId()).thenReturn(1L);

		Station 강남역 = mock(Station.class);
		Station 양재역 = mock(Station.class);
		when(강남역.getId()).thenReturn(1L);
		when(양재역.getId()).thenReturn(2L);

		Member dummyMember = new Member(MemberAcceptanceTest.EMAIL, MemberAcceptanceTest.PASSWORD,MemberAcceptanceTest.AGE);
		FavoriteRequest favoriteRequest = new FavoriteRequest(강남역.getId(), 양재역.getId());

		Favorite saveFavoriteEntity = Favorite.builder()
			.member(dummyMember)
			.source(강남역)
			.target(양재역)
			.build();

		Favorite saveFavoriteResultExpected = mock(Favorite.class);
		when(saveFavoriteResultExpected.getId()).thenReturn(1L);
		when(saveFavoriteResultExpected.getSource()).thenReturn(강남역);
		when(saveFavoriteResultExpected.getTarget()).thenReturn(양재역);

		when(memberService.findMemberById(loginMember.getId())).thenReturn(dummyMember);
		when(stationService.findStationById(favoriteRequest.getSource())).thenReturn(강남역);
		when(stationService.findStationById(favoriteRequest.getTarget())).thenReturn(양재역);
		when(favoriteRepository.save(saveFavoriteEntity)).thenReturn(saveFavoriteResultExpected);
		FavoriteService favoriteService = new FavoriteService(favoriteRepository, stationService, memberService);

		// when
		FavoriteResponse response = favoriteService.save(loginMember, favoriteRequest);
		// then
		assertThat(response.getId()).isNotNull();
	}

	@DisplayName("즐겨찾기 조회")
	@Test
	void findFavoritesOfMine() {
		// given
		Station 강남역 = mock(Station.class);
		Station 양재역 = mock(Station.class);
		when(강남역.getId()).thenReturn(1L);
		when(양재역.getId()).thenReturn(2L);

		Favorite dummyFavorite = mock(Favorite.class);
		when(dummyFavorite.getId()).thenReturn(1L);
		when(dummyFavorite.getSource()).thenReturn(강남역);
		when(dummyFavorite.getTarget()).thenReturn(양재역);

		Member dummyMember = mock(Member.class);
		when(dummyMember.getId()).thenReturn(1L);
		when(dummyMember.getFavorites()).thenReturn(Arrays.asList(dummyFavorite));

		when(memberService.findMemberById(dummyMember.getId())).thenReturn(dummyMember);
		FavoriteService favoriteService = new FavoriteService(favoriteRepository, stationService, memberService);

		// when
		List<FavoriteResponse> favoriteResponses = favoriteService.findFavoritesByMemberId(dummyMember.getId());

		//then
		assertThat(favoriteResponses).containsExactly(new FavoriteResponse(dummyFavorite));
	}

	@DisplayName("즐겨찾기 삭제")
	@Test
	void deleteFavorite() {
		// given
		Favorite dummyFavorite = mock(Favorite.class);
		when(dummyFavorite.getId()).thenReturn(1L);

		Member dummyMember = mock(Member.class);
		when(dummyMember.getId()).thenReturn(1L);
		dummyMember.addFavorite(dummyFavorite);

		Station 강남역 = mock(Station.class);
		Station 양재역 = mock(Station.class);

		when(memberService.findMemberById(dummyMember.getId())).thenReturn(dummyMember);
		FavoriteService favoriteService = new FavoriteService(favoriteRepository, stationService, memberService);

		// when
		favoriteService.deleteFavorite(dummyMember.getId(), dummyFavorite.getId());

		// then
		assertThat(dummyMember.getFavorites()).hasSize(0);
	}
}
package nextstep.subway.favorite.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

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
	void save(){
		// given
		LoginMember loginMember = new LoginMember(1L, MemberAcceptanceTest.EMAIL, MemberAcceptanceTest.AGE);
		Member dummyMember = new Member(1L, MemberAcceptanceTest.EMAIL, MemberAcceptanceTest.PASSWORD, MemberAcceptanceTest.AGE);
		Station 강남역 = new Station(1L, "강남역");
		Station 양재역 = new Station(2L, "양재역");
		FavoriteRequest favoriteRequest = new FavoriteRequest(강남역.getId(), 양재역.getId());
		Favorite saveFavoriteEntity = new Favorite(dummyMember, 강남역, 양재역);

		when(memberService.findMemberById(loginMember.getId())).thenReturn(dummyMember);
		when(stationService.findStationById(favoriteRequest.getSource())).thenReturn(강남역);
		when(stationService.findStationById(favoriteRequest.getTarget())).thenReturn(양재역);
		when(favoriteRepository.save(saveFavoriteEntity)).thenReturn(new Favorite(1L,dummyMember, 강남역, 양재역));
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

		Member dummyMember = new Member(1L, MemberAcceptanceTest.EMAIL, MemberAcceptanceTest.PASSWORD, MemberAcceptanceTest.AGE);
		Station 강남역 = new Station(1L, "강남역");
		Station 양재역 = new Station(2L, "양재역");
		Favorite dummyFavorite = new Favorite(1L, dummyMember, 강남역, 양재역);
		dummyMember.addFavorite(dummyFavorite);

		when(memberService.findMemberById(dummyMember.getId())).thenReturn(dummyMember);
		FavoriteService favoriteService = new FavoriteService(favoriteRepository, stationService, memberService);

		// when
		List<FavoriteResponse> favoriteResponses = favoriteService.findFavoritesByMemberId(dummyMember.getId());

		//then
		assertThat(favoriteResponses).containsExactly(new FavoriteResponse(dummyFavorite));
	}


}
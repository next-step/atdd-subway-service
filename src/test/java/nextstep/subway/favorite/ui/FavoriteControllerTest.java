package nextstep.subway.favorite.ui;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.application.FavoriteService;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.MemberAcceptanceTest;
import nextstep.subway.station.domain.Station;

/**
 * @author : byungkyu
 * @date : 2021/01/18
 * @description :
 **/
@ExtendWith(MockitoExtension.class)
public class FavoriteControllerTest {
	@Mock
	FavoriteService favoriteService;

	@DisplayName("즐겨찾기 생성")
	@Test
	void createFavorite(){
		//given
		Station 강남역 = mock(Station.class);
		Station 양재역 = mock(Station.class);
		when(강남역.getId()).thenReturn(1L);
		when(양재역.getId()).thenReturn(2L);

		LoginMember loginMember = new LoginMember(1L, MemberAcceptanceTest.EMAIL, MemberAcceptanceTest.AGE);
		FavoriteRequest request = new FavoriteRequest(강남역.getId(), 양재역.getId());

		Favorite dummyFavorite = mock(Favorite.class);
		when(dummyFavorite.getId()).thenReturn(1L);
		when(dummyFavorite.getSource()).thenReturn(강남역);
		when(dummyFavorite.getTarget()).thenReturn(양재역);

		FavoriteResponse favoriteResponse = new FavoriteResponse(dummyFavorite);

		when(favoriteService.save(loginMember, request)).thenReturn(favoriteResponse);
		FavoriteController favoriteController = new FavoriteController(favoriteService);

		// when
		ResponseEntity response = favoriteController.createFavorite(loginMember, request);

		// then
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
	}

	@DisplayName("즐겨찾기 조회")
	@Test
	void findFavoritesOfMine(){
		//given
		Station 강남역 = mock(Station.class);
		Station 양재역 = mock(Station.class);
		when(강남역.getId()).thenReturn(1L);
		when(양재역.getId()).thenReturn(2L);

		LoginMember loginMember = mock(LoginMember.class);
		when(loginMember.getId()).thenReturn(1L);

		Favorite dummyFavorite = mock(Favorite.class);
		when(dummyFavorite.getId()).thenReturn(1L);
		when(dummyFavorite.getSource()).thenReturn(강남역);
		when(dummyFavorite.getTarget()).thenReturn(양재역);

		List<FavoriteResponse> favoriteResponses = Arrays.asList(new FavoriteResponse(dummyFavorite));

		when(favoriteService.findFavoritesByMemberId(loginMember.getId())).thenReturn(favoriteResponses);
		FavoriteController favoriteController = new FavoriteController(favoriteService);

		// when
		ResponseEntity response = favoriteController.findFavoritesOfMine(loginMember);

		// then
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@DisplayName("즐겨찾기 삭제")
	@Test
	void deleteFavorite(){
		//given
		LoginMember loginMember = new LoginMember(1L, MemberAcceptanceTest.EMAIL, MemberAcceptanceTest.AGE);
		Long favoriteId = 1L;

		FavoriteController favoriteController = new FavoriteController(favoriteService);

		//when
		ResponseEntity response = favoriteController.deleteFavorite(loginMember, favoriteId);

		//then
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
	}

}

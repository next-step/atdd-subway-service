package nextstep.subway.favorite.ui;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.application.FavoriteService;
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
		Station 강남역 = new Station(1L, "강남역");
		Station 양재역 = new Station(2L, "양재역");

		LoginMember loginMember = new LoginMember(1L, MemberAcceptanceTest.EMAIL, MemberAcceptanceTest.AGE);
		FavoriteRequest request = new FavoriteRequest(강남역.getId(), 양재역.getId());
		when(favoriteService.save(loginMember, request)).thenReturn(new FavoriteResponse(1L));
		FavoriteController favoriteController = new FavoriteController(favoriteService);

		// when
		ResponseEntity response = favoriteController.save(loginMember, request);

		// then
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
	}
}

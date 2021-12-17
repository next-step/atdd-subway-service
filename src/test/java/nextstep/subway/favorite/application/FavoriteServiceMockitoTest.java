package nextstep.subway.favorite.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.OngoingStubbing;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.exception.AuthorizationException;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.dto.MemberResponse;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

@DisplayName("MockitoExtension을 사용한 FavoriteServiceTest")
@ExtendWith(MockitoExtension.class)
class FavoriteServiceMockitoTest {
	private static final String EMAIL = "wooahan@naver.com";
	private static final String PASSWORD = "password";
	private static final Integer AGE = 33;

	private static Station 두정역 = new Station(1L, "두정역");
	private static Station 봉명역 = new Station(3L, "봉명역");
	private static Member 사용자 = new Member(1L,EMAIL, PASSWORD, AGE);
	private static Favorite 두정_TO_봉명 = new Favorite(사용자, 두정역, 봉명역);

	@Mock
	private MemberService memberService;

	@Mock
	private StationService stationService;

	@Mock
	private FavoriteRepository favoriteRepository;

	@Test
	@DisplayName("즐겨찾기 저장 테스트")
	public void saveTest() {
		//given
		when(memberService.findByEmail(EMAIL)).thenReturn(사용자);
		when(stationService.findById(1L)).thenReturn(두정역);
		when(stationService.findById(3L)).thenReturn(봉명역);
		when(favoriteRepository.save(두정_TO_봉명)).thenReturn(두정_TO_봉명);
		FavoriteService favoriteService = new FavoriteService(memberService, stationService, favoriteRepository);

		//when
		LoginMember loginMember = new LoginMember(1L, EMAIL, AGE);
		FavoriteRequest favoriteRequest = new FavoriteRequest(1L, 3L);
		Favorite saveFavorite = favoriteService.save(loginMember, favoriteRequest);

		//then
		assertThat(saveFavorite).isEqualTo(두정_TO_봉명);
	}

	@Test
	@DisplayName("즐겨찾기 목록 조회 테스트")
	public void findFavoritesTest() {
		//given
		when(memberService.findByEmail(EMAIL)).thenReturn(사용자);
		when(favoriteRepository.findFavoritesByMember(사용자)).thenReturn(Lists.newArrayList(두정_TO_봉명));
		FavoriteService favoriteService = new FavoriteService(memberService, stationService, favoriteRepository);

		//when
		LoginMember loginMember = new LoginMember(1L, EMAIL, AGE);
		List<FavoriteResponse> favoriteResponses = favoriteService.findFavoriteResponses(loginMember);

		//then
		assertThat(favoriteResponses.get(0).getSource()).isEqualTo(StationResponse.of(두정역));
		assertThat(favoriteResponses.get(0).getTarget()).isEqualTo(StationResponse.of(봉명역));
	}

	@Test
	@DisplayName("즐겨찾기 삭제 성공 테스트")
	public void deleteFavoritesSuccessTest() {
		//given
		when(favoriteRepository.findById(1L)).thenReturn(Optional.of(두정_TO_봉명));
		FavoriteService favoriteService = new FavoriteService(memberService, stationService, favoriteRepository);

		//when
		LoginMember loginMember = new LoginMember(1L, EMAIL, AGE);
		favoriteService.delete(loginMember, 1L);
	}

	@Test
	@DisplayName("즐겨찾기 삭제 실패 테스트")
	public void deleteFavoritesFailTest() {
		//given
		when(favoriteRepository.findById(1L)).thenReturn(Optional.of(두정_TO_봉명));
		FavoriteService favoriteService = new FavoriteService(memberService, stationService, favoriteRepository);

		//when
		LoginMember loginMember = new LoginMember(2L, EMAIL, AGE);
		//then
		assertThatThrownBy(() -> favoriteService.delete(loginMember, 1L))
			.isInstanceOf(AuthorizationException.class)
			.hasMessage("해당 즐겨찾기를 삭제 할 수 없습니다.");
	}

}


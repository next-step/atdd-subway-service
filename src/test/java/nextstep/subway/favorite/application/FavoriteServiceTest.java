package nextstep.subway.favorite.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import nextstep.subway.BaseTest;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.dto.StationRequest;
import nextstep.subway.station.dto.StationResponse;

class FavoriteServiceTest extends BaseTest {

	@Autowired
	private MemberService memberService;

	@Autowired
	private StationService stationService;

	@Autowired
	private FavoriteService favoriteService;

	@Autowired
	private FavoriteRepository favoriteRepository;

	private MemberResponse 사용자;
	private StationResponse 강남역;
	private StationResponse 교대역;
	private StationResponse 양재역;
	private FavoriteResponse 즐겨찾기_예제1;
	private FavoriteResponse 즐겨찾기_예제2;

	@BeforeEach
	public void setUp() {
		super.setUp();
		사용자 = memberService.createMember(new MemberRequest("test@test.com", "testpass1234", 20));
		강남역 = stationService.saveStation(new StationRequest("강남역"));
		교대역 = stationService.saveStation(new StationRequest("교대역"));
		양재역 = stationService.saveStation(new StationRequest("양재역"));

		즐겨찾기_예제1 = favoriteService.createFavorite(사용자.getId(), FavoriteRequest.of(
			강남역.getId(),
			양재역.getId()
		));

		즐겨찾기_예제2 = favoriteService.createFavorite(사용자.getId(), FavoriteRequest.of(
			교대역.getId(),
			양재역.getId()
		));
	}

	@DisplayName("createFavorite메소드는 즐겨찾기 정보를 저장할 수 있다.")
	@Test
	void createFavorite() {
		FavoriteResponse favoriteResponse = favoriteService.createFavorite(사용자.getId(), FavoriteRequest.of(
			강남역.getId(),
			교대역.getId()
		));

		Favorite favorite = favoriteRepository.findById(favoriteResponse.getId())
			.orElse(null);

		assertAll(
			() -> assertThat(favoriteResponse.getMember().getEmail()).isEqualTo(favorite.getMember().getEmail()),
			() -> assertThat(favoriteResponse.getSource().getName()).isEqualTo(favorite.getSource().getName()),
			() -> assertThat(favoriteResponse.getTarget().getName()).isEqualTo(favorite.getTarget().getName())
		);
	}

	@DisplayName("deleteFavorite메소드는 즐겨찾기 정보를 삭제할 수 있다.")
	@Test
	void deleteFavorite() {

		favoriteService.deleteFavorite(즐겨찾기_예제1.getId());

		Favorite favorite = favoriteRepository.findById(즐겨찾기_예제1.getId())
			.orElse(null);

		assertThat(favorite).isNull();
	}

	@DisplayName("getFavoritesByMemberId메소드는 즐겨찾기 정보를 조회할 수 있다.")
	@Test
	void getFavoritesByMemberId() {

		List<FavoriteResponse> favorites = favoriteService.getFavoritesByMemberId(사용자.getId());
		List<Long> favoriteIds = favorites.stream()
			.map(FavoriteResponse::getId)
			.collect(Collectors.toList());
		assertAll(
			() -> assertThat(favorites).hasSize(2),
			() -> assertThat(favoriteIds).containsExactlyElementsOf(Arrays.asList(즐겨찾기_예제1.getId(), 즐겨찾기_예제2.getId()))
		);
	}
}
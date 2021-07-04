package nextstep.subway.favorite.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

@Service
public class FavoriteService {
	private FavoriteRepository favoriteRepository;
	private MemberService memberService;
	private StationService stationService;

	public FavoriteService(FavoriteRepository favoriteRepository, MemberService memberService, StationService stationService) {
		this.favoriteRepository = favoriteRepository;
		this.memberService = memberService;
		this.stationService = stationService;
	}

	public FavoriteResponse createFavorite(Long memberId, FavoriteRequest favoriteRequest) {
		Member member = memberService.findMemberById(memberId);
		Station source = stationService.findStationById(favoriteRequest.getSource());
		Station target = stationService.findStationById(favoriteRequest.getTarget());

		Favorite favorite = favoriteRepository.save(favoriteRequest.toFavorite(member, source, target));

		return FavoriteResponse.of(favorite);
	}

	public List<FavoriteResponse> findFavorites(Long memberId) {
		List<Favorite> favorites = favoriteRepository.findByMemberId(memberId).orElseThrow(() -> new IllegalArgumentException("등록된 즐겨찾기가 없습니다."));

		return favorites.stream().map(FavoriteResponse::of).collect(Collectors.toList());
	}
}

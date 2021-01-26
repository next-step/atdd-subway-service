package nextstep.subway.favorite.application;

import java.util.List;

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
	private final FavoriteRepository favoriteRepository;
	private final MemberService memberService;
	private final StationService stationService;

	public FavoriteService(
		FavoriteRepository favoriteRepository,
		MemberService memberService,
		StationService stationService) {
		this.favoriteRepository = favoriteRepository;
		this.memberService = memberService;
		this.stationService = stationService;
	}

	public FavoriteResponse saveFavorite(Long memberId, FavoriteRequest favoriteRequest) {
		Member member = memberService.findMemberById(memberId);
		Station source = stationService.findStationById(favoriteRequest.getSource());
		Station target = stationService.findStationById(favoriteRequest.getTarget());

		Favorite savedFavorite = favoriteRepository.save(new Favorite(member, source, target));

		return FavoriteResponse.of(savedFavorite);
	}

	public List<FavoriteResponse> findAllFavoritesByMember(Long memberId) {
		List<Favorite> favorites = favoriteRepository.findAllByMemberId(memberId);

		return FavoriteResponse.of(favorites);
	}

	public void deleteFavorite(Long id) {
		favoriteRepository.deleteById(id);
	}
}

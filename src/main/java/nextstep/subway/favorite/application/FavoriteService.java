package nextstep.subway.favorite.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class FavoriteService {
	private final FavoriteRepository favoriteRepository;
	private final StationService stationService;
	private final MemberService memberService;

	@Transactional
	public FavoriteResponse createFavorite(Long memberId, FavoriteRequest request) {
		Station source = stationService.findById(request.getSource());
		Station target = stationService.findById(request.getTarget());
		Member member = memberService.findById(memberId);
		return FavoriteResponse.of(favoriteRepository.save(Favorite.create(member, source, target)));
	}

	@Transactional
	public void deleteFavorite(long favoriteId) {
		favoriteRepository.deleteById(favoriteId);
	}

	public List<FavoriteResponse> getFavoritesByMemberId(Long memberId) {
		List<Favorite> favorites = favoriteRepository.findAllByMemberId(memberId);
		return favorites.stream()
			.map(FavoriteResponse::of)
			.collect(Collectors.toList());
	}
}

package nextstep.subway.favorite.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class FavoriteService {

	private final MemberService memberService;
	private final StationService stationService;
	private final FavoriteRepository favoriteRepository;

	public FavoriteResponse createFavorite(Long loginMemberId, FavoriteRequest request) {
		Member member = memberService.findMemberById(loginMemberId);

		Station source = stationService.findStationById(request.getSource());
		Station target = stationService.findStationById(request.getTarget());

		Favorite favorite = favoriteRepository.save(new Favorite(member, source, target));
		return FavoriteResponse.of(favorite);
	}

	public List<FavoriteResponse> findFavorites(Long id) {
		return null;
	}
}

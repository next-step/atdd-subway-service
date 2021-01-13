package nextstep.subway.favorite.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import nextstep.subway.common.exception.NothingException;
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

	@Transactional
	public FavoriteResponse createFavorite(Long loginMemberId, FavoriteRequest request) {
		Member member = memberService.findMemberById(loginMemberId);

		Station source = stationService.findStationById(request.getSource());
		Station target = stationService.findStationById(request.getTarget());

		Favorite persistFavorite = favoriteRepository.save(new Favorite(member, source, target));
		return FavoriteResponse.of(persistFavorite);
	}

	public List<FavoriteResponse> findFavorites(Long loginMemberId) {
		Member member = memberService.findMemberById(loginMemberId);
		List<Favorite> persistFavorites = favoriteRepository.findAllByMember(member);
		return persistFavorites.stream()
			.map(FavoriteResponse::of)
			.collect(Collectors.toList());
	}

	@Transactional
	public void deleteFavoriteById(long id) {
		Favorite favorite = findFavoriteById(id);
		favoriteRepository.delete(favorite);
	}

	private Favorite findFavoriteById(Long id) {
		return favoriteRepository.findById(id).orElseThrow(NothingException::new);
	}
}

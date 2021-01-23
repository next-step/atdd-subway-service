package nextstep.subway.favorite.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

@Service
@Transactional
public class FavoriteService {
	private final FavoriteRepository favoriteRepository;
	private final StationService stationService;
	private final MemberService memberService;

	public FavoriteService(FavoriteRepository favoriteRepository, StationService stationService,
		MemberService memberService) {
		this.favoriteRepository = favoriteRepository;
		this.stationService = stationService;
		this.memberService = memberService;
	}

	public FavoriteResponse createFavorite(LoginMember loginMember, FavoriteRequest request) {
		Member member = memberService.findById(loginMember.getId());
		Station source = stationService.findById(request.getSource());
		Station target = stationService.findById(request.getTarget());
		Favorite persistFavorite = favoriteRepository.save(new Favorite(member, source, target));
		return FavoriteResponse.from(persistFavorite);
	}

	public List<FavoriteResponse> findFavorites(LoginMember loginMember) {
		return favoriteRepository.findAllByMemberId(loginMember.getId())
			.stream()
			.map(FavoriteResponse::from)
			.collect(Collectors.toList());
	}

	public void deleteFavorite(LoginMember loginMember, Long id) {
		favoriteRepository.deleteByIdAndMemberId(id, loginMember.getId());
	}
}

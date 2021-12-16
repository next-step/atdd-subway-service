package nextstep.subway.favorite.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.exception.AuthorizationException;
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
	private final MemberService memberService;
	private final StationService stationService;
	private final FavoriteRepository favoriteRepository;

	public FavoriteService(MemberService memberService, StationService stationService,
		FavoriteRepository favoriteRepository) {
		this.memberService = memberService;
		this.stationService = stationService;
		this.favoriteRepository = favoriteRepository;
	}

	public Favorite save(LoginMember loginMember, FavoriteRequest favoriteRequest) {
		Member findMember = findByMember(loginMember);
		Station source = stationService.findById(favoriteRequest.getSource());
		Station target = stationService.findById(favoriteRequest.getTarget());
		return favoriteRepository.save(new Favorite(findMember, source, target));
	}

	public List<FavoriteResponse> findFavoriteResponses(LoginMember loginMember) {
		Member findMember = findByMember(loginMember);
		List<Favorite> favorites = favoriteRepository.findFavoritesByMember(findMember);
		return extractFavoriteResponses(favorites);
	}

	public void delete(LoginMember loginMember, Long favoriteId) {
		Favorite favorite = findFavoriteById(favoriteId);
		validateAuthorization(loginMember, favoriteId);
		favoriteRepository.delete(favorite);
	}

	private void validateAuthorization(LoginMember loginMember, Long favoriteId) {
		if (!loginMember.isDelete(favoriteId)) {
			throw new AuthorizationException("해당 즐겨찾기를 삭제 할 수 없습니다.");
		}
	}

	private List<FavoriteResponse> extractFavoriteResponses(List<Favorite> favorites) {
		return favorites.stream()
			.map(FavoriteResponse::of)
			.collect(Collectors.toList());
	}

	private Favorite findFavoriteById(Long id) {
		return favoriteRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 즐겨찾기입니다."));
	}

	private Member findByMember(LoginMember loginMember) {
		return memberService.findByEmail(loginMember.getEmail());
	}
}

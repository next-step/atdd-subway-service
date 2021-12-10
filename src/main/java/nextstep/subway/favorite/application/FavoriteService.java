package nextstep.subway.favorite.application;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

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
public class FavoriteService {

	private static final String ERROR_MESSAGE_NOT_EXIST_FAVORITE = "없는 즐겨찾기 입니다.";
	private static final String ERROR_MESSAGE_MEMBER_NOT_OWN_FAVORITE = "잘못된 접근입니다.";

	private final FavoriteRepository favoriteRepository;
	private final MemberService memberService;
	private final StationService stationService;

	public FavoriteService(FavoriteRepository favoriteRepository,
		MemberService memberService, StationService stationService) {
		this.favoriteRepository = favoriteRepository;
		this.memberService = memberService;
		this.stationService = stationService;
	}

	public Favorite saveFavorite(LoginMember loginMember, FavoriteRequest favoriteRequest) {
		Member member = memberService.findMember(loginMember.getId());
		Station source = stationService.findStationById(favoriteRequest.getSource());
		Station target = stationService.findStationById(favoriteRequest.getTarget());

		Favorite favorite = new Favorite(member, source, target);
		Favorite saveFavorite = favoriteRepository.save(favorite);

		return saveFavorite;
	}

	public List<Favorite> findFavorites(LoginMember loginMember) {
		return favoriteRepository.findWithStationsByMember(loginMember.getId());
	}

	public List<FavoriteResponse> findFavoriteResponses(LoginMember loginMember) {
		return findFavorites(loginMember)
			.stream()
			.map(FavoriteResponse::from)
			.collect(Collectors.toList());
	}

	public void deleteFavorite(LoginMember loginMember, Long favoriteId) {
		Member findMember = memberService.findMember(loginMember.getId());
		Favorite findFavorite = favoriteRepository.findById(favoriteId).orElseThrow(
			() -> new IllegalStateException(ERROR_MESSAGE_NOT_EXIST_FAVORITE));
		validateMemberOwnFavorite(findFavorite, findMember);
		favoriteRepository.delete(findFavorite);
	}

	private void validateMemberOwnFavorite(Favorite favorite, Member member) {
		if (!favorite.getMember().equals(member)) {
			throw new IllegalStateException(ERROR_MESSAGE_MEMBER_NOT_OWN_FAVORITE);
		}
	}
}
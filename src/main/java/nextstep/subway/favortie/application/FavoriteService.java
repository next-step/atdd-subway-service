package nextstep.subway.favortie.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.exception.AppException;
import nextstep.subway.exception.ErrorCode;
import nextstep.subway.favortie.domain.Favorite;
import nextstep.subway.favortie.domain.FavoriteRepository;
import nextstep.subway.favortie.dto.FavoriteRequest;
import nextstep.subway.favortie.dto.FavoriteResponse;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

@Service
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

	public List<FavoriteResponse> getAll(LoginMember loginMember) {
		Member member = memberService.getById(loginMember.getId());
		List<Favorite> favorites = favoriteRepository.findByMember(member);
		return favorites.stream().map(FavoriteResponse::of)
			.collect(Collectors.toList());
	}

	public FavoriteResponse create(LoginMember loginMember, FavoriteRequest request) {
		Member member = memberService.getById(loginMember.getId());
		Station source = stationService.findById(request.getSource());
		Station target = stationService.findById(request.getTarget());

		Favorite favorite = Favorite.of(member, source, target);
		favorite = favoriteRepository.save(favorite);
		return FavoriteResponse.of(favorite);
	}

	public void delete(LoginMember loginMember, Long favoriteId) {
		Member loggedMember = memberService.getById(loginMember.getId());
		Favorite favorite = findById(favoriteId);
		if (!favorite.isSameMember(loggedMember)) {
			throw new AppException(ErrorCode.WRONG_INPUT, "즐겨찾기 유저와 로그인 유저 정보가 다릅니다");
		}
		favoriteRepository.delete(favorite);
	}

	public Favorite findById(Long id) {
		return favoriteRepository.findById(id)
			.orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND, "favorite.id:{}", id));
	}

}

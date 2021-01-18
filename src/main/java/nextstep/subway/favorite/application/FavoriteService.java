package nextstep.subway.favorite.application;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

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

/**
 * @author : byungkyu
 * @date : 2021/01/18
 * @description :
 **/
@Service
@Transactional
public class FavoriteService {
	private FavoriteRepository favoriteRepository;
	private StationService stationService;
	private MemberService memberService;

	public FavoriteService(FavoriteRepository favoriteRepository,
		StationService stationService,
		MemberService memberService) {
		this.favoriteRepository = favoriteRepository;
		this.stationService = stationService;
		this.memberService = memberService;
	}

	public FavoriteResponse save(LoginMember loginMember, FavoriteRequest request) {
		Member member = memberService.findMemberById(loginMember.getId());
		Station sourceStation = stationService.findStationById(request.getSource());
		Station targetStation = stationService.findStationById(request.getTarget());

		Favorite saved = favoriteRepository.save(new Favorite(member, sourceStation, targetStation));
		return FavoriteResponse.of(saved);
	}

	public List<FavoriteResponse> findFavoritesByMemberId(Long memberId) {
		Member member = memberService.findMemberById(memberId);
		return member.getFavorites().stream()
			.map(FavoriteResponse::of)
			.collect(Collectors.toList());
	}

	public void deleteFavorite(Long memberId, Long favoriteId) {
		Member member = memberService.findMemberById(memberId);
		member.removeFavorite(favoriteId);
	}
}

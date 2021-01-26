package nextstep.subway.favorite.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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

	public FavoriteResponse createFavorite(Long memberId, FavoriteRequest request) {
		Member member = memberService.findById(memberId);
		Station source = stationService.findById(request.getSource());
		Station target = stationService.findById(request.getTarget());
		Favorite persistFavorite = favoriteRepository.save(new Favorite(member, source, target));
		return FavoriteResponse.from(persistFavorite);
	}

	@Transactional(readOnly = true)
	public List<FavoriteResponse> findFavorites(Long memberId) {
		return favoriteRepository.findAllByMemberId(memberId)
			.stream()
			.map(FavoriteResponse::from)
			.collect(Collectors.toList());
	}

	public void deleteFavorite(Long memberId, Long id) {
		Favorite favorite = favoriteRepository.findById(id)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "주어진 id의 즐겨찾기가 없습니다."));
		if(favorite.haveNoPermissionToDeleteByMemberId(memberId)){
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "주어진 id의 즐겨찾기를 삭제할 권한이 없습니다.");
		}

		favoriteRepository.delete(favorite);
	}
}

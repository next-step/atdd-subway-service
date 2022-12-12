package nextstep.subway.Favorite.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.Favorite.domain.Favorite;
import nextstep.subway.Favorite.domain.FavoriteRepository;
import nextstep.subway.Favorite.dto.FavoriteRequest;
import nextstep.subway.Favorite.dto.FavoritesResponse;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.common.exception.InvalidDataException;
import nextstep.subway.common.exception.NotFoundException;
import nextstep.subway.path.application.PathService;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

@Service
public class FavoriteService {

	private final PathService pathService;
	private final StationService stationService;
	private final FavoriteRepository favoriteRepository;

	public FavoriteService(PathService pathService, StationService stationService,
		FavoriteRepository favoriteRepository) {
		this.pathService = pathService;
		this.stationService = stationService;
		this.favoriteRepository = favoriteRepository;
	}

	@Transactional
	public FavoritesResponse saveFavorite(LoginMember loginMember, FavoriteRequest request) {
		Station source = stationService.findById(request.getSource());
		Station target = stationService.findById(request.getTarget());
		validate(source, target, loginMember.getId());
		Favorite favorite = savedFavorite(loginMember, source, target);
		return FavoritesResponse.from(favorite);
	}

	@Transactional(readOnly = true)
	public List<FavoritesResponse> findAllFavorites(LoginMember loginMember) {
		List<Favorite> favorites = favoriteRepository.findAllByMemberId(loginMember.getId());
		return FavoritesResponse.listOf(favorites);
	}

	@Transactional
	public void deleteFavorite(LoginMember loginMember, long id) {
		favoriteRepository.delete(favorite(loginMember, id));
	}

	private Favorite favorite(LoginMember loginMember, long id) {
		return favoriteRepository.findByIdAndMemberId(id, loginMember.getId())
			.orElseThrow(
				() -> new NotFoundException(String.format("사용자(%s)가 저장한 즐겨찾기 id(%s)가 존재하지 않습니다.", loginMember, id)));
	}

	private void validate(Station source, Station target, long memberId) {
		validatePath(source, target);
		validateDuplicateFavorite(source, target, memberId);
	}

	private void validateDuplicateFavorite(Station source, Station target, long memberId) {
		boolean existFavorite = favoriteRepository.existsBySourceIdAndTargetIdAndMemberId(
			source.getId(), target.getId(), memberId);
		// if (existFavorite) {
		// 	throw new DuplicateDataException(
		// 		String.format("출발역(%s), 도착역(%s) 인 즐겨찾기가 이미 존재합니다.", source.name(), target.name()));
		// }
	}

	private void validatePath(Station source, Station target) {
		boolean invalidPath = pathService.isInvalidPath(source, target);
		if (invalidPath) {
			throw new InvalidDataException(String.format("출발역(%s)과 도착역(%s)이 연결되어 있지 않습니다.", source, target));
		}
	}

	private Favorite savedFavorite(LoginMember loginMember, Station source, Station target) {
		Favorite of = Favorite.of(source, target, loginMember.getId());
		return favoriteRepository.save(of);
	}

}

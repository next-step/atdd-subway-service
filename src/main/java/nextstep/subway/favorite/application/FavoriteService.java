package nextstep.subway.favorite.application;

import nextstep.subway.auth.application.AuthorizationException;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class FavoriteService {
	private static final String EXCEPTION_NOT_EXIST_FAVORITE = "존재하지 않는 즐겨찾기입니다.";
	private static final String EXCEPTION_NOT_EXIST_STATION = "존재하지 않는 역을 즐겨찾기로 추가할 수 없습니다.";
	private static final String EXCEPTION_SAME_STATION = "출발역 도착역이 같아 등록이 불가능합니다.";
	private static final String EXCEPTION_ALREADY_EXIST = "출발역 도착역이 같은 즐겨찾기가 이미 등록되어 있습니다.";
	private final FavoriteRepository favoriteRepository;
	private final StationRepository stationRepository;
	private final MemberRepository memberRepository;

	public FavoriteService(FavoriteRepository favoriteRepository, StationRepository stationRepository, MemberRepository memberRepository) {
		this.favoriteRepository = favoriteRepository;
		this.stationRepository = stationRepository;
		this.memberRepository = memberRepository;
	}

	public List<FavoriteResponse> getFavorites(LoginMember loginMember) {
		List<Favorite> favorites = favoriteRepository.findAllByMember_Id(loginMember.getId());
		return favorites.stream()
				.map(FavoriteResponse::of)
				.collect(Collectors.toList());
	}

	@Transactional
	public FavoriteResponse createFavorite(LoginMember loginMember, FavoriteRequest favoriteRequest) {
		validateCreateFavorite(loginMember, favoriteRequest);
		Favorite favorite = favoriteRepository.save(createNewEntity(loginMember, favoriteRequest));
		return FavoriteResponse.of(favorite);
	}

	private void validateCreateFavorite(LoginMember loginMember, FavoriteRequest favoriteRequest) {
		if (Objects.equals(favoriteRequest.getSource(), favoriteRequest.getTarget())) {
			throw new FavoriteValidationException(EXCEPTION_SAME_STATION);
		}

		final boolean alreadyExist = favoriteRepository.existsFavoriteByMember_IdAndSource_IdAndTarget_Id(
				loginMember.getId(), favoriteRequest.getSource(), favoriteRequest.getTarget());
		if (alreadyExist) {
			throw new FavoriteValidationException(EXCEPTION_ALREADY_EXIST);
		}
	}

	private Favorite createNewEntity(LoginMember loginMember, FavoriteRequest favoriteRequest) {
		Member member = memberRepository.findById(loginMember.getId())
				.orElseThrow(() -> new AuthorizationException("만료된 ID 입니다."));

		List<Station> findResult = stationRepository.findAllByIdIn(
				Arrays.asList(favoriteRequest.getSource(), favoriteRequest.getTarget()));
		Station source = findResult.stream()
				.filter(station -> station.getId().equals(favoriteRequest.getSource()))
				.findFirst()
				.orElseThrow(() -> new FavoriteValidationException(EXCEPTION_NOT_EXIST_STATION));
		Station target = findResult.stream()
				.filter(station -> station.getId().equals(favoriteRequest.getTarget()))
				.findFirst()
				.orElseThrow(() -> new FavoriteValidationException(EXCEPTION_NOT_EXIST_STATION));
		return new Favorite(member, source, target);
	}

	@Transactional
	public void deleteFavoriteById(LoginMember loginMember, Long favoriteId) {
		Favorite favorite = favoriteRepository.findByIdAndMember_Id(favoriteId, loginMember.getId())
				.orElseThrow(() -> new FavoriteValidationException(EXCEPTION_NOT_EXIST_FAVORITE));
		favoriteRepository.delete(favorite);
	}
}

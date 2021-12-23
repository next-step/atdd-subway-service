package nextstep.subway.favorite.application;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.common.exception.BadParameterException;
import nextstep.subway.common.exception.Exceptions;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteCreateRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;

@Service
@Transactional(readOnly = true)
public class FavoriteService {
	private final FavoriteRepository favoriteRepository;
	private final StationRepository stationRepository;
	private final MemberRepository memberRepository;

	public FavoriteService(FavoriteRepository favoriteRepository,
		StationRepository stationRepository, MemberRepository memberRepository) {
		this.favoriteRepository = favoriteRepository;
		this.stationRepository = stationRepository;
		this.memberRepository = memberRepository;
	}

	@Transactional
	public FavoriteResponse saveFavorite(LoginMember loginMember, FavoriteCreateRequest favoriteCreateRequest) {
		validateOnSaveFavorite(favoriteCreateRequest);
		Station source = findStationById(favoriteCreateRequest.getSource());
		Station target = findStationById(favoriteCreateRequest.getTarget());
		Member member = findMemberById(loginMember.getId());
		Favorite favorite = favoriteRepository.save(new Favorite(source, target, member));
		return FavoriteResponse.of(favorite);
	}

	private void validateOnSaveFavorite(FavoriteCreateRequest favoriteCreateRequest) {
		if (Objects.isNull(favoriteCreateRequest.getSource())) {
			throw new BadParameterException("즐겨찾기를 등록하기 위해 출발지를 입력해주세요.");
		}

		if (Objects.isNull(favoriteCreateRequest.getTarget())) {
			throw new BadParameterException("즐겨찾기를 등록하기 위해 도착지를 입력해주세요.");
		}

		if (favoriteCreateRequest.getSource().equals(favoriteCreateRequest.getTarget())) {
			throw new BadParameterException("출발지와 도착지가 같아 즐겨찾기를 등록할 수 없습니다.");
		}
	}

	public List<FavoriteResponse> findFavorites(LoginMember loginMember) {
		List<Favorite> favorites = favoriteRepository.findAllByMemberId(loginMember.getId());
		return favorites.stream()
			.map(FavoriteResponse::of)
			.collect(Collectors.toList());
	}

	@Transactional
	public void deleteFavorite(LoginMember loginMember, Long favoriteId) {
		Favorite favorite = favoriteRepository.findByIdAndMemberId(favoriteId, loginMember.getId())
			.orElseThrow(Exceptions.FAVORITE_NOT_FOUND::getException);
		favoriteRepository.delete(favorite);
	}

	private Station findStationById(Long stationId) {
		return stationRepository.findById(stationId).orElseThrow(Exceptions.STATION_NOT_EXIST::getException);
	}

	private Member findMemberById(Long memberId) {
		return memberRepository.findById(memberId).orElseThrow(Exceptions.MEMBER_NOT_FOUND::getException);
	}
}

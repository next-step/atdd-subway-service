package nextstep.subway.favorite.application;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.auth.application.AuthorizationException;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.favorite.exception.FavoriteNotFoundException;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.member.exception.MemberNotFoundException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.exception.StationNotFoundException;

@Service
public class FavoriteService {

	private final FavoriteRepository favoriteRepository;
	private final MemberRepository memberRepository;
	private final StationRepository stationRepository;

	public FavoriteService(FavoriteRepository favoriteRepository,
		MemberRepository memberRepository, StationRepository stationRepository) {
		this.favoriteRepository = favoriteRepository;
		this.memberRepository = memberRepository;
		this.stationRepository = stationRepository;
	}

	public FavoriteResponse add(Long memberId, FavoriteRequest request) {
		validateToAdd(request);
		final Member member = findMember(memberId);
		final List<Station> stations = findStations(request.getSource(), request.getTarget());
		final Station source = getStation(stations, request.getSource());
		final Station target = getStation(stations, request.getTarget());
		final Favorite favorite = favoriteRepository.save(Favorite.of(member, source, target));
		return FavoriteResponse.of(favorite);
	}

	private void validateToAdd(FavoriteRequest request) {
		if (Objects.equals(request.getSource(), request.getTarget())) {
			throw new IllegalArgumentException("출발역과 도착역의 식별자는 서로 달라야 합니다.");
		}
	}

	@Transactional(readOnly = true)
	public List<FavoriteResponse> getAll(Long memberId) {
		final List<Favorite> favorites = favoriteRepository.findAllByMemberId(memberId);
		return favorites.stream()
			.map(FavoriteResponse::new)
			.collect(Collectors.toList());
	}

	public void delete(Long memberId, Long id) {
		final Favorite favorite = favoriteRepository.findById(id).orElseThrow(FavoriteNotFoundException::new);
		if (!favorite.isCreatedBy(memberId)) {
			throw new AuthorizationException(String.format("%d는 삭제할 권한이 없습니다.", memberId));
		}
		favoriteRepository.deleteById(id);
	}

	private Member findMember(Long id) {
		return memberRepository.findById(id)
			.orElseThrow(MemberNotFoundException::new);
	}

	private Station getStation(List<Station> stations, Long stationId) {
		return stations.stream()
			.filter(station -> station.getId().equals(stationId))
			.findAny()
			.orElseThrow(StationNotFoundException::new);
	}

	private List<Station> findStations(Long... ids) {
		return stationRepository.findByIdIn(ids);
	}
}

package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteService {
    private final MemberRepository memberRepository;
    private final StationRepository stationRepository;
    private final FavoriteRepository favoriteRepository;

    public FavoriteService(MemberRepository memberRepository, StationRepository stationRepository, FavoriteRepository favoriteRepository) {
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
        this.favoriteRepository = favoriteRepository;
    }

    @Transactional
    public FavoriteResponse createFavorite(Long memberId, Long sourceStationId, Long targetStationId) {
        Member member = memberRepository.findById(memberId).orElseThrow(NoResultException::new);
        Station sourceStation = stationRepository.findById(sourceStationId).orElseThrow(NoResultException::new);
        Station targetStation = stationRepository.findById(targetStationId).orElseThrow(NoResultException::new);

        Favorite favorite = new Favorite(sourceStation, targetStation);
        favorite.setMember(member);

        favorite = favoriteRepository.save(favorite);
        return FavoriteResponse.of(favorite);
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> getFavorites(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(NoResultException::new)
                .getFavorites()
                .stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toList());
    }
}

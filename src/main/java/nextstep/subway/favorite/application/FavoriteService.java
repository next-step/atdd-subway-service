package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.favorite.exception.FavoriteException;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.favorite.exception.FavoriteExceptionCode.*;

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

    public FavoriteResponse createFavorite(Long memberId, Long srcStationId, Long targetStationId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new FavoriteException(NONE_EXISTS_MEMBER));
        Station sourceStation = stationRepository.findById(srcStationId).orElseThrow(() -> new FavoriteException(NONE_EXISTS_SOURCE_STATION));
        Station targetStation = stationRepository.findById(targetStationId).orElseThrow(() -> new FavoriteException(NONE_EXISTS_TARGET_STATION));
        favoriteRepository.findByMemberAndSourceStationAndTargetStation(member, sourceStation, targetStation)
                .ifPresent(favorite -> {
                    throw new FavoriteException(ALREADY_REGISTER);
                });
        return FavoriteResponse.of(member, sourceStation, targetStation);
    }

    public List<FavoriteResponse> findAllFavoriteByMember(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new FavoriteException(NONE_EXISTS_MEMBER));
        return favoriteRepository.findAllByMember(member)
                .stream()
                .map(favorite -> FavoriteResponse.of(favorite))
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteFavorite(Long favoriteId,Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new FavoriteException(NONE_EXISTS_MEMBER));
        Favorite favorite = favoriteRepository.findById(favoriteId).orElseThrow(() -> new FavoriteException(NONE_EXISTS_FAVORITE));
        favorite.validateOwner(member);
        favoriteRepository.delete(favorite);
    }
}

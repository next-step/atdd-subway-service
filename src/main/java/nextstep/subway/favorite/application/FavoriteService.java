package nextstep.subway.favorite.application;

import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.favorite.exception.FavoriteException;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;

import static nextstep.subway.favorite.exception.FavoriteExceptionCode.*;

@Service
public class FavoriteService {

    private final MemberRepository memberRepository;
    private final StationRepository stationRepository;

    public FavoriteService(MemberRepository memberRepository, StationRepository stationRepository) {
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
    }

    public FavoriteResponse createFavorite(Long memberId, Long srcStationId, Long targetStationId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new FavoriteException(NONE_EXISTS_MEMBER));
        Station srcStation = stationRepository.findById(srcStationId).orElseThrow(() -> new FavoriteException(NONE_EXISTS_SOURCE_STATION));
        Station targetStation = stationRepository.findById(targetStationId).orElseThrow(() -> new FavoriteException(NONE_EXISTS_TARGET_STATION));
        return FavoriteResponse.of(member, srcStation, targetStation);

    }
}

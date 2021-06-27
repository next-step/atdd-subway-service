package nextstep.subway.favorite.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;

import java.util.Optional;

@Service
@Transactional
public class FavoriteService {
    public static final String NOT_FOUND_STATION = "역을 찾을 수 없습니다.";
    public static final String FAVORITE_ALREADY_ADDED = "이미 생성 된 즐겨찾기 구간입니다.";

    private final FavoriteRepository favoriteRepository;
    private final MemberRepository memberRepository;
    private final StationRepository stationRepository;

    public FavoriteService(FavoriteRepository favoriteRepository, MemberRepository memberRepository, StationRepository stationRepository) {
        this.favoriteRepository = favoriteRepository;
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
    }

    public void add(LoginMember loginMember, FavoriteRequest favoriteRequest) {
        Member member = memberRepository.findById(loginMember.getId()).orElseThrow(RuntimeException::new);
        Station source = stationRepository.findById(favoriteRequest.getSource()).orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_STATION));
        Station target = stationRepository.findById(favoriteRequest.getTarget()).orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_STATION));

        Optional<Favorite> favoriteOptional = favoriteRepository.findByMemberAndSourceAndTarget(member, source, target);
        if (favoriteOptional.isPresent()) {
            throw new IllegalArgumentException(FAVORITE_ALREADY_ADDED);
        }

        favoriteRepository.save(new Favorite(member, source, target));
    }
}

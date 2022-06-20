package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FavoriteService {
    private FavoriteRepository favoriteRepository;
    private StationService stationService;
    private MemberRepository memberRepository;

    public FavoriteService(FavoriteRepository favoriteRepository, StationService stationService, MemberRepository memberRepository) {
        this.favoriteRepository = favoriteRepository;
        this.stationService = stationService;
        this.memberRepository = memberRepository;
    }

    public FavoriteResponse saveFavorite(LoginMember loginMember, FavoriteRequest favoriteRequest) {
        Station source = stationService.findById(favoriteRequest.getSource());
        Station target = stationService.findById(favoriteRequest.getTarget());
        Member member = memberRepository.findById(loginMember.getId()).orElseThrow(RuntimeException::new);
        Favorite favorite = favoriteRepository.save(new Favorite(member, source, target));
        return FavoriteResponse.of(favorite);
    }
}

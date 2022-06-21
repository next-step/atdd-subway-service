package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.favorite.exception.NotFoundFavoriteException;
import nextstep.subway.member.exception.NotFoundMemberException;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
        Member member = memberRepository.findById(loginMember.getId()).orElseThrow(NotFoundMemberException::new);
        Favorite favorite = favoriteRepository.save(new Favorite(member, source, target));
        return FavoriteResponse.from(favorite);
    }

    public List<FavoriteResponse> findByMemberId(LoginMember loginMember) {
        Member member = memberRepository.findById(loginMember.getId()).orElseThrow(NotFoundMemberException::new);
        List<Favorite> favoriteList = favoriteRepository.findByMemberId(member.getId());
        return toFavoriteResponses(favoriteList);
    }

    public void delete(LoginMember loginMember, Long favoriteId) {
        Member member = memberRepository.findById(loginMember.getId()).orElseThrow(NotFoundMemberException::new);
        Favorite favorite = favoriteRepository.findByIdAndMemberId(favoriteId, member.getId()).orElseThrow(NotFoundFavoriteException::new);
        favoriteRepository.delete(favorite);
    }

    private List<FavoriteResponse> toFavoriteResponses (List<Favorite> favorites) {
        return favorites.stream().map(FavoriteResponse::from).collect(Collectors.toList());
    }
}

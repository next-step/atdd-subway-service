package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteService {
    private FavoriteRepository favoriteRepository;
    private MemberRepository memberRepository;
    private StationRepository stationRepository;

    @Autowired
    public FavoriteService(FavoriteRepository favoriteRepository, MemberRepository memberRepository, StationRepository stationRepository) {
        this.favoriteRepository = favoriteRepository;
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
    }

    public FavoriteResponse createFavorite(FavoriteRequest favoriteRequest, LoginMember loginMember) {
        Station sourceStation = stationRepository.findById(Long.valueOf(favoriteRequest.getSource()))
                .orElseThrow(RuntimeException::new);
        Station targetStation = stationRepository.findById(Long.valueOf(favoriteRequest.getTarget()))
                .orElseThrow(RuntimeException::new);
        Member member = memberRepository.findById(loginMember.getId())
                .orElseThrow(RuntimeException::new);

        Favorite savedFavorite = favoriteRepository.save(new Favorite(sourceStation, targetStation, member));
        return FavoriteResponse.of(savedFavorite);
    }

    public List<FavoriteResponse> findFavorites(LoginMember loginMember) {
        Member member = memberRepository.findById(loginMember.getId())
                .orElseThrow(RuntimeException::new);

        List<Favorite> favorite = favoriteRepository.findByMember(member)
                .orElseGet(ArrayList::new);
        return favorite.stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toList());
    }

    public void deleteFavorite(Long id, LoginMember loginMember) {
        Member member = memberRepository.findById(loginMember.getId())
                .orElseThrow(RuntimeException::new);

        favoriteRepository.deleteById(id);
    }
}

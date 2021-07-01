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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class FavoriteService {
    private FavoriteRepository favoriteRepository;
    private StationRepository stationRepository;
    private MemberRepository memberRepository;

    public FavoriteService(FavoriteRepository favoriteRepository,
                           StationRepository stationRepository, MemberRepository memberRepository) {
        this.favoriteRepository = favoriteRepository;
        this.stationRepository = stationRepository;
        this.memberRepository = memberRepository;
    }

    public FavoriteResponse saveFavorites(LoginMember loginMember, FavoriteRequest favoriteRequest) {
        Member member = findMemeberById(loginMember);
        Station source = findStationById(favoriteRequest.getSource());
        Station target = findStationById(favoriteRequest.getTarget());
        Favorite saved = favoriteRepository.save(new Favorite(member, source, target));
        return FavoriteResponse.of(saved);
    }

    private Member findMemeberById(LoginMember loginMember) {
        return memberRepository.findById(loginMember.getId()).orElseThrow(NoSuchElementException::new);
    }

    private Station findStationById(Long id) {
        return stationRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    public List<Favorite> findFavorites(LoginMember loginMember) {
        return favoriteRepository.findByMemberId(loginMember.getId());
    }

    public void deleteFavorite(Long id) {
        favoriteRepository.deleteById(id);
    }
}

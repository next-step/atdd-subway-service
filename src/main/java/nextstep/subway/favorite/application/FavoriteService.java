package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FavoriteService {

    private final FavoriteRepository repository;
    private final MemberRepository memberRepository;
    private final StationRepository stationRepository;

    public FavoriteService(FavoriteRepository repository,
                           MemberRepository memberRepository,
                           StationRepository stationRepository) {
        this.repository = repository;
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
    }

    public List<Favorite> getAllFavorites(LoginMember member) {
        return repository.findAllByMember(getMember(member));
    }

    private Member getMember(LoginMember member) {
        return memberRepository.findById(member.getId())
                .orElseThrow(() -> new RuntimeException("멤버를 찾지 못했습니다"));
    }

    public Favorite register(LoginMember member, FavoriteRequest request) {
        Station sourceStation = getStation(request.getSource());
        Station targetStation = getStation(request.getTarget());

        Favorite favorite = Favorite.of(getMember(member), sourceStation, targetStation);
        return repository.save(favorite);
    }

    private Station getStation(Long source) {
        return stationRepository.findById(source).orElseThrow(() -> new RuntimeException("지하철 역을 찾지 못했습니다."));
    }

    public void deleteFavorite(LoginMember member, Long favoriteId) {
        Optional<Favorite> favorite = repository.findFavoriteByIdAndMember(favoriteId, getMember(member));
        favorite.ifPresent(repository::delete);
    }
}

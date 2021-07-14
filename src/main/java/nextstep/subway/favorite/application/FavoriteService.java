package nextstep.subway.favorite.application;

import static java.util.stream.Collectors.*;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.member.exception.NotFoundMemberException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.exeption.NotFoundStationException;

@Service
public class FavoriteService {
    private FavoriteRepository favoriteRepository;
    private MemberRepository memberRepository;
    private StationRepository stationRepository;

    public FavoriteService(FavoriteRepository favoriteRepository,
        MemberRepository memberRepository, StationRepository stationRepository) {
        this.favoriteRepository = favoriteRepository;
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public FavoriteResponse createFavorite(Long id, FavoriteRequest request) {
        Station source = stationRepository.findById(request.getSource()).orElseThrow(NotFoundStationException::new);
        Station target = stationRepository.findById(request.getTarget()).orElseThrow(NotFoundStationException::new);
        Member member = memberRepository.findById(id).orElseThrow(NotFoundMemberException::new);
        Favorite favorite = favoriteRepository.save(new Favorite(member, source, target));
        return FavoriteResponse.of(favorite);
    }

    public List<FavoriteResponse> findByLoginMember(LoginMember loginMember) {
        Member member = memberRepository.findById(loginMember.getId()).orElseThrow(NotFoundMemberException::new);
        return favoriteRepository.findByMember(member).stream()
                                 .map(FavoriteResponse::of).collect(toList());
    }

    @Transactional
    public void deleteFavorite(LoginMember loginMember, Long id) {
        Member member = memberRepository.findById(loginMember.getId()).orElseThrow(NotFoundMemberException::new);
        favoriteRepository.deleteByIdAndMember(id, member);
    }
}

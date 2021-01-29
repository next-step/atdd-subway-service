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
import nextstep.subway.util.Message;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

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

    public FavoriteResponse createFavorite(Long memberId, FavoriteRequest favoriteRequest) {
        validateCreateFavorite(favoriteRequest);
        Member member = selectMember(memberId);
        Station source = selectStation(favoriteRequest.getSource());
        Station target = selectStation(favoriteRequest.getTarget());

        Favorite persistFavorite = favoriteRepository.save(new Favorite(source, target, member));
        return FavoriteResponse.of(persistFavorite);
    }

    private Member selectMember(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException(Message.NOT_FOUND_MEMBER_MESSAGE));
    }

    private void validateCreateFavorite(FavoriteRequest favoriteRequest) {
        if (Objects.equals(favoriteRequest.getSource(), favoriteRequest.getTarget())) {
            throw new IllegalArgumentException(Message.SAME_SOURCE_AND_TARGET_MESSAGE);
        }
    }

    private Station selectStation(Long id) {
        return stationRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(Message.NOT_EXIST_STATION_MESSAGE));
    }

    public List<FavoriteResponse> getFavorites(Long memberId) {
        Member member = selectMember(memberId);
        List<Favorite> favorites = favoriteRepository.findAllByMemberId(member.getId());
        return favorites.stream()
                .map(FavoriteResponse::of)
                .collect(toList());
    }

    public void deleteFavorite(Long memberId, Long favoriteId) {
        Member member = selectMember(memberId);
        Favorite favorite = favoriteRepository.findByIdAndMemberId(favoriteId, member.getId())
                .orElseThrow(() -> new DataRetrievalFailureException(Message.NOT_EXIST_FAVORITE_MESSAGE));
        favoriteRepository.delete(favorite);
    }
}

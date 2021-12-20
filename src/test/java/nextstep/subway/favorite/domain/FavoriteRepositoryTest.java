package nextstep.subway.favorite.domain;

import nextstep.subway.line.domain.*;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class FavoriteRepositoryTest {
    @Autowired
    LineRepository lineRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    StationRepository stationRepository;

    @Autowired
    FavoriteRepository favoriteRepository;

    @DisplayName("즐겨찾기를 저장한다.")
    @Test
    void myFavoriteSave() {
        // given
        final Member member = memberRepository.save(Member.of("email@email.com", "1234", 10));
        final Station source = stationRepository.save(Station.from("잠실역"));
        final Station target = stationRepository.save(Station.from("강변역"));
        final Section section = Section.of(source, target, Distance.of(10));
        lineRepository.save(Line.of("이호선", "blue", Sections.from(Arrays.asList(section)), Fare.from(900)));
        final Favorite actual = Favorite.of(member.getId(), source.getId(), target.getId());

        // when
        final Favorite expected = favoriteRepository.save(actual);

        // then
        assertAll(
                () -> assertThat(actual).isEqualTo(expected)
        );
    }

    @DisplayName("즐겨찾기 목록을 조회한다.")
    @Test
    void myFavoriteSearch() {
        // given
        final Member member = memberRepository.save(Member.of("email@email.com", "1234", 10));
        final Station source = stationRepository.save(Station.from("잠실역"));
        final Station target = stationRepository.save(Station.from("강변역"));
        final Section section = Section.of(source, target, Distance.of(10));
        lineRepository.save(Line.of("이호선", "blue", Sections.from(Arrays.asList(section)), Fare.from(900)));
        final Favorite actual = favoriteRepository.save(Favorite.of(member.getId(), source.getId(), target.getId()));

        // when
        final Favorite expected = favoriteRepository.findById(actual.getId()).get();

        // then
        assertAll(
                () -> assertThat(actual).isEqualTo(expected)
        );
    }

    @DisplayName("즐겨찾기를 삭제한다.")
    @Test
    void myFavoriteDelete() {
        // given
        final Member member = memberRepository.save(Member.of("email@email.com", "1234", 10));
        final Station source = stationRepository.save(Station.from("잠실역"));
        final Station target = stationRepository.save(Station.from("강변역"));
        final Section section = Section.of(source, target, Distance.of(10));
        lineRepository.save(Line.of("이호선", "blue", Sections.from(Arrays.asList(section)), Fare.from(900)));
        final Favorite actual = favoriteRepository.save(Favorite.of(member.getId(), source.getId(), target.getId()));
        final Long favoriteId = actual.getId();

        // when
        favoriteRepository.delete(actual);
        final Favorite expected = favoriteRepository.findById(favoriteId).orElse(null);

        // then
        assertThat(expected).isNull();
    }
}

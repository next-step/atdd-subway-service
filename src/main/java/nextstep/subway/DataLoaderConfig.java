package nextstep.subway;

import com.google.common.collect.Lists;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
public class DataLoaderConfig implements CommandLineRunner {
    private final LineRepository lineRepository;
    private final MemberRepository memberRepository;

    public DataLoaderConfig(LineRepository lineRepository, MemberRepository memberRepository) {
        this.lineRepository = lineRepository;
        this.memberRepository = memberRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Station 강남역 = new Station("강남역");
        Station 교대역 = new Station("교대역");
        Station 양재역 = new Station("양재역");
        Station 남부터미널역 = new Station("남부터미널역");

        Line 신분당선 = Line.builder()
                .name("신분당선")
                .color("red lighten-1")
                .upStation(강남역)
                .downStation(양재역)
                .distance(10)
                .addedFare(0)
                .build();

        Line 이호선 = Line.builder()
                .name("2호선")
                .color("green lighten-1")
                .upStation(교대역)
                .downStation(강남역)
                .distance(10)
                .addedFare(500)
                .build();

        Line 삼호선 = Line.builder()
                .name("3호선")
                .color("orange darken-1")
                .upStation(교대역)
                .downStation(양재역)
                .distance(10)
                .addedFare(900)
                .build();

        lineRepository.saveAll(Lists.newArrayList(신분당선, 이호선, 삼호선));

        memberRepository.save(new Member("probitanima11@gmail.com", "11", 10));
        memberRepository.save(new Member("shlee@gmail.com", "11", 13));
    }
}

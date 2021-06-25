package nextstep.subway;

import com.google.common.collect.Lists;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
public class DataLoaderConfig implements CommandLineRunner {
    private LineRepository lineRepository;
    private MemberRepository memberRepository;

    public DataLoaderConfig(LineRepository lineRepository, MemberRepository memberRepository) {
        this.lineRepository = lineRepository;
        this.memberRepository = memberRepository;
    }

    @Override
    public void run(String... args) {

        Station 서울역 = new Station("서울역");
        Station 시청역 = new Station("시청역");
        Station 종각역 = new Station("종각역");
        Station 종로3가역 = new Station("종로3가역");
        Station 아현역 = new Station("아현역");
        Station 충정로역 = new Station("충정로역");
        Station 을지로입구역 = new Station("을지로입구역");
        Station 애오개역 = new Station("애오개역");
        Station 서대문역 = new Station("서대문역");
        Station 광화문역 = new Station("광화문역");

        Line 일호선 = new Line("일호선", "blue");
        Line 이호선 = new Line("이호선", "green");
        Line 오호선 = new Line("오호선", "purple");

        일호선.addSection(new Section(서울역, 시청역, 10));
        일호선.addSection(new Section(시청역, 종각역, 9));
        일호선.addSection(new Section(종각역, 종로3가역, 10));
        이호선.addSection(new Section(아현역, 충정로역, 10));
        이호선.addSection(new Section(충정로역, 시청역, 10));
        이호선.addSection(new Section(시청역, 을지로입구역, 10));
        오호선.addSection(new Section(애오개역, 충정로역, 10));
        오호선.addSection(new Section(충정로역, 서대문역, 10));
        오호선.addSection(new Section(서대문역, 광화문역, 10));
        오호선.addSection(new Section(광화문역, 종로3가역, 10));

        lineRepository.saveAll(Lists.newArrayList(일호선, 이호선, 오호선));

        memberRepository.save(new Member("jhh992000@gmail.com", "1234", 39));
    }
}
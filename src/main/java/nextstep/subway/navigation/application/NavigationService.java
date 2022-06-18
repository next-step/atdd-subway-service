package nextstep.subway.navigation.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.navigation.domain.Navigation;
import nextstep.subway.navigation.dto.NavigationResponse;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NavigationService {

    public NavigationResponse findShortest(List<Line> persistLines, Station sourceStation, Station targetStation) {
        Navigation navigation = Navigation.of(persistLines);
        return navigation.findShortest(sourceStation, targetStation);
    }
}

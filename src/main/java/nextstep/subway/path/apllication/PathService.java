package nextstep.subway.path.apllication;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.Price;
import nextstep.subway.path.domain.SectionEdge;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class PathService {
    private StationService stationService;
    private LineService lineService;
    private Path path;
    private Price price;


    public PathService(StationService stationService, LineService lineService, Path path, Price price) {
        this.stationService = stationService;
        this.lineService = lineService;
        this.path = path;
        this.price = price;
    }

    public PathResponse findPath(LoginMember loginMember, Long source, Long target) {
        Station sourceStation = stationService.findStationById(source);
        Station targetStation = stationService.findStationById(target);
        List<Line> lines = lineService.findAll();
        path.initLines(lines);
        return getPathResponse(loginMember, sourceStation, targetStation);
    }

    private PathResponse getPathResponse(LoginMember loginMember, Station sourceStation, Station targetStation) {
        GraphPath<Long, SectionEdge> pathFind = path.find(sourceStation, targetStation);
        List<StationResponse> stationResponses = stationService.findAllStations(pathFind.getVertexList());
        stationResponses = stationResponseOrderByVertexList(pathFind, stationResponses);

        price.calculatePrice(pathFind.getWeight());

        return new PathResponse(stationResponses, pathFind.getWeight(), getPriceValue(pathFind, loginMember));
    }

    private int getPriceValue(GraphPath<Long, SectionEdge> pathFind, LoginMember loginMember) {
        int priceValue = price.getPrice() + getMaxSurcharge(pathFind);
        if (loginMember == null) {
            return priceValue;
        }
        return discount(priceValue, loginMember.getAge());
    }

    private int discount(int priceValue, int age) {
        if (age < 6) {
            return 0;
        }

        if (age < 13) {
            return (int) ((priceValue - 350) * 0.5);
        }

        if (age < 19) {
            return (int) ((priceValue - 350) * 0.8);
        }

        return priceValue;
    }

    private int getMaxSurcharge(GraphPath<Long, SectionEdge> pathFind) {
        return pathFind.getEdgeList().stream()
                .max(Comparator.comparingInt(SectionEdge::getLineSurcharge))
                .orElseThrow(RuntimeException::new)
                .getLineSurcharge();
    }

    private List<StationResponse> stationResponseOrderByVertexList(GraphPath<Long, SectionEdge> pathFind, List<StationResponse> stationResponses) {
        List<StationResponse> stationResponsesOrderBy = new ArrayList<>();
        pathFind.getVertexList().stream().forEach(
                id -> stationResponsesOrderBy.add(findStationById(stationResponses, id))
        );
        return stationResponsesOrderBy;
    }

    private StationResponse findStationById(List<StationResponse> stationResponses, Long id) {
        return stationResponses.stream()
                .filter(s -> s.getId() == id)
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }
}

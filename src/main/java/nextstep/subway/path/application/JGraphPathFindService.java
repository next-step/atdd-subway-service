package nextstep.subway.path.application;

import java.util.List;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.domain.PathFindResult;
import nextstep.subway.path.domain.PathFindService;
import nextstep.subway.path.domain.exception.NotExistPathException;
import nextstep.subway.station.domain.Station;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Component;

@Component
public class JGraphPathFindService implements PathFindService {

    private final LineRepository lineRepository;

    public JGraphPathFindService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    @Override
    public PathFindResult findShortestPath(Station startStation, Station endStation) throws NotExistPathException {
        List<Line> lines = lineRepository.findAll();
        WeightedMultigraph<Station, SectionEdge> graph = toGraph(lines);
        ShortestPathAlgorithm<Station, SectionEdge> algorithm = new DijkstraShortestPath<>(graph);

        GraphPath<Station, SectionEdge> shortestPath = algorithm.getPath(startStation, endStation);
        boolean notExistPath = shortestPath == null;
        if(notExistPath){
            throw new NotExistPathException("도달가능한 경로가 없습니다.");
        }
        return new PathFindResult(shortestPath.getVertexList(),(int) shortestPath.getWeight());
    }

    private WeightedMultigraph<Station, SectionEdge> toGraph(List<Line> lines){
        WeightedMultigraph<Station, SectionEdge> graph = new WeightedMultigraph<>(SectionEdge.class);
        lines.stream().forEach( line -> addLineToGraph(graph,line));
        return graph;
    }

    private void addLineToGraph(WeightedMultigraph<Station, SectionEdge> graph, Line line) {
        List<Station> stations = line.getStations();
        stations.stream().forEach(graph::addVertex);
        Sections sections = line.getSections();
        List<SectionEdge> edges = sections.toSectionEdge();
        edges.stream().forEach(edge-> graph.addEdge(edge.getSource(),edge.getTarget(),edge));
    }
}

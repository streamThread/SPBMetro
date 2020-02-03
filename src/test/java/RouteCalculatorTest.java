import core.Line;
import core.Station;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


public class RouteCalculatorTest extends TestCase {

    StationIndex ind = new StationIndex();
    RouteCalculator calc = new RouteCalculator(ind);

    @Override
    protected void setUp() throws Exception {

        ind.addLine(new Line(1, "Green"));
        ind.addLine(new Line(2, "Yellow"));
        ind.addLine(new Line(3, "Blue"));

        IntStream.range(1, 5).forEach(num -> ind.addStation(new Station("g" + num,
                ind.getLine(1))));
        IntStream.range(1, 5).forEach(num -> ind.addStation(new Station("y" + num,
                ind.getLine(2))));
        IntStream.range(1, 5).forEach(num -> ind.addStation(new Station("b" + num,
                ind.getLine(3))));

        ind.addConnection(Stream.of(ind.getStation("g4"), ind.getStation("y1")).collect(Collectors.toList()));
        ind.addConnection(Stream.of(ind.getStation("y4"), ind.getStation("b1")).collect(Collectors.toList()));

        ind.stations.forEach(station -> station.getLine().addStation(station));
    }

    public void testCalculateDuration1() {
        System.out.println("inside testCalculateDuration" +
                "\n(shortest direct route without transfers)");
        List<Station> route = new ArrayList<>((ind.getLine(1).getStations()));
        double actual = RouteCalculator.calculateDuration(route);
        double expected = 7.5;
        assertEquals(expected, actual);
    }

    public void testCalculateDuration2() {
        System.out.println("inside testCalculateDuration" +
                "\n(route with 1 transfer)");
        List<Station> route = new ArrayList<>();
        route.addAll(ind.getLine(1).getStations().subList(2, 4));
        route.addAll(ind.getLine(2).getStations().subList(0, 2));
        double actual = RouteCalculator.calculateDuration(route);
        double expected = 8.5;
        assertEquals(expected, actual);
    }

    public void testCalculateDuration3() {
        System.out.println("inside testCalculateDuration" +
                "\n(route with 2 transfers)");
        List<Station> route = new ArrayList<>();
        route.addAll(ind.getLine(1).getStations().subList(2, 4));
        route.addAll(ind.getLine(2).getStations());
        route.addAll(ind.getLine(3).getStations().subList(0, 2));
        double actual = RouteCalculator.calculateDuration(route);
        double expected = 19.5;
        assertEquals(expected, actual);
    }

    public void testCalculateDuration4() {
        System.out.println("inside testCalculateDuration" +
                "\n(route with only 1 stop)");
        List<Station> route = new ArrayList<>();
        route.add(ind.getStation("y1"));
        double actual = RouteCalculator.calculateDuration(route);
        double expected = 0;
        assertEquals(expected, actual);
    }

    public void testGetShortestRoute1() {
        System.out.println("inside testGetShortestRoute() " +
                "\n(shortest direct route without transfers)");
        List<Station> actual = calc.getShortestRoute(ind.getStation("y1"), ind.getStation("y4"));
        List<Station> expect = new ArrayList<>(ind.getLine(2).getStations());
        assertEquals(expect, actual);
    }

    public void testGetShortestRoute2() {
        System.out.println("inside testGetShortestRoute() " +
                "\n(route with 1 transfer)");
        List<Station> actual = calc.getShortestRoute(ind.getStation("y1"), ind.getStation("b4"));
        List<Station> expect = new ArrayList<>();
        expect.addAll(ind.getLine(2).getStations());
        expect.addAll(ind.getLine(3).getStations());
        assertEquals(expect, actual);
    }

    public void testGetShortestRoute3() {
        System.out.println("inside testGetShortestRoute() " +
                "\n(route with 2 transfers)");
        List<Station> actual = calc.getShortestRoute(ind.getStation("g1"), ind.getStation("b4"));
        List<Station> expect = new ArrayList<>();
        expect.addAll(ind.getLine(1).getStations());
        expect.addAll(ind.getLine(2).getStations());
        expect.addAll(ind.getLine(3).getStations());
        assertEquals(expect, actual);
    }

    public void testGetShortestRoute4() {
        System.out.println("inside testGetShortestRoute() " +
                "\n(The destination station is the same as the transfer station(two transfers))");
        List<Station> actual = calc.getShortestRoute(ind.getStation("g1"), ind.getStation("b1"));
        List<Station> expect = new ArrayList<>();
        expect.addAll(ind.getLine(1).getStations());
        expect.addAll(ind.getLine(2).getStations());
        expect.add(ind.getStation("b1"));
        assertEquals(expect, actual);
    }

    public void testGetShortestRoute5() {
        System.out.println("inside testGetShortestRoute() " +
                "\n(The destination station is the same as the transfer station(one transfer))");
        List<Station> actual = calc.getShortestRoute(ind.getStation("g1"), ind.getStation("y1"));
        List<Station> expect = new ArrayList<>(ind.getLine(1).getStations());
        expect.add(ind.getStation("y1"));
        assertEquals(expect, actual);
    }

    public void testGetShortestRoute6() {
        System.out.println("inside testGetShortestRoute() " +
                "\n(Departure station is the same as the transfer station(two transfers))");
        List<Station> actual = calc.getShortestRoute(ind.getStation("g4"), ind.getStation("b4"));
        List<Station> expect = new ArrayList<>();
        expect.add(ind.getStation("g4"));
        expect.addAll(ind.getLine(2).getStations());
        expect.addAll(ind.getLine(3).getStations());
        assertEquals(expect, actual);
    }

    public void testGetShortestRoute7() {
        System.out.println("inside testGetShortestRoute() " +
                "\n(Departure station is the same as the transfer station(one transfers))");
        List<Station> actual = calc.getShortestRoute(ind.getStation("y4"), ind.getStation("b4"));
        List<Station> expect = new ArrayList<>();
        expect.add(ind.getStation("y4"));
        expect.addAll(ind.getLine(3).getStations());
        assertEquals(expect, actual);
    }

    public void testGetShortestRoute8() {
        System.out.println("inside testGetShortestRoute() " +
                "\n(Departure station is the same as the destination station)");
        List<Station> actual = calc.getShortestRoute(ind.getStation("y4"), ind.getStation("y4"));
        List<Station> expect = new ArrayList<>();
        expect.add(ind.getStation("y4"));
        assertEquals(expect, actual);
    }

    public void testGetShortestRoute9() {
        System.out.println("inside testGetShortestRoute() " +
                "\n(reverse shortest direct route without transfers)");
        List<Station> actual = calc.getShortestRoute(ind.getStation("y4"), ind.getStation("y1"));
        List<Station> expect = Stream.of(ind.getStation("y4"), ind.getStation("y3"), ind.getStation("y2"), ind.getStation("y1")).collect(Collectors.toList());
        assertEquals(expect, actual);
    }

    @Override
    protected void tearDown() throws Exception {

    }
}

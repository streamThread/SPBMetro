import core.Line;
import core.Station;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;


public class RouteCalculatorTest extends TestCase {

    StationIndex ind = new StationIndex();
    RouteCalculator calc = new RouteCalculator(ind);
    List<Station> connectedStations = new ArrayList<>();
    List<Station> connectedStations2 = new ArrayList<>();

    Line line1 = new Line(1, "Green");
    Line line2 = new Line(2, "Yellow");
    Line line3 = new Line(3, "Blue");

    Station oneInLine1 = new Station("Первая", line1);
    Station twoInLine1 = new Station("Вторая", line1);
    Station threeInLine1 = new Station("Третья", line1);
    Station fourInLine1 = new Station("Четвертая", line1);
    Station oneInLine2 = new Station("Первая2", line2);
    Station twoInLine2 = new Station("Вторая2", line2);
    Station threeInLine2 = new Station("Третья2", line2);
    Station fourInLine2 = new Station("Четвертая2", line2);
    Station oneInLine3 = new Station("Первая3", line3);
    Station twoInLine3 = new Station("Вторая3", line3);
    Station threeInLine3 = new Station("Третья3", line3);
    Station fourInLine3 = new Station("Четвертая3", line3);

    @Override
    protected void setUp() throws Exception {

        line1.addStation(oneInLine1);
        line1.addStation(twoInLine1);
        line1.addStation(threeInLine1);
        line1.addStation(fourInLine1);
        line2.addStation(oneInLine2);
        line2.addStation(twoInLine2);
        line2.addStation(threeInLine2);
        line2.addStation(fourInLine2);
        line3.addStation(oneInLine3);
        line3.addStation(twoInLine3);
        line3.addStation(threeInLine3);
        line3.addStation(fourInLine3);

        connectedStations.add(fourInLine1);
        connectedStations.add(oneInLine2);

        connectedStations2.add(fourInLine2);
        connectedStations2.add(oneInLine3);

        ind.addConnection(connectedStations);
        ind.addConnection(connectedStations2);
    }

    public void testCalculateDuration1() {
        System.out.println("inside testCalculateDuration" +
                "\n(shortest direct route without transfers)");
        List<Station> route = new ArrayList<>();
        route.add(oneInLine1);
        route.add(twoInLine1);
        route.add(threeInLine1);
        double actual = RouteCalculator.calculateDuration(route);
        double expected = 5;
        assertEquals(expected, actual);
    }

    public void testCalculateDuration2() {
        System.out.println("inside testCalculateDuration" +
                "\n(route with 1 transfer)");
        List<Station> route = new ArrayList<>();
        route.add(threeInLine1);
        route.add(fourInLine1);
        route.add(oneInLine2);
        route.add(twoInLine2);
        double actual = RouteCalculator.calculateDuration(route);
        double expected = 8.5;
        assertEquals(expected, actual);
    }

    public void testCalculateDuration3() {
        System.out.println("inside testCalculateDuration" +
                "\n(route with 2 transfers)");
        List<Station> route = new ArrayList<>();
        route.add(threeInLine1);
        route.add(fourInLine1);
        route.add(oneInLine2);
        route.add(twoInLine2);
        route.add(threeInLine2);
        route.add(fourInLine2);
        route.add(oneInLine3);
        route.add(twoInLine3);
        double actual = RouteCalculator.calculateDuration(route);
        double expected = 19.5;
        assertEquals(expected, actual);
    }

    public void testCalculateDuration4() {
        System.out.println("inside testCalculateDuration" +
                "\n(route with only 1 stop)");
        List<Station> route = new ArrayList<>();
        route.add(threeInLine1);
        double actual = RouteCalculator.calculateDuration(route);
        double expected = 0;
        assertEquals(expected, actual);
    }

    public void testGetShortestRoute1() {
        System.out.println("inside testGetShortestRoute() " +
                "\n(shortest direct route without transfers)");
        List<Station> actual = calc.getShortestRoute(oneInLine1, fourInLine1);
        List<Station> expect = new ArrayList<>();
        expect.add(oneInLine1);
        expect.add(twoInLine1);
        expect.add(threeInLine1);
        expect.add(fourInLine1);
        assertEquals(expect, actual);
    }

    public void testGetShortestRoute2() {
        System.out.println("inside testGetShortestRoute() " +
                "\n(route with 1 transfer)");
        List<Station> actual = calc.getShortestRoute(oneInLine1, fourInLine2);
        List<Station> expect = new ArrayList<>();
        expect.add(oneInLine1);
        expect.add(twoInLine1);
        expect.add(threeInLine1);
        expect.add(fourInLine1);
        expect.add(oneInLine2);
        expect.add(twoInLine2);
        expect.add(threeInLine2);
        expect.add(fourInLine2);
        assertEquals(expect, actual);
    }

    public void testGetShortestRoute3() {
        System.out.println("inside testGetShortestRoute() " +
                "\n(route with 2 transfers)");
        List<Station> actual = calc.getShortestRoute(oneInLine1, fourInLine3);
        List<Station> expect = new ArrayList<>();
        expect.add(oneInLine1);
        expect.add(twoInLine1);
        expect.add(threeInLine1);
        expect.add(fourInLine1);
        expect.add(oneInLine2);
        expect.add(twoInLine2);
        expect.add(threeInLine2);
        expect.add(fourInLine2);
        expect.add(oneInLine3);
        expect.add(twoInLine3);
        expect.add(threeInLine3);
        expect.add(fourInLine3);
        assertEquals(expect, actual);
    }

    public void testGetShortestRoute4() {
        System.out.println("inside testGetShortestRoute() " +
                "\n(The destination station is the same as the transfer station(two transfers))");
        List<Station> actual = calc.getShortestRoute(oneInLine1, oneInLine3);
        List<Station> expect = new ArrayList<>();
        expect.add(oneInLine1);
        expect.add(twoInLine1);
        expect.add(threeInLine1);
        expect.add(fourInLine1);
        expect.add(oneInLine2);
        expect.add(twoInLine2);
        expect.add(threeInLine2);
        expect.add(fourInLine2);
        expect.add(oneInLine3);
        assertEquals(expect, actual);
    }

    public void testGetShortestRoute5() {
        System.out.println("inside testGetShortestRoute() " +
                "\n(The destination station is the same as the transfer station(one transfer))");
        List<Station> actual = calc.getShortestRoute(oneInLine1, oneInLine2);
        List<Station> expect = new ArrayList<>();
        expect.add(oneInLine1);
        expect.add(twoInLine1);
        expect.add(threeInLine1);
        expect.add(fourInLine1);
        expect.add(oneInLine2);
        assertEquals(expect, actual);
    }

    public void testGetShortestRoute6() {
        System.out.println("inside testGetShortestRoute() " +
                "\n(Departure station is the same as the transfer station(two transfers))");
        List<Station> actual = calc.getShortestRoute(fourInLine1, fourInLine3);
        List<Station> expect = new ArrayList<>();
        expect.add(fourInLine1);
        expect.add(oneInLine2);
        expect.add(twoInLine2);
        expect.add(threeInLine2);
        expect.add(fourInLine2);
        expect.add(oneInLine3);
        expect.add(twoInLine3);
        expect.add(threeInLine3);
        expect.add(fourInLine3);
        assertEquals(expect, actual);
    }

    public void testGetShortestRoute7() {
        System.out.println("inside testGetShortestRoute() " +
                "\n(Departure station is the same as the transfer station(one transfers))");
        List<Station> actual = calc.getShortestRoute(fourInLine2, fourInLine3);
        List<Station> expect = new ArrayList<>();
        expect.add(fourInLine2);
        expect.add(oneInLine3);
        expect.add(twoInLine3);
        expect.add(threeInLine3);
        expect.add(fourInLine3);
        assertEquals(expect, actual);
    }

    public void testGetShortestRoute8() {
        System.out.println("inside testGetShortestRoute() " +
                "\n(Departure station is the same as the destination station)");
        List<Station> actual = calc.getShortestRoute(fourInLine2, fourInLine2);
        List<Station> expect = new ArrayList<>();
        expect.add(fourInLine2);
        assertEquals(expect, actual);
    }

    public void testGetShortestRoute9() {
        System.out.println("inside testGetShortestRoute() " +
                "\n(reverse shortest direct route without transfers)");
        List<Station> actual = calc.getShortestRoute(fourInLine1, oneInLine1);
        List<Station> expect = new ArrayList<>();
        expect.add(fourInLine1);
        expect.add(threeInLine1);
        expect.add(twoInLine1);
        expect.add(oneInLine1);
        assertEquals(expect, actual);
    }

    @Override
    protected void tearDown() throws Exception {
        connectedStations.clear();
        connectedStations2.clear();
    }
}

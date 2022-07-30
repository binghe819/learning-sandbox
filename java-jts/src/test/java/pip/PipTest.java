package pip;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.util.GeometricShapeFactory;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PipTest {

    @Test
    void jts_within_임의의점이_특정Polygon_안에_존재한다() {
        // given
        GeometryFactory factory = new GeometryFactory();
        Coordinate[] coordinates = {
                new Coordinate(37.499972, 127.028335),
                new Coordinate(37.499197, 127.025384),
                new Coordinate(37.496277, 127.025309),
                new Coordinate(37.496788, 127.029976),
                new Coordinate(37.499972, 127.028335)
        };

        Polygon 강남역_주변_영역 = factory.createPolygon(coordinates);
        Point 강남역 = factory.createPoint(new Coordinate(37.498031, 127.027777));
        Point 교보문고 = factory.createPoint(new Coordinate(37.50385, 127.024188));

        // when
        boolean 강남역_inside_여부 = 강남역.within(강남역_주변_영역);
        boolean 교보문고_inside_여부 = 교보문고.within(강남역_주변_영역);

        // then
        assertTrue(강남역_inside_여부, "강남역은 강남역 주변 영역 안에 위치합니다.");
        assertFalse(교보문고_inside_여부, "교보문고는 강남역 주변 영역에 위치하지 않습니다.");
    }

    @Test
    void PIP_수학적_접근_테스트() {
        // given
        Coordinate[] coordinates = {
                new Coordinate(2.0, 10.0),
                new Coordinate(10.0, 11.0),
                new Coordinate(5.0, 1.0),
                new Coordinate(2.0, 10.0)
        };

        // when
        boolean within = within(coordinates, 4, 8);
        boolean without = within(coordinates, 1, 8);

        // then
        assertTrue(within);
        assertFalse(without);
    }

    private boolean within(Coordinate[] coordinates, int x, int y) {
        List<Double> xIntersections = new ArrayList<>();
        for (int i = 0; i < coordinates.length - 1; i++) {
            Coordinate current = coordinates[i];
            Coordinate next = coordinates[i + 1];

            // 3점을 지나는 직선을 구해준다. (직선 방정식 -> y = angle * x + yIntercept)
            Double angle = (next.y - current.y) / (next.x - current.x); // 기울기
            Double yIntercept = current.y - (angle * current.x); // y절편

            // 직선과 y = 8의 교차점의 x 값
            Double xIntersection = (y - yIntercept) / angle;

            // 예외 처리 (다각형의 변인 직선에 위치하지않는 점 예외처리)
            // 두 점의 중간값과 차이를 구하여 교점이 사이에 있는지 판단한다.
            Double middle = (current.x + next.x) / 2;
            Double distance = Math.abs(next.x - middle);
            boolean between = Math.abs(xIntersection - middle) <= distance;
            if (between) {
                xIntersections.add(xIntersection);
            }
        }

        // 점 (4,8)이 교점들 교점들 사이에 몇 번째에 속하는지 판단한다.
        // 간단한 계산을 위해 최대값과 최소값을 넣어줌.
        xIntersections.add(Double.MAX_VALUE);
        xIntersections.add(Double.MIN_VALUE);
        xIntersections.sort(Double::compareTo);

        for (int i = 0; i < xIntersections.size() - 1; i++) {
            Double current = xIntersections.get(i);
            Double next = xIntersections.get(i + 1);
            if (current < x && x < next) {
                // 짝수번째면 out, 홀수번째면 in
                boolean within = (i % 2 == 1);
                return within;
            }
        }
        return false;
    }
}

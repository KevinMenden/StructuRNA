package Model3D;

import HBondInference.HbondConstants;
import javafx.geometry.Point3D;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

/**
 * Created by kevin_000 on 29.01.2016.
 */
public class Line3D {

    public static Cylinder makeLine3D(Point3D origin, Point3D target, PhongMaterial material, double radius){
        Point3D yAxis = new Point3D(0, 1, 0);
        Point3D diff = target.subtract(origin);
        double height = diff.magnitude();

        Point3D mid = target.midpoint(origin);
        Translate moveToMidpoint = new Translate(mid.getX(), mid.getY(), mid.getZ());

        Point3D axisOfRotation = diff.crossProduct(yAxis);
        double angle = Math.acos(diff.normalize().dotProduct(yAxis));
        Rotate rotateAroundCenter = new Rotate(-Math.toDegrees(angle), axisOfRotation);

        Cylinder line = new Cylinder(radius, height);
        line.setMaterial(material);
        line.getTransforms().addAll(moveToMidpoint, rotateAroundCenter);

        return line;    }
}

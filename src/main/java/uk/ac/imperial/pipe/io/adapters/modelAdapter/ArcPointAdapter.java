package uk.ac.imperial.pipe.io.adapters.modelAdapter;

import uk.ac.imperial.pipe.io.adapters.model.AdaptedArcPoint;
import uk.ac.imperial.pipe.models.petrinet.ArcPoint;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.awt.geom.Point2D;

public class ArcPointAdapter extends XmlAdapter<AdaptedArcPoint, ArcPoint> {
    @Override
    public ArcPoint unmarshal(AdaptedArcPoint adaptedArcPoint) {
        Point2D point = new Point2D.Double(adaptedArcPoint.getX(), adaptedArcPoint.getY());
        return new ArcPoint(point, adaptedArcPoint.isCurved());
    }

    @Override
    public AdaptedArcPoint marshal(ArcPoint arcPoint) {
        AdaptedArcPoint adaptedArcPoint = new AdaptedArcPoint();
        adaptedArcPoint.setX(arcPoint.getX());
        adaptedArcPoint.setY(arcPoint.getY());
        adaptedArcPoint.setCurved(arcPoint.isCurved());
        return adaptedArcPoint;
    }
}

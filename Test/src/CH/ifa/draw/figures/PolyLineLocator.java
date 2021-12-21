/*
 * @(#)PolyLineLocator.java 5.1
 *
 */

package CH.ifa.draw.figures;

import java.awt.Point;

import CH.ifa.draw.framework.Figure;
import CH.ifa.draw.standard.AbstractLocator;

/**
 * A poly line figure consists of a list of points.
 * It has an optional line decoration at the start and end.
 *
 * @see LineDecoration
 */
class PolyLineLocator extends AbstractLocator {
	int fIndex;

	public PolyLineLocator(int index) {
		fIndex = index;
	}

	public Point locate(Figure owner) {
		PolyLineFigure plf = (PolyLineFigure)owner;
		// guard against changing PolyLineFigures -> temporary hack
		if (fIndex < plf.pointCount())
			return ((PolyLineFigure)owner).pointAt(fIndex);
		return new Point(0, 0);
	}
}

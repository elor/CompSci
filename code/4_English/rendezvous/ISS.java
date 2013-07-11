package rendezvous;

import java.awt.Color;
import java.awt.Graphics;

import org.opensourcephysics.display.Drawable;
import org.opensourcephysics.display.DrawingPanel;

public class ISS implements Drawable {

  double altitude = 5.0;
  double angle = 0.0;
  double halfsize = 0.5;
  double centerwidth = 0.1;

  public ISS(double altitude) {
    this.altitude = altitude;
  }

  @Override
  public void draw(DrawingPanel panel, Graphics g) {
    int x = panel.xToPix(-altitude);
    int y = panel.yToPix(altitude);
    int wx = panel.xToPix(altitude) - x;
    int wy = panel.yToPix(-altitude) - y;

    g.setColor(Color.BLACK);

    g.drawArc(x, y, wx, wy, 0, 360);

    double posx = Math.sin(angle) * altitude;
    double posy = Math.cos(angle) * altitude;

    // draw center
    x = panel.xToPix(posx - centerwidth);
    y = panel.yToPix(posy + centerwidth);
    wx = panel.xToPix(posx + centerwidth) - x;
    wy = panel.yToPix(posy - centerwidth) - y;

    g.setColor(Color.DARK_GRAY);

    g.fillRect(x, y, wx, wy);

    // draw left panel
    x = panel.xToPix(posx - halfsize);
    y = panel.yToPix(posy + halfsize);
    wx = panel.xToPix(posx - centerwidth) - x;
    wy = panel.yToPix(posy - halfsize) - y;

    g.setColor(Color.DARK_GRAY);

    g.fillRect(x, y, wx, wy);

    // draw right panel
    x = panel.xToPix(posx + centerwidth);
    y = panel.yToPix(posy + halfsize);
    wx = panel.xToPix(posx + halfsize) - x;
    wy = panel.yToPix(posy - halfsize) - y;

    g.setColor(Color.DARK_GRAY);

    g.fillRect(x, y, wx, wy);
  }
}

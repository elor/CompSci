package rendezvous;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import org.opensourcephysics.display.Drawable;
import org.opensourcephysics.display.DrawingPanel;

public class MultiDrawer implements Drawable {

  List<Drawable> drawables = new ArrayList<Drawable>();

  @Override
  public void draw(DrawingPanel panel, Graphics g) {
    for (Drawable drawable : drawables) {
      drawable.draw(panel, g);
    }
  }

  void add(Drawable drawable) {
    drawables.add(drawable);
  }

  void clear() {
    drawables.clear();
  }

}

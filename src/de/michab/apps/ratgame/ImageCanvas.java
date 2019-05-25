
package de.michab.apps.ratgame;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

public class ImageCanvas
  extends
    Canvas
{
  private final Image image;

  public ImageCanvas( Image i )
  {
    image = i;
  }

  public Dimension getSize()
  {
    return new Dimension( image.getWidth( null ), image.getHeight( null ) );
  }
  public void paint(Graphics g)
  {
    g.drawImage( image, 0, 0, null );
  }
}

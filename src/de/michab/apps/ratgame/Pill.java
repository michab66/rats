/* $Id: Pill.java 115 2008-12-31 17:02:19Z binzm $
 *
 * Copyright (c) 1996 Michael G. Binz
 */

package de.michab.apps.ratgame;

import java.applet.Applet;
import java.awt.Color;



/**
 * Static pill, sits at a fixed position in the animation grid.
 */
public class Pill
  extends
    Existence
{
  static private String ourName = "Pill";
  static private String ourFile = "images/ztab.gif";

  // image data
  static private PictureStrip strip;



  /**
   * ctor 1: insert pill into AnimationGrid.
   * ctor 2: preload
   */
  public Pill( Applet app, AnimationGrid ag, GridPosition p )
  {
    super( ag, ourName );
    initGfx( app );
    // go...
    terminated( null, ag.getCell( p ) );
  }
  public Pill( Applet app )
  {
    super( null, ourName );
    initGfx( app );
  }



  public void terminated( PictureStrip s, AnimatedCell p )
  {
    // loop
    p.addStrip( this, strip, 0, 5 );
  }



  private void initGfx( Applet app )
  {
    if ( null == strip )
      strip = new PictureStrip( app, ourFile, new DropColour( Color.white ) );
  }
}

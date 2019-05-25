/* $Id: Fluff.java 115 2008-12-31 17:02:19Z binzm $
 *
 * Copyright (c) 1996,97 Michael G. Binz
 */

package de.michab.apps.ratgame;

import java.applet.Applet;
import java.awt.Color;



/**
 * Static Fluff, short bang, if any item is placed wrong
 */
public class Fluff
  extends
    Existence
{
  static private String ourName = "Fluff";
  static private String ourFile = "images/zx.gif";

  // image data
  static private PictureStrip strip;



  /**
   * ctor 1: insert Fluff into AnimationGrid.
   * ctor 2: preload
   */
  public Fluff( Applet app, AnimationGrid ag, GridPosition p )
  {
    super( ag, ourName );
    initGfx( app );
    // go...
    ag.addStrip( this, p, strip );
  }
  public Fluff( Applet app )
  {
    super( null, ourName );
    initGfx( app );
  }



  private void initGfx( Applet app )
  {
    if ( null == strip )
      strip = new PictureStrip( app, ourFile, new DropColour( Color.white ) );
  }
}

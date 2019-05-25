/* $Id: StopTheFat.java 115 2008-12-31 17:02:19Z binzm $
 *
 * Copyright (c) 1996,97 Michael G. Binz
 */

package de.michab.apps.ratgame;

import java.applet.Applet;
import java.awt.Color;



/**
 * This object can only be passed by the slim rat.
 */
public class StopTheFat
  extends
    Existence
{
  static private String ourName = "StopTheFat";
  static private String ourFile = "images/zeng.gif";

  // picture data
  static private PictureStrip strip;



  /**
   * ctor 1: Add StopTheFat to AnimationGrid
   * ctor 2: preload
   */
  public StopTheFat( Applet app, AnimationGrid ag, GridPosition p )
  {
    super( ag, ourName );
    initGfx( app );
    // go...
    terminated( null, ag.getCell( p ) );
  }
  public StopTheFat( Applet app )
  {
    super( null, ourName );
    initGfx( app );
  }



  public void terminated( PictureStrip s, AnimatedCell p )
  {
    p.addStrip( this, strip );
  }



  private void initGfx( Applet app )
  {
    // first time init
    if ( null == strip )
      strip = new PictureStrip( app, ourFile, new DropColour( Color.white ) );
  }
}

/* $Id: Cheese.java 115 2008-12-31 17:02:19Z binzm $
 *
 * Copyright (c) 1996,97 Michael G. Binz
 */

package de.michab.apps.ratgame;

import java.applet.Applet;
import java.awt.Color;



/**
 * Just sits on the AnimationGrid and loops
 */
public class Cheese
  extends
    Existence
{
  static private String ourName = "Cheese";
  static private String ourFile = "images/zkaese.gif";

  static private PictureStrip strip;



  /**
   * ctor 1: standard with AnimationGrid
   *
   * ctor 2: preload
   */
  public Cheese( Applet app, AnimationGrid ag, GridPosition p )
  {
    super( ag, ourName );
    initGfx( app );
    // go for the gold...
    terminated( null, ag.getCell( p ) );
  }
  public Cheese( Applet app )
  {
    super( null, ourName );
    initGfx( app );
  }



  public void terminated( PictureStrip s, AnimatedCell ac )
  {
    // if our strip terminates, activate it again => loop
    ac.addStrip( this, strip );
  }



  private void initGfx( Applet app )
  {
    // first time init of the strip
    if ( null == strip )
      strip = new PictureStrip( app, ourFile, new DropColour( Color.white ) );
  }
}

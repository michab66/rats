/* $Id: Block.java 115 2008-12-31 17:02:19Z binzm $
 *
 * Copyright (c) 1996 Michael G. Binz
 */

package de.michab.apps.ratgame;

import java.applet.Applet;
import java.awt.Color;



/**
 * Just sits in the AnimationGrid and loops
 */
public class Block
  extends
    Existence
{
  static private String ourName = "Block";
  static private String ourFile = "images/zsperr.gif";

  static private PictureStrip strip;



  /**
   * Konstruktor ohne Argumente erlaubt Preload
   */
  public Block( Applet app, AnimationGrid ag, GridPosition p )
  {
    super( ag, ourName );
    initGfx( app );
    // do your work, looper :)
    terminated( null, ag.getCell( p ) );
  }
  public Block( Applet app )
  {
    super( null, ourName );
    initGfx( app );
  }



  public void terminated( PictureStrip s, AnimatedCell p )
  {
    // just loop...
    p.addStrip( this, strip, 0, 7 );
  }



  private void initGfx( Applet app )
  {
    // first time init...
    if ( null == strip )
      strip = new PictureStrip( app, ourFile, new DropColour( Color.white ) );
  }
}

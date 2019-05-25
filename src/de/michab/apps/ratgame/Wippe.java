/* $Id: Wippe.java 115 2008-12-31 17:02:19Z binzm $
 *
 * Copyright (c) 1996 Michael G. Binz
 */

package de.michab.apps.ratgame;

import java.applet.Applet;
import java.awt.Color;



/**
 * Stupid object, just loops...
 */
public class Wippe
  extends
    Existence
{
  static private String ourName = "Wippe";
  static private String ourFile = "images/zwip.gif";

  // Der PictureStrip
  static private PictureStrip strip;

  // controlled map links, init'd from subclasses
  protected AnimatedCell easternCell, westernCell;
  // state
  protected boolean stateWippeWestOpen;



  /**
   * ctor 1: Add Wippe to AnimationGrid
   * ctor 2: Only preload
   */
  public Wippe( Applet app, AnimationGrid ag, GridPosition p, boolean state )
  {
    super( ag, ourName );
    initGfx( app );
    stateWippeWestOpen = state;
    // go...
    terminated( null, ag.getCell( p ) );
  }
  public Wippe( Applet app, AnimationGrid ag, GridPosition p )
  {
    this( app, ag, p, true );
  }
  public Wippe( Applet app )
  {
    super( null, ourName );
    initGfx( app );
  }



  public void terminated( PictureStrip s, AnimatedCell p )
  {
    if ( stateWippeWestOpen )
      p.addStrip( this, strip, 0, 4 );
    else
      p.addStrip( this, strip, 5, 9 );
  }



  private void initGfx( Applet app )
  {
    if ( null == strip )
      strip = new PictureStrip( app, ourFile, new DropColour( Color.white ) );
  }
}


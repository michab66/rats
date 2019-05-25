/* $Id: XButton.java 115 2008-12-31 17:02:19Z binzm $
 *
 * Copyright (c) 1996 Michael G. Binz
 */

package de.michab.apps.ratgame;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Event;



/**
 * Static button, sits unmovable in the animation grid.
 */
public class XButton
  extends
    Existence
{
  static private String ourName = "XButton";
  static private String ourFile = "images/but.gif";

  // image data
  static private PictureStrip strip;

  // button state
  protected boolean butPressed;



  /**
   * XButton
   *
   * ctor 1: insert button into AnimationGrid.
   * ctor 2: preload
   */
  public XButton( Applet app, AnimationGrid ag, GridPosition p )
  {
    super( ag, ourName );
    initGfx( app );
    // go...
    showUp( p );
  }
  public XButton( Applet app )
  {
    super( null, ourName );
    initGfx( app );
  }



  public boolean mouseDown( Event e, int x, int y, GridPosition p )
  {
    butPressed = true;
    showUp( p );
    return true;
  }



  public boolean mouseUp( Event e, int x, int y, GridPosition p )
  {
    butPressed = false;
    showUp( p );
    return true;
  }



  private void showUp( GridPosition p )
  {
    int imgIdx = butPressed ? 1 : 0;
    home.addStrip( this, p, strip, imgIdx, imgIdx, true );
  }



  private void initGfx( Applet app )
  {
    if ( null == strip )
      strip = new PictureStrip( app, ourFile, new DropColour( Color.white ) );
  }
}

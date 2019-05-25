/* Latch.java
 *
 * $Id: Latch.java 115 2008-12-31 17:02:19Z binzm $
 *
 * Copyright (c) 1996 Michael G. Binz
 */

package de.michab.apps.ratgame;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Event;



/**
 * Object represents the door between the waiting tube
 * and the play field.  On selecting the object with
 * the mouse, the door opens.  To close the door again, it has
 * to be selected a second time.
 *
 * If the door opens, we modify the underlying map in such a way
 * that we add a WE connection between the cells beneath us. If
 * the door closes, this connection is cut.
 *
 * This prevents errors from transferring the rat objects to
 * cells were another rat currently resides, which would kill
 * the poor thing.
 */
public class Latch
  extends
    Existence
{
  static private String ourName = "Latch";
  static private String ourFile = "images/zzauf.gif";

  // image data.
  static private PictureStrip strip;

  // state of arm; false = up (closed), true = down (open)
  private boolean arm;

  // Cells to connect and disconnect
  private AnimatedCell toCell;
  private AnimatedCell fromCell;



  /**
   * ctor 1: Add Latch to AnimationGrid
   * ctor 2: Only preload
   */
  public Latch( Applet app, AnimationGrid ag, GridPosition p, GridPosition target )
  {
    super( ag, ourName );
    initGfx( app );
    // init our internal state
    arm = false;
    toCell = ag.getCell( new GridPosition( p.x-1, p.y ) );
    fromCell = ag.getCell( new GridPosition( p.x+1, p.y ) );
    // go...
    showUp( p );
  }
  public Latch( Applet app )
  {
    super( null, ourName );
    initGfx( app );
  }



  public boolean mouseUp( Event e, int x, int y, GridPosition p )
  {
    // switch state
    arm = ! arm;
    // update display
    showUp( p );
    return true;
  }



  private void showUp( GridPosition p )
  {
    if ( arm )
    {
      // connect cells
      toCell.setLinks(
          toCell.toWest(),
          toCell.toNorth(),
          fromCell,
          toCell.toSouth()
      );
      fromCell.setLinks(
          toCell,
          fromCell.toNorth(),
          fromCell.toEast(),
          fromCell.toSouth()
      );
      // show image
      home.addStrip( this, p, strip, 1, 1, true );
    }
    else
    {
      // disconnect cells
      toCell.setLinks(
          toCell.toWest(),
          toCell.toNorth(),
          null,
          toCell.toSouth()
      );
      fromCell.setLinks(
          null,
          fromCell.toNorth(),
          fromCell.toEast(),
          fromCell.toSouth()
      );
      // show image
      home.addStrip( this, p, strip, 0, 0, true );
    }
  }



  private void initGfx( Applet app )
  {
    if ( null == strip )
      strip = new PictureStrip( app, ourFile, new DropColour( Color.white ) );
  }
}

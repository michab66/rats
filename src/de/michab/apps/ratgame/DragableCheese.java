/* $Id: DragableCheese.java 115 2008-12-31 17:02:19Z binzm $
 *
 * Copyright (c) 1996 Michael G. Binz
 */

package de.michab.apps.ratgame;

import java.applet.Applet;
import java.awt.Event;



/**
 * Pill can be moved with the mouse.
 */
public class DragableCheese
  extends
    Cheese
{
  private GridPosition crtPosition;



  /**
   * Supports creation on an AnimationGrid
   */
  public DragableCheese( Applet app, AnimationGrid ag, GridPosition p )
  {
    super( app, ag, p );
    // remember our home position
    crtPosition = p;
    // get the cell
    AnimatedCell ac = home.getCell( p );
      // If our target position has only east and west connections...
    if ( null == ac.toSouth() && null == ac.toNorth() )
    {
      ; // everything is cool
    }
    else if ( this == home.getCell( p ).getExistence() )
    {
        home.removeStrip( p );
        new Fluff( null, home, p );
    }
  }



  public boolean mouseDrag( Event evt, int x, int y, GridPosition p )
  {
    // If our position changed...
    if ( ! p.equals( crtPosition ) )
    {
      /* ...remove the PictureStrip from our old position
       * if we are the controller
       */
      if ( this == home.getCell( crtPosition ).getExistence() )
        home.removeStrip( crtPosition );
      // ...save the new position...
      crtPosition = p;
      // ...and activate a new picture strip in the superclass
      terminated( null, home.getCell( p ) );
    }

    return true;
  }



  public boolean mouseUp( Event evt, int x, int y, GridPosition p )
  {
    if ( this == home.getCell( p ).getExistence() )
    {
      AnimatedCell ac = home.getCell( crtPosition = p );

      // If our target position has only east and west connections...
      if ( null == ac.toSouth() && null == ac.toNorth() &&
           null != ac.toWest()  && null != ac.toEast() )
      {
        ; // nothing to do
      }
      // in the other case do suicide
      else
      {
        home.removeStrip( p );
        new Fluff( null, home, p );
      }
    }

    return true;
  }
}

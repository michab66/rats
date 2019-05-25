/* $Id: DragableWippe.java 115 2008-12-31 17:02:19Z binzm $
 *
 * Copyright (c) 1996 Michael G. Binz
 */

package de.michab.apps.ratgame;

import java.applet.Applet;
import java.awt.Event;



/**
 * Pill can be moved with the mouse.
 */
public class DragableWippe
  extends
    Wippe
{
  private GridPosition crtPosition;

  private boolean mapChanged = false;



  /**
   * Supports creation on an AnimationGrid
   */
  public DragableWippe( Applet app, AnimationGrid ag, GridPosition p, boolean state )
  {
    super( app, ag, p, state );
    // remeber our home position
    crtPosition = p;
    // get the cell
    AnimatedCell ac = home.getCell( p );
    // If our target position has only east and west connections...
    if ( null != ac.toWest() && null != ac.toEast() &&
         null == ac.toSouth() && null == ac.toNorth() )
    {
      // everything is cool
      changeMap();
    }
    else if ( this == home.getCell( p ).getExistence() )
    {
        home.removeStrip( p );
        new Fluff( null, home, p );
    }
  }
  public DragableWippe( Applet app, AnimationGrid ag, GridPosition p )
  {
    this( app, ag, p, true );
  }



  /**
   *
   */
  public void changeMap()
  {
    AnimatedCell ac = home.getCell( crtPosition );

    if ( stateWippeWestOpen )
    {
      ac.toWest().setLinks( ac.toWest().toWest(),
                            ac.toWest().toNorth(),
                            ac,
                            ac.toWest().toSouth() );
      ac.toEast().setLinks( null,
                            ac.toEast().toNorth(),
                            ac.toEast().toEast(),
                            ac.toEast().toSouth() );
    }
    else
    {
      ac.toWest().setLinks( ac.toWest().toWest(),
                            ac.toWest().toNorth(),
                            null,
                            ac.toWest().toSouth() );
      ac.toEast().setLinks( ac,
                            ac.toEast().toNorth(),
                            ac.toEast().toEast(),
                            ac.toEast().toSouth() );
    }

    mapChanged = true;
  }



  public void fixMap()
  {
    System.out.println( "fix: " + mapChanged );

    if ( mapChanged )
    {
      AnimatedCell ac = home.getCell( crtPosition );

      ac.toWest().setLinks( ac.toWest().toWest(),
                            ac.toWest().toNorth(),
                            ac,
                            ac.toWest().toSouth() );
      ac.toEast().setLinks( ac,
                            ac.toEast().toNorth(),
                            ac.toEast().toEast(),
                            ac.toEast().toSouth() );

      mapChanged = false;
    }
  }



  public boolean mouseDown( Event evt, int x, int y, GridPosition p )
  {
    fixMap();

    return true;
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
      if ( null != ac.toWest() && null != ac.toEast() &&
           null == ac.toSouth() && null == ac.toNorth() )
      {
        changeMap();
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

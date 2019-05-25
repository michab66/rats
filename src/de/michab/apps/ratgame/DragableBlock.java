/* $Id: DragableBlock.java 115 2008-12-31 17:02:19Z binzm $
 *
 * Copyright (c) 1996 Michael G. Binz
 */

package de.michab.apps.ratgame;

import java.applet.Applet;
import java.awt.Event;



/**
 * Pill can be moved with the mouse.
 */
public class DragableBlock
  extends
    Block
{
  private GridPosition crtPosition;

  private AnimatedCell N, W, E, S;

  boolean mapChanged;



  /**
   * Supports creation on an AnimationGrid
   */
  public DragableBlock( Applet app, AnimationGrid ag, GridPosition p )
  {
    super( app, ag, p );

    // remember our home position
    crtPosition = p;
    // get the cell
    AnimatedCell ac = home.getCell( p );
    // If our target position has EW or NS connections...
    if ( ((null != ac.toWest()  && null != ac.toEast()) && (null == ac.toSouth() && null == ac.toNorth()))
        || ((null == ac.toWest()  && null == ac.toEast()) && (null != ac.toSouth() && null != ac.toNorth())) )
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



  /**
   *
   */
  public void changeMap()
  {
    AnimatedCell ac = home.getCell( crtPosition );

    N = ac.toNorth();
    E = ac.toEast();
    S = ac.toSouth();
    W = ac.toWest();

    if ( null != N )
      N.setLinks( N.toWest(), N.toNorth(), N.toEast(), null );
    if ( null != S )
      S.setLinks( S.toWest(), null,        S.toEast(), S.toSouth() );
    if ( null != W )
      W.setLinks( W.toWest(), W.toNorth(), null,       W.toSouth() );
    if ( null != E )
      E.setLinks( null,       E.toNorth(), E.toEast(), E.toSouth() );

    mapChanged = true;
  }



  public void fixMap()
  {
    System.out.println( "fix: " + mapChanged );

    if ( mapChanged )
    {
      AnimatedCell ac = home.getCell( crtPosition );

      if ( null != N )
        N.setLinks( N.toWest(), N.toNorth(), N.toEast(), ac );
      if ( null != S )
        S.setLinks( S.toWest(), ac,          S.toEast(), S.toSouth() );
      if ( null != W )
        W.setLinks( W.toWest(), W.toNorth(), ac,         W.toSouth() );
      if ( null != E )
        E.setLinks( ac,         E.toNorth(), E.toEast(), E.toSouth() );

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

      // If our target position has any connections...
      if ( ((null != ac.toWest()  && null != ac.toEast()) && (null == ac.toSouth() && null == ac.toNorth()))
        || ((null == ac.toWest()  && null == ac.toEast()) && (null != ac.toSouth() && null != ac.toNorth())) )
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

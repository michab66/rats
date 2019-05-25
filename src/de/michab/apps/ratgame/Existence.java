/* $Id: Existence.java 115 2008-12-31 17:02:19Z binzm $
 *
 * Copyright (c) 1996 Michael G. Binz
 */

package de.michab.apps.ratgame;

import java.awt.Event;



/**
 * Kontrolliert eine Spielfigur im AnimationGrid.
 */
public class Existence
{
  private final static boolean Debug = false;

  final static int STRIP_WO = 0;
  final static int STRIP_OW = 1;
  final static int STRIP_NS = 2;
  final static int STRIP_SN = 3;
  final static int STRIP_WN = 4;
  final static int STRIP_NW = 5;
  final static int STRIP_NO = 6;
  final static int STRIP_ON = 7;
  final static int STRIP_OS = 8;
  final static int STRIP_SO = 9;
  final static int STRIP_SW = 10;
  final static int STRIP_WS = 11;
  final static int STRIP_WW = 12;
  final static int STRIP_NN = 13;
  final static int STRIP_OO = 14;
  final static int STRIP_SS = 15;

  // size of array needed to hold strip references
  final static int STRIP_USER = 16;

  // these have to be greater than 0 (zero)
  final static int COLL_O = 1;
  final static int COLL_S = 2;
  final static int COLL_W = 3;
  final static int COLL_N = 4;

  // our grid and name
  protected AnimationGrid home;
  protected String xName;



  /**
   * Simple ctor, just save the args.
   */
  public Existence( AnimationGrid g, String n )
  {
    home = g;
    xName = n;
  }



  public String getName()
  {
    return xName;
  }



  /**
   * If a signal is defined in the PictureStrip, this method is
   * called, when the associated image is displayed by the
   * cell.
   */
  public void signal( int sig, AnimatedCell p, PictureStrip s )
  {
    if ( Debug )
      System.out.println( "Existence: " + xName + " signal " + sig );
  }



  /**
   * Is called by an AnimatedCell object after having displayed
   * the last image of an PictureStrip.
   */
  public void terminated( PictureStrip s, AnimatedCell p )
  {
    if ( Debug )
      System.out.println( "Existence: " + xName + " " + s.getName() +
                          " terminated." );
  }



  /**
   * Can be called on an Existence object returned by 'addStrip'
   * to enable a simple communication between colliding objects.
   */
  public int collisionRequest( Existence identity, AnimatedCell idshome, int collDir )
  {
    return 0;
  }



  public boolean mouseDown( Event evt, int x, int y, GridPosition p )
  {
    if ( Debug )
      System.out.println( "Existence: " + xName + "rcvd mouseDown" );

    return false;
  }



  public boolean mouseDrag( Event evt, int x, int y, GridPosition p )
  {
    if ( Debug )
      System.out.println( "Existence: " + xName + "rcvd mouseDrag" );

    return false;
  }



  public boolean mouseUp( Event evt, int x, int y, GridPosition p )
  {
    if ( Debug )
      System.out.println( "Existence: " + xName + "rcvd mouseUp" );

    return false;
  }



  protected AnimatedCell getLeftCell( int collDir, AnimatedCell crtCell )
  {
    switch ( collDir )
    {
      case COLL_O:
        return crtCell.toNorth();
      case COLL_S:
        return crtCell.toEast();
      case COLL_W:
        return crtCell.toSouth();
      case COLL_N:
        return crtCell.toWest();
      default:
        return null;
    }
  }



  protected AnimatedCell getFrontCell( int collDir, AnimatedCell crtCell )
  {
    switch ( collDir )
    {
      case COLL_O:
        return crtCell.toEast();
      case COLL_S:
        return crtCell.toSouth();
      case COLL_W:
        return crtCell.toWest();
      case COLL_N:
        return crtCell.toNorth();
      default:
        return null;
    }
  }



  protected AnimatedCell getRightCell( int collDir, AnimatedCell crtCell )
  {
    switch ( collDir )
    {
      case COLL_O:
        return crtCell.toSouth();
      case COLL_S:
        return crtCell.toWest();
      case COLL_W:
        return crtCell.toNorth();
      case COLL_N:
        return crtCell.toEast();
      default:
        return null;
    }
  }



  protected int getIdxToLeft( int collDir )
  {
    switch ( collDir )
    {
      case COLL_O:
        return STRIP_WN;
      case COLL_S:
        return STRIP_NO;
      case COLL_W:
        return STRIP_OS;
      case COLL_N:
        return STRIP_SW;
      default:
        return -1;
    }
  }



  protected int getIdxToFront( int collDir )
  {
    switch ( collDir )
    {
      case COLL_O:
        return STRIP_WO;
      case COLL_S:
        return STRIP_NS;
      case COLL_W:
        return STRIP_OW;
      case COLL_N:
        return STRIP_SN;
      default:
        return -1;
    }
  }



  protected int getIdxToRight( int collDir )
  {
    switch ( collDir )
    {
      case COLL_O:
        return STRIP_WS;
      case COLL_S:
        return STRIP_NW;
      case COLL_W:
        return STRIP_ON;
      case COLL_N:
        return STRIP_SO;
      default:
        return -1;
    }
  }



  protected int getIdxToBack( int collDir )
  {
    switch ( collDir )
    {
      case COLL_O:
        return STRIP_WW;
      case COLL_S:
        return STRIP_NN;
      case COLL_W:
        return STRIP_OO;
      case COLL_N:
        return STRIP_SS;
      default:
        return -1;
    }
  }
}

/* $Id: Rat.java 115 2008-12-31 17:02:19Z binzm $
 *
 * Copyright (c) 1996 Michael G. Binz
 */

package de.michab.apps.ratgame;

import java.applet.Applet;
import java.awt.Color;



/**
 * Implements rat intelligence. Rats know how to move
 * in the underlying AnimationGrid.
 */
public abstract class Rat
  extends
    RatLike
{
  private static String ourName = "Rat";

  private static boolean Debug = false;



  public Rat( AnimationGrid ag, String name )
  {
    super( ag, (null != name) ? name : ourName );
  }
  public Rat( Applet app )
  {
    super( null, ourName );
  }



  /**
   * Receives collision signals and contains navigation system.
   *
   * The rat always follows the wall at its right side.  If going
   * down, a fall sequence starts.  In this case the rat falls until
   * it reaches some obstacle, where it turns east, if possible.
   *
   * The GridPosition argument should change to an AnimatedCell
   * reference to stop the repeated dereferencing...
   */
  public void signal( int sigval, AnimatedCell currentCell, PictureStrip ps )
  {
    if ( Debug )
      System.out.println( "Rat: Collision signal: " + sigval + " "
                          + ps.getName() );

    // we try to pass from currentCell to targetCell.
    AnimatedCell targetCell  = getFrontCell( sigval, currentCell ),
                 secTarget;

    // in case we collide, this is the one we hit
    Existence opponent = null;
    // this is needed in case of an collision
    int stripIdx;
    // Array of our picture strips
    PictureStrip[] strip = getStripArray();


    // if our target cell is not reachable, do hard turn
    if ( null == targetCell )
    {
      currentCell.addStrip( this, strip[ getIdxToBack( sigval ) ], 4, 12, true );
      // Since we forced the add no collision possible, bail out
      return;
    }
    // we are in a free fall...
    else if ( COLL_S == sigval )
    {
      /* This is special logic for handling free
       * fall and the continuation after hitting the
       * floor.
       */
      // go forward if possible...
      if ( null != (secTarget = getFrontCell( sigval, targetCell ))
           && !preventCollision( secTarget.getExistence() ) )
        stripIdx = getIdxToFront( sigval );
      // or turn left
      else if ( null != (secTarget = getLeftCell( sigval, targetCell ))
                && !preventCollision( secTarget.getExistence() ) )
        stripIdx = getIdxToLeft( sigval );
      // or right
      else if ( null != (secTarget = getRightCell( sigval, targetCell ))
                && !preventCollision( secTarget.getExistence() ) )
        stripIdx = getIdxToRight( sigval );
      // no way out -- straight with turn in next cycle
      else
        stripIdx = getIdxToFront( sigval );
    }
    // can we turn right?
    else if ( null != (secTarget = getRightCell( sigval, targetCell ))
              && !preventCollision( secTarget.getExistence() ) )
      stripIdx = getIdxToRight( sigval );
    // can we go straight?
    else if ( null != (secTarget = getFrontCell( sigval, targetCell ))
              && !preventCollision( secTarget.getExistence() ) )
      stripIdx = getIdxToFront( sigval );
    // or , at least, turn left?
    else if ( null != (secTarget = getLeftCell( sigval, targetCell ))
              && !preventCollision( secTarget.getExistence() ) )
      stripIdx = getIdxToLeft( sigval );
    // really no way out. Go straight and turn in next cycle
    else
      stripIdx = getIdxToFront( sigval );

    // now try to start our new strip and check if we have a collision
    if ( null != (opponent = targetCell.addStrip( this, strip[ stripIdx ] )) )
      // yeah, we had one, decide what to do...
      if ( resolveCollision( opponent, targetCell, stripIdx, sigval ) )
        // returned true - so let's turn.
        currentCell.addStrip( this, strip[ getIdxToBack( sigval ) ], 4, 12, true );
  }



  /**
   * Should a collision with this existence be prevented?
   * Called by the navigation logic in case a cell seems to be
   * free according to the map.
   *
   * The Existence in the target cell is passed to this method.
   * If there is none, 'null' is submitted.
   *
   * On return of 'false' nothing is done to prevent the collision,
   * in the other case this way is not longer counted on by the
   * navigation logic and an other target cell has to be found.
   */
  protected boolean preventCollision( Existence inTarget )
  {
    return false;
  }



  /**
   * Decide what to do in case of an collision.  On return of true
   * the default action is to do a turn backwards.
   */
  protected abstract boolean resolveCollision( Existence opponent,
                                   AnimatedCell targetCell,
                                   int stripIdx, int collDir );



  /**
   * getStripArray - abstract
   */
  protected abstract PictureStrip[] getStripArray();



  /**
   * Creates an array of PictureStrips with all slots initialized.
   */
  protected PictureStrip[] loadImages( Applet app, String prefix )
  {
    DropColour dc = new DropColour( Color.white );

    // Array erzeugen...
    PictureStrip[] strip = new PictureStrip[ STRIP_USER ];

    // ...und Images laden
    (strip[ STRIP_NO ] = new PictureStrip( app, prefix + "no.gif", "no", dc ))
      .addSignal( 4, COLL_O );

    (strip[ STRIP_NS ] = new PictureStrip( app, prefix + "ns.gif", "ns", dc ))
      .addSignal( 4, COLL_S );

    (strip[ STRIP_NW ] = new PictureStrip( app, prefix + "nw.gif", "nw", dc ))
      .addSignal( 4, COLL_W );

    (strip[ STRIP_ON ] = new PictureStrip( app, prefix + "on.gif", "on", dc ))
      .addSignal( 4, COLL_N );

    (strip[ STRIP_OS ] = new PictureStrip( app, prefix + "os.gif", "os", dc ))
      .addSignal( 4, COLL_S );

    (strip[ STRIP_OW ] = new PictureStrip( app, prefix + "ow.gif", "ow", dc ))
      .addSignal( 4, COLL_W );

    (strip[ STRIP_SN ] = new PictureStrip( app, prefix + "sn.gif", "sn", dc ))
      .addSignal( 4, COLL_N );

    (strip[ STRIP_SO ] = new PictureStrip( app, prefix + "so.gif", "so", dc ))
      .addSignal( 4, COLL_O );

    (strip[ STRIP_SW ] = new PictureStrip( app, prefix + "sw.gif", "sw", dc ))
      .addSignal( 4, COLL_W );

    (strip[ STRIP_WN ] = new PictureStrip( app, prefix + "wn.gif", "wn", dc ))
      .addSignal( 4, COLL_N );

    (strip[ STRIP_WO ] = new PictureStrip( app, prefix + "wo.gif", "wo", dc ))
      .addSignal( 4, COLL_O );

    (strip[ STRIP_WS ] = new PictureStrip( app, prefix + "ws.gif", "ws", dc ))
      .addSignal( 4, COLL_S );

    (strip[ STRIP_WW ] = new PictureStrip( app, prefix + "ww.gif", "ww", dc ))
      .addSignal( 8, COLL_W );

    (strip[ STRIP_NN ] = new PictureStrip( app, prefix + "nn.gif", "nn", dc ))
      .addSignal( 8, COLL_N );

    (strip[ STRIP_OO ] = new PictureStrip( app, prefix + "oo.gif", "oo", dc ))
      .addSignal( 8, COLL_O );

    (strip[ STRIP_SS ] = new PictureStrip( app, prefix + "ss.gif", "ss", dc ))
      .addSignal( 8, COLL_S );

    return strip;
  }
}

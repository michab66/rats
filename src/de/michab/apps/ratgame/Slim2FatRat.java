/* $Id: Slim2FatRat.java 115 2008-12-31 17:02:19Z binzm $
 *
 * Copyright (c) 1996 Michael G. Binz
 */

package de.michab.apps.ratgame;

import java.applet.Applet;
import java.awt.Color;



/**
 * Just a one step animation rat used for the transformation
 * from the slim to fat after consuming a piece of cheese.
 *
 * Only a horizontal transformation is possible.
 */
public class Slim2FatRat
  extends
    RatLike
{
  private static String ourName = "Slim2FatRat";
  private static boolean Debug = false;

  static private PictureStrip[] strip;




  public Slim2FatRat( AnimationGrid ag, GridPosition p, int whatStrip )
  {
    super( ag, ourName );

    if ( null == strip || null == strip[ whatStrip ] )
    {
      System.out.println( "Slim2FatRat: strip or strip idx invalid..." );
      return;
    }
    ag.addStrip( this, p, strip[ whatStrip ] );
  }
  public Slim2FatRat( Applet app )
  {
    super( null, ourName );

    if ( null == strip )
      initImages( app );
  }



  /**
   * signal
   *
   * Receives collision signals and contains navigation system.
   */
  public void signal( int sigval, AnimatedCell currentCell, PictureStrip ps )
  {
    // we try to pass from currentCell to targetCell.
    AnimatedCell targetCell  = getFrontCell( sigval, currentCell ),
                 secTarget;

    // this is needed in case of an collision
    int stripIdx;

    // if our target cell is not reachable, do hard turn
    if ( null == targetCell || null != targetCell.getExistence() )
    {
      currentCell.addStrip( this,
                            strip[ getIdxToBack( sigval ) ],
                            4, 12, true );
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
      if ( (null != (secTarget = getFrontCell( sigval, targetCell )))
              && !(secTarget.getExistence() instanceof StopTheFat) )
        stripIdx = getIdxToFront( sigval );
      // or turn left
      else if ( (null != (secTarget = getLeftCell( sigval, targetCell )))
              && !(secTarget.getExistence() instanceof StopTheFat) )
        stripIdx = getIdxToLeft( sigval );
      // or right
      else if ( (null != (secTarget = getRightCell( sigval, targetCell )))
              && !(secTarget.getExistence() instanceof StopTheFat) )
        stripIdx = getIdxToRight( sigval );
      // fuck -- no way out -- straight with turn in next cycle
      else
        stripIdx = getIdxToFront( sigval );
    }
    // can we turn right?
    else if ( (null != (secTarget = getRightCell( sigval, targetCell )))
              && !(secTarget.getExistence() instanceof StopTheFat) )
      stripIdx = getIdxToRight( sigval );
    // can we go straight?
    else if ( (null != (secTarget = getFrontCell( sigval, targetCell )))
              && !(secTarget.getExistence() instanceof StopTheFat) )
      stripIdx = getIdxToFront( sigval );
    // or , at least, turn left?
    else if ( (null != (secTarget = getLeftCell( sigval, targetCell )))
              && !(secTarget.getExistence() instanceof StopTheFat) )
      stripIdx = getIdxToLeft( sigval );
    // really no way out. Go straight and turn in next cycle
    else
      stripIdx = getIdxToFront( sigval );

    // if the target cell is empty...
    if ( null == targetCell.getExistence() )
      // ...create a new rat in it
      new FatRat( home, targetCell.gridPosition, stripIdx );
    else
      // ...in the other case, turn
      currentCell.addStrip( this, strip[ getIdxToBack( sigval ) ], 4, 12, true );
  }



  /**
   * initImages
   */
  private void initImages( Applet app )
  {
    String prefix = "images/sratk";

    DropColour dc = new DropColour( Color.white );

    // Array erzeugen...
    strip = new PictureStrip[ STRIP_USER ];

    // ...und Images laden
    (strip[ STRIP_OO ] = new PictureStrip( app, prefix + "oo.gif", "s2foo", dc ))
      .addSignal( 8, COLL_O );
    (strip[ STRIP_OW ] = new PictureStrip( app, prefix + "ow.gif", "s2fow", dc ))
      .addSignal( 4, COLL_W );
    (strip[ STRIP_WO ] = new PictureStrip( app, prefix + "wo.gif", "s2fwo", dc ))
      .addSignal( 4, COLL_O );
    (strip[ STRIP_WW ] = new PictureStrip( app, prefix + "ww.gif", "s2fww", dc ))
      .addSignal( 8, COLL_W );
  }
}

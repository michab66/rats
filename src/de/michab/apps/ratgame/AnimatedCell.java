/* $Id: AnimatedCell.java 115 2008-12-31 17:02:19Z binzm $
 *
 * Copyright (c) 1996-2009 Michael G. Binz
 */

package de.michab.apps.ratgame;

import java.awt.Event;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;



/**
 * Describes one active cell of the play field, i.e. a cell which can
 * contain animation.  For constructing the cell a background image is
 * needed.  Animation is controlled by a call of the step method.
 * AnimatedCells are able to animate picture strips.
 */
public class AnimatedCell
{
  // Current animation...
  PictureStrip picStrip;
  // ...and controlling Existence
  Existence picExistence;

  /* Do we have a static animation?
   * This means that the start picture index is the same as
   * the end picture index when doing add strip.
   * As a result, the Picture is only drawn as a result of
   * a paint() event, but not for every animation cycle.
   * Another consequence is, that the controlling Existence
   * object won't receive 'terminated' signals.
   */
  private boolean picStripStatic;

  // Neighbour cells
  private AnimatedCell western,
                       northern,
                       eastern,
                       southern;

  // Is animated strip visible?
  private boolean visible;

  // Background of this cell
  private final Image imgBack;

  // Indices in PictureStrip
  int startPic,
      endPic,
      currentPic;

  // Cell coordinates
  public GridPosition gridPosition;

  // Should the cell be redrawn in next paint?
  private boolean changedFlag;

  // Set to false for final version
  private final boolean Debug = false;



  /**
   * Create a new cell
   */
  public AnimatedCell( GridPosition gp, Image background )
  {
    imgBack = background;
    gridPosition = gp;
    changedFlag = visible = true;
  }



  /**
   * Set animation to invisible in this cell
   */
  public boolean setVisible( boolean v )
  {
    boolean ret = visible;
    visible = v;
    return ret;
  }



  /**
   * Sets connections to other cells.  This could be any cells...
   */
  public void setLinks( AnimatedCell w,
                        AnimatedCell n,
                        AnimatedCell e,
                        AnimatedCell s )
  {
    western = w;
    northern = n;
    eastern = e;
    southern = s;
  }



  /**
   * toWest, toEast, toNorth, toSouth
   *
   * Returns the cell connections
   */
  public AnimatedCell toWest()
  {
    return western;
  }
  public AnimatedCell toNorth()
  {
    return northern;
  }
  public AnimatedCell toEast()
  {
    return eastern;
  }
  public AnimatedCell toSouth()
  {
    return southern;
  }



  /**
   * Executes one step of animation for this cell
   */
  public void step( int cycle )
  {
    /* If we have a strip defined for this cell, the strip
     * is not static and is not yet changed
     */
    if ( (null != picStrip) && !picStripStatic && !changedFlag )
    {
      // switch to next picture of the strip
      currentPic++;
      changedFlag = true;

      // if this was the last picture...
      if ( currentPic > endPic )
      {
        Existence    tmpExistence = picExistence;
        PictureStrip tmpStrip = picStrip;
        // ...remove the object...
        picExistence = null;
        picStrip = null;
        // ...and send termination
        tmpExistence.terminated( tmpStrip, this );
      }
      else
      {
        // if a signal is defined for the current picture...
        int sig = picStrip.hasSignal( currentPic );
        if ( sig != 0 )
          // ...send it
          picExistence.signal( sig, this, picStrip );
      }
    }
  }



  /**
   * Places a PictureStrip object into this cell, which will be
   * animated in the following animation cycles.
   * If a picture strip exists when trying to add a new one this
   * won't be overwritten.  If you want to overwrite existing
   * picture strips, call add strip with 'force=true'.
   * This method returns a reference to the controlling Existence
   * of the picture strip which was set prior to the addStrip call
   * or 'null' if there wasn't any.
   *
   * In case the start and end indices are the same, this strip
   * is added as static, which results in more efficient redraw
   * cycles but prevents the strip from receiving 'terminated'
   * signals.
   *
   * If a strip exists in the current cell, this is returned.
   */
  public Existence addStrip( Existence x,
                             PictureStrip is,
                             int start, int end, boolean force )
  {
    // If there doesn't exist an Existence or force is selected...
    if ( (null == picStrip) || force )
    {
      // ...remember old Existence...
      Existence old = picExistence;

      // ...set the new one...
      picExistence = x;
      picStrip = is;

      // ...init the picture indices...
      startPic = currentPic = start;
      endPic = end;
      // ...in the next cycle this cell has to be redrawn...
      changedFlag = true;
      // ...but only once, if this is a static strip...
      picStripStatic = (start == end);

      // ...return the previous Existence object
      return old;
    }
    else
      /* we had an active Existence in this cell, return it
       * without setting the new one
       */
      return picExistence;
  }
  public Existence addStrip( Existence x, PictureStrip is, int start, int end )
  {
    return addStrip( x, is, start, end, false );
  }
  public Existence addStrip( Existence x, PictureStrip s )
  {
    return addStrip( x, s, 0, s.getLength()-1, false );
  }
  public Existence addStrip( Existence x, PictureStrip s, boolean force )
  {
    return addStrip( x, s, 0, s.getLength()-1, force );
  }



  /**
   * getExistence
   *
   */
  public Existence getExistence()
  {
    return picExistence;
  }



  /**
   * mouseDown
   */
  public boolean mouseDown( Event e, int x, int y, GridPosition gp )
  {
    if ( null != picExistence )
      return picExistence.mouseDown( e, x, y, gp );
    else
      return false;
  }



  /**
   * mouseDrag
   */
  public boolean mouseDrag( Event e, int x, int y, GridPosition gp )
  {
    if ( null != picExistence )
      return picExistence.mouseDrag( e, x, y, gp );
    else
      return false;
  }



  /**
   * mouseUp
   */
  public boolean mouseUp( Event e, int x, int y, GridPosition gp )
  {
    if ( null != picExistence )
      return picExistence.mouseUp( e, x, y, gp );
    else
      return false;
  }



  /**
   * paint
   *
   * Draws one cell if its contents has changed
   */
  public void paint( ImageObserver o, Graphics gfx, int x, int y, boolean force )
  {
    if ( changedFlag || force )
    {
      // draw background
      gfx.drawImage( imgBack, x, y, o );
      // in debug mode draw cell frame
      if ( Debug )
        gfx.drawRect( x, y, 28, 28 );

      // if we have a visible object...
      if ( null != picStrip && visible )
        // ...draw it onto the background
        gfx.drawImage( picStrip.Pics[ currentPic ], x, y, o );

      changedFlag = false;
    }
  }
  public void paint( ImageObserver o, Graphics gfx, int x, int y )
  {
    paint( o, gfx, x, y, false );
  }
}

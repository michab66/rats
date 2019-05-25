/* $Id: CloneableCheese.java 115 2008-12-31 17:02:19Z binzm $
 *
 * Copyright (c) 1996 Michael G. Binz
 */

package de.michab.apps.ratgame;

import java.applet.Applet;
import java.awt.Event;



/**
 * Cheese can be moved with the mouse.  Maintains a source object.
 */
public class CloneableCheese
  extends
    Cheese
{
  private final GridPosition homePosition;
  private GridPosition crtPosition;



  /**
   * Supports creation on an AnimationGrid
   */
  public CloneableCheese( Applet app, AnimationGrid ag, GridPosition p )
  {
    super( app, ag, p );
    homePosition = crtPosition = p;
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
    // If our new position is not our home position...
    if ( ! p.equals( homePosition ) )
    {
      // ... move back to our home position...
      mouseDrag( evt, x, y, homePosition );
      // ..and place a new object at the target position if allowed
      new DragableCheese( null, home, p );
    }

    return true;
  }
}

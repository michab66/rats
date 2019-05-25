/* $Id: ResetButton.java 115 2008-12-31 17:02:19Z binzm $
 *
 * Copyright (c) 1997 Michael G. Binz
 */

package de.michab.apps.ratgame;

import java.applet.Applet;
import java.awt.Event;



/**
 * Triggers BattleField rest.
 */
public class ResetButton
  extends
    XButton
{
  public ResetButton( Applet app, AnimationGrid ag, GridPosition p )
  {
    super( app, ag, p );
  }



  public boolean mouseUp( Event e, int x, int y, GridPosition p )
  {
    boolean ret = super.mouseUp( e, x, y, p );
    System.out.println( "Resetting playfield..." );
    home.reset();
    return ret;
  }
}

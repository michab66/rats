/* $Id: HelpScreen.java 115 2008-12-31 17:02:19Z binzm $
 *
 * Copyright (c) 1997 Michael G. Binz
 */

package de.michab.apps.ratgame;



/**
 *
 * @author Michael Binz
 */
public class HelpScreen
  extends
    AnimationGrid
{
  public HelpScreen( World w )
  {
    // calls reset
    super( w );
  }



  /**
   * Resets the grid images and the underlying map
   */
  public void reset()
  {
    super.reset();

    // Place objects in their positions
    new Pill( null, this, new GridPosition( 20, 3 ) );
    new Cheese(  null, this, new GridPosition( 20, 5 ) );
    new Wippe( null, this, new GridPosition( 20, 7 ) );
    new StopTheFat( null, this, new GridPosition( 20, 9 ) );
    new Block( null, this, new GridPosition( 20, 11 ) );

    new RatSource( null, this, new GridPosition( 20, 12 ) );
    new Latch( null, this, new GridPosition( 1, 13 ),
                               new GridPosition( 0, 13 ) );

    new XButton( null, this, new GridPosition( 13, 15 ) );
    new XButton( null, this, new GridPosition( 17, 15 ) );
  }
}

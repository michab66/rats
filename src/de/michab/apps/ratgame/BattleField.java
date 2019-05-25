/* $Id: BattleField.java 115 2008-12-31 17:02:19Z binzm $
 *
 * Copyright (c) 1997 Michael G. Binz
 */

package de.michab.apps.ratgame;



/**
 *
 * @author Michael Binz
 */
public class BattleField
  extends
    AnimationGrid
{
  public BattleField( World w )
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
    new CloneablePill( null, this, new GridPosition( 20, 3 ) );
    new CloneableCheese(  null, this, new GridPosition( 20, 5 ) );
    new CloneableWippe( null, this, new GridPosition( 20, 7 ) );
    new CloneableStopTheFat( null, this, new GridPosition( 20, 9 ) );
    new CloneableBlock( null, this, new GridPosition( 20, 11 ) );

    new RatSource( null, this, new GridPosition( 20, 12 ) );
    new Latch( null, this, new GridPosition( 1, 13 ),
                               new GridPosition( 0, 13 ) );

    new ResetButton( null, this, new GridPosition( 17, 15 ) );
    new XButton( null, this, new GridPosition( 13, 15 ) );

    // Patch map entry
    AnimatedCell tC = cells[ 18 ][ 12 ];

    tC.setLinks( cells[ 18-1 ][ 12 ],
                 null,
                 null,
                 cells[ 18 ][ 12+2 ] );
  }
}

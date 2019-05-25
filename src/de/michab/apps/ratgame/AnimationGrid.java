/* $Id: AnimationGrid.java 115 2008-12-31 17:02:19Z binzm $
 *
 * Copyright (c) 1996,97 Michael G. Binz
 */

package de.michab.apps.ratgame;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;



/**
 * Describes the play field.
 */
public class AnimationGrid
{
  private final World world;
  private final int cellWidth, cellHeight;

  protected AnimatedCell[][] cells;

  // All we need for offscreen drawing
  private Image imgOffScreen;



  public AnimationGrid( World w )
  {
    world = w;

    Dimension wdim = world.getDimensionInCells();
    cellWidth = wdim.width;
    cellHeight = wdim.height;

    cells = new AnimatedCell[ cellWidth ][ cellHeight ];

    // Initialise battlefield data
    reset();
  }



  /**
   * reset
   *
   * Resets the grid images and the underlying map
   */
  public void reset()
  {
    int x, y;

    imgOffScreen = world.getOffscreenImage();

    // init the animation cells
    for ( x = 0 ; x < cellWidth ; x++ )
      for ( y = 0 ; y < cellHeight ; y++ )
        cells[ x ][ y ] = new AnimatedCell( new GridPosition( x, y ),
                                            world.getCellImage( x, y ) );

    // create cell connections
    if ( world.hasCellConnections() )
      for ( x = 0 ; x < cellWidth ; x++ )
        for ( y = 0 ; y < cellHeight ; y++ )
        {
          byte c = world.getCellConnections( x, y );

          cells[ x ][ y ].setLinks(
            (c & World.WEST)  != 0 ? cells[ x-1 ][ y ] : null,
            (c & World.NORTH) != 0 ? cells[ x ][ y-1 ] : null,
            (c & World.EAST)  != 0 ? cells[ x+1 ][ y ] : null,
            (c & World.SOUTH) != 0 ? cells[ x ][ y+1 ] : null );
          cells[ x ][ y ].setVisible( (c & World.INVISIBLE) == 0 );
        }
  }



  /**
   * getOverallDimensionInPixels
   *
   * Returns this AnimationGrid's World size including
   * the passive frame 'round the play cells.
   * Useful for resizing the applet containign this grid
   */
  public Dimension getOverallDimensionInPixels()
  {
    return world.getDimensionInPixels();
  }



  /**
   * transPixelToCellCoo
   *
   * Transform pixel coordinate to cell coordinate.
   */
  public GridPosition transPixelToCell( int x, int y )
  {
    // Just pipe this to our world
    return world.transPixelToCell( x, y );
  }



  /**
   * addStrip
   *
   * place a picure strip in an animation cell
   */
  public Existence addStrip( Existence x,
                             GridPosition p,
                             PictureStrip s )
  {
    return cells[ p.x ][ p.y ].addStrip( x, s );
  }
  public Existence addStrip( Existence x,
                             GridPosition p,
                             PictureStrip s,
                             boolean force )
  {
    return cells[ p.x ][ p.y ].addStrip( x, s, force );
  }
  public Existence addStrip( Existence x,
                             GridPosition p,
                             PictureStrip s,
                             int start, int stop )
  {
    return cells[ p.x ][ p.y ].addStrip( x, s, start, stop );
  }
  public Existence addStrip( Existence x,
                             GridPosition p,
                             PictureStrip s,
                             int start, int stop, boolean force )
  {
    return cells[ p.x ][ p.y ].addStrip( x, s, start, stop, force );
  }




  /**
   * removeStrip
   *
   */
  public void removeStrip( GridPosition p )
  {
    cells[ p.x ][ p.y ].addStrip( null, null, 0, 0, true );
  }



  /**
   * getExistence
   *
   */
  public Existence getExistence( GridPosition p )
  {
    return cells[ p.x ][ p.y ].getExistence();
  }



  /**
   * step
   *
   * Execute one animation cycle
   */
  public void step( int cycle )
  {
    for ( int x = 0 ; x < cellWidth ; x++ )
      for ( int y = 0 ; y < cellHeight ; y++ )
        cells[ x ][ y ].step( cycle );
  }



  /**
   * getCell
   *
   */
  public AnimatedCell getCell( GridPosition p )
  {
    return cells[ p.x ][ p.y ];
  }



  /**
   * paint
   *
   * Is called after part of the displayed graphics is
   * destroyed.  All cells have to be repainted.
   */
  public void paint( Graphics g )
  {
    // Redraw playfield
    imgOffScreen.getGraphics().drawImage( world.getBackground(), 0, 0, null );
    // Draw cells
    updateHandler( imgOffScreen.getGraphics(), true );
    // One-step output to visible graphics object
    g.drawImage( imgOffScreen, 0, 0, null );
  }



  /**
   * update
   *
   * Is called after repaint() is called from the animation thread.
   * Only cells changed have to be redrawn.
   */
  public void update( Graphics g )
  {
    // Draw cells
    updateHandler( imgOffScreen.getGraphics(), false );
    // One-step output to visible graphics object
    g.drawImage( imgOffScreen, 0, 0, null );
  }



  /**
   * updateHandler
   *
   * Does the real repainting of the cells.  Called by paint() and
   * update() with an offscreen graphics object.
   */
  private void updateHandler( Graphics g, boolean force )
  {
    int magic = world.cellDimension;

    /* our playfield has to be repainted from the
     * right bottom to left top to allow for multi
     * cell objects without additional repaint logic
     */
    for ( int y = cellHeight-1 ; y >= 0 ; y-- )
      for ( int x = cellWidth-1 ; x >= 0 ; x-- )
        cells[ x ][ y ].paint( null,
                               g,
                               world.pixOffsetToCellsX + (x * magic),
                               world.pixOffsetToCellsY + (y * magic),
                               force );
  }
}



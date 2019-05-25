/* $Id: TheGame2.java 2121 2019-01-02 10:18:08Z Michael $
 *
 * Applet event dispatch.
 *
 * Copyright (c) 1996, 97 Michael G. Binz
 */

package de.michab.apps.ratgame;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Event;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;



/**
 *
 */
public class TheGame2
extends
Applet
implements
Runnable
{
    /* Definition of the cell dimensions in pixels. A number
     * of objects in the animation engine depend on this.
     */
    final static int cellDim = 28;

    // current cycle number
    private int cycle;

    // preload vector
    java.util.Vector preLoad;

    private Thread animationThread;

    private AnimationGrid gameGrid;
    private AnimationGrid helpGrid;
    private AnimationGrid activeGrid;

    private AnimatedCell grabbedCell;
    private Existence    grabbedExistence;

    private final static boolean Debug = false;


    public TheGame2()
    {
        init();
    }

    @Override
    public URL getDocumentBase()
    {
        return null;
    }

    @Override
    public Image getImage( URL url, String name )
    {
        try ( InputStream is =
                getClass().getResourceAsStream( "/" + name ) )
        {

            Toolkit.getDefaultToolkit().
            List<Byte> bytes = new ArrayList<>();

            while ( true )
            {
                int current = is.read();
                if ( current == -1 )
                    break;
                bytes.add( (byte)current );
            }

            byte[] buffer = new byte[bytes.size()];

            for ( int i = 0 ; i < bytes.size() ; i++ )
                buffer[i] = bytes.get( i );

            return new ImageIcon( buffer ).getImage();
        }
        catch ( Exception e )
        {
            System.err.println( "Name: " + name );
            e.printStackTrace();
            System.exit( 1 );
            return null;
        }

    }

    @Override
    public Image createImage( int width, int height )
    {
        BufferedImage result = new BufferedImage(
                width,
                height,
                BufferedImage.TYPE_INT_RGB );

        return result;
    }

    @Override
    public void showStatus( String msg )
    {
        System.out.println( msg );
    }

    @Override
    public void init()
    {
        debugOut( "init called" );

        // Load title graphics
        activeGrid = new AnimationGrid(
                new World( this, 0, 0, cellDim, "images/titel" )
                );

        // Init preload vector
        preLoad = new java.util.Vector();

        // Resize the applet dimensions
        resize( activeGrid.getOverallDimensionInPixels() );

        // Reset our cycle number
        cycle = 0;

        // Start the animation thread
        (animationThread = new Thread( this ))
        .start();
    }



    @Override
    public void destroy()
    {
        debugOut( "Applet: destroy" );

        if ( null != animationThread )
        {
            animationThread.stop();
            animationThread = null;
        }

        activeGrid  = null;
    }



    @Override
    public void start()
    {
        debugOut( "Applet: start" );

        if ( null != animationThread )
            animationThread.resume();
    }



    @Override
    public void stop()
    {
        debugOut( "Applet: stop" );

        if ( null != animationThread )
            animationThread.suspend();
    }



    @Override
    public void paint( Graphics g )
    {
        debugOut( "Applet: paint" );

        // repaint only if we really have something to paint
        if ( null != activeGrid )
            activeGrid.paint( g );
    }



    @Override
    public void update( Graphics g )
    {
        debugOut( "Applet: update" );

        activeGrid.update( g );
    }



    @Override
    public boolean mouseDown( Event e, int x, int y )
    {
        debugOut( "Applet: mouseDown" );

        if ( activeGrid instanceof HelpScreen )
        {
            debugOut( "we are in help screen" );
            activeGrid  = gameGrid;
            repaint();
            return true;
        }

        GridPosition gp  = activeGrid.transPixelToCell( x, y );

        // no valid cell position -- bail out
        if ( null == gp )
            return false;

        // set the mouse grab
        grabbedCell = activeGrid.getCell( gp );
        grabbedExistence = grabbedCell.getExistence();


        // Middle mouse button
        if ( (e.modifiers & Event.ALT_MASK) != 0 )
        {
            String out = "clicked cell: " + gp + " ";

            out = out +
                    ((null == grabbedCell.toWest()) ? "" : "west ") +
                    ((null == grabbedCell.toNorth()) ? "" : "north ") +
                    ((null == grabbedCell.toEast()) ? "" : "east ") +
                    ((null == grabbedCell.toSouth()) ? "" : "south");
            showStatus( out );
        }

        else
        {
            boolean ret;

            if ( null != grabbedExistence )
                ret = grabbedExistence.mouseDown( e, x, y, gp );
            else if ( null != grabbedCell )
                ret = grabbedCell.mouseDown( e, x, y, gp );
            else
                ret = false;

            return ret;
        }

        return true;
    }



    @Override
    public boolean mouseDrag( Event e, int x, int y )
    {
        debugOut( "Applet: mouseDrag" );

        GridPosition gp  = activeGrid.transPixelToCell( x, y ) ;

        // no valid cell position -- bail out
        if ( null == gp )
            return false;

        boolean ret;

        if ( null != grabbedExistence )
            ret = grabbedExistence.mouseDrag( e, x, y, gp );
        else if ( null != grabbedCell )
            ret = grabbedCell.mouseDrag( e, x, y, gp );
        else
            ret = false;

        return ret;
    }



    @Override
    public boolean mouseUp( Event e, int x, int y )
    {
        debugOut( "Applet: mouseUp" );

        GridPosition gp  = activeGrid.transPixelToCell( x, y ) ;

        // no valid cell position -- bail out
        if ( null == gp )
            return false;

        boolean ret;

        // if there is an existence grabbed...
        if ( null != grabbedExistence )
            // ...send it the event
            ret = grabbedExistence.mouseUp( e, x, y, gp );
        else if ( null != grabbedCell )
            // in the other case send it to the cell
            ret = grabbedCell.mouseUp( e, x, y, gp );
        else
            ret = false;

        // release the grabs
        grabbedExistence = null;
        grabbedCell = null;

        return ret;
    }



    @Override
    public boolean keyDown( Event e, int key )
    {
        if ( Event.F1 == key )
        {
            activeGrid  = helpGrid;
            repaint();
            return true;
        }

        return true;
    }



    @Override
    public String getAppletInfo()
    {
        return "TheRatGame\n" +
                "\n Game design, graphics and animation:  Andre Kinne\n" +
                "\n Java programming: Michael Binz\n" +
                "\n\n  Copyright (c) 1996, 1997 Michael G. Binz & Andre Kinne";
    }



    /**
     * run
     *
     * Two step logic: The first step preloads all graphics and
     * if done so switches the AnimationGrid data.
     *
     * After having done this the animation loop for the game starts
     */
    @Override
    public void run()
    {
        try
        {
            // Preload creatures
            preLoad.addElement( new EndRat( this ) );
            preLoad.addElement( new SlimRat( this ) );

            preLoad.addElement( new Slim2FatRat( this ) );
            preLoad.addElement( new Slim2SlimRat( this ) );
            preLoad.addElement( new Slim3WippeRat( this ) );
            preLoad.addElement( new Slim3STFRat( this ) );

            preLoad.addElement( new FatRat( this ) );
            preLoad.addElement( new Fat2SlimRat( this ) );
            preLoad.addElement( new Fat2FatRat( this ) );
            preLoad.addElement( new Fat3WippeRat( this ) );

            preLoad.addElement( new Fluff( this ) );
            preLoad.addElement( new XButton( this ) );
            preLoad.addElement( new Block( this ) );
            preLoad.addElement( new Pill( this ) );
            preLoad.addElement( new Cheese( this ) );
            preLoad.addElement( new Wippe( this ) );
            preLoad.addElement( new StopTheFat( this ) );

            preLoad.addElement( new RatSource( this ) );
            preLoad.addElement( new Latch( this ) );
            preLoad.addElement( new Target( this ) );

            // Preload background and game map
            gameGrid = new BattleField(
                    new World( this, 27, 32, cellDim, "images/screen" )
                    );
            helpGrid = new HelpScreen(
                    new World( this, 27, 32, cellDim, "images/info" )
                    );


            // Now do a garbage collection
            showStatus( "Loader terminated: Collecting garbage..." );
            System.gc();

            // Preload done, switch playground
            activeGrid  = helpGrid;
            repaint();


            /* Step 2: Animation loop */
            showStatus( "Animation thread starts..." );
            while ( true )
            {
                // get the starting time for this step
                long startTime = System.currentTimeMillis();
                // execute one animation step
                activeGrid.step( cycle++ );
                // print time needed for this step
                showStatus( "Cycle time: " + (System.currentTimeMillis() - startTime) + "ms" );
                repaint();

                try
                {
                    long sleepTime = 200 - (System.currentTimeMillis() - startTime);

                    if ( sleepTime <= 0 )
                        sleepTime = 1;

                    Thread.sleep( sleepTime );
                }
                catch ( InterruptedException e )
                {}
            }
        } catch ( Throwable t )
        {
            showStatus( "Exception: " + t );
        }
    }



    private void debugOut( String err, boolean force )
    {
        if ( Debug || force )
            System.out.println( err );
    }
    private void debugOut( String err )
    {
        debugOut( err, false );
    }

    public static void main(String argv[])
    {
        Frame f = new Frame( "Hello world!" );
        f.addWindowListener(
                new WindowAdapter(){
                    @Override
                    public void windowClosing( WindowEvent e ){ System.exit( 0 ); } } );

        TheGame2 game = new TheGame2();

        f.add( game, BorderLayout.CENTER );

        f.setSize( 300, 100 );
        f.setVisible( true );
    }
}

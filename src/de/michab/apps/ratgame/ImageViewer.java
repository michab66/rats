
package de.michab.apps.ratgame;

import java.awt.Event;
import java.awt.Frame;
import java.awt.Image;

public class ImageViewer
  extends
    Frame
{
  ImageCanvas ic;

  public ImageViewer( Image leImage )
  {
    this( leImage, "" );
  }
  public ImageViewer( Image leImage, String title )
  {
    super( title );

    ic = new ImageCanvas( leImage );

    add( ic );
    pack();
    show();
  }

   public boolean handleEvent( Event e )
   {
      boolean evtHandled = false;

      if ( e.id == Event.WINDOW_DESTROY )
      {
        dispose();
        return true;
      }
      else
        return super.handleEvent( e );
    }



   static public void main( String[] argv )
   {
   }
}

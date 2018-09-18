package com.aemmie.vk.NewClasses;

import com.aemmie.vk.music.Player;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicSliderUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class NewSliders extends BasicSliderUI
{
    //public static final ImageIcon BG_LEFT_ICON =
    //        new ImageIcon("G:/icons/lel.png");
    //new ImageIcon ( MySliderUI.class.getResource ( "icons/bg_left.png" ) );
    //public static final ImageIcon BG_MID_ICON =
    //        new ImageIcon("G://icons//bg_left.png");
    //      new ImageIcon ( MySliderUI.class.getResource ( "G:/icons/bg_mid.png" ) );
    //public static final ImageIcon BG_RIGHT_ICON =
    //        new ImageIcon("G://icons//aaa.png");
    //     new ImageIcon ( MySliderUI.class.getResource ( "G:/icons/bg_right.png" ) );
    public static final ImageIcon BG_FILL_ICON =
            new ImageIcon("icons/fill2.png");
    //    new ImageIcon ( MySliderUI.class.getResource ( "G:/icons/bg_fill.png" ) );
    public static final ImageIcon GRIPPER_ICON =
            new ImageIcon("icons/sdr1.png");
    //    new ImageIcon ( MySliderUI.class.getResource ( "G:/icons/gripper.png" ) );
    public static final ImageIcon GRIPPER_PRESSED_ICON =
            new ImageIcon("icons/sdr1.png");
    //    new ImageIcon ( MySliderUI.class.getResource ( "G:/icons/gripper_pressed.png" ) );
    public static final ImageIcon BG_FILL_ICON1 =
            new ImageIcon("icons/fill2_2.png");
   // public static final ImageIcon BG_FILL_LR =
    //        new ImageIcon("G:/icons/LRFILL.png");;

            int current_x = 0;

    public NewSliders(final JSlider b )
    {
        super ( b );





        // Для корректной перерисовки слайдера
        b.addChangeListener ( new ChangeListener()
        {
            public void stateChanged ( ChangeEvent e )
            {
                b.repaint ();

            }
        } );
        b.addMouseListener ( new MouseAdapter()
        {
            public void mousePressed ( MouseEvent e )
            {
                b.repaint ();
            }

            public void mouseReleased ( MouseEvent e )
            {
                b.repaint ();
            }
        } );
    }

    // Возвращаем новый размер гриппера
    protected Dimension getThumbSize ()
    {
        return new Dimension ( GRIPPER_ICON.getIconWidth (), GRIPPER_ICON.getIconHeight ());
    }

    // Отрисовываем сам гриппер в необходимом месте
    public void paintThumb ( Graphics g )
    {

        int positionX = thumbRect.x + thumbRect.width /2;

        int positionY = thumbRect.y + thumbRect.height/2;
        float value = (float) ((positionX-25)* 100/150);
        if (positionX < 25) {
            //current_x = 0;
            g.drawImage(isDragging() ? GRIPPER_PRESSED_ICON.getImage() : GRIPPER_ICON.getImage(),
                    26 - GRIPPER_ICON.getIconWidth() / 2,
                    positionY - GRIPPER_ICON.getIconHeight() / 2, null);
            Player.updateVolume(0);
        } else if (positionX > 175)
        {
            //current_x = 150;
            g.drawImage(isDragging() ? GRIPPER_PRESSED_ICON.getImage() : GRIPPER_ICON.getImage(),
                    174 - GRIPPER_ICON.getIconWidth() / 2,
                    positionY - GRIPPER_ICON.getIconHeight() / 2, null);
            Player.updateVolume(100);
        } else {
            //current_x = thumbRect.x + thumbRect.width /2 -25;
            g.drawImage(isDragging() ? GRIPPER_PRESSED_ICON.getImage() : GRIPPER_ICON.getImage(),
                    positionX - GRIPPER_ICON.getIconWidth() / 2,
                    positionY - GRIPPER_ICON.getIconHeight() / 2, null);
            Player.updateVolume(Math.round(value));
            //System.out.println(Math.round(value));
        }


/*
        {
            System.out.println("x = " + positionX);
            float value = (float) ((positionX-25)* 100/150);

            Player.updateVolume(Math.round(value));
            System.out.println("lol  " + value);

            System.out.println("dwsdw " + Math.round(value));
            g.drawImage(isDragging() ? GRIPPER_PRESSED_ICON.getImage() : GRIPPER_ICON.getImage(),
                    positionX - GRIPPER_ICON.getIconWidth() / 2,
                    positionY - GRIPPER_ICON.getIconHeight() / 2, null);

        }
*/
    }

    // Отрисовываем «путь» слайдера
    public void paintTrack ( Graphics g )
    {
        if ( slider.getOrientation () == JSlider.HORIZONTAL )
        {
            // Заполнение участка пути справа от гриппера
            //System.err.println(slider.getValue() + "   " + trackRect.x);
            g.drawImage(BG_FILL_ICON1.getImage(), 25,
                    trackRect.y + trackRect.height / 2 - BG_FILL_ICON.getIconHeight() / 2,
                    slider.getWidth() - 50   ,
                    BG_FILL_ICON1.getIconHeight(), null);

            // Сам путь

            /*
            g.drawImage ( BG_MID_ICON.getImage (), trackRect.x + BG_LEFT_ICON.getIconWidth (),
                    trackRect.y + trackRect.height / 2 - BG_MID_ICON.getIconHeight () / 2,
                    trackRect.width - BG_LEFT_ICON.getIconWidth () - BG_RIGHT_ICON.getIconWidth (),
                    BG_MID_ICON.getIconHeight (), null );

            g.drawImage ( BG_RIGHT_ICON.getImage (),
                    trackRect.x + trackRect.width - BG_RIGHT_ICON.getIconWidth (),
                    trackRect.y + trackRect.height / 2 - BG_RIGHT_ICON.getIconHeight () / 2, null );

            */
            // Заполнение участка пути слева от гриппера

            if ( thumbRect.x + thumbRect.width /2 < 25)
            {
                current_x = 0;
            } else if ( thumbRect.x + thumbRect.width /2 > 175)
            {
                current_x  = 150;
            } else
            {
                current_x = thumbRect.x + thumbRect.width /2 -25;
            }

            g.drawImage(BG_FILL_ICON.getImage(), 25,
                    trackRect.y + trackRect.height / 2 - BG_FILL_ICON.getIconHeight() / 2,
                    current_x,
                    BG_FILL_ICON.getIconHeight(), null);


            //g.drawImage ( BG_LEFT_ICON.getImage (), 0,
            //        trackRect.y + trackRect.height / 2 - BG_LEFT_ICON.getIconHeight () / 2, null );

        }

    }
}
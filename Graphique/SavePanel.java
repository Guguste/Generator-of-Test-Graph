import java.io.*;
import java.awt.*;
import javax.swing.*;
import javax.imageio.*;
import java.awt.event.*;
import java.awt.image.*;

public class SavePanel extends JPanel{
  private static final Dimension MAIN_SIZE = new Dimension(400, 200);
    public void paint(Graphics g) {
    Graphics2D g2d = (Graphics2D) g;
    Dimension dimension = getSize();
    g2d.drawLine(10,50,80,80);
    g2d.drawRect(130,50,80,80);
    Polygon polygon= new Polygon();
     for (int i = 0; i < 6; i++){
      polygon.addPoint((int) (300 + 50 * Math.cos(i * 2 * Math.PI / 6)),
          (int) (100 + 50 * Math.sin(i * 2 * Math.PI / 6)));
   }
   g2d.drawPolygon(polygon);
  }
  public SavePanel(){
     final JPanel p=this;
    JButton button = new JButton("Make Image");
    add(button);
    setPreferredSize(MAIN_SIZE);
    button.addActionListener(new ActionListener(){
    public void actionPerformed(ActionEvent ae){
      JFrame win = (JFrame)SwingUtilities.getWindowAncestor(p);
      Dimension size = win.getSize();
      BufferedImage image = (BufferedImage)win.createImage(size.width, size.height);
      Graphics g = image.getGraphics();
      win.paint(g);
      g.dispose();
      try      {
        ImageIO.write(image, "jpg", new File("image.jpg"));
      }
      catch (IOException e){
        e.printStackTrace();
      }
    }
  });
    }
  public static void main(String[] args)  {
    JFrame frame = new JFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.getContentPane().add(new SavePanel());
    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }
}
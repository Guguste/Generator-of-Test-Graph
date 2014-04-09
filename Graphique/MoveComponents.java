/*
Salut,

Il te faudra utiliser en complément du MouseListener , 
un MouseMotionListener pour pouvoir réagir aux mouvements de la souris. 
La classe MouseMotionAdapter combine déjà ces deux écouteurs. 
Ensuite, il faut prendre en compte les coordonnées relatives du composant déplacé par rapport aux coordonnées de la souris. 
Comme tes composants sont déplaçables à des coordonnées quelquonques, 
il faudra supprimer le layout manager, qui justement sert à gérer automatiquement les coordonnées (et les tailles) des composants.
Personnellement, j'utilise une bibliothèque qui me permet de gérer ça beaucoup plus facilement, et beaucoup d'autres choses, qui s'appelle Piccolo2D

Une base trouvée sur le net.

*/

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
 
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
 
public class MoveComponents extends JPanel {
 
    public MoveComponents() {
 
        setLayout(null); // on supprime le layout manager
 
        ComponentMove listener = new ComponentMove(this);
        for(int i=0; i<2; i++) {
            add(createComponent());
        }
        addMouseListener(listener);
        addMouseMotionListener(listener);
 
    }
 
    private final static Color[] COLORS= {Color.RED};
 
    private JComponent createComponent() {
        JPanel component=new JPanel(); // ici on peut faire n'importe quel JComponent, JLabel, par exemple
        component.setLocation((int)(Math.random()*150), (int)(Math.random()*150)); // position aléatoire
        component.setSize(10+(int)(Math.random()*100), 10+(int)(Math.random()*100)); // taille aléatoire
        component.setBackground(Color.RED /*COLORS[(int)(Math.random()*COLORS.length)]*/ ); // couleur aléatoire
        component.setEnabled(false); // les composants ne doivent pas intercepter la souris
        return component;
    }
 
    private static class ComponentMove extends MouseAdapter {
 
        private boolean move;
        private int relx;
        private JComponent component;
        private int rely;
        private Container container;
 
        public ComponentMove(Container container) {
            this.container=container;
        }
 
        @Override
        public void mousePressed(MouseEvent e) {
            if ( move ) {
                move=false; // arrêt du mouvement
                component.setBorder(null); // on  supprime la bordure noire
                component=null;
            }
            else {
                component = getComponent(e.getX(),e.getY()); // on mémorise le composant en déplacement
                if ( component!=null ) {
                    container.setComponentZOrder(component,0); // place le composant le plus haut possible
                    relx = e.getX()-component.getX(); // on mémorise la position relative
                    rely = e.getY()-component.getY(); // on mémorise la position relative
                    move=true; // démarrage du mouvement
                    component.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // on indique le composant sélectionné par une bordure noire
                }
            }
        }
 
        private JComponent getComponent(int x, int y) {
            // on recherche le premier composant qui correspond aux coordonnées de la souris
            for(Component component : container.getComponents()) {
                if ( component instanceof JComponent && component.getBounds().contains(x, y) ) {
                    return (JComponent)component;
                }
            }
            return null;
        }
 
        @Override
        public void mouseMoved(MouseEvent e) {
            if ( move ) {
                // si on déplace
                component.setLocation(e.getX()-relx, e.getY()-rely);
            }
        }
 
    }
 
    public static void main(String[] args) {
 
        JFrame frame = new JFrame("exemple déplacement");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        frame.setContentPane(new MoveComponents());
 
        frame.setSize(800, 800);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
 
    }
 
}
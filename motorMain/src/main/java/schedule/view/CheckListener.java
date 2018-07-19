package schedule.view;

import view.MyJList;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class CheckListener implements MouseListener, KeyListener {
    protected ScheduleRelation m_parent;
    protected JList m_list;
    int index=-1;
    int lastIndex=-1;
    boolean addGroup=true;

    public CheckListener(ScheduleRelation parent) {
        m_parent = parent;
        m_list = parent.jlScheduleList;
    }

    public void mouseClicked(MouseEvent e) {
//        if (e.getX() < 20)
            doCheck();
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyChar() == ' ')
            doCheck();
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }

    protected void doCheck() {
        index = m_list.getSelectedIndex();
        if (index < 0 && index==lastIndex)
            return;
        if(addGroup){
            if(lastIndex!=-1){
                ScheduleData data = (ScheduleData) m_list.getModel().getElementAt(lastIndex);
                data.invertSelected();
            }
        }
        ScheduleData data = (ScheduleData) m_list.getModel().getElementAt(index);
        data.invertSelected();
        m_list.repaint();
        m_parent.selectedIndex(index);
        lastIndex=index;
//        m_parent.recalcTotal();
    }

    public int getSelectedIndex(){
        return index;
    }
}

package schedule.view;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class CheckGroupListener implements MouseListener, KeyListener {
    protected ScheduleRelation m_parent;
    protected JList m_list;
    int index=-1;
    int lastIndex=-1;
    boolean addGroup=true;

    public CheckGroupListener(ScheduleRelation parent,JList list) {
        m_parent = parent;
        m_list = list;
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
        System.out.println("doCheck");
//        index = m_list.getSelectedIndex();
//        if (index < 0 && index==lastIndex)
//            return;
//        int[] selectedIndices = m_list.getSelectedIndices();
//        if(selectedIndices.length==0){
//            return;
//        }
        int leadSelectionIndex = m_list.getLeadSelectionIndex();
        if(leadSelectionIndex<0){
            return;
        }
        index=leadSelectionIndex;
        if(addGroup){
            if(lastIndex!=-1){
                GroupData data = (GroupData) m_list.getModel().getElementAt(lastIndex);
                data.invertSelected();
            }
        }
//        for (int i = 0; i < selectedIndices.length; i++) {
//            index=selectedIndices[i];
            GroupData data = (GroupData) m_list.getModel().getElementAt(index);
            System.out.println("doCheck "+index+"    "+data.isSelected());
            data.invertSelected();
            System.out.println("doCheck "+index+"    "+data.isSelected());
//        }
//        m_list.repaint();
        lastIndex=index;
//        m_parent.recalcTotal();
    }

    public int getSelectedIndex(){
        return index;
    }
}

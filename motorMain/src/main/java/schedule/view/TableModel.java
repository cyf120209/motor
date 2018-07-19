package schedule.view;

import util.Public;
import view.Schedule;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class TableModel extends AbstractTableModel {

    private Vector vector = new Vector();
    private String[] columnNames = {"","Time", "Percent", "Weeks","ScheduleName"};

    /**
     * ------------------------------------------------------
     * <p>
     * 重写父类中的抽象方法
     * <p>
     * <p>
     * <p>
     * 获得表格中的列数
     */

    @Override
    public int getColumnCount() {
        // TODO Auto-generated method stub
//        System.out.println("getColumnCount()");
        return columnNames.length;
    }

    @Override
    public int getRowCount() {
        // TODO Auto-generated method stub
//        System.out.println("getRowCount()");
        return vector.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        // TODO Auto-generated method stub
        //vector.get(rowIndex);得到的是Object的对象
//        System.out.println("getValueAt() rowIndex: "+rowIndex+" columnIndex: "+columnIndex);
        return ((Vector) vector.get(rowIndex)).get(columnIndex);
    }

    /**
     * 抽象方法
     * -----------------------------------------------------------
     */

    /**
     * 重写父类中非抽象的方法------覆盖父类中的方法
     */

    public String getColumnName(int columnIndex) {
//        System.out.println("getColumnName");
        return columnNames[columnIndex];
    }

    /**
     * 重写父类中的方法=======获得输入数据的类型,实现复选框的显示
     */

    public Class getColumnClass(int columnIndex) {
        return getValueAt(0, columnIndex).getClass();
    }


    /**
     * 让表格中某些值可以进行修改
     * <p>
     * return false,说明不能进行修改
     */

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if(columnIndex==0){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 重写父类中的方法=====实现表格的数据可操作
     */
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        //先删除，在添加
//        System.out.println("setValueAt()");
        ((Vector) vector.get(rowIndex)).remove(columnIndex);
        ((Vector) vector.get(rowIndex)).add(columnIndex, value);
        this.fireTableCellUpdated(rowIndex, columnIndex);
    }


    /**
     * 无参构造方法------初始化数据
     */

    public TableModel() {

    }


    /**
     * 往行中添加数据----这个方法名可以随意，由用户进行自行调用，
     * <p>
     * 否则table.updateUI()是不会自动调用的
     */
    public void addRow(Object[] data) {
        int size = data.length;
        Vector v = new Vector();
        for (int i = 0; i < size; i++) {
            v.add(data[i]);
        }
        vector.add(v);
    }

    public synchronized void addRow(Schedule schedule) {
        Vector v = schedule2Vector(schedule);
        vector.add(v);
    }

    public synchronized void updateRow(int index,Schedule schedule) {
        Vector v = schedule2Vector(schedule);
        vector.set(index,v);
    }

    public List<String> getUpgradeSelected(){
        List<String> list=new ArrayList<>();
        for (int i = 0; i < vector.size(); i++) {
            Vector o = (Vector)vector.get(i);
            if((Boolean)o.get(0)){
                list.add((String)o.get(1));
            }
        }
        return list;
    }

    public List<String> getUpgradeVersion(){
        List<String> list=new ArrayList<>();
        for (int i = 0; i < vector.size(); i++) {
            Vector o = (Vector)vector.get(i);
            if((Boolean)o.get(0)){
                list.add((String)o.get(4));
            }
        }
        return list;
    }

    public void removeRow(int index){
        vector.remove(index);
    }

    public void removeAll(){
        vector.removeAllElements();
    }

    private   Vector schedule2Vector(Schedule schedule){
        Vector vector=new Vector();
        vector.add(new Boolean(false));
        vector.add(schedule.getHour()+": "+schedule.getMin());
        vector.add(""+schedule.getPercent());
        vector.add(schedule.getWeek());
        vector.add(""+schedule.getScheduleName());
        return vector;
    }

}

package database.view;

import common.Common;
import database.presenter.DataBasePresenterImpl;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by lenovo on 2017/8/24.
 */
public class DataBase extends JFrame implements ActionListener,DataBaseView{

    //定义二维数组作为表格数据
    Object[][] tableData =
            {
                    new Object[]{"李清照" , 29 , "女"},
                    new Object[]{"苏格拉底", 56 , "男"},
                    new Object[]{"李白", 35 , "男"},
                    new Object[]{"弄玉", 18 , "女"}
            };
    //定义一维数据作为列标题
    Object[] columnTitle = {"姓名" , "年龄" , "性别"};
    private DataBasePresenterImpl mDataBasePresenter;
    public JButton refresh=new JButton("刷新");
    DefaultTableModel model=new DefaultTableModel(tableData,columnTitle);
    public JTable data=new JTable(model);

    public DataBase() {
        setTitle("BoxLayout");
        setSize(Common.SCREEN_WEIGHT,Common.SCREEN_HEIGHT);

        setLocationRelativeTo(null);
        setLayout(null);
//        setResizable(false);

        refresh.setBounds(new Rectangle(10, 10, 100, Common.HEIGHT));
        data.setBounds(new Rectangle(10,30,500,400));
        add(refresh);
        add(data);

        mDataBasePresenter=new DataBasePresenterImpl(this);
        refresh.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(refresh.equals(e.getSource())){
            //定义二维数组作为表格数据
            mDataBasePresenter.open();
        }
    }

    @Override
    public void refresh(Object[][] tableData) {
        model.setDataVector(tableData,columnTitle);
    }
}

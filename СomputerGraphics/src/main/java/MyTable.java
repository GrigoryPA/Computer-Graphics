import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class MyTable {
    public DefaultTableModel tableModel;
    public JTable Table;

    public JScrollPane scroller;
    public String tableName;

    public MyTable(String[] headers, String TName, int defalutrowscount)
    {
        String[][] data = new String[defalutrowscount][headers.length];
        tableModel = new DefaultTableModel(data, headers);
        Table = new JTable(tableModel);
        Table.setRowHeight(Table.getRowHeight()+10);
        scroller = new JScrollPane(Table);
        tableName = TName;


        Font font = new Font("Verdana", Font.PLAIN, 24);
        Font fontHeaders = new Font("Verdana", Font.CENTER_BASELINE, 16);
        JTableHeader tableHeader = Table.getTableHeader();
        tableHeader.setFont(fontHeaders);
        Table.setFont(font);


    }
}

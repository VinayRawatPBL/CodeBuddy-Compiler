import java.util.List;
import javax.swing.*;

public class SymbolTableFrame extends JFrame {
    public SymbolTableFrame(List<SymbolEntry> symbols) {
        setTitle("Symbol Table");
        setSize(400, 300);

        String[] columns = {"Name", "Type"};
        Object[][] data = new Object[symbols.size()][2];

        for (int i = 0; i < symbols.size(); i++) {
            data[i][0] = symbols.get(i).getName();
            data[i][1] = symbols.get(i).getType();
        }

        JTable table = new JTable(data, columns);
        add(new JScrollPane(table));
    }
}

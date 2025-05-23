import java.util.List;
import javax.swing.*;

public class TokenTableFrame extends JFrame {
    public TokenTableFrame(List<Token> tokens) {
        setTitle("Tokens");
        setSize(400, 300);
        
        String[] columns = {"Type", "Value"};
        Object[][] data = new Object[tokens.size()][2];
        
        for (int i = 0; i < tokens.size(); i++) {
            data[i][0] = tokens.get(i).getType();
            data[i][1] = tokens.get(i).getValue();
        }
        
        JTable table = new JTable(data, columns);
        add(new JScrollPane(table));
    }
}
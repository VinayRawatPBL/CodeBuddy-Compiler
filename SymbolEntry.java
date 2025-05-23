public class SymbolEntry {
    private String name;
    private String type;
    
    // Pratyush: Simplified constructor and getters
    public SymbolEntry(String name, String type) {
        this.name = name;
        this.type = type;
    }
    
    public String getName() { return name; }
    public String getType() { return type; }
    
    // Nikhil: Added setter for type updates
    public void setType(String type) { this.type = type; }
}
import java.awt.Color;
import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

class ListEntry
{
   private String value;
   private ImageIcon icon;
  
   public ListEntry(String value, ImageIcon icon) {
      this.value = value;
      this.icon = icon;

   }
  
   public String getValue() {
      return value;
   }
  
   public ImageIcon getIcon() {
      return icon;
   }
  
   public String toString() {
      return value;
   }
}
   class ListEntryCellRenderer
   extends JLabel implements ListCellRenderer<Object>{
    /**
	 * 
	 */
	private static final long serialVersionUID = 7127843271346897163L;

	public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

    	//Get the selected index. (The index param isn't
        //always valid, so just use the value.)
        ListEntry entry = (ListEntry) value;
 
        setText(value.toString());
        setIcon(entry.getIcon());
        

        if (index % 2 == 0) {
        	setBackground(new Color(227,227,227));
        }
        else {
        	setBackground(Color.WHITE);
        }
        if (isSelected) {
           setBackground(list.getSelectionBackground());
           setForeground(list.getSelectionForeground());
        }
        

    
        setEnabled(list.isEnabled());
        setFont(list.getFont());
        setOpaque(true);
    
        return this;
    }
 }

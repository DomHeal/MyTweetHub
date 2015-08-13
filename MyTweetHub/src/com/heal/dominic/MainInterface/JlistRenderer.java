package com.heal.dominic.MainInterface;
import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JSeparator;
import javax.swing.ListCellRenderer;
import javax.swing.border.EmptyBorder;

public class JlistRenderer extends JLabel implements ListCellRenderer {

	private static final long serialVersionUID = 1L;
	
	JSeparator separator;
	final String SEPARATOR = "SEPARATOR";

	public JlistRenderer() {
		setOpaque(false);
		setBorder(new EmptyBorder(1, 1, 1, 1));
		separator = new JSeparator(JSeparator.HORIZONTAL);
	}
	@Override
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		String str = (value == null) ? "" : value.toString();
		if (SEPARATOR.equals(str)) {
			return separator;
		}
		if (isSelected) {
			setForeground(Color.GREEN);
		} else {
			setForeground(Color.WHITE);
		}
		setFont(list.getFont());
		setText(str);
		return this;
	}
}
package edu.louisiana.cacs.csce450GProject;

import javax.swing.JFrame;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.util.ArrayList;

public class CreateGUI {

	private JFrame frmTable;

	CreateGUI(ArrayList<Parser.TableRow> tData) {
		frmTable = new JFrame();
		frmTable.setTitle("Table");
		frmTable.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		initialize(tData);

		frmTable.pack();
		frmTable.setVisible(true);
	}

	//Adjust width of the columns in the table based on the output data 
	public static void fitToContentWidth(final JTable table, final int column) {
		int width = table.getColumnModel().getColumn(column)
				.getPreferredWidth();
		for (int row = 0; row < table.getRowCount(); ++row) {
			final Object cellValue = table.getValueAt(row, column);
			final TableCellRenderer renderer = table.getCellRenderer(row,
					column);
			final Component comp = renderer.getTableCellRendererComponent(
					table, cellValue, false, false, row, column);
			width = Math.max(width, comp.getPreferredSize().width);
		}
		final TableColumn tc = table.getColumn(table.getColumnName(column));
		width += table.getIntercellSpacing().width * 2;
		tc.setPreferredWidth(width);
		tc.setMinWidth(width);
	}

	private void initialize(ArrayList<Parser.TableRow> tData) {

		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout(0, 0));

		JTable table = new JTable() {
			@Override
			public boolean getScrollableTracksViewportWidth() {
				return getRowCount() == 0 ? super
						.getScrollableTracksViewportWidth()
						: getPreferredSize().width < getParent().getWidth();
			}
		};

		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setAutoCreateColumnsFromModel(true);

		table.setModel(new DefaultTableModel(new Object[][] {},
				new String[] { "Step", "Stack", "Input Tokens",
						"Action Lookup", "Action Value", "Value of LHS",
						"Length of RHS", "Temp Stack", "Goto Lookup",
						"Goto Value", "Stack Action", "Parse Tree Stack" }) {
			Class[] columnTypes = new Class[] { String.class, String.class,
					String.class, String.class, String.class, String.class,
					String.class, String.class, String.class, String.class,
					String.class, String.class };

			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}

			boolean[] columnEditables = new boolean[] { false, false, false,
					false, false, false, false, false, false, false, false,
					false };

			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});

		table.getColumnModel().getColumn(0).setPreferredWidth(45);
		table.getColumnModel().getColumn(1).setPreferredWidth(45);
		table.getColumnModel().getColumn(2).setPreferredWidth(93);
		table.getColumnModel().getColumn(3).setPreferredWidth(111);
		table.getColumnModel().getColumn(4).setPreferredWidth(96);
		table.getColumnModel().getColumn(5).setPreferredWidth(97);
		table.getColumnModel().getColumn(6).setPreferredWidth(95);
		table.getColumnModel().getColumn(7).setPreferredWidth(86);
		table.getColumnModel().getColumn(8).setPreferredWidth(88);
		table.getColumnModel().getColumn(9).setPreferredWidth(87);
		table.getColumnModel().getColumn(10).setPreferredWidth(98);
		table.getColumnModel().getColumn(11).setPreferredWidth(118);

		table.setRowSelectionAllowed(false);

		for (int i = 0, size = tData.size(); i < size; i++) {
			DefaultTableModel model = (DefaultTableModel) table.getModel();
			model.addRow(new Object[] { Integer.toString(i + 1),
					tData.get(i).stack, tData.get(i).iTokens,
					tData.get(i).aLookup, tData.get(i).aValue,
					tData.get(i).vLHS, tData.get(i).lRHS, tData.get(i).tStack,
					tData.get(i).gLookup, tData.get(i).gValue,
					tData.get(i).sAction, tData.get(i).ptStack });
		}

		fitToContentWidth(table, 0);
		fitToContentWidth(table, 1);
		fitToContentWidth(table, 2);
		fitToContentWidth(table, 3);
		fitToContentWidth(table, 4);
		fitToContentWidth(table, 5);
		fitToContentWidth(table, 6);
		fitToContentWidth(table, 7);
		fitToContentWidth(table, 8);
		fitToContentWidth(table, 9);
		fitToContentWidth(table, 10);
		fitToContentWidth(table, 11);

		JScrollPane scrollPane = new JScrollPane(table);

		panel.add(scrollPane, BorderLayout.CENTER);

		frmTable.getContentPane().add(panel);

	}
}

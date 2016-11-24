package view;

import java.awt.Checkbox;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import model.NoPathFoundException;
import model.PathFoundException;
import model.PathfindingEnv;
import model.PuzzleEnv;
import model.UnknownCellTypeException;
import model.AStar;
import model.AStarPuzzle;
import model.Algorithm;

public class ControlBar extends JPanel{
	
	private JButton final_step;
	private JComboBox<String> type_list, env_list, algo_list;
	private DefaultComboBoxModel<String> env_grid, env_land, env_land_grid, env_puzzle;
	
	public ControlBar(){
		super();
		
		this.setLayout(new GridLayout(1, 0));
		
		algo_list = new JComboBox<String>();
		algo_list.addItem("A*");
		algo_list.addItem("Dijkstra");
		algo_list.addItem("BFS");
		
		algo_list.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(UIView.getEnvView().environment.getAlgo() instanceof AStar)
					((AStar) UIView.getEnvView().environment.getAlgo()).setMode((String) algo_list.getSelectedItem());
			}
		});
		
		type_list = new JComboBox<String>();
		type_list.addItem("Problem A.1");
		type_list.addItem("Problem A.2");
		type_list.addItem("Problem A.3");
		type_list.addItem("Problem B");
		
		type_list.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(UIView.getEnvView().environment.getAlgo() instanceof AStar)
					((AStar) UIView.getEnvView().environment.getAlgo()).setMode("A*");
				
				switch((String) type_list.getSelectedItem()){
					case "Problem A.1":
						env_list.setModel(env_grid);	
						removeAlgoList();
						break;
					case "Problem A.2":
						env_list.setModel(env_land);
						removeAlgoList();
						break;
					case "Problem A.3":
						env_list.setModel(env_land_grid);
						addAlgoList();
						break;
					case "Problem B":
						env_list.setModel(env_puzzle);
						removeAlgoList();
						break;
				}
				
				env_list.setSelectedIndex(0);
			}
			
		});
		
		this.add(type_list);
		
		//We list all the files in the board 
		env_grid = new DefaultComboBoxModel<String>();
		env_grid.addElement("board-1-1");
		env_grid.addElement("board-1-2");
		env_grid.addElement("board-1-3");
		env_grid.addElement("board-1-4");
		
		//We list all the files in the board 
		env_land = new DefaultComboBoxModel<String>();
		env_land.addElement("board-2-1");
		env_land.addElement("board-2-2");
		env_land.addElement("board-2-3");
		env_land.addElement("board-2-4");	
		env_land.addElement("board-2-5");	
		
		env_land_grid = new DefaultComboBoxModel<String>();
		for (int i = 0; i < env_land.getSize(); i++) {
			env_land_grid.addElement(env_land.getElementAt(i));
		}
		for (int i = 0; i < env_grid.getSize(); i++) {
			env_land_grid.addElement(env_grid.getElementAt(i));
		}
		
				
		//We list all the files in the board 
		env_puzzle = new DefaultComboBoxModel<String>();
		env_puzzle.addElement("puzzle-easy-3");
		env_puzzle.addElement("puzzle-medium-1");
		env_puzzle.addElement("puzzle-hard-3");
		env_puzzle.addElement("puzzle-expert-2");
		
		env_list = new JComboBox<String>();
		env_list.setModel(env_grid);
		
		env_list.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				
				UIView view = UIView.getInstance();
				
				if(UIView.getEnvView().environment.getAlgo() instanceof AStar)
					((AStar) UIView.getEnvView().environment.getAlgo()).setMode("A*");
				
				algo_list.setSelectedIndex(0);
				
				try {
					view.changeView((String) type_list.getSelectedItem(), (String) env_list.getSelectedItem());
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(view, "There was a IOException during the file parsing.", "IOException", JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
				} catch (UnknownCellTypeException e1) {
					JOptionPane.showMessageDialog(view, e1.getMessage(), "Cell Type Error", JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
				}
			}
			
		});
		
		this.add(env_list);
		
		final_step = new JButton("Final step");
		
		final_step.addActionListener(new ActionListener() {
			
			UIView view = UIView.getInstance();
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					UIView.getEnvView().environment.finalStep();
				} catch (NoPathFoundException e1) {
					JOptionPane.showMessageDialog(view, e1.getMessage(), "Error during the algorithm execution", JOptionPane.ERROR_MESSAGE);
				} catch (PathFoundException e1) {
					if(UIView.getEnvView().environment.getAlgo() instanceof AStarPuzzle)
						JOptionPane.showMessageDialog(view, e1.getMessage(), "Finished!", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});

		this.add(final_step);
		
		setVisible(true);
	}
	
	private void addAlgoList(){
		this.add(algo_list, 2);
	}
	
	private void removeAlgoList(){
		this.remove(algo_list);
	}
	
}

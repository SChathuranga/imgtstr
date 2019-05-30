package com.userinterface.tracker;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class TesterInterface {

	private JFrame frame;
	private JTextField txt_image1;
	private JTextField txt_image2;
	private JButton btn_compare;
	private JTextField txt_ratio;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TesterInterface window = new TesterInterface();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @throws UnsupportedLookAndFeelException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws ClassNotFoundException 
	 */
	public TesterInterface() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws UnsupportedLookAndFeelException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws ClassNotFoundException 
	 */
	private void initialize() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		frame = new JFrame("UI Tracker");
		frame.setResizable(false);
		UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		frame.setBounds(100, 100, 586, 483);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Image 1 :");
		lblNewLabel.setBounds(28, 89, 105, 30);
		lblNewLabel.setForeground(new Color(0, 0, 255));
		lblNewLabel.setFont(new Font("Segoe UI Symbol", Font.BOLD, 17));
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Image 2 :");
		lblNewLabel_1.setBounds(28, 148, 105, 24);
		lblNewLabel_1.setForeground(new Color(0, 0, 255));
		lblNewLabel_1.setFont(new Font("Segoe UI Symbol", Font.BOLD, 17));
		frame.getContentPane().add(lblNewLabel_1);
		
		txt_image1 = new JTextField();
		txt_image1.setBounds(114, 91, 424, 30);
		frame.getContentPane().add(txt_image1);
		txt_image1.setColumns(10);
		
		txt_image2 = new JTextField();
		txt_image2.setBounds(114, 147, 424, 30);
		txt_image2.setColumns(10);
		frame.getContentPane().add(txt_image2);
		
		btn_compare = new JButton("Compare");
		btn_compare.setBounds(359, 362, 163, 61);
		btn_compare.setFont(new Font("Tahoma", Font.BOLD, 17));
		frame.getContentPane().add(btn_compare);
		
		final JLabel lbl_diffresult = new JLabel("00.00");
		lbl_diffresult.setBounds(114, 304, 442, 44);
		lbl_diffresult.setFont(new Font("Tahoma", Font.BOLD, 30));
		frame.getContentPane().add(lbl_diffresult);
		
		JLabel lblRatio = new JLabel("Ratio");
		lblRatio.setBounds(28, 205, 74, 24);
		lblRatio.setForeground(Color.BLUE);
		lblRatio.setFont(new Font("Segoe UI Symbol", Font.BOLD, 17));
		frame.getContentPane().add(lblRatio);
		
		txt_ratio = new JTextField();
		txt_ratio.setBounds(114, 204, 424, 30);
		frame.getContentPane().add(txt_ratio);
		txt_ratio.setColumns(10);
		
		JLabel lblNewLabel_2 = new JLabel("Diff :");
		lblNewLabel_2.setBounds(28, 292, 74, 68);
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 30));
		frame.getContentPane().add(lblNewLabel_2);
		
		final JLabel lbl_result = new JLabel("NO RESULT");
		lbl_result.setFont(new Font("Tahoma", Font.BOLD, 30));
		lbl_result.setBounds(211, 247, 185, 44);
		frame.getContentPane().add(lbl_result);
		btn_compare.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String imgpath1 = txt_image1.getText();
				String imgpath2 = txt_image2.getText();
				String ratio = txt_ratio.getText();

				Double d = Double.parseDouble(ratio);
				try {
					BufferedImage img1 = ImageIO.read(new File(imgpath1));
					BufferedImage img2 = ImageIO.read(new File(imgpath2));
					double p = getDifferencePercent(img1, img2);
					String diff = String.valueOf(p);

					if (p > d) {
						System.out.println("Diff percent: " + p + "-------------- FAIL --------------");
						lbl_result.setText("FAILED");
						lbl_diffresult.setText(diff);
						lbl_result.setForeground(Color.RED);
					} else {
						System.out.println("Diff percent: " + p + "-------------- PASS --------------");
						lbl_result.setText("PASSSED");
						lbl_result.setForeground(Color.green);
						lbl_diffresult.setText(diff);
					}

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println(e);
				}
			}
			
			private  double getDifferencePercent(BufferedImage img1, BufferedImage img2) {
				int width = img1.getWidth();
				int height = img1.getHeight();
				int width2 = img2.getWidth();
				int height2 = img2.getHeight();
				if (width != width2 || height != height2) {
					throw new IllegalArgumentException(String.format(
							"Images must have the same dimensions: (%d,%d) vs. (%d,%d)", width, height, width2, height2));
				}

				long diff = 0;
				for (int y = 0; y < height; y++) {
					for (int x = 0; x < width; x++) {
						diff += pixelDiff(img1.getRGB(x, y), img2.getRGB(x, y));
					}
				}
				long maxDiff = 3L * 255 * width * height;

				return 100.0 * diff / maxDiff;
			}
			
			private int pixelDiff(int rgb1, int rgb2) {
				int r1 = (rgb1 >> 16) & 0xff;
				int g1 = (rgb1 >> 8) & 0xff;
				int b1 = rgb1 & 0xff;
				int r2 = (rgb2 >> 16) & 0xff;
				int g2 = (rgb2 >> 8) & 0xff;
				int b2 = rgb2 & 0xff;

				return Math.abs(r1 - r2) + Math.abs(g1 - g2) + Math.abs(b1 - b2);
			}
		});
		
		
		JButton btnNewButton = new JButton("Reset");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				txt_image1.setText("");
				txt_image2.setText("");
				txt_ratio.setText("");
				
				lbl_result.setText("NO RESULT");
				lbl_diffresult.setText("00.00");
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 17));
		btnNewButton.setBounds(28, 364, 161, 61);
		frame.getContentPane().add(btnNewButton);
	}
}

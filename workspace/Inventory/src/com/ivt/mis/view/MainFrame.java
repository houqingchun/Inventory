package com.ivt.mis.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import com.ivt.mis.common.CommonFactory;
import com.ivt.mis.common.Constants;
import com.ivt.mis.common.swing.SpringUtilities;
import com.ivt.mis.service.CustomizationService;

public class MainFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3929927656702505266L;
	public static String power;
	public static String loginname;

	public static JDesktopPane desktopPane;
	private static MainFrame mainFrame;
	private JMenuItem companyInfoMenuItem;

	private static Map internalFrameMap = new HashMap();

	/**
	 * 保证只打开一个内部视窗的方法
	 * 
	 * @param newFrame
	 *            要呼叫的内部视窗对象引用
	 */
	public static boolean isInternalFrameOpened(String frameId, boolean isMaxmize) {
		// 获取桌面面板组件数组
		JInternalFrame[] interFrames = desktopPane.getAllFrames();
		boolean isExist = false;
		boolean isSearchFrame = frameId.indexOf("_SEARCH") > 0;

		// 遍历查找指定引用的内部视窗组件是否存在
		for (JInternalFrame tmpFrame : interFrames) {
			BaseJInternalFrame tmpFrameBase = (BaseJInternalFrame) tmpFrame;
//			boolean isSelected = tmpFrame.isSelected();
			if (tmpFrameBase.getFrameId().equals(frameId)) {
				// tmpFrame.dispose();
				// desktopPane.remove(tmpFrame);
				// 查询界面，直接切换至已经打开的窗口
				if (isSearchFrame) {
					switchInternalFrame(tmpFrame, frameId, isMaxmize);
					isExist = true;
				} else {// 编辑页面，则弹出提示
					int option = JOptionPane
							.showConfirmDialog(
									MainFrame.desktopPane,
									"温馨提示：存在未关闭的相同窗口，继续将丢失原有窗口所修改的数据。\n 点击‘是’继续浏览   或‘否’切换至原有窗口",
									"警告", JOptionPane.YES_NO_OPTION);
					if (option == JOptionPane.YES_OPTION) {
						tmpFrame.dispose();
						desktopPane.remove(tmpFrame);
						isExist = false;
					} else {
						switchInternalFrame(tmpFrame, frameId, isMaxmize);
						isExist = true;
					}
				}
			} 
//			else {
//				if (!isSelected) {
//					tmpFrame.getDesktopPane().getDesktopManager()
//							.minimizeFrame(tmpFrame);
//				}
//			}
		}

		return isExist;
	}

	public static void switchInternalFrame(JInternalFrame tmpFrame,
			String frameId, boolean isMaxmize) {
		tmpFrame.setVisible(true);
		try {
			tmpFrame.setSelected(true);
		} catch (PropertyVetoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			tmpFrame.getDesktopPane().getDesktopManager()
					.deiconifyFrame(tmpFrame);
			tmpFrame.getDesktopPane().getDesktopManager()
					.maximizeFrame(tmpFrame);
			if (!isMaxmize) {
				tmpFrame.getDesktopPane().getDesktopManager()
						.minimizeFrame(tmpFrame);
			}
			// tmpFrame.setSelected(true);
			// desktopPane.getDesktopManager().openFrame(tmpFrame);
			// tmpFrame.restoreSubcomponentFocus();
			// desktopPane.getDesktopManager().deiconifyFrame(tmpFrame);
			tmpFrame.moveToFront();

	}

	public static void openNewFrame(JInternalFrame newFrame, boolean isMaxmize) {
		desktopPane.add(newFrame);
		if (!isMaxmize) {
			// 设置内部视窗位置居中
			int x = (desktopPane.getWidth() - newFrame.getWidth()) / 2;
			int y = (desktopPane.getHeight() - newFrame.getHeight()) / 2;
			newFrame.setLocation(x, y);
		} else {
			try {
				newFrame.setMaximum(true);
			} catch (PropertyVetoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// 将被呼叫内部视窗重新以默认方式布局（解决最小化后还原默认大小的问题）
		// newFrame.pack();
		// 将被呼叫内部视窗显示
		newFrame.setVisible(true);
		// 将被呼叫内部视窗置顶
		// 方式一： 从类 java.awt.Window 继承的方法
		// frame.toFront();
		// 方式二：从类 javax.swing.JInternalFrame 继承的方法
		newFrame.moveToFront();

		// 将被呼叫内部视窗设置为选中状态（标题栏高亮）
		try {
			newFrame.setSelected(true);
		} catch (PropertyVetoException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 创建主窗体
	 */
	private MainFrame() {
		super(Constants.TOOLS_TITLE_PREFIX + "库存小帮手V" + Constants.IVT_VERSION
				+ Constants.TOOLS_TITLE_SUFFIX);
		SpringUtilities.initGlobalStyle();

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setBounds(screenSize.width / 6, screenSize.height / 6,
				screenSize.width * 2 / 3, screenSize.height * 2 / 3);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Image image = null;
		try {
			image = ImageIO.read(this.getClass().getResource(
					Constants.TASK_ICON_PATH));
		} catch (IOException e1) {
			e1.printStackTrace();
		} // 创建图片对象
		this.setIconImage(image);// 设置图标

		desktopPane = new JDesktopPane();

		desktopPane.setOpaque(true);
		this.setContentPane(desktopPane);
		// this.setLayout(new BorderLayout());
		// JToolBar toolBar = new JToolBar("test");
		// StatusBar statusBar = new StatusBar();
		// this.add(statusBar, BorderLayout.SOUTH);
		this.setJMenuBar(createMenuBar());

		// Set up the backgound image
		// desktopPane.setBackground(new Color(200, 218, 235));
		// Make dragging a little faster but perhaps uglier.
		desktopPane.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);

		// Add listener when first use this tool
		desktopPane.addMouseListener(new CompanyInfoMaintainListener());
	}

	public static MainFrame getMainFrame() {
		if (mainFrame == null) {
			mainFrame = new MainFrame();
		}
		return mainFrame;
	}

	/**
	 * 创建主窗体的菜单栏
	 * 
	 * @return JMenuBar
	 */
	protected JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();

		// Set up the info menu.
		JMenu menu = new JMenu("基础信息管理");
		menuBar.add(menu);
		JMenuItem menuItem = new JMenuItem("客户管理");
		menuItem.setAccelerator(KeyStroke
				.getKeyStroke('C', InputEvent.ALT_MASK));
		menuItem.addActionListener(MainAction.clickCustomerInforserch());
		menu.add(menuItem);
		menuItem = new JMenuItem("产品管理");
		menuItem.setAccelerator(KeyStroke
				.getKeyStroke('P', InputEvent.ALT_MASK));
		menuItem.addActionListener(MainAction.clickProductInforserch());
		menu.add(menuItem);
		menuItem = new JMenuItem("供应商管理");
		menuItem.setAccelerator(KeyStroke
				.getKeyStroke('V', InputEvent.ALT_MASK));
		menuItem.addActionListener(MainAction.clickPrivoderInforSearch());
		menu.add(menuItem);

		// Set up the storeprocurement menu.
		menu = new JMenu("进货管理");
		menuBar.add(menu);
		menuItem = new JMenuItem("入库单");
		menuItem.setAccelerator(KeyStroke
				.getKeyStroke('G', InputEvent.ALT_MASK));
		menuItem.addActionListener(MainAction.storeProcurement(null,
				Constants.FORM_TYPE_ADD));
		menu.add(menuItem);
		menuItem = new JMenuItem("入库查询");
		menuItem.setAccelerator(KeyStroke
				.getKeyStroke('S', InputEvent.ALT_MASK));
		menuItem.addActionListener(MainAction.clickInputInforserch());
		menu.add(menuItem);

		// Set up the storeShipment menu.
		menu = new JMenu("销售管理");
		menuBar.add(menu);
		menuItem = new JMenuItem("出库单");
		menuItem.setAccelerator(KeyStroke
				.getKeyStroke('L', InputEvent.ALT_MASK));
		menuItem.addActionListener(MainAction.storeShipment(null,
				Constants.FORM_TYPE_ADD));
		menu.add(menuItem);
		menuItem = new JMenuItem("出库查询");
		menuItem.setAccelerator(KeyStroke
				.getKeyStroke('K', InputEvent.ALT_MASK));
		menuItem.addActionListener(MainAction.clickSaleInforSearch());
		menu.add(menuItem);

		// Set up the save menu.
		menu = new JMenu("库存管理");
		menuBar.add(menu);
		menuItem = new JMenuItem("库存盘点");
		menuItem.addActionListener(MainAction.storeHouseInfor());
		menu.add(menuItem);
		// menuItem = new JMenuItem("价格调整");
		// menuItem.addActionListener(MainAction.priceChange());
		// menu.add(menuItem);

		// Set up the system menu.
		menu = new JMenu("系统配置");
		menuBar.add(menu);
		menuItem = new JMenuItem("更改密码");
		menuItem.addActionListener(MainAction.changePassword());
		menu.add(menuItem);
		if (power.equals(Constants.POWER_ADMIN)) {
			menuItem = new JMenuItem("操作员管理");
			menuItem.addActionListener(MainAction.operaterManager());
			menu.add(menuItem);
		} else if (power.equals(Constants.POWER_OPERATOR)) {
		} else {
			JOptionPane.showMessageDialog(this, "非法用户！！！", "警告",
					JOptionPane.WARNING_MESSAGE);
			System.exit(0);
		}
		menu.addSeparator();
		menuItem = new JMenuItem("编码规则");
		menuItem.addActionListener(MainAction.codeRuleConfiguration(null,
				Constants.FORM_TYPE_ADD_UPDATE));
		menu.add(menuItem);

		companyInfoMenuItem = new JMenuItem("公司信息配置");
		companyInfoMenuItem.addActionListener(MainAction.clickCustomization());
		menu.add(companyInfoMenuItem);
		menuItem = new JMenuItem("数据备份与还原");
		menuItem.addActionListener(MainAction.clickDataBackupRestore());
		menu.add(menuItem);

		menu = new JMenu("帮助");
		menuBar.add(menu);
		menuItem = new JMenuItem("问题反馈");
		menuItem.addActionListener(MainAction
				.clickFeedback(Constants.FORM_TYPE_ADD));
		menu.add(menuItem);

		menuItem = new JMenuItem("联系我们");
		menuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				StringBuffer versionInfo = new StringBuffer();
				versionInfo.append("库存小帮手 V" + Constants.IVT_VERSION + "\n");
				versionInfo.append("北京时代数据工作室 版权所有 翻版必究\n");
				versionInfo.append("\n");
				versionInfo.append("联系人：侯青春\n");
				versionInfo.append("联系电话：18600182885\n");
				versionInfo.append("电子邮箱：houqingchun@hotmail.com\n");
				versionInfo.append("QQ：8083851\n");

				JOptionPane.showMessageDialog(MainFrame.desktopPane,
						versionInfo.toString(), "版本信息",
						JOptionPane.PLAIN_MESSAGE);
			}

		});
		menu.add(menuItem);

		return menuBar;
	}

	// 首次访问时检查
	class CompanyInfoMaintainListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseEntered(MouseEvent e) {
			if (!Constants.COMPANY_INFO_IS_MAINTAINED) {
				CustomizationService customizationService = CommonFactory
						.getCustomizationService();
				if (!customizationService.isExited()) {
					companyInfoMenuItem.doClick();
				} else {
					Constants.COMPANY_INFO_IS_MAINTAINED = true;
				}
			}
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}
	}

	/**
	 * Swing状态栏Panel
	 *
	 */
	class StatusBar extends JPanel {
		private static final long serialVersionUID = 1L;

		public StatusBar() {
			setLayout(new BorderLayout());
			setPreferredSize(new Dimension(10, 20));

			JPanel rightPanel = new JPanel(new BorderLayout());
			rightPanel.add(new JLabel("北京时代数据工作室     联系人：侯先生 18600182885"),
					BorderLayout.SOUTH);
			rightPanel.setOpaque(false);

			add(rightPanel, BorderLayout.WEST);
			setBackground(SystemColor.control);
		}

		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			// 状态栏顶部阴影
			int y = 0;
			g.setColor(new Color(156, 154, 140));
			g.drawLine(0, y, getWidth(), y);
			y++;
			g.setColor(new Color(196, 194, 183));
			g.drawLine(0, y, getWidth(), y);
			y++;
			g.setColor(new Color(218, 215, 201));
			g.drawLine(0, y, getWidth(), y);
			y++;
			g.setColor(new Color(233, 231, 217));
			g.drawLine(0, y, getWidth(), y);
			// 状态栏底部阴影
			y = getHeight() - 3;
			g.setColor(new Color(233, 232, 218));
			g.drawLine(0, y, getWidth(), y);
			y++;
			g.setColor(new Color(233, 231, 216));
			g.drawLine(0, y, getWidth(), y);
			y++;
			g.setColor(new Color(221, 221, 220));
			g.drawLine(0, y, getWidth(), y);
		}
	}
}

/**
 * 状态栏右侧的图标
 *
 */
class AngledLinesWindowsCornerIcon implements Icon {
	private static final Color WHITE_LINE_COLOR = new Color(255, 255, 255);

	private static final Color GRAY_LINE_COLOR = new Color(172, 168, 153);
	private static final int WIDTH = 13;

	private static final int HEIGHT = 13;

	public int getIconHeight() {
		return WIDTH;
	}

	public int getIconWidth() {
		return HEIGHT;
	}

	public void paintIcon(Component c, Graphics g, int x, int y) {

		g.setColor(WHITE_LINE_COLOR);
		g.drawLine(0, 12, 12, 0);
		g.drawLine(5, 12, 12, 5);
		g.drawLine(10, 12, 12, 10);

		g.setColor(GRAY_LINE_COLOR);
		g.drawLine(1, 12, 12, 1);
		g.drawLine(2, 12, 12, 2);
		g.drawLine(3, 12, 12, 3);

		g.drawLine(6, 12, 12, 6);
		g.drawLine(7, 12, 12, 7);
		g.drawLine(8, 12, 12, 8);

		g.drawLine(11, 12, 12, 11);
		g.drawLine(12, 12, 12, 12);
	}
}
package com.ivt.mis.common.swing;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: future</p>
 * @author libaojian
 * @version 1.0
 */
import javax.swing.UIManager;
import java.awt.*;


public class AppFont {
  public static void setFont(Font pFont){
  UIManager.put("Button.font", pFont);
  UIManager.put("ToggleButton.font", pFont);
  UIManager.put("RadioButton.font", pFont);
  UIManager.put("CheckBox.font", pFont);
  UIManager.put("ColorChooser.font", pFont);
  UIManager.put("ToggleButton.font", pFont);
  UIManager.put("ComboBox.font", pFont);
  UIManager.put("ComboBoxItem.font", pFont);
  UIManager.put("InternalFrame.titleFont", pFont);
  UIManager.put("Label.font", pFont);
  UIManager.put("List.font", pFont);
  UIManager.put("MenuBar.font", pFont);
  UIManager.put("Menu.font", pFont);
  UIManager.put("MenuItem.font", pFont);
  UIManager.put("RadioButtonMenuItem.font", pFont);
  UIManager.put("CheckBoxMenuItem.font", pFont);
  UIManager.put("PopupMenu.font", pFont);
  UIManager.put("OptionPane.font", pFont);
  UIManager.put("Panel.font", pFont);
  UIManager.put("ProgressBar.font", pFont);
  UIManager.put("ScrollPane.font", pFont);
  UIManager.put("Viewport", pFont);
  UIManager.put("TabbedPane.font", pFont);
  UIManager.put("TableHeader.font", pFont);
  UIManager.put("Table.font", pFont);
  UIManager.put("TextField.font", pFont);
  UIManager.put("PasswordFiled.font", pFont);
  UIManager.put("TextArea.font", pFont);
  UIManager.put("TextPane.font", pFont);
  UIManager.put("EditorPane.font", pFont);
  UIManager.put("TitledBorder.font", pFont);
  UIManager.put("ToolBar.font", pFont);
  UIManager.put("ToolTip.font", pFont);
  UIManager.put("Tree.font", pFont);
}


}

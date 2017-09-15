package com.ivt.mis.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import com.ivt.mis.common.CommonFactory;
import com.ivt.mis.common.Constants;
import com.ivt.mis.common.swing.IvtJTextField;
import com.ivt.mis.common.swing.IvtLayoutUtilities;
import com.ivt.mis.model.Customer;
import com.ivt.mis.model.Product;
import com.ivt.mis.service.ProductService;
import com.ivt.mis.service.ProviderService;
import com.ivt.mis.view.validator.ProductValidator;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;

public class ProductFrame extends BaseJInternalFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1097690471039328018L;
	private Product product;
	JTextField nameField;
	JTextField idField;
	JTextField codeField;
	JTextField manufacturerField;
	JTextField brandField;
	JTextField sizeField;
	JTextField packField;
	JTextField miniPackField;
	JComboBox unitComboBox;
	JTextField categoryField;
	JTextField promitCodeField;
	JTextField decriptionField;
	ProductSearchFrame callBackSearchFrame;

	public ProductFrame(Product product, String editType, ProductSearchFrame callBackSearchFrame) {
		super("产品信息管理", true, true, false, true);
		super.setFrameId(Constants.MODULE_PRODUCT);
		this.product = product;
		super.editType = editType;
		//this.setLayer(this.getLayer() + 1);
		Dimension deskPaneDim = MainFrame.desktopPane.getSize();
		this.setSize(500, 320);
		this.setResizable(false);
		Dimension internalDim = this.getSize();
		this.setLocation(
				(deskPaneDim.width - internalDim.width) / 2 + this.getLayer()
						* 5, (deskPaneDim.height - internalDim.height) / 2
						+ this.getLayer() * 5);
		this.getContentPane().add(createJPanel());
		this.callBackSearchFrame = callBackSearchFrame;
	}

	public JPanel createJPanel() {
		JPanel mainContainer = new JPanel();
		mainContainer
				.setLayout(new BoxLayout(mainContainer, BoxLayout.Y_AXIS));
		/*
		 * 建立产品添加信息页面
		 */
		JPanel addPanel = new JPanel();

		nameField = new IvtJTextField(50);
		idField = new IvtJTextField(50, true);
		codeField = new IvtJTextField(16, true);
		brandField = new IvtJTextField(50);
		sizeField = new IvtJTextField(50);
		packField = new IvtJTextField(50);
		miniPackField = new IvtJTextField(50);
		unitComboBox = super.getUnits();
		unitComboBox.setEditable(true);
		unitComboBox.setBackground(Color.white);
		categoryField = new IvtJTextField(50);
		promitCodeField = new IvtJTextField(50,true);
		manufacturerField = new IvtJTextField(50);
		decriptionField = new IvtJTextField(100);


		DefaultFormBuilder builder = IvtLayoutUtilities.getFormBuilder();
		Product fields = new Product();
		// Fill the table with labels and components.
		CellConstraints cc = new CellConstraints();
//		builder.append(new JLabel("产品编号:"));
		builder.append(fields.getFieldDefn("id").getDescrLong());
		builder.append(idField);
		builder.append(new JLabel("此为根据编码规则自动生成,可修改"), 3);
		builder.nextLine();
//		builder.append(new JLabel("产品名称:"));
		builder.append(fields.getFieldDefn("productName").getDescrLong());
		builder.append(nameField);
//		builder.append(new JLabel("产品型号:"));
		builder.append(fields.getFieldDefn("productCode").getDescrLong());
		builder.append(codeField);

		builder.nextLine();
//		builder.append(new JLabel("规格:"));
		builder.append(fields.getFieldDefn("size").getDescrLong());
		builder.append(sizeField);
//		builder.append(new JLabel("单位:"));
		builder.append(fields.getFieldDefn("unit").getDescrLong());
		builder.append(unitComboBox);
		builder.nextLine();
//		builder.append(new JLabel("包装:"));
		builder.append(fields.getFieldDefn("pack").getDescrLong());
		builder.append(packField);
//		builder.append(new JLabel("最小包装:"));
		builder.append(fields.getFieldDefn("miniPack").getDescrLong());
		builder.append(miniPackField);
		builder.nextLine();
//		builder.append(new JLabel("品牌:"));
		builder.append(fields.getFieldDefn("brand").getDescrLong());
		builder.append(brandField);
//		builder.append(new JLabel("批准文号:"));
		builder.append(fields.getFieldDefn("promitCode").getDescrLong());
		builder.append(promitCodeField);
		builder.nextLine();
//		builder.append(new JLabel("生产商:"));
		builder.append(fields.getFieldDefn("manufacturer").getDescrLong());
		builder.append(manufacturerField);
//		builder.append(new JLabel("类别:"));
		builder.append(fields.getFieldDefn("category").getDescrLong());
		builder.append(categoryField);
		
		builder.nextLine();
//		builder.append(new JLabel("备注: "));
		builder.append(fields.getFieldDefn("description").getDescrLong());
		builder.append(decriptionField, 5);

		addPanel.add(builder.getPanel());

		JPanel buttonsPanel = super.createCRUDButtons(new AddButtonListener(),
				new UpdateButtonListener(), new DeleteButtonListener());
//		addPanel.add(buttonsPanel);

		// 装载数据
		populateForm();
		
		mainContainer.add(addPanel);
		mainContainer.add(buttonsPanel);

		return mainContainer;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public String getEditType() {
		return editType;
	}

	public void setEditType(String editType) {
		this.editType = editType;
	}

	public void populateForm() {
		if (Constants.FORM_TYPE_UPDATE.equals(editType)
				|| Constants.FORM_TYPE_DELETE.equals(editType)) {
			idField.setText(product.getId());
			codeField.setText(product.getProductCode());
			codeField.setEditable(true);
			idField.setEditable(false);
			nameField.setText(product.getProductName());
			manufacturerField.setText(product.getManufacturer());
			brandField.setText(product.getBrand());
			categoryField.setText(product.getCategory());
			sizeField.setText(product.getSize());
			packField.setText(product.getPack());
			miniPackField.setText(product.getMiniPack());
			unitComboBox.setSelectedItem(product.getUnit());
			promitCodeField.setText(product.getPromitCode());
			decriptionField.setText(product.getDescription());
			if (Constants.FORM_TYPE_DELETE.equals(editType)) {
				idField.setEditable(false);
				codeField.setEditable(false);
				nameField.setEditable(false);
				manufacturerField.setEditable(false);
				brandField.setEditable(false);
				categoryField.setEditable(false);
				sizeField.setEditable(false);
				packField.setEditable(false);
				miniPackField.setEditable(false);
				unitComboBox.setEnabled(false);
				promitCodeField.setEditable(false);
				decriptionField.setEditable(false);
			}
		} else if (Constants.FORM_TYPE_ADD.equals(editType)) {
			idField.setText(CommonFactory.getCodeRuleService().getNextObjectCode(Constants.MODULE_PRODUCT));
			codeField.setText("");
			nameField.setText("");
			manufacturerField.setText("");
			brandField.setText("");
			categoryField.setText("");
			sizeField.setText("");
			packField.setText("");
			packField.setText("");
			promitCodeField.setText("");
			decriptionField.setText("");
		}
	}

	public Product buildProduct() {
		Product product = new Product();
		product.setId(idField.getText());
		product.setProductCode(codeField.getText());
		product.setProductName(nameField.getText());
		product.setManufacturer(manufacturerField.getText());
		product.setBrand(brandField.getText());
		product.setCategory(categoryField.getText());
		product.setSize(sizeField.getText());
		product.setPack(packField.getText());
		product.setMiniPack(miniPackField.getText());
		product.setPromitCode(promitCodeField.getText());
		product.setDescription(decriptionField.getText());
		product.setOwnerId(MainFrame.loginname);
		if (unitComboBox.getSelectedIndex() != -1){
			product.setUnit(unitComboBox.getSelectedItem().toString());
		}

		return product;
	}

	class AddButtonListener extends BaseAddButtonListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			Product product = buildProduct();

			ProductService productService = CommonFactory.getProductService();

			ProviderService providerService = CommonFactory
					.getProviderService();

			ProductValidator productValidator = new ProductValidator(product,
					productService, providerService, editType);

			String errorString = productValidator.getErrorString();
			if (errorString != null) {
				showWarningMsg(errorString);
				return;
			}

			if (productService.saveProduct(product)) {
				showInfoMsg("添加成功");
				if (callBackSearchFrame != null){
					callBackSearchFrame.callBackRefresh(true);	
				}
				dispose();
			} else {
				showErrorMsg("添加失败");
			}
		}
	}

	class UpdateButtonListener extends BaseUpdateButtonListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			Product product = buildProduct();
			ProductService productService = CommonFactory.getProductService();

			ProviderService providerService = CommonFactory
					.getProviderService();

			ProductValidator productValidator = new ProductValidator(product,
					productService, providerService, editType);

			String errorString = productValidator.getErrorString();
			if (errorString != null) {
				showWarningMsg(errorString);
				return;
			}
			if (productService.modifyProduct(product)) {
				showInfoMsg("修改成功");
				if (callBackSearchFrame != null){
					callBackSearchFrame.callBackRefresh(false);	
				}
				dispose();
			} else {
				showErrorMsg("修改失败");
			}
		}
	}

	class DeleteButtonListener extends BaseDeleteButtonListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			ProductService productService = CommonFactory.getProductService();
			if (showConfirmDialog() == JOptionPane.YES_OPTION) {
				if (productService.removeProduct(idField.getText())) {
					showInfoMsg("删除成功");
					if (callBackSearchFrame != null){
						callBackSearchFrame.callBackRefresh(false);	
					}
					dispose();
				} else {
					showErrorMsg("删除失败");
				}
			} else {
				dispose();
			}
		}
	}
}

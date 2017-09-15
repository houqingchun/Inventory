package com.ivt.mis.view;

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
import com.ivt.mis.model.Product;
import com.ivt.mis.model.Provider;
import com.ivt.mis.service.ProviderService;
import com.ivt.mis.view.validator.ProviderValidator;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;

public class ProviderFrame extends BaseJInternalFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7545257431510344576L;
	Provider provider;
	JTextField nameField;
	JTextField idField;
	JTextField zipField;
	JTextField addressField;
	JTextField shipAddressField;
	JTextField telephoneField;
	JTextField mobileField;
	JTextField emailField;
	JTextField faxField;
	JTextField contactsField;
	JTextField bankField;
	JTextField bankAccountField;
	JTextField regPlaceField;
	JTextField regNumberField;
	JComboBox regTypeComboBox;
	JComboBox levelStateComboBox;
	ProviderSearchFrame callBackSearchFrame;

	public ProviderFrame(Provider provider, String editType, ProviderSearchFrame callBackSearchFrame) {
		super("供应商管理", true, true, false, true);
		super.setFrameId(Constants.MODULE_PROVIDER);
		//this.setLayer(this.getLayer() + 1);
		this.provider = provider;
		super.editType = editType;
		Dimension deskPaneDim = MainFrame.desktopPane.getSize();
		this.setSize(500, 420);
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
		 * 建立供应商添加信息页面
		 */

		nameField = new IvtJTextField(50);
		idField = new IvtJTextField(16, true);
		zipField = new IvtJTextField(6);
		addressField = new IvtJTextField(100);
		shipAddressField = new IvtJTextField(100);
		telephoneField = new IvtJTextField(20);
		mobileField = new IvtJTextField(20);
		emailField = new IvtJTextField(50);
		faxField = new IvtJTextField(20);
		contactsField = new IvtJTextField(50);
		bankField = new IvtJTextField(50);
		bankAccountField = new IvtJTextField(50);
		regPlaceField = new IvtJTextField(50);
		regNumberField = new IvtJTextField(50);
		regTypeComboBox = super.getIvtRegistryType();
		levelStateComboBox = super.getIvtLevelState();

		JPanel addPanel = new JPanel();

		DefaultFormBuilder builder = IvtLayoutUtilities.getFormBuilder();

		Provider fields = new Provider();
		
//		builder.append(new JLabel("供应商编号:"));
		builder.append(fields.getFieldDefn("id").getDescrLong());
		builder.append(idField);
		builder.append(new JLabel("此为根据编码规则自动生成,可修改"), 3);
		builder.nextLine();
//		builder.append(new JLabel("供应商全称:"));
		builder.append(fields.getFieldDefn("name").getDescrLong());
		builder.append(nameField,5);
		builder.nextLine();
//		builder.append(new JLabel("联系地址:"));
		builder.append(fields.getFieldDefn("address").getDescrLong());
		builder.append(addressField,5);
		builder.nextLine();
//		builder.append(new JLabel("收货地址:"));
		builder.append(fields.getFieldDefn("shipAddress").getDescrLong());
		builder.append(shipAddressField,5);
		builder.nextLine();
//		builder.append(new JLabel("联系人:"));
		builder.append(fields.getFieldDefn("contacts").getDescrLong());
		builder.append(contactsField);
//		builder.append(new JLabel("邮政编码:"));
		builder.append(fields.getFieldDefn("zip").getDescrLong());
		builder.append(zipField);
		builder.nextLine();
//		builder.append(new JLabel("电话号码:"));
		builder.append(fields.getFieldDefn("telephone").getDescrLong());
		builder.append(telephoneField);
//		builder.append(new JLabel("移动电话:"));
		builder.append(fields.getFieldDefn("mobile").getDescrLong());
		builder.append(mobileField);
		builder.nextLine();
//		builder.append(new JLabel("电子邮箱:"));
		builder.append(fields.getFieldDefn("email").getDescrLong());
		builder.append(emailField);
//		builder.append(new JLabel("传真:"));
		builder.append(fields.getFieldDefn("fax").getDescrLong());
		builder.append(faxField);
		builder.nextLine();
//		builder.append(new JLabel("注册地:"));
		builder.append(fields.getFieldDefn("registryPlace").getDescrLong());
		builder.append(regPlaceField);
//		builder.append(new JLabel("注册号:"));
		builder.append(fields.getFieldDefn("registryNumber").getDescrLong());
		builder.append(regNumberField);
		builder.nextLine();
//		builder.append(new JLabel("注册类型:"));
		builder.append(fields.getFieldDefn("registryType").getDescrLong());
		builder.append(regTypeComboBox);
//		builder.append(new JLabel("供应商级别:"));
		builder.append(fields.getFieldDefn("levelState").getDescrLong());
		builder.append(levelStateComboBox);
		builder.nextLine();
//		builder.append(new JLabel("开户银行:"));
		builder.append(fields.getFieldDefn("bank").getDescrLong());
		builder.append(bankField,3);
		builder.nextLine();
//		builder.append(new JLabel("银行账号:"));
		builder.append(fields.getFieldDefn("account").getDescrLong());
		builder.append(bankAccountField,5);

		// 添加到主Panel内
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

	public void populateForm() {
		if (Constants.FORM_TYPE_UPDATE.equals(editType)
				|| Constants.FORM_TYPE_DELETE.equals(editType)) {
			idField.setText(provider.getId());
			nameField.setText(provider.getName());
			idField.setEditable(false);
			zipField.setText(provider.getZip());
			addressField.setText(provider.getAddress());
			shipAddressField.setText(provider.getShipAddress());
			contactsField.setText(provider.getContacts());
			mobileField.setText(provider.getMobile());
			emailField.setText(provider.getEmail());
			faxField.setText(provider.getFax());
			bankField.setText(provider.getBank());
			bankAccountField.setText(provider.getAccount());
			telephoneField.setText(provider.getTelephone());
			regPlaceField.setText(provider.getRegistryPlace());
			regNumberField.setText(provider.getRegistryNumber());
			regTypeComboBox.setSelectedItem(provider
					.getRegistryNumber());
			levelStateComboBox.setSelectedItem(provider.getLevelState());
			if (Constants.FORM_TYPE_DELETE.equals(editType)) {
				idField.setEditable(false);
				nameField.setEditable(false);
				zipField.setEditable(false);
				addressField.setEditable(false);
				shipAddressField.setEditable(false);
				contactsField.setEditable(false);
				mobileField.setEditable(false);
				emailField.setEditable(false);
				faxField.setEditable(false);
				bankField.setEditable(false);
				bankAccountField.setEditable(false);
				telephoneField.setEditable(false);
				regPlaceField.setEditable(false);
				regNumberField.setEditable(false);
				regTypeComboBox.setEnabled(false);
				levelStateComboBox.setEnabled(false);
			}
		} else if (Constants.FORM_TYPE_ADD.equals(editType)) {
			idField.setText(CommonFactory.getCodeRuleService().getNextObjectCode(Constants.MODULE_PROVIDER));
			nameField.setText("");
			zipField.setText("");
			addressField.setText("");
			shipAddressField.setText("");
			mobileField.setText("");
			emailField.setText("");
			faxField.setText("");
			contactsField.setText("");
			bankField.setText("");
			bankAccountField.setText("");
			telephoneField.setText("");
			regNumberField.setText("");
			regPlaceField.setText("");
			regTypeComboBox.setSelectedIndex(0);
			levelStateComboBox.setSelectedIndex(0);
		}
	}

	private Provider buildProvider() {
		Provider provider = new Provider();
		provider.setId(idField.getText());
		provider.setName(nameField.getText());
		provider.setZip(zipField.getText());
		provider.setAddress(addressField.getText());
		provider.setShipAddress(shipAddressField.getText());
		provider.setTelephone(telephoneField.getText());
		provider.setContacts(contactsField.getText());
		provider.setMobile(mobileField.getText());
		provider.setEmail(emailField.getText());
		provider.setFax(faxField.getText());
		provider.setBank(bankField.getText());
		provider.setAccount(bankAccountField.getText());
		provider.setRegistryPlace(regPlaceField.getText());
		provider.setRegistryNumber(regNumberField.getText());
		if (regTypeComboBox.getSelectedItem() != null) {
			provider.setRegistryType(regTypeComboBox.getSelectedItem()
					.toString());
		}
		if (levelStateComboBox.getSelectedItem() != null) {
			provider.setLevelState(levelStateComboBox.getSelectedItem()
					.toString());
		}
		
		return provider;
	}

	class AddButtonListener extends BaseAddButtonListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			Provider provider = buildProvider();

			ProviderService providerService = CommonFactory
					.getProviderService();

			ProviderValidator providerValidator = new ProviderValidator(
					provider, providerService, editType);

			String errorString = providerValidator.getErrorString();
			if (errorString != null) {
				showWarningMsg(errorString);
				return;
			}

			if (providerService.saveProvider(provider)) {
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
			Provider provider = buildProvider();
			ProviderService providerService = CommonFactory
					.getProviderService();
			ProviderValidator providerValidator = new ProviderValidator(
					provider, providerService, editType);

			String errorString = providerValidator.getErrorString();
			if (errorString != null) {
				showWarningMsg(errorString);
				return;
			}
			if (providerService.modifyProvider(provider)) {
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
			ProviderService providerService = CommonFactory
					.getProviderService();
			if (showConfirmDialog() == JOptionPane.YES_OPTION) {
				if (providerService.removeProvider(idField.getText())) {
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

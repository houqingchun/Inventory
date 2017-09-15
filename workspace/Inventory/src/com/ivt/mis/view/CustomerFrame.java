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
import com.ivt.mis.model.Customer;
import com.ivt.mis.model.FieldDefn;
import com.ivt.mis.service.CustomerService;
import com.ivt.mis.view.validator.CustomerValidator;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.Size;
import com.jgoodies.forms.layout.Sizes;

public class CustomerFrame extends BaseJInternalFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8719497764925469726L;

	Customer customer;
	JTextField idField;
	JTextField nameField;
	JTextField addressField;
	JTextField shipAddressField;
	JTextField zipField;
	JTextField telephoneField;
	JTextField contactsField;
	JTextField mobileField;
	JTextField emailField;
	JTextField faxField;
	JTextField bankField;
	JTextField bankAccountField;
	JTextField regPlaceField;
	JTextField regNumberField;
	JTextField commentsField;
	JComboBox regTypeComboBox;
	JComboBox levelStateComboBox;

	CustomerSearchFrame callBackSearchFrame;

	public CustomerFrame(Customer customer, String editType,
			CustomerSearchFrame callBackSearchFrame) {
		super("客户信息管理", true, true, false, true);
		super.setFrameId(Constants.MODULE_CUSTOMER);
		
		//this.setLayer(this.getLayer() + 1);
		this.customer = customer;
		this.editType = editType;
		Dimension deskPaneDim = MainFrame.desktopPane.getSize();
		this.setSize(500, 420);
		this.setResizable(false);
		Dimension internalDim = this.getSize();
		this.setLocation((deskPaneDim.width - internalDim.width) / 2
				+ this.getLayer() * 5,
				(deskPaneDim.height - internalDim.height) / 2 + this.getLayer()
						* 5);

		this.getContentPane().add(createCustomerPanel());
		this.callBackSearchFrame = callBackSearchFrame;
	}

	public JPanel createCustomerPanel() {
		JPanel mainContainer = new JPanel();
		mainContainer.setLayout(new BoxLayout(mainContainer, BoxLayout.Y_AXIS));
		/*
		 * 建立客户添加信息页面
		 */
		nameField = new IvtJTextField(50);
		addressField = new IvtJTextField(100);
		shipAddressField = new IvtJTextField(100);
		idField = new IvtJTextField(16, true);
		zipField = new IvtJTextField(6);
		telephoneField = new IvtJTextField(20);
		contactsField = new IvtJTextField(50);
		mobileField = new IvtJTextField(20);
		emailField = new IvtJTextField(50);
		faxField = new IvtJTextField(20);
		bankField = new IvtJTextField(50);
		bankAccountField = new IvtJTextField(50);
		regPlaceField = new IvtJTextField(50);
		regNumberField = new IvtJTextField(50);
		commentsField = new IvtJTextField(100);
		regTypeComboBox = super.getIvtRegistryType();
		levelStateComboBox = super.getIvtLevelState();

		JPanel addPanel = new JPanel();

		CellConstraints cc = new CellConstraints();

		DefaultFormBuilder builder = IvtLayoutUtilities.getFormBuilder();
		Customer fields = new Customer();
		// builder.append(new JLabel("客户编号"));
		builder.append(fields.getFieldDefn("id").getDescrLong());
		builder.append(idField);
		builder.append(new JLabel("此为根据编码规则自动生成,可修改"), 3);
		builder.nextLine();
		// builder.append(new JLabel("客户名称"));
		builder.append(fields.getFieldDefn("name").getDescrLong());
		builder.append(nameField, 5);
		builder.nextLine();
		// builder.append(new JLabel("联系地址"));
		builder.append(fields.getFieldDefn("address").getDescrLong());
		builder.append(addressField, 5);
		builder.nextLine();
		// builder.append(new JLabel("收货地址"));
		builder.append(fields.getFieldDefn("shipAddress").getDescrLong());
		builder.append(shipAddressField, 5);
		builder.nextLine();
		// builder.append(new JLabel("联系人"));
		builder.append(fields.getFieldDefn("contacts").getDescrLong());
		builder.append(contactsField);
		// builder.append(new JLabel("邮政编码"));
		builder.append(fields.getFieldDefn("zip").getDescrLong());
		builder.append(zipField);
		builder.nextLine();
		// builder.append(new JLabel("联系电话"));
		builder.append(fields.getFieldDefn("telephone").getDescrLong());
		builder.append(telephoneField);
		// builder.append(new JLabel("移动电话"));
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
		// builder.append(new JLabel("注册地"));
		builder.append(fields.getFieldDefn("registryPlace").getDescrLong());
		builder.append(regPlaceField);
		// builder.append(new JLabel("注册号"));
		builder.append(fields.getFieldDefn("registryNumber").getDescrLong());
		builder.append(regNumberField);
		builder.nextLine();
		// builder.append(new JLabel("注册类型"));
		builder.append(fields.getFieldDefn("registryType").getDescrLong());
		builder.append(regTypeComboBox);
		// builder.append(new JLabel("级别"));
		builder.append(fields.getFieldDefn("levelState").getDescrLong());
		builder.append(levelStateComboBox);

		builder.nextLine();
		// builder.append(new JLabel("开户银行"));
		builder.append(fields.getFieldDefn("bank").getDescrLong());
		builder.append(bankField, 3);
		builder.nextLine();
		// builder.append(new JLabel("开户帐号"));
		builder.append(fields.getFieldDefn("account").getDescrLong());
		builder.append(bankAccountField, 5);
		builder.nextLine();
		// builder.append(new JLabel("备注"));
		builder.append(fields.getFieldDefn("comments").getDescrLong());
		builder.append(commentsField, 5);

		// builder.add(customerBankAccountField, cc.xyw(builder.getColumn(),
		// builder.getRow(), 5));

		// 添加到主Panel内
		addPanel.add(builder.getPanel());

		JPanel buttonsPanel = super.createCRUDButtons(new AddButtonListener(),
				new UpdateButtonListener(), new DeleteButtonListener());

		// 装载数据
		populateForm();
		mainContainer.add(addPanel);
		mainContainer.add(buttonsPanel);

		return mainContainer;
	}

	public void populateForm() {
		if (Constants.FORM_TYPE_UPDATE.equals(editType)
				|| Constants.FORM_TYPE_DELETE.equals(editType)) {
			idField.setText(customer.getId());
			nameField.setText(customer.getName());
			idField.setEditable(false);
			zipField.setText(customer.getZip());
			addressField.setText(customer.getAddress());
			shipAddressField.setText(customer.getShipAddress());
			contactsField.setText(customer.getContacts());
			mobileField.setText(customer.getMobile());
			emailField.setText(customer.getEmail());
			faxField.setText(customer.getFax());
			bankField.setText(customer.getBank());
			bankAccountField.setText(customer.getAccount());
			telephoneField.setText(customer.getTelephone());
			regPlaceField.setText(customer.getRegistryPlace());
			regNumberField.setText(customer.getRegistryNumber());
			regTypeComboBox.setSelectedItem(customer.getRegistryType());
			commentsField.setText(customer.getComments());
			levelStateComboBox
					.setSelectedItem(customer.getLevelState());
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
				commentsField.setEditable(false);
				regTypeComboBox.setEnabled(false);
				levelStateComboBox.setEnabled(false);
			}
		} else if (Constants.FORM_TYPE_ADD.equals(editType)) {
			idField.setText(CommonFactory.getCodeRuleService()
					.getNextObjectCode(Constants.MODULE_CUSTOMER));
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
			commentsField.setText("");
			regPlaceField.setText("");
			regTypeComboBox.setSelectedIndex(0);
			levelStateComboBox.setSelectedIndex(0);
		}
	}

	public Customer buildCustomer() {
		Customer customer = new Customer();
		customer.setId(idField.getText());
		customer.setName(nameField.getText());
		customer.setZip(zipField.getText());
		customer.setAddress(addressField.getText());
		customer.setShipAddress(shipAddressField.getText());
		customer.setTelephone(telephoneField.getText());
		customer.setContacts(contactsField.getText());
		customer.setMobile(mobileField.getText());
		customer.setEmail(emailField.getText());
		customer.setFax(faxField.getText());
		if (regTypeComboBox.getSelectedItem() != null) {
			customer.setRegistryType(regTypeComboBox.getSelectedItem()
					.toString());
		}
		if (levelStateComboBox.getSelectedItem() != null) {
			customer.setLevelState(levelStateComboBox.getSelectedItem()
					.toString());
		}

		customer.setBank(bankField.getText());
		customer.setAccount(bankAccountField.getText());
		customer.setRegistryPlace(regPlaceField.getText());
		customer.setRegistryNumber(regNumberField.getText());
		customer.setComments(commentsField.getText());
		customer.setOwnerId(MainFrame.loginname);

		return customer;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	class AddButtonListener extends BaseAddButtonListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			Customer customer = buildCustomer();

			CustomerService customerService = CommonFactory
					.getCustomerService();

			CustomerValidator customerValidator = new CustomerValidator(
					customer, customerService, editType);

			String errorString = customerValidator.getErrorString();
			if (errorString != null) {
				showWarningMsg(errorString);
				return;
			}

			if (customerService.saveCustomer(customer)) {
				showInfoMsg("客户信息"
						+ getText("form.event.success",
								new String[] { getText("form.btn.save") }));

				logger.debug("callback object:" + callBackSearchFrame);
				if (callBackSearchFrame != null) {
					logger.debug("call back method execute");
					callBackSearchFrame.callBackRefresh(false);
				}
				dispose();
			} else {
				showErrorMsg("客户信息"
						+ getText("form.event.fail",
								new String[] { getText("form.btn.save") }));
			}
		}
	}

	class UpdateButtonListener extends BaseUpdateButtonListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			Customer customer = buildCustomer();
			CustomerService customerService = CommonFactory
					.getCustomerService();
			CustomerValidator customerValidator = new CustomerValidator(
					customer, customerService, editType);

			String errorString = customerValidator.getErrorString();
			if (errorString != null) {
				showWarningMsg(errorString);
				return;
			}

			if (customerService.modifyCustomer(customer)) {
				showInfoMsg("客户信息"
						+ getText("form.event.success",
								new String[] { getText("form.btn.update") }));

				logger.debug("Save customer info successfully");
				logger.debug("callback object:" + callBackSearchFrame);
				if (callBackSearchFrame != null) {
					logger.debug("call back method execute");
					callBackSearchFrame.callBackRefresh(false);
				}
				dispose();
			} else {
				showErrorMsg("客户信息"
						+ getText("form.event.fail",
								new String[] { getText("form.btn.update") }));
			}
		}
	}

	class DeleteButtonListener extends BaseDeleteButtonListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			CustomerService customerService = CommonFactory
					.getCustomerService();
			if (showConfirmDialog() == JOptionPane.YES_OPTION) {
				if (customerService.removeCustomer(idField.getText())) {
					showInfoMsg("客户信息"
							+ getText("form.event.success",
									new String[] { getText("form.btn.delete") }));

					if (callBackSearchFrame != null) {
						callBackSearchFrame.callBackRefresh(false);
					}
					dispose();
				} else {
					showErrorMsg("客户信息"
							+ getText("form.event.fail",
									new String[] { getText("form.btn.delete") }));
				}
			} else {
				dispose();
			}
		}
	}
}

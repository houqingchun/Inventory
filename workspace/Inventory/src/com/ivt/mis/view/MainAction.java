package com.ivt.mis.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.util.Vector;

import javax.swing.JInternalFrame;

import com.ivt.mis.common.Constants;
import com.ivt.mis.model.CodeRule;
import com.ivt.mis.model.Customer;
import com.ivt.mis.model.Product;
import com.ivt.mis.model.Provider;
import com.ivt.mis.model.StoreProcurement;
import com.ivt.mis.model.StoreShipment;

public class MainAction {
	public static ActionListener clickCustomerInfoManager(
			final Customer customer, final String editType) {

		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!MainFrame.isInternalFrameOpened(Constants.MODULE_CUSTOMER, false)) {
					MainFrame.openNewFrame(new CustomerFrame(customer,
							editType, null), false);
				}
			}
		};
	}

	public static ActionListener clickProductInfoManager(final Product product,
			final String editType) {

		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!MainFrame.isInternalFrameOpened(Constants.MODULE_PRODUCT, false)) {
					MainFrame.openNewFrame(new ProductFrame(product, editType, null), false);
				}
			}
		};
	}

	public static ActionListener clickProviderInfoManager(
			final Provider provider, final String editType) {

		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!MainFrame.isInternalFrameOpened(Constants.MODULE_PROVIDER, false)) {
					MainFrame.openNewFrame(new ProviderFrame(provider, editType,
							null), false);
				}
			}
		};
	}

	public static ActionListener clickCustomerInforserch() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!MainFrame.isInternalFrameOpened(Constants.MODULE_CUSTOMER_SEARCH, true)) {
					MainFrame.openNewFrame(new CustomerSearchFrame(), true);
				}
			}
		};
	}

	public static ActionListener clickProductInforserch() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!MainFrame.isInternalFrameOpened(Constants.MODULE_PRODUCT_SEARCH, true)) {
					MainFrame.openNewFrame(new ProductSearchFrame(), true);
				}
			}
		};
	}

	public static ActionListener clickInputInforserch() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!MainFrame.isInternalFrameOpened(Constants.MODULE_PROCUREMENT_SEARCH,true)) {
					MainFrame.openNewFrame(new StoreProcurementSearchFrame(), true);
				}
			}
		};
	}

	public static ActionListener clickPrivoderInforSearch() {
		return new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (!MainFrame.isInternalFrameOpened(Constants.MODULE_PROVIDER_SEARCH, true)) {
					MainFrame.openNewFrame(new ProviderSearchFrame(), true);
				}
			}
		};
	}

	public static ActionListener clickSaleInforSearch() {
		return new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (!MainFrame.isInternalFrameOpened(Constants.MODULE_SHIPMENT_SEARCH, true)) {
					MainFrame.openNewFrame(new StoreShipmentSearchFrame(), true);
				}
			}
		};
	}

	public static ActionListener clickCustomization() {
		return new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				new CustomizationJDialog();
			}
		};
	}

	public static ActionListener clickFeedback(final String editType) {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!MainFrame.isInternalFrameOpened(Constants.MODULE_CONTACTME, false)) {
					MainFrame.openNewFrame(new ContactMeFrame(editType), false);
				}
			}
		};
	}

	public static ActionListener clickDataBackupRestore() {
		return new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (!MainFrame.isInternalFrameOpened(Constants.MODULE_DB_BACKUP, false)) {
					MainFrame.openNewFrame(new DataBackupRestoreFrame(), false);
				}
			}
		};
	}

	public static ActionListener clickDataImport() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!MainFrame.isInternalFrameOpened(Constants.MODULE_EXCEL_TEMPLATE, false)) {
					MainFrame.openNewFrame(new ExcelDataImportFrame(
							new Vector<Object>(), new Customer()), false);
				}
			}
		};
	}

	public static ActionListener operaterManager() {
		return new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (!MainFrame.isInternalFrameOpened(Constants.MODULE_USER, false)) {
					MainFrame.openNewFrame(new UserFrame(), false);
				}
			}
		};
	}

	public static ActionListener storeShipment(
			final Vector<StoreShipment> storeShipments, final String editType) {
		return new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (!MainFrame.isInternalFrameOpened(Constants.MODULE_SHIPMENT, true)) {
					MainFrame.openNewFrame( new StoreShipmentFrame(
							storeShipments, editType, null), true);
				}
			}
		};
	}

	public static ActionListener storeProcurement(
			final Vector<StoreProcurement> storeProcurements,
			final String editType) {
		return new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (!MainFrame.isInternalFrameOpened(Constants.MODULE_PROCUREMENT, true)) {
					MainFrame.openNewFrame( new StoreProcurementFrame(
							storeProcurements, editType, null), true);
				}
			}
		};
	}

	public static ActionListener storeHouseInfor() {
		return new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (!MainFrame.isInternalFrameOpened(Constants.MODULE_STORE_MANAGE, true)) {
					MainFrame.openNewFrame( new StoreManageFrame(), true);
				}
			}
		};
	}

	public static ActionListener codeRuleConfiguration(final CodeRule codeRule,
			final String editType) {
		return new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (!MainFrame.isInternalFrameOpened(Constants.MODULE_CODE_RULE, false)) {
					MainFrame.openNewFrame(new CodeRuleFrame(codeRule, editType), false);
				}
			}
		};
	}

	public static ActionListener changePassword() {
		return new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				UserPwdChangeFrame frame = new UserPwdChangeFrame();

				if (!MainFrame.isInternalFrameOpened(Constants.MODULE_USER_PWD, false)) {
					MainFrame.openNewFrame(new UserPwdChangeFrame(), false);
				}

			}
		};
	}

	/**
	 * 设置窗口初始状态即为最大化
	 * 
	 * @param frame
	 */
	private static void maxtJInternalFrame(JInternalFrame frame) {
		try {
			if (frame != null) {
				frame.setMaximum(true);
			}
		} catch (PropertyVetoException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}

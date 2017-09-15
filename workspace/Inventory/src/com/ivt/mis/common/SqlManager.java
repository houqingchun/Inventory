/*
 * 数据库操作类，进行数据库底层操作
 * 配置信息在Config.properties文件中
 * Made By:coolszy
 * 2009.07.07
 */

package com.ivt.mis.common;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.PropertyResourceBundle;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;

public class SqlManager {
	public static final Logger logger = Logger.getLogger(SqlManager.class);

	// 定义常量
	private final int PSTM_TYPE = 0;
	private final int CALL_TYPE = 1;

	private static SqlManager manager = null; // 静态成员变量，支持单态模式
	private PropertyResourceBundle bundle; // 配置资源文件
	private static String jdbcDrive = null; // JDBC驱动类型
	private String DBhost = ""; // 数据库主机地址
	private String DBname = ""; // 数据库名
	private String DBprot = ""; // 数据库端口
	private String DBuser = ""; // 数据库用户名
	private String DBpasswd = ""; // 数据库密码
	private String strcon = null; // 连接字符串
	private String DBUrl = null; // 连接字符串

	private Connection conn = null; // 连接对象
	private PreparedStatement pstm = null;
	private CallableStatement cstm = null;

	public String databaseType = "sqlite";

	private BasicDataSource ds;

	public static boolean isNewDB = false;

	/**
	 * 私有构造函数,不可实例化
	 */
	private SqlManager() {
		try {
			// 读取配置文件
			bundle = new PropertyResourceBundle(
					SqlManager.class.getResourceAsStream("/db.properties"));
			this.DBhost = getString("DBhost"); // 读取主机名
			this.DBname = getString("DBname"); // 读取用户名
			this.DBprot = getString("DBport"); // 读取端口
			this.DBuser = getString("DBuser"); // 读取用户
			this.DBpasswd = getString("DBpassword"); // 读取密码
			this.DBUrl = getString("DBurl"); // 读取连接字串
			databaseType = getString("database-type"); // 读取数据库类型
			if (databaseType != null) // 如果类型不为空
			{
				if (databaseType.toLowerCase().equals("mysql")) { // 设置mysql数据库的驱动程序和连接字符
					jdbcDrive = "com.mysql.jdbc.Driver";
					strcon = "jdbc:mysql://" + DBhost + ":" + DBprot + "/"
							+ DBname;
				} else if (databaseType.toLowerCase().equals("oracle")) { // 设置oracle数据库的驱动程序和连接字符
					jdbcDrive = "oracle.jdbc.driver.OracleDrive";
					strcon = "jdbc:oracle:thin:@" + DBhost + ":" + DBprot + ":"
							+ DBname;
				} else if (databaseType.toLowerCase().equals("sqlserver2000")) { // 设置sqlserver2000数据库的驱动程序和连接字符
					jdbcDrive = "com.microsoft.jdbc.sqlserver.SQLServerDriver";
					strcon = "jdbc:micorsoft:sqlserver://" + DBhost + ":"
							+ DBprot + ";DatabaseName=" + DBname;
				} else if (databaseType.toLowerCase().equals("sqlserver2005")) { // 设置sqlserver2005数据库的驱动程序和连接字符
					jdbcDrive = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
					strcon = "jdbc:sqlserver://" + DBhost + ":" + DBprot
							+ ";DatabaseName=" + DBname;
				} else if (databaseType.toLowerCase().equals("sqlite")) { // 设置sqlite数据库的驱动程序和连接字符
					jdbcDrive = "org.sqlite.JDBC";
					strcon = "jdbc:sqlite:" + DBname + ".db";
				}
			}
		} catch (Exception e) {
			logger.error("SqlManager Error!" + e.getMessage());

			e.printStackTrace();
		}

		ds = new BasicDataSource();
		ds.setDriverClassName(jdbcDrive);
		ds.setUsername(DBuser);
		ds.setPassword(DBpasswd);
		ds.setUrl(strcon);

		// the settings below are optional -- dbcp can work with defaults
		ds.setMinIdle(2);
		ds.setMaxIdle(5);
		ds.setMaxOpenPreparedStatements(180);
	}

	/**
	 * 读取配置文件中的值
	 * 
	 * @param key
	 *            配置文件的key
	 * @return key对应的值
	 */
	private String getString(String key) {
		return this.bundle.getString(key);
	}

	/**
	 * 单态模式获取实例
	 * 
	 * @return SqlManager对象
	 */
	public static SqlManager createInstance() {
		if (manager == null) {
			manager = new SqlManager();
		}
		return manager;
	}

	/**
	 * 连接数据库
	 */
	public void connectDB() {
		try {
			conn = this.ds.getConnection();
			conn.setAutoCommit(false); // 设置自动提交为false
		} catch (Exception e) {
			logger.error("connectDB Error!" + e.getMessage());
			e.printStackTrace();
			return;
		}
		logger.info("成功连接到数据库！");
	}

	/**
	 * 断开数据库
	 */
	public void closeDB() {
		if (pstm != null) {
			try {
				pstm.close();
			} catch (SQLException e) {
				e.printStackTrace();
				logger.error("Close preparedstatement Error!" + e.getMessage());
			}
		}
		if (cstm != null) {
			try {
				cstm.close();
			} catch (SQLException e) {
				e.printStackTrace();
				logger.error("Close callstatemnet Error!" + e.getMessage());
			}
		}
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
				logger.error("Close connection Error!" + e.getMessage());
			}
		}

		logger.info("成功关闭数据库！");
	}

	public void closeRs(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
				logger.error("Close resultset Error!" + e.getMessage());
			}
		}
	}

	/**
	 * 设置PrepareStatement对象中Sql语句中的参数
	 * 
	 * @param sql
	 *            sql语句
	 * @param params
	 *            参数列表
	 */
	private void setPrepareStatementParams(String sql, Object[] params) {
		try {
			pstm = conn.prepareStatement(sql); // 获取对象
			if (params != null) {
				for (int i = 0; i < params.length; i++) // 遍历参数列表填充参数
				{
					pstm.setObject(i + 1, params[i]);
				}
			}
		} catch (SQLException e) {
			logger.error("setPrepareStatementParams Error!" + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 设置CallableStatementParams对象中sql语句中的参数
	 * 
	 * @param sql
	 *            sql语句
	 * @param params
	 *            参数列表
	 */
	private void setCallableStatementParams(String sql, Object[] params) {
		try {
			cstm = conn.prepareCall(sql); // 获取CallableStatement对象
			if (params != null) {
				for (int i = 0; i < params.length; i++) // 遍历数组填充参数
				{
					cstm.setObject(i + 1, params[i]);
				}
			}
		} catch (SQLException e) {
			logger.error("setCallableStatementParams Error!" + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 执行查询
	 * 
	 * @param sql
	 *            sql语句
	 * @param params
	 *            参数列表
	 * @param type
	 *            sql语句的类型
	 * @return 返回ResultSet类型的查询结果
	 */
	public ResultSet executeQuery(String sql, Object[] params, int type) { // 执行查询数据库接口
		ResultSet rs = null;
		try {
			switch (type)
			// 判断是PrepareStatement还是CallableStatement
			{
			case PSTM_TYPE:
				manager.setPrepareStatementParams(sql, params); // 填充参数
				rs = pstm.executeQuery(); // 执行查询操作
				break;
			case CALL_TYPE:
				manager.setCallableStatementParams(sql, params); // 填充参数
				rs = cstm.executeQuery(); // 执行查询
				break;
			default:
				throw new Exception("不存在该选项");
			}
		} catch (Exception e) {
			logger.error("executeQuery Error!" + e.getMessage());
			e.printStackTrace();
		}
		return rs;
	}

	// public ResultSet executeQuery(String sql)
	// {
	// ResultSet rs = null;
	// try
	// {
	// Statement stmt=conn.createStatement();
	// rs=stmt.executeQuery(sql);
	// }
	// catch (Exception e)
	// {
	// logger.error("executeQuery Error!" + e.getMessage());
	// e.printStackTrace();
	// }
	// return rs;
	// }

	/**
	 * 更新数据库操作
	 * 
	 * @param sql
	 *            sql语句
	 * @param params
	 *            参数列表
	 * @param type
	 *            sql语句的类型
	 * @return 执行操作的结果
	 */
	public boolean executeUpdate(String sql, Object[] params, int type) // 执行无返回数据的数据查询，返回值是被改变的书库的数据库项数
	{
		boolean result = false;
		try {
			switch (type)
			// 判断是PrepareStatement还是CallableStatement
			{
			case PSTM_TYPE:
				manager.setPrepareStatementParams(sql, params); // 填充参数
				pstm.executeUpdate(); // 执行更新
				manager.commitChange();
				result = true;
				break;
			case CALL_TYPE:
				manager.setCallableStatementParams(sql, params); // 填充参数
				cstm.executeUpdate(); // 执行更新
				manager.commitChange();
				result = true;
				break;
			default:
				throw new Exception("不存在该选项");
			}
		} catch (Exception e) {
			logger.error("executeUpdate Error!:" + e.getMessage());
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 提交信息到数据库
	 * 
	 * @throws SQLException
	 */
	private void commitChange() throws SQLException {
		try {
			conn.commit();
			logger.debug("数据提交成功！");
		} catch (Exception e) {
			logger.error("CommitChange Error!" + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 初始化数据库，创建系统数据表
	 */
	public void buildDatabase() {
		Statement batch = null;
		Connection connBd = null;
		try {
			Class.forName(jdbcDrive);
			connBd = DriverManager.getConnection(DBUrl);
			batch = connBd.createStatement();

			String sql = "CREATE TABLE ivt_customer(														"
					+ "	id char(32) primary key,                                                    "
					+ "	name varchar(255)  NOT NULL,                                         "
					+ "	zip char(6)  NULL,                                                          "
					+ "	address varchar(255)  NULL,                                                  "
					+ "	shipaddress varchar(255)  NULL,                                                  "
					+ "	telephone varchar(20)  NULL,                                                "
					+ "	contacts varchar(255)  NULL,                                         "
					+ "	mobile varchar(20)  NULL,                                                    "
					+ "	bank varchar(255)  NULL,                                                     "
					+ "	account varchar(255) NULL,                                                   "
					+ "	email varchar(255)  NULL,                                                    "
					+ "	fax varchar(20)  NULL,                                                      "
					+ "	registryplace varchar(255),                                                  "
					+ "	registrynumber varchar(255),                                                 "
					+ "	registrytype varchar(255),                                                 "
					+ "	levelState varchar(255),                                                 "
					+ "	comments varchar(255),                                           "
					+ "	createTime datetime ,                                                "
					+ "	ownerid varchar(255),                                                 "
					+ "	version int NOT NULL ,                                                 "
					+ "	available  int not null                                                     "
					+ ");                                                                             ";
			batch.addBatch(sql);

			sql = "CREATE TABLE ivt_provider(                                                      "
					+ "	id char(32) NOT NULL primary key,                                                    "
					+ "	name varchar(255)  NOT NULL,                                         "
					+ "	zip char(6)  NULL,                                                          "
					+ "	address varchar(255)  NULL,                                                  "
					+ "	shipaddress varchar(255)  NULL,                                                  "
					+ "	telephone varchar(20)  NULL,                                                "
					+ "	contacts varchar(255)  NULL,                                         "
					+ "	mobile varchar(20)  NULL,                                                    "
					+ "	bank varchar(255)  NULL,                                                     "
					+ "	account varchar(255) NULL,                                                   "
					+ "	email varchar(255)  NULL,                                                    "
					+ "	fax varchar(20)  NULL,                                                      "
					+ "	registryplace varchar(255),                                                  "
					+ "	registrynumber varchar(255),                                                 "
					+ "	registrytype varchar(255),                                                 "
					+ "	levelState varchar(255),                                                 "
					+ "	ownerid varchar(255),                                                 "
					+ "	createTime datetime,                                                "
					+ "	version int NOT NULL ,                                                 "
					+ "	available int not null	                                                    "
					+ ");                                                                             ";
			batch.addBatch(sql);
			sql = "CREATE TABLE ivt_product(                                                         "
					+ "	id char(32) NOT NULL primary key,                                                    "
					+ "	productcode varchar(255) NOT NULL,                                             "
					+ "	productname varchar(255) NOT NULL,                                             "
					+ "	manufacturer varchar(255) NULL,                                              "
					+ "	brand  varchar(255) NULL,                                                "
					+ "	size varchar(255) NULL,                                                      "
					+ "	category varchar(255) NULL,                                                   "
					+ "	unit varchar(255) NULL,                                                   "
					+ "	pack varchar(255) NULL,                                               "
					+ "	miniPack varchar(255) NULL,                                               "
					+ "	promitcode varchar(255) NULL,                                                "
					+ "	description varchar(255) NULL,                                               "
					+ "	available  int not null,                                                    "
					+ "	createTime datetime ,                                                "
					+ "	version int NOT NULL,                                                 "
					+ "	ownerid varchar(255)                                                 "
					+ ");                                                                             ";
			batch.addBatch(sql);
			sql = "CREATE TABLE ivt_storeshipment(                                                         "
					+ "	id char(32) NOT NULL primary key,                                                    "
					+ "	groupId char(32) NOT NULL,   												"
					+ "	productId char(32) NOT NULL,                                                  "
					+ "	storeProcurementId char(32) NULL,                                                  "
					+ "	productName varchar(255) NULL,                                                  "
					+ "	productCode varchar(255) NULL,                                                  "
					+ "	productbrand varchar(255) NULL,                                                  "
					+ "	customerId varchar(255) NOT NULL,                                               "
					+ "	customerName varchar(255) NULL,                                               "
					+ "	pack varchar(255) NULL,                                               "
					+ "	miniPack varchar(255) NULL,                                               "
					+ "	produceTime datetime NULL,                                                "
					+ "	produceCode varchar(255) NULL,                                               "
					+ "	sellCode varchar(255) NULL,                                               "
					+ "	expressNbr varchar(255) NULL,                                               "
					+ "	createTime datetime NOT NULL,                                                "
					+ "	userid varchar(255) NOT NULL,                                         "
					+ "	shipNumber int NULL,                                                        "
					+ "	unitPrice double NULL,                                                      "
					+ "	shipType varchar(255) NULL,                                                   "
					+ "	unit varchar(255) NULL,                                                   "
					+ "	currency varchar(20) NULL,                                                   "
					+ "	payDueDate date NULL,                                                  "
					+ "	actPayDueDate date NULL,                                                  "
					+ "	comment varchar(255) NULL,                                                  "
					+ "	comment varchar(255) NULL,                                                   "
					+ "	version int NOT NULL,                                                 "
					+ "	foreign key(customerid) references ivt_customer(id),                         "
					+ "	foreign key(productid) references ivt_product(id)                                "
					+ ");                                                                             ";
			batch.addBatch(sql);
			sql = "CREATE TABLE ivt_storeprocurement(                                                        "
					+ "	id char(32)  NOT NULL primary key,                                                    "
					+ "	groupid char(32) NOT NULL,                                               "
					+ "	providerid char(32) NOT NULL,                                               "
					+ "	providername varchar(255) NULL,                                               "
					+ "	customerid char(32) NULL,                                               "
					+ "	customername varchar(255) NULL,                                               "
					+ "	productid char(32) NOT NULL,                                                  "
					+ "	productname varchar(255) NULL,                                                  "
					+ "	productcode varchar(255) NULL,                                                  "
					+ "	productbrand varchar(255) NULL,                                                  "
					+ "	pack varchar(255) NULL,                                                  "
					+ "	miniPack varchar(255) NULL,                                                  "
					+ "	producecode varchar(255) NULL,                                                  "
					+ "	purchasecode varchar(255) NULL,                                                  "
					+ "	daycode varchar(255) NULL,                                                  "
					+ "	createtime datetime NOT NULL,                                               "
					+ "	producetime date NULL,                                              "
					+ "	pronumber int NULL,                                                        "
					+ "	proType varchar(255) NULL,                                                   "
					+ "	unit varchar(255) NULL,                                                   "
					+ "	unitprice double NULL,	                                                "
					+ "	currency varchar(20) NULL,                                                   "
					+ "	comment varchar(255) NULL,                                                  "
					+ "	userid varchar(255) NOT NULL,                                         "
					+ "	version int NOT NULL,                                                 "
					+ "	FOREIGN KEY(productid) REFERENCES ivt_product (id),                              "
					+ "	FOREIGN KEY(providerid) REFERENCES ivt_provider (id)                         "
					+ ");                                                                             ";
			batch.addBatch(sql);
			sql = "CREATE TABLE ivt_storemanage(                                                  "
					+ "	id  char(32) NOT NULL primary key,                                                    "
					+ "	storeProcurementId char(32)  NOT NULL,                                                 "
					+ "	productId char(32)  NOT NULL,                                                 "
					+ "	productCode char(32)  NULL,                                                 "
					+ "	productBrand varchar(255)  NULL,                                                 "
					+ "	totalNbr int NULL,                                                        "
					+ "	version int NOT NULL,                                                 "
					+ "	FOREIGN KEY(productid) REFERENCES ivt_product(id)                                "
					+ ");                                                                             ";
			batch.addBatch(sql);
			sql = "CREATE TABLE ivt_version(                                                      "
					+ "	version char(32) NOT NULL primary key,                                           "
					+ "	comments varchar(255),                                           "
					+ "	updateTime datetime NOT NULL                                               "
					+ ");                                                                          ";
			batch.addBatch(sql);
			sql = "CREATE TABLE ivt_dataversion(                                                      "
					+ "	id char(32) NOT NULL primary key,                                           "
					+ "	dataPath varchar(255),                                               "
					+ "	dataType varchar(255),                                               "
					+ "	comments varchar(255),                                           "
					+ "	version int,                                           "
					+ "	createTime datetime NOT NULL                                               "
					+ ");                                                                          ";
			batch.addBatch(sql);
			sql = "CREATE TABLE ivt_customization(                                                      "
					+ "	id char(32) NOT NULL primary key,                                           "
					+ "	companyName varchar(255),                                           "
					+ "	contacts varchar(255),                                           "
					+ "	mobile varchar(255),                                           "
					+ "	fax varchar(255),                                           "
					+ "	address varchar(255),                                           "
					+ "	email varchar(255),                                           "
					+ "	logoPath varchar(255),                                           "
					+ "	sn varchar(255),                                           "
					+ "	signature varchar(255),                                           "
					+ "	isTrial int,                                           "
					+ "	expiredDays int,                                           "
					+ "	comments varchar(255),                                           "
					+ "	createTime datetime NOT NULL,                                              "
					+ "	version int                                           "
					+ ");                                                                          ";
			batch.addBatch(sql);
			sql = "CREATE TABLE ivt_code_rule(                                                      "
					+ "	id char(32) NOT NULL primary key,                                           "
					+ "	objectType varchar(255),                                           "
					+ "	objectName varchar(255),                                           "
					+ "	available int(10),                                           "
					+ "	autoIncrease int(10),                                           "
					+ "	prefixEnabled int(10),                                           "
					+ "	prefix varchar(10),                                           "
					+ "	suffixEanbled int(10),                                           "
					+ "	suffix varchar(10),                                           "
					+ "	nbrOfSeq int(10),                                           "
					+ "	seqType varchar(10),                                           "
					+ "	seqFormat varchar(30),                                           "
					+ "	currentSeq varchar(255),                                           "
					+ "	comments varchar(255),                                           "
					+ "	createTime datetime NOT NULL,                                              "
					+ "	version int                                           "
					+ "); ";
			batch.addBatch(sql);
			sql = "CREATE TABLE ivt_purchase_order(                                                      "
					+ "	id char(32) NOT NULL primary key,                                           "
					+ "	oppsiteId char(32),                                           "
					+ "	orderType varchar(255),                                           "
					+ "	providerId varchar(255),                                           "
					+ "	providerName varchar(255),                                           "
					+ "	available int(10),                                           "
					+ "	exchangeRate double,                                           "
					+ "	totalAmount double,                                           "
					+ "	depositeRate double,                                           "
					+ "	invoiceType varchar(255),                                           "
					+ "	invoiceRate double,                                           "
					+ "	contact varchar(255),                                           "
					+ "	telephone varchar(20),                                           "
					+ "	fax varchar(20),                                           "
					+ "	email varchar(255),                                           "
					+ "	ownerCompanyName varchar(255),                                           "
					+ "	ownerDeptName varchar(255),                                           "
					+ "	ownerSaleMan varchar(255),                                           "
					+ "	ownerSignedDate datetime,                                           "
					+ "	transportType varchar(255),                                           "
					+ "	transportCost double,                                           "
					+ "	otherCost double,                                           "
					+ "	discount double,                                           "
					+ "	deliveryDays int,                                           "
					+ "	maxDelayDate datetime,                                           "
					+ "	payDate datetime,                                           "
					+ "	currency varchar(255),                                           "
					+ "	comments datetime,                                           "
					+ "	createPerson datetime NOT NULL,                                              "
					+ "	createTime datetime NOT NULL,                                              "
					+ "	updatePerson datetime NULL,                                              "
					+ "	updateTime datetime NULL,                                              "
					+ "	version int                                           "
					+ "); ";
			batch.addBatch(sql);
			sql = "CREATE TABLE ivt_purchase_order_item(                                                      "
					+ "	id char(32) NOT NULL primary key,                                           "
					+ "	purchaseOrderId char(32) not null,                                           "
					+ "	productId char(32),                                           "
					+ "	buyNbr int,                                           "
					+ "	unit varchar(255),                                           "
					+ "	unitPrice double,                                           "
					+ "	totalAmount double,                                           "
					+ "	deliveryDays int,                                           "
					+ "	comments datetime,                                           "
					+ "	version int                                           "
					+ "); ";
			batch.addBatch(sql);
			sql = "CREATE TABLE ivt_user(                                                      "
					+ "	id char(32) NOT NULL primary key,                                                    "
					+ "	loginname varchar(255)  NOT NULL,                                 "
					+ "	password varchar(255)  NOT NULL,                                             "
					+ "	name varchar(255)  NOT NULL,                                                 "
					+ "	version int NOT NULL,                                                      "
					+ "	available int NOT NULL,                                                    "
					+ "	createTime datetime ,                                                "
					+ "	power varchar(255) NOT NULL                                                   "
					+ ");                                                                          ";
			batch.addBatch(sql);
			sql = "insert into ivt_version values ('1.00.02','1.00.02版本','"
					+ BasicTypeUtils.getLongFmtDate() + "');          ";
			batch.addBatch(sql);
			sql = "insert into ivt_user(id,loginname,password,name,version,available,power) values ('1','admin','admin','admin',1,1,'管理员');                   ";
			batch.addBatch(sql);
			batch.executeBatch();
			logger.info("本地数据库创建完成!");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (batch != null) {
				try {
					batch.close();
				} catch (SQLException e) {
					logger.error(e.getMessage());
				}
			}
			if (connBd != null) {
				try {
					connBd.close();
				} catch (SQLException e) {
					logger.error(e.getMessage());
				}
			}
		}
	}

	public void upgradeDatabase() {
		ArrayList<String[]> upgradePool = new ArrayList<String[]>();
		String sql = "";
		/************************************************************************/
		// 1.00.01 版本
		String version = "1.00.01";
		sql = "alter table ivt_storeprocurement add currency varchar(20) null;";
		upgradePool.add(new String[] { version, sql });

		sql = "alter table ivt_storeshipment add currency varchar(20) null;";
		upgradePool.add(new String[] { version, sql });

		sql = "alter table ivt_storeshipment add payDueDate date null;";
		upgradePool.add(new String[] { version, sql });

		sql = "alter table ivt_storeshipment add actPayDueDate date null;";
		upgradePool.add(new String[] { version, sql });

		sql = "alter table ivt_product add unit varchar(255) null;";
		upgradePool.add(new String[] { version, sql });

		// 更新版本
		sql = "insert into ivt_version values('" + version + "','" + version
				+ "版本','" + BasicTypeUtils.getLongFmtDate() + "');";
		upgradePool.add(new String[] { version, sql });
		/************************************************************************/

		// 1.00.02版本
		version = "1.00.02";
		sql = "alter table ivt_storeshipment add miniPack varchar(255) null;";
		upgradePool.add(new String[] { version, sql });
		sql = "alter table ivt_product add pack varchar(255) null;";
		upgradePool.add(new String[] { version, sql });
		sql = "alter table ivt_product add miniPack varchar(255) null;";
		upgradePool.add(new String[] { version, sql });

		sql = "CREATE TABLE ivt_purchase_order(                                                      "
				+ "	id char(32) NOT NULL primary key,                                           "
				+ "	oppsiteId char(32),                                           "
				+ "	orderType varchar(255),                                           "
				+ "	providerId varchar(255),                                           "
				+ "	providerName varchar(255),                                           "
				+ "	available int(10),                                           "
				+ "	exchangeRate double,                                           "
				+ "	totalAmount double,                                           "
				+ "	depositeRate double,                                           "
				+ "	invoiceType varchar(255),                                           "
				+ "	invoiceRate double,                                           "
				+ "	contact varchar(255),                                           "
				+ "	telephone varchar(20),                                           "
				+ "	fax varchar(20),                                           "
				+ "	email varchar(255),                                           "
				+ "	ownerCompanyName varchar(255),                                           "
				+ "	ownerDeptName varchar(255),                                           "
				+ "	ownerSaleMan varchar(255),                                           "
				+ "	ownerSignedDate datetime,                                           "
				+ "	transportType varchar(255),                                           "
				+ "	transportCost double,                                           "
				+ "	otherCost double,                                           "
				+ "	discount double,                                           "
				+ "	deliveryDays int,                                           "
				+ "	maxDelayDate datetime,                                           "
				+ "	payDate datetime,                                           "
				+ "	currency varchar(255),                                           "
				+ "	comments datetime,                                           "
				+ "	createPerson datetime NOT NULL,                                              "
				+ "	createTime datetime NOT NULL,                                              "
				+ "	updatePerson datetime NULL,                                              "
				+ "	updateTime datetime NULL,                                              "
				+ "	version int                                           "
				+ "); ";
		upgradePool.add(new String[] { version, sql });
		sql = "CREATE TABLE ivt_purchase_order_item(                                                      "
				+ "	id char(32) NOT NULL primary key,                                           "
				+ "	purchaseOrderId char(32) not null,                                           "
				+ "	productId char(32),                                           "
				+ "	buyNbr int,                                           "
				+ "	unit varchar(255),                                           "
				+ "	unitPrice double,                                           "
				+ "	totalAmount double,                                           "
				+ "	deliveryDays int,                                           "
				+ "	comments datetime,                                           "
				+ "	version int                                           "
				+ "); ";
		upgradePool.add(new String[] { version, sql });
		// 更新版本
		sql = "insert into ivt_version values('" + version + "','" + version
				+ "版本','" + BasicTypeUtils.getLongFmtDate() + "');";
		upgradePool.add(new String[] { version, sql });

		/************************************************************************/
		// 1.00.03版本
		version = "1.00.03";
//		sql = "; ";
//		upgradePool.add(new String[] { version, sql });
		
		/************************************************************************/

		// 清除编码规则
		// upgradePool.add(new String[] {
		// "1.07",
		// "delete from ivt_code_rule;" });

		Statement batch = null;
		Connection connBd = null;
		ResultSet rs = null;
		try {
			Class.forName(jdbcDrive);
			connBd = DriverManager.getConnection(DBUrl);
			batch = connBd.createStatement();
			String oldVersion = "1.00";
			// 取得DB中的最大版本
			rs = batch.executeQuery("select max(version) from ivt_version");
			if (rs.next()) {
				oldVersion = rs.getString(1);
				Constants.IVT_VERSION = rs.getString(1);
			}
			rs.close();

			// if (Constants.IS_UPGRADE_MODE) {
			for (int i = 0; i < upgradePool.size(); i++) {
				String[] tmp = upgradePool.get(i);
				String newVersion = tmp[0];
				if (newVersion.compareTo(oldVersion) > 0) {
					batch.addBatch(tmp[1]);
					logger.info("升级数据库，编号" + newVersion + " 内容：" + tmp[1]);
				}
			}
			// }

			batch.executeBatch();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (batch != null) {
				try {
					batch.close();
				} catch (SQLException e) {
					logger.error(e.getMessage());
				}
			}
			if (connBd != null) {
				try {
					connBd.close();
				} catch (SQLException e) {
					logger.error(e.getMessage());
				}
			}

		}
	}
}

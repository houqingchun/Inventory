package com.ivt.mis.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.ivt.mis.common.Constants;

public abstract class BaseDAO extends SqlMapClientDaoSupport {
	// SqlMapClient sqlMapClient = IbatisUtil.getSqlMapClient();
	public BaseDAO() {

	}

	public BaseDAO(SqlMapClient sqlMapClient) {
		setSqlMapClient(sqlMapClient);
	}

	// 添加对象
	public boolean addObj(String sqlKey, Object obj) {
		try {
			this.getSqlMapClient().insert(sqlKey, obj);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	// 批量添加对象
	public boolean addBatchObj(String sqlKey, List<?> objs) {
		int total = 0;
		int count = 0;
		try {
			this.getSqlMapClient().startTransaction();
			this.getSqlMapClient().startBatch();
			for (int i = 0; i < objs.size(); i++) {
				this.getSqlMapClient().insert(sqlKey, objs.get(i));
				count++;

				if (count % Constants.DB_BATCH_SIZE == 0) {
					total += this.getSqlMapClient().executeBatch();
					this.getSqlMapClient().startBatch();
				}
			}

			total += this.getSqlMapClient().executeBatch();
			this.getSqlMapClient().commitTransaction();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				this.getSqlMapClient().endTransaction();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return objs.size() == total;
	}

	// 批量添加对象
	// public boolean addBatchObj(String sqlKey, List<?> objs) {
	// int total = 0;
	// int count = 0;
	// SqlMapClient client = IbatisUtil.getSqlMapClient();
	// try {
	// client.startTransaction();
	// client.startBatch();
	// for (int i = 0; i < objs.size(); i++) {
	// client.insert(sqlKey, objs.get(i));
	// count++;
	//
	// if (count % Constants.DB_BATCH_SIZE == 0) {
	// total += sqlMapClient.executeBatch();
	// sqlMapClient.startBatch();
	// }
	// }
	//
	// total += sqlMapClient.executeBatch();
	// client.commitTransaction();
	// } catch (SQLException e) {
	// e.printStackTrace();
	// } finally {
	// try {
	// client.endTransaction();
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	// }
	//
	// return objs.size() == total;
	// }

	// 批量更新对象
	public boolean updateBatchObj(String sqlKey, List<?> objs) {
		int total = 0;
		int count = 0;
		try {
			this.getSqlMapClient().startTransaction();
			this.getSqlMapClient().startBatch();
			for (int i = 0; i < objs.size(); i++) {
				this.getSqlMapClient().update(sqlKey, objs.get(i));
				count++;

				if (count % Constants.DB_BATCH_SIZE == 0) {
					total += this.getSqlMapClient().executeBatch();
					this.getSqlMapClient().startBatch();
				}
			}

			total += this.getSqlMapClient().executeBatch();
			this.getSqlMapClient().commitTransaction();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				this.getSqlMapClient().endTransaction();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return objs.size() == total;
	}

	// 更新对象
	public boolean updateObj(String sqlKey, Object obj) {
		try {
			this.getSqlMapClient().update(sqlKey, obj);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	// 删除对象
	public boolean deleteObj(String sqlKey, Object obj) {
		int affectRows;
		try {
			affectRows = this.getSqlMapClient().delete(sqlKey, obj);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

		return affectRows > 0;
	}

	// 删除对象
	public boolean deleteObj(String sqlKey) {
		int affectRows;
		try {
			affectRows = this.getSqlMapClient().delete(sqlKey);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

		return affectRows > 0;
	}

	// 获取所有对象
	public Vector getAllObjs(String sqlKey) {
		Vector objs = new Vector();
		try {
			List result = this.getSqlMapClient().queryForList(sqlKey);

			objs.addAll(result);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return objs;
	}

	// 根据条件获取所有对象
	public Vector getAllObjs(String sqlKey, Object obj) {
		Vector objs = new Vector();
		try {
			List result = this.getSqlMapClient().queryForList(sqlKey, obj);

			objs.addAll(result);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return objs;
	}

	// 通过id获取对象
	public Object getObj(String sqlKey, Object object) {
		Object obj = null;
		try {
			obj = this.getSqlMapClient().queryForObject(sqlKey, object);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return obj;
	}

	/**
	 * 计算OBJ总量，用于分页
	 * 
	 * @param sqlKey
	 * @param object
	 * @return
	 */
	public int countObjs(String sqlKey, Object object) {
		Integer obj = null;
		try {
			obj = (Integer) this.getSqlMapClient().queryForObject(sqlKey,
					object);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return obj.intValue();
	}

	/**
	 * 根据对象主键，差别是否存在此对象
	 * 
	 * @param sqlKey
	 * @param object
	 * @return
	 */
	public boolean isExistObj(String sqlKey, Object object) {
		Integer obj = null;
		try {
			obj = (Integer) this.getSqlMapClient().queryForObject(sqlKey,
					object);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return obj.intValue() > 0;
	}
}

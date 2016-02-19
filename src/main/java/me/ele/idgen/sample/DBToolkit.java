package me.ele.idgen.sample;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC工具类
 * 
 */
public class DBToolkit {
	private static String url = "jdbc:mysql://192.168.80.122/idgen?characterEncoding=utf8&allowMultiQueries=true";
	private static String username = "root";
	private static String password = "123456";

	static {
		// 注册驱动类
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
		}
	}

	/**
	 * 创建一个数据库连接
	 * 
	 * @return 一个数据库连接
	 */
	public static Connection getConnection() {
		Connection conn = null;
		// 创建数据库连接
		try {
			conn = DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
		}
		return conn;
	}

	
	 public static boolean updateByPreparedStatement(Connection connection,String sql, List<String> params)
	            throws SQLException
	    {
	        boolean flag = false;
	        int result = -1;// 表示当用户执行添加删除和修改的时候所影响数据库的行数
	        PreparedStatement  pstmt = connection.prepareStatement(sql);
	        int index = 1;
	        // 填充sql语句中的占位符
	        if (params != null && !params.isEmpty())
	        {
	            for (int i = 0; i < params.size(); i++)
	            {
	                pstmt.setObject(index++, params.get(i));
	            }
	        }
	        result = pstmt.executeUpdate();
	        flag = result > 0 ? true : false;
	        return flag;
	    }

	

	public static void closeConnection(Connection conn) {
		if (conn == null)
			return;
		try {
			if (!conn.isClosed()) {
				// 关闭数据库连接
				conn.close();
			}
		} catch (SQLException e) {
		}
	}
	
	public static boolean insertIdgen(List<String> params){
		Connection c = DBToolkit.getConnection();
		boolean res = false;
		try {
			//res = DBToolkit.updateByPreparedStatement(c, "delete  from tb_idgen_test where id = '121212'",null);
			res = DBToolkit.updateByPreparedStatement(c, "insert into tb_idgen_test(id,msg,version) values (?,?,?) ",params);
		} catch (SQLException e) {
			System.out.println("---插入失败----"+e.getMessage());
		}finally{
			DBToolkit.closeConnection(c);
		}
		return res;
	}
	
	public static void main(String[] args) {
		List<String> params = new ArrayList<String>();
		long start = System.currentTimeMillis();
		params.add("121212");
		params.add("fdsfds");
		params.add("1");
		for(int i =0 ;i <1 ;i++	){
			insertIdgen(params);
		}
		System.out.println(System.currentTimeMillis() - start );
	}
}
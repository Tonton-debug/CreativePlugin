package tonton.creative.Creative;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
public class DataBaseManager {
	private static DataBaseManager _get;
	private File _main;
	public final String Name;
	private Connection _connection;
	public static DataBaseManager Get(String name) {
		if(_get==null)
			_get=new DataBaseManager(name);
		if(!_get.Name.equals(name)) {
			_get.CloseConnection();
			_get=new DataBaseManager(name);
		}
		return _get;
	}
	private DataBaseManager(String name) {
		// TODO Auto-generated constructor stub
		Name=name;
		_main = new File(Main.getInstance().getDataFolder(), name+".db");
		IntDB(_main);
		
	}
	public void CloseConnection() {
		try {
			_connection.close();
		} catch (SQLException ex) {
			// TODO Auto-generated catch block
		     PrintError("Connection close failed...",ex);
		}
	}
	private void IntDB(File file) {
		 try{
			 System.out.println("HAS FILE:"+file.exists());
			 if(!file.exists()) 
				 file.createNewFile();
			 _connection = DriverManager.getConnection("jdbc:sqlite:"+file);
			   System.out.println("База "+Name+" подключена!");

	    }
	    catch(Exception ex){
	       PrintError("Create db "+Name+" failed...",ex);
	    }
	}
	private void PrintError(String base,Exception ex) {
		 System.out.println(base);
	     System.out.println(ex.getMessage());
		 ex.printStackTrace(System.out);   
	}
	public ResultSet SendSqlWithResult(String execute) {
		try{
			PreparedStatement state= _connection.prepareStatement(execute);
					 return state.executeQuery();
		 } catch (SQLException e) {
		     PrintError("Error query sql:"+execute,e);
		     return null;
		}
	}
	public boolean SendSqlExecute(SqlExecuteType type,String execute)  {
		 try(PreparedStatement state= _connection.prepareStatement(execute)){
			 if(type==SqlExecuteType.Update)
				 state.executeUpdate();
				 else
					 return state.execute();
			 
			return true;
		 } catch (SQLException e) {
			 PrintError("Error execute sql:"+execute,e);
			 
		     return false;
		}
		 
	}

}

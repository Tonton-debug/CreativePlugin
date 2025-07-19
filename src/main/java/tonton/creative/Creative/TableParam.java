package tonton.creative.Creative;

public class TableParam {
	public final String Name;
	public final String Value;
	public TableParam(String name,String value) {
		Name=name;
		Value=value;
	}
	public String toString(boolean withValue) {
		return withValue?"'"+Name+"' "+Value:"'"+Name+"'";
	}

}

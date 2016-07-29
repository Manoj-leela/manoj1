package toDoist;


import play.Logger;
public class Pojo {
	private static org.slf4j.Logger log = play.Logger.underlying();
	
	private String test;
	private String test2;
	private String test3;
	private String test4;
	public Pojo(String test, String test2, String test3, String test4) {
		super();
		this.test = test;
		this.test2 = test2;
		this.test3 = test3;
		this.test4 = test4;
	}

	public static void main(String[] args) {
		Pojo pojo=new Pojo("ewe", "rewre", "test3", "test4");
		log.debug("pojoj ----  "+ pojo);
	}
}

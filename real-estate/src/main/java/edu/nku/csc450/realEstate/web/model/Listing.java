package edu.nku.csc450.realEstate.web.model;
import java.time.LocalDateTime;

public class Listing {

	int t_id;
	int b_id;
	int s_id;
	String status;
	String address;
	String city;
	int zip;
	String state;
	int has_pool;
	int ask_price;
	int sell_price;
	LocalDateTime list_date;
	LocalDateTime sold_date;

	public Listing(){
		this.t_id = 0;
		this.b_id = 0;
		this.s_id = 0;
		this.status = "";
		this.address = "";
		this.city = "";
		this.zip = 0;
		this.state = "";
		this.has_pool = 0;
		this.list_date = LocalDateTime.now();
		this.sold_date = null;
	}

	public Listing(int s_id,String status,String address,String city,int zip,String state,int has_pool,int ask_price){ //when we create a new listing
		this.t_id = 0;
		this.b_id = 0;
		this.s_id = s_id;
		this.status = "available";
		this.address = address;
		this.city = city;
		this.zip = zip;
		this.state = state;
		this.has_pool = has_pool;
		this.list_date = LocalDateTime.now();
		this.sold_date = null;
	}

	public Listing(int t_id,String address,String city,int zip,String state,int has_pool){ //when we update a listing
		this.t_id = t_id;
		this.b_id = 0;
		this.s_id = 0;
		this.status = "";
		this.address = address;
		this.city = city;
		this.zip = zip;
		this.state = state;
		this.has_pool = has_pool;
		this.list_date = null;
		this.sold_date = null;
	}

	public Listing(int t_id,int s_id,String address,String city,int zip,String state,int has_pool,int ask_price,LocalDateTime list_date){ //when we search for and return listings
		this.t_id = t_id;
		this.b_id = b_id;
		this.s_id = s_id;
		this.status = status;
		this.address = address;
		this.city = city;
		this.zip = zip;
		this.state = state;
		this.has_pool = has_pool;
		this.ask_price = ask_price;
		this.list_date = list_date;
		this.sold_date = sold_date;
	}

	public int getTID(){
		return this.t_id;
	}

	public int getBID(){
		return this.b_id;
	}

	public int getSID(){
		return this.s_id;
	}

	public String getStatus(){
		return this.status;
	}

	public String getAddress(){
		return this.address;
	}

	public String getCity(){
		return this.city;
	}

	public int getZip(){
		return this.zip;
	}

	public String getState(){
		return this.state;
	}

	public int getHasPool(){
		return this.has_pool;
	}

	public LocalDateTime getListDate(){
		return this.list_date;
	}

	public LocalDateTime getSoldDate(){
		return this.sold_date;
	}

}
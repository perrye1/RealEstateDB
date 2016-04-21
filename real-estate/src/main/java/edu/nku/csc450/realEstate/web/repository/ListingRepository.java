// ListingRepository.java

package edu.nku.csc450.realEstate.web.repository;

import edu.nku.csc450.realEstate.web.model.Listing;
import java.time.LocalDateTime;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

import java.sql.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.time.*;

public class ListingRepository {

	private DataSource data_source;
	private static final String INSERT_TRANSACTION_SQL = "INSERT INTO Transactions (House_ID, Agent_ID, Seller_ID, Status, Asking_Price, Listed_Date) VALUES (?, ?, ?, ?, ?, ?)";
	private static final String INSERT_HOUSE_SQL = "INSERT INTO House (Address, City, Zip, US_State, Has_Pool) VALUES(?, ?, ?, ?, ?)";
	private static final String UPDATE_TRANSACTION_SQL = "UPDATE Transactions SET Asking_Price = ? WHERE Tran_ID = ?";
	private static final String UPDATE_HOUSE_SQL = "UPDATE House SET Address = ?, City = ?, US_State = ?, Zip = ?, Has_Pool = ? WHERE House_ID = ?";
	private static final String UPDATE_IMAGE_SQL = "UPDATE House SET Picture = ? WHERE House_ID = ?";
	private static final String UPDATE_TRANSACTION_SOLD_SQL = "UPDATE Transactions SET Buyer_ID = ?, Selling_Price = ?, Status = ?, Sold_Date = ? WHERE Tran_ID = ?";
	private static final String SELECT_ALL_SQL = "SELECT * FROM Transactions JOIN House ON Transactions.House_ID=House.House_ID WHERE Status = 'available' ORDER BY Asking_Price";
	private static final String SEARCH_SQL = "SELECT * FROM Transactions JOIN House ON Transactions.House_ID=House.House_ID WHERE ((Asking_Price < ? AND Asking_Price > ?) AND Status = ? AND City = ? AND US_State = ? AND Zip = ? AND Has_Pool = ? )";
	private static final String SELECT_HOUSE_ID_SQL = "SELECT House_ID FROM Transactions WHERE Tran_ID = ?";
	private static final String SELECT_IMAGE_SQL = "SELECT Picture FROM House WHERE House_ID = ?";

	public ListingRepository(DataSource data_source) {
        this.data_source = data_source;
    }

    //saves a new listing in the database
	public void add(int s_id, int agent_id, int list_price, String address, String city, String state, int zip, int pool) {

		int house_id = -1;
        try (
            Connection connection = data_source.getConnection();
            PreparedStatement statement = connection.prepareStatement(INSERT_HOUSE_SQL, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, address);
            statement.setString(2, city);
            statement.setInt(3, zip);
            statement.setString(4, state);
            statement.setInt(5, pool);
            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            rs.next();
            house_id = rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (
            Connection connection = data_source.getConnection();
            PreparedStatement statement = connection.prepareStatement(INSERT_TRANSACTION_SQL)
        ) {
            statement.setInt(1, house_id);
            statement.setInt(2, agent_id);
            statement.setInt(3, s_id);
            statement.setString(4, "available");
            statement.setInt(5, list_price);
            statement.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

	}

	//finds listings, given search params
	public List<Listing> search(int min_price, int max_price, String city, String state, int zip, int pool) {
		List<Listing> listings = new ArrayList<>();
		try (
            Connection connection = data_source.getConnection();
            PreparedStatement statement = connection.prepareStatement(SEARCH_SQL)
        ) {
            statement.setInt(1, max_price);
            statement.setInt(2, min_price);
			statement.setString(3, "available");
			statement.setString(4, city);
            statement.setString(5, state);
            statement.setInt(6, zip);
            statement.setInt(7, pool);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
				Listing l = new Listing(rs.getInt("Tran_ID"),rs.getInt("Seller_ID"),rs.getString("Address"),rs.getString("City"),rs.getInt("Zip"),rs.getString("US_State"),rs.getInt("Has_Pool"),rs.getInt("Asking_Price"),(rs.getTimestamp("Listed_Date")).toLocalDateTime());
				listings.add(l);
			}
			return listings;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
	}

	//updates a listings info
	public void updateInfo(int t_id, int list_price, String address, String city, String state, int zip, int pool) {
		
		int house_id = -1;

		try (
            Connection connection = data_source.getConnection();
            PreparedStatement statement = connection.prepareStatement(SELECT_HOUSE_ID_SQL)
        ) {
            statement.setInt(1, t_id);
            ResultSet rs = statement.executeQuery();
            rs.next();
            house_id = rs.getInt("House_ID");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (
            Connection connection = data_source.getConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE_TRANSACTION_SQL)
        ) {
            statement.setInt(1, list_price);
            statement.setInt(2, t_id);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (
            Connection connection = data_source.getConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE_HOUSE_SQL)
        ) {
            statement.setString(1, address);
            statement.setString(2, city);
            statement.setString(3, state);
            statement.setInt(4, zip);
            statement.setInt(5, pool);
            statement.setInt(6, house_id);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }


	}

	//marks a listing as sold
	public void updateSold(int t_id, int b_id, int sold_price) {

		try (
            Connection connection = data_source.getConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE_TRANSACTION_SOLD_SQL)
        ) {
            statement.setInt(1, b_id);
            statement.setInt(2, sold_price);
            statement.setString(3, "sold");
            statement.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            statement.setInt(5, t_id);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
	}

	//returns all listings
	public List<Listing> findAll() {
		try (
			Connection connection = data_source.getConnection();
			Statement statement = connection.createStatement()
		) {
			ResultSet rs = statement.executeQuery(SELECT_ALL_SQL);
			List<Listing> listings = new ArrayList<>();
			while (rs.next()) {
				Listing l = new Listing(rs.getInt("Tran_ID"),rs.getInt("Seller_ID"),rs.getString("Address"),rs.getString("City"),rs.getInt("Zip"),rs.getString("US_State"),rs.getInt("Has_Pool"),rs.getInt("Asking_Price"),(rs.getTimestamp("Listed_Date")).toLocalDateTime());
				listings.add(l);
			}
			return listings;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Collections.emptyList();
	}

	public void uploadImage(int t_id, InputStream is) {

		int house_id = -1;

		try (
            Connection connection = data_source.getConnection();
            PreparedStatement statement = connection.prepareStatement(SELECT_HOUSE_ID_SQL)
        ) {
            statement.setInt(1, t_id);
            ResultSet rs = statement.executeQuery();
            rs.next();
            house_id = rs.getInt("House_ID");
        } catch (SQLException e) {
            e.printStackTrace();
        }

		try (
            Connection connection = data_source.getConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE_IMAGE_SQL)
        ) {
            statement.setBlob(1, is);
            statement.setInt(2, house_id);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
	}

	public String retrieveImage(int t_id) {

		int house_id = -1;
		String encodedImage = "";

		try (
            Connection connection = data_source.getConnection();
            PreparedStatement statement = connection.prepareStatement(SELECT_HOUSE_ID_SQL)
        ) {
            statement.setInt(1, t_id);
            ResultSet rs = statement.executeQuery();
            rs.next();
            house_id = rs.getInt("House_ID");
        } catch (SQLException e) {
            e.printStackTrace();
        }

		try (
            Connection connection = data_source.getConnection();
            PreparedStatement statement = connection.prepareStatement(SELECT_IMAGE_SQL)
        ) {
            statement.setInt(1, house_id);
            ResultSet rs = statement.executeQuery();
            rs.next();
            InputStream is = rs.getBinaryStream("Picture");
            byte[] buf = IOUtils.toByteArray(is);
            byte[] encoded = Base64.encodeBase64(buf);
            encodedImage = new String(encoded);
            return encodedImage;
        } catch (IOException|SQLException e) {
            e.printStackTrace();
        }
        return encodedImage;
	}
}
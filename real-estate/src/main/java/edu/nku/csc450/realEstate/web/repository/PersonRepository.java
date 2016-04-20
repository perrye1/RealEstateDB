// PersonRepository.java
// for The Voting Game

package edu.nku.csc450.realEstate.web.repository;

import edu.nku.csc450.realEstate.web.model.Person;

import org.apache.tomcat.jdbc.pool.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.time.*;

public class PersonRepository {

	private DataSource data_source;
	private static final String INSERT_SQL = "INSERT INTO Person (Email, First_Name, Last_Name, User_Name) VALUES (?, ?, ?, ? )";
	private static final String SELECT_ALL_SQL = "SELECT * FROM Person;";
	private static final String SELECT_PERSON_SQL = "SELECT * FROM Person WHERE User_Name = ";

	public PersonRepository(DataSource data_source) {
        this.data_source = data_source;
    }

    //saves a newly registered person in the database
	public void savePerson(String e_mail, String f_name, String l_name, String u_name) {
		try (
			Connection connection = data_source.getConnection();
			PreparedStatement statement = connection.prepareStatement(INSERT_SQL)
		) {
			statement.setString(1, e_mail);
			statement.setString(2, f_name);
			statement.setString(3, l_name);
			statement.setString(4, u_name.toLowerCase());
			statement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	//finds a persons record given their username
	public Person findPerson(String u_name) {
		try (
			Connection connection = data_source.getConnection();
			Statement statement = connection.createStatement()
		) {
			statement.execute(SELECT_PERSON_SQL + "'" + u_name + "'");
			ResultSet resultSet = statement.getResultSet();
			while (resultSet.next()) {
				Person p = new Person(resultSet.getInt("Person_ID"), resultSet.getString("Email"), resultSet.getString("First_Name"), resultSet.getString("Last_Name"), resultSet.getString("User_Name"), resultSet.getInt("Is_Agent"), resultSet.getInt("Salary"));
				return p;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Person p = new Person();
		return p;
	}

	//returns all users from the person table
	public List<Person> findAll() {
		try (
			Connection connection = data_source.getConnection();
			Statement statement = connection.createStatement()
		) {
			ResultSet resultSet = statement.executeQuery(SELECT_ALL_SQL);
			List<Person> people = new ArrayList<>();
			while (resultSet.next()) {
				Person p = new Person(resultSet.getInt("Person_ID"), resultSet.getString("Email"), resultSet.getString("First_Name"), resultSet.getString("Last_Name"), resultSet.getString("User_Name"), resultSet.getInt("Is_Agent"), resultSet.getInt("Salary"));
				people.add(p);
			}
			return people;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Collections.emptyList();
	}
}
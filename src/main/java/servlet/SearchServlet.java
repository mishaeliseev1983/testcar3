package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import model.ConnectionDB;
import model.DataOwner;

/**
 * Servlet implementation class SearchServlet
 */
public class SearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public SearchServlet() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		System.out.println("do get !");
		response.setContentType("text/html;charset=utf-8");

		HttpSession currentSession = request.getSession();

		if (currentSession.getAttribute("login") != null) {
			System.out.println("current user = "
					+ currentSession.getAttribute("login").toString());
		}

		System.out.println("-------- PostgreSQL JDBC Connection------------");

		Connection connection = ConnectionDB.getConnection();
		if (connection != null) {
			System.out.println(
					"SearchServlet: You made it, take control your database now!");
		} else {
			System.out.println("SearchServlet: Failed to make connection!");
			return;
		}

		System.out.println("SearchServlet: dataquery = "
				+ request.getParameter("dataquery"));

		Gson gson = new Gson();
		DataOwner owner = gson.fromJson(request.getParameter("dataquery"),
				DataOwner.class);
		if (owner == null)
			return;

		String name = ConnectionDB.getQueryValue(owner.getName());
		String surname = ConnectionDB.getQueryValue(owner.getSurname());
		String patronymic = ConnectionDB.getQueryValue(owner.getPatronymic());
		String city = ConnectionDB.getQueryValue(owner.getCity());
		String model = ConnectionDB.getQueryValue(owner.getModel());

		System.out.println("name = " + name + " | surname = " + surname
				+ " | patronymic = " + patronymic + " | city = " + city
				+ " | model = " + model);

		PrintWriter out = response.getWriter();
		String sql = ConnectionDB.getSearchSql();
		PreparedStatement ps = null;
		List<DataOwner> list = new ArrayList<DataOwner>();
		try {

			ps = connection.prepareStatement(sql);
			ps.setString(1, city);
			ps.setString(2, model);
			ps.setString(3, name);
			ps.setString(4, surname);
			ps.setString(5, patronymic);

			ResultSet rs = ps.executeQuery();

			int index = 0;
			while (rs.next()) {
				String oname = rs.getString("oname");
				String osurname = rs.getString("osurname");
				String opatronymic = rs.getString("opatronymic");
				String camodel = rs.getString("camodel");
				String canumber = rs.getString("canumber");
				String ciname = rs.getString("ciname");

				System.out.println(" oname = " + oname + "| osurname = "
						+ osurname + " | opatronymic= " + opatronymic
						+ " | camodel = " + camodel + " | canumber = "
						+ canumber + " | ciname = " + ciname);

				DataOwner data = new DataOwner();
				data.setId(index++);
				data.setName(oname);
				data.setSurname(osurname);
				data.setCity(ciname);
				data.setPatronymic(opatronymic);
				data.setModel(camodel);
				data.setNumber(canumber);
				list.add(data);
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out
					.println("SearchServlet: problems with executing query...");
		}
		System.out.println(
				"SearchServlet: start preparing response with data...");

		String responseFromDB = new Gson().toJson(list);
		System.out.println("SearchServlet: prepared response with data = "
				+ responseFromDB);
		out.print(responseFromDB);
		out.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}

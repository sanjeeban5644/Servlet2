// Refactored version using static data and external HTML templates.

package com.sanjeeban.registration;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// A simple Book class to hold book details
class Book {
    int id;
    String name;
    String edition;
    float price;

    public Book(int id, String name, String edition, float price) {
        this.id = id;
        this.name = name;
        this.edition = edition;
        this.price = price;
    }
}

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private static List<Book> books = new ArrayList<>();
    private static int bookCounter = 1;

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String bookName = req.getParameter("bookName");
        String bookEdition = req.getParameter("bookEdition");
        float bookPrice = Float.parseFloat(req.getParameter("bookPrice"));

        books.add(new Book(bookCounter++, bookName, bookEdition, bookPrice));

        res.sendRedirect("/confirmation.html");
    }
}

@WebServlet("/bookList")
public class BookListServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        PrintWriter pw = res.getWriter();
        res.setContentType("text/html");

        pw.println("<html><body>");
        pw.println("<h1>Book List</h1>");
        pw.println("<table border='1'>");
        pw.println("<tr><th>ID</th><th>Name</th><th>Edition</th><th>Price</th><th>Actions</th></tr>");

        for (Book book : RegisterServlet.books) {
            pw.println("<tr>");
            pw.println("<td>" + book.id + "</td>");
            pw.println("<td>" + book.name + "</td>");
            pw.println("<td>" + book.edition + "</td>");
            pw.println("<td>" + book.price + "</td>");
            pw.println("<td><a href='/delete?id=" + book.id + "'>Delete</a></td>");
            pw.println("</tr>");
        }

        pw.println("</table>");
        pw.println("<a href='/home.html'>Back to Home</a>");
        pw.println("</body></html>");
    }
}

@WebServlet("/delete")
public class DeleteServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        RegisterServlet.books.removeIf(book -> book.id == id);

        res.sendRedirect("/bookList");
    }
}

@WebServlet("/home")
public class HomeServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        serveHtmlFile(res, "home.html");
    }

    private void serveHtmlFile(HttpServletResponse res, String fileName) throws IOException {
        res.setContentType("text/html");
        PrintWriter pw = res.getWriter();
        String content = new String(Files.readAllBytes(Paths.get(fileName)));
        pw.println(content);
    }
}

package pl.revolut.test;

import org.apache.log4j.Logger;
import org.eclipse.jetty.http.MimeTypes;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;
import pl.revolut.test.dao.DatasetDAO;
import pl.revolut.test.service.AccountService;
import pl.revolut.test.service.TransactionService;

public class MainApp {

    private static Logger log = Logger.getLogger(MainApp.class);

    public static void main(String[] args) throws Exception {
        log.info("************************");
        log.info("**** Create dataset ****");
        log.info("************************");
        DatasetDAO datasetDAO = new DatasetDAO();
        log.info("************************");
        log.info("****** Init tables *****");
        log.info("************************");
        datasetDAO.initData();
        log.info("*****************************************************");
        log.info("***** Start jetty server on localhost port 8000 *****");
        log.info("*****************************************************");
        startService();
    }

    private static void startService() throws Exception {
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        Server server = new Server(8080);
        server.setHandler(context);
        ServletHolder servletHolder = context.addServlet(ServletContainer.class, "/*");
        servletHolder.setInitOrder(0);
        servletHolder.setInitParameter("jersey.config.server.provider.classnames",
                AccountService.class.getCanonicalName() + "," + TransactionService.class.getCanonicalName());
        try {
            server.start();
            server.join();
        } finally {
            server.destroy();
        }
    }

}

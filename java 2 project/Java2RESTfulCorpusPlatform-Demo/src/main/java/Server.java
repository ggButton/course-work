import dao.TextDao;
import io.javalin.Javalin;
import io.swagger.v3.oas.models.info.Info;
import io.javalin.plugin.openapi.OpenApiOptions;
import io.javalin.plugin.openapi.OpenApiPlugin;
import io.javalin.plugin.openapi.ui.ReDocOptions;
import io.javalin.plugin.openapi.ui.SwaggerOptions;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import service.TextService;

/**
 *
 * This class set up the connction with database and has a relationship with {@see TextService} and {@see TextDao} to handle the exists, upload, download, compare, list. delete requests from {@see Client}.
 *
 * <p>Server receives the requests form Client as an interface because the detailed work is done by {@see TextService} and {@see TextDao}.
 * Server uses <em>Javalin</em> light web framework.
 *
 * @author Gao Manlin
 * @since JDK1.8
 *
 */
public class Server {

    /**
     * Set up a connection with the database. Receive the html requests.
     *
     * @param args
     * User input.
     * @throws ClassNotFoundException
     * If some methods throws Exception.
     *
     */
    public static void main(String[] args) throws ClassNotFoundException {
        //TODO:connect database
        Class.forName("org.sqlite.JDBC");
        Sql2o sql2o = new Sql2o("jdbc:sqlite:File.db", null, null);
        String initSql = "CREATE TABLE IF NOT EXISTS\"files\" (\n" +
                "\t\"md5\"\tTEXT NOT NULL UNIQUE,\n" +
                "\t\"content\"\tTEXT,\n" +
                "\t\"length\"\tINTEGER,\n" +
                "\t\"preview\"\tTEXT\n" +
                ")";
        try(Connection con = sql2o.open()){
            con.createQuery(initSql).executeUpdate();
        }


        TextDao dao=new TextDao(sql2o);
        TextService service = new TextService(dao);

        Javalin app = Javalin.create(config -> {
            config.registerPlugin(getConfiguredOpenApiPlugin());
        }).start(7001);
        app.get("/", ctx -> ctx.result("Welcome to RESTful Corpus Platform!"));
        // handle exist
        app.get("/files/:md5/exists", service::handleExists);
        // handle upload
        app.post("/files/:md5", service::handleUpload);
        // handle compare
        app.get("/files/:md51/compare/:md52", service::handleCompare);
        // handle download
        app.get("/files/:md5", service::handleDownload);
        //handle list
        app.get("/files", service::handleList);
        //handle delete
        app.get("/files/:md5/delete",service::handleDelete);
        //handle delete all
        //app.get("/files/delete",service::handleDeleteAll);
    }

    /**
     *Open api plugin.
     *
     * @return OpenApiPlugin
     *
     */
    private static OpenApiPlugin getConfiguredOpenApiPlugin() {
        Info info = new Info().version("1.0").description("RESTful Corpus Platform API");
        OpenApiOptions options = new OpenApiOptions(info)
                .activateAnnotationScanningFor("cn.edu.sustech.java2.RESTfulCorpusPlatform")
                .path("/swagger-docs") // endpoint for OpenAPI json
                .swagger(new SwaggerOptions("/swagger-ui")); // endpoint for swagger-ui
//                .reDoc(new ReDocOptions("/redoc")); // endpoint for redoc
        return new OpenApiPlugin(options);
    }
}
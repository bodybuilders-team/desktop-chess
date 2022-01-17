import androidx.compose.ui.window.*
import storage.*
import ui.compose.*

/**
 * The application entry point.
 *
 * Execution is parameterized through the following environment variables:
 * - MONGO_DB_NAME, bearing the name of the database to be used
 * - MONGO_DB_CONNECTION, bearing the connection string to the database server. If absent, the application
 * uses a local server instance (it must be already running)
 */
fun main() {
    /*val dbInfo = getDBConnectionInfo()
    val driver = createMongoClient(if (dbInfo.mode == DbMode.REMOTE) dbInfo.connectionString else null)

    val dataBase = MongoDBGameStorage(tryDataBaseAccess { driver.getDatabase(System.getenv(ENV_DB_NAME)) })
    */
    val dataBase = GameStorageStub()

    //driver.use {
    application {
        MainWindow(dataBase, onCloseRequest = {
            println("BYE.")
            exitApplication()
        })
    }
    //}
}

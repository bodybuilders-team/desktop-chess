import androidx.compose.ui.window.application
import storage.DbMode
import storage.ENV_DB_NAME
import storage.MongoDBGameStorage
import storage.getDBConnectionInfo
import storage.mongodb.createMongoClient
import ui.compose.MainWindow

/**
 * The application entry point.
 *
 * Execution is parameterized through the following environment variables:
 * - MONGO_DB_NAME, bearing the name of the database to be used
 * - MONGO_DB_CONNECTION, bearing the connection string to the database server. If absent, the application
 * uses a local server instance (it must be already running)
 */
fun main() {
    val dbInfo = getDBConnectionInfo()
    val driver = createMongoClient(if (dbInfo.mode == DbMode.REMOTE) dbInfo.connectionString else null)

    // val gameStorage = GameStorageStub()

    driver.use {
        val gameStorage = MongoDBGameStorage(driver.getDatabase(System.getenv(ENV_DB_NAME)))
        application {
            MainWindow(gameStorage, onCloseRequest = {
                println("BYE.")
                exitApplication()
            })
        }
    }
}

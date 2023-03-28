import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.goosebuddy.models.Routines

@Dao
interface UserDao {
    @Query("SELECT * FROM routines")
    fun getAll(): List<Routines>

    @Insert
    fun insertAll(vararg routines: Routines)

    @Delete
    fun delete(routines: Routines)
}
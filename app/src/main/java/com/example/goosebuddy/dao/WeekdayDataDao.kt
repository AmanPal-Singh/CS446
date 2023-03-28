import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.goosebuddy.models.WeekdayData

@Dao
interface WeekdayData {
    @Query("SELECT * FROM weekday_data")
    fun getAll(): List<WeekdayData>

    @Insert
    fun insertAll(vararg weekday_data: WeekdayData)

    @Delete
    fun delete(weekday_data: WeekdayData)
}
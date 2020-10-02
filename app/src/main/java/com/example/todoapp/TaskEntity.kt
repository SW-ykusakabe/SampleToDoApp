import android.os.Parcel
import android.os.Parcelable
import java.time.LocalDateTime

data class TaskEntity(
    var startTime: LocalDateTime,
    var endTime: LocalDateTime,
    var title: String
): Parcelable {

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeSerializable(startTime)
        dest.writeSerializable(endTime)
        dest.writeString(title)
    }

    override fun describeContents() = 0

    companion object {

        @JvmField
        val CREATOR: Parcelable.Creator<TaskEntity> = object: Parcelable.Creator<TaskEntity> {
            override fun createFromParcel(source: Parcel): TaskEntity {
                return TaskEntity(source.readSerializable() as LocalDateTime,
                    source.readSerializable() as LocalDateTime,
                    source.readString() as String)
            }

            override fun newArray(size: Int): Array<TaskEntity?>  = arrayOfNulls(size)
        }
    }
}
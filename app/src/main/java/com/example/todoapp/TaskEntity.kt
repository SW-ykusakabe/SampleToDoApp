import android.os.Parcel
import android.os.Parcelable

data class TaskEntity(
    var startTime: String? = null,
    var endTime: String? = null,
    var title: String? = null
): Parcelable {

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(startTime)
        dest.writeString(endTime)
        dest.writeString(title)
    }

    override fun describeContents() = 0

    companion object {

        @JvmField
        val CREATOR: Parcelable.Creator<TaskEntity> = object: Parcelable.Creator<TaskEntity> {
            override fun createFromParcel(source: Parcel): TaskEntity {
                return TaskEntity(source.readString(), source.readString(), source.readString())
            }

            override fun newArray(size: Int): Array<TaskEntity?>  = arrayOfNulls(size)
        }
    }
}
package com.quantbit.accidentmanagement.ui.emergency_contacts

import android.os.Parcel
import android.os.Parcelable

data class EmergencyContact(
    var contact_name: String? = "",
    var phone: String? = "",
    var email: String? = "",
    var relation: String? = ""
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(contact_name)
        parcel.writeString(phone)
        parcel.writeString(email)
        parcel.writeString(relation)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<EmergencyContact> {
        override fun createFromParcel(parcel: Parcel): EmergencyContact {
            return EmergencyContact(parcel)
        }

        override fun newArray(size: Int): Array<EmergencyContact?> {
            return arrayOfNulls(size)
        }
    }
}
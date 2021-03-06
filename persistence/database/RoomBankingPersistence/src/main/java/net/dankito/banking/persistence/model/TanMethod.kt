package net.dankito.banking.persistence.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import net.dankito.banking.persistence.dao.BaseDao
import net.dankito.banking.ui.model.tan.AllowedTanFormat
import net.dankito.banking.ui.model.tan.TanMethod
import net.dankito.banking.ui.model.tan.TanMethodType


@Entity
open class TanMethod(
    override var displayName: String,
    override var type: TanMethodType,
    override var bankInternalMethodCode: String,
    override var maxTanInputLength: Int? = null,
    override var allowedTanFormat: AllowedTanFormat = AllowedTanFormat.Alphanumeric
) : TanMethod(displayName, type, bankInternalMethodCode, maxTanInputLength, allowedTanFormat) {

    internal constructor() : this("", TanMethodType.EnterTan, "") // for object deserializers


    @PrimaryKey
    open var id: String = technicalId

    // Room doesn't allow me to add getters and setters -> have to map it manually
    open var bankId: Long = BaseDao.ObjectNotInsertedId

}
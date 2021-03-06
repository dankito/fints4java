package net.dankito.banking.persistence.model

import com.fasterxml.jackson.annotation.*
import net.dankito.banking.ui.model.IBankData
import net.dankito.banking.ui.model.tan.TanMedium
import net.dankito.banking.ui.model.tan.TanMethod
import java.util.*


@JsonIdentityInfo(property = "technicalId", generator = ObjectIdGenerators.PropertyGenerator::class) // to avoid stack overflow due to circular references
// had to define all properties as 'var' 'cause MapStruct cannot handle vals (and cannot use Pozo's mapstruct-kotlin as SerializableCustomerBuilder would fail with @Context)
open class BankDataEntity(
    override var bankCode: String,
    override var userName: String,
    override var password: String,
    override var finTsServerAddress: String,
    override var bankName: String,
    override var bic: String,
    override var customerName: String,
    override var userId: String = userName,
    override var iconData: ByteArray? = null,
    override var accounts: List<BankAccountEntity> = listOf(),
    override var supportedTanMethods: List<TanMethod> = listOf(),
    override var selectedTanMethod: TanMethod? = null,
    override var tanMedia: List<TanMedium> = listOf(),
    override var technicalId: String = UUID.randomUUID().toString(),
    override var wrongCredentialsEntered: Boolean = false,
    override var savePassword: Boolean = true,
    override var userSetDisplayName: String? = null,
    override var displayIndex: Int = 0
) : IBankData<BankAccountEntity, AccountTransactionEntity> {

    internal constructor() : this("", "", "", "", "", "", "") // for object deserializers


    override fun toString(): String {
        return stringRepresentation
    }

}
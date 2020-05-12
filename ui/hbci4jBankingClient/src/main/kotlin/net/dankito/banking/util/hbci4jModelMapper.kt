package net.dankito.banking.util

import net.dankito.banking.ui.model.Account
import net.dankito.banking.ui.model.Bank
import net.dankito.banking.ui.model.BankAccount
import net.dankito.banking.ui.model.BankAccountType
import net.dankito.banking.ui.model.parameters.TransferMoneyData
import net.dankito.banking.ui.model.tan.TanProcedureType
import org.kapott.hbci.passport.HBCIPassport
import org.kapott.hbci.structures.Konto
import java.math.BigDecimal


open class hbci4jModelMapper {

    open fun mapToKonto(bank: Bank, bankAccount: BankAccount): Konto {
        val konto = Konto("DE", bank.bankCode, bankAccount.identifier, bankAccount.subAccountNumber)

        konto.name = bank.name
        konto.iban = bankAccount.iban
        konto.bic = bank.bic

        return konto
    }

    open fun mapToKonto(data: TransferMoneyData): Konto {
        return mapToKonto(data.creditorName, data.creditorIban, data.creditorBic)
    }

    open fun mapToKonto(accountHolderName: String, iban: String, bic: String): Konto {
        val konto = Konto()

        konto.name = accountHolderName
        konto.iban = iban
        konto.bic = bic

        return konto
    }


    open fun mapBankAccounts(account: Account, bankAccounts: Array<out Konto>, passport: HBCIPassport): List<BankAccount> {
        return bankAccounts.map { bankAccount ->
            val iban = if (bankAccount.iban.isNullOrBlank() == false) bankAccount.iban else passport.upd.getProperty("KInfo.iban") ?: ""

            BankAccount(account, bankAccount.number,
                if (bankAccount.name2.isNullOrBlank() == false) bankAccount.name + " " + bankAccount.name2 else bankAccount.name,
                iban, bankAccount.subnumber, BigDecimal.ZERO, bankAccount.curr, mapBankAccountType(bankAccount),
                bankAccount.allowedGVs.contains("HKKAZ"), bankAccount.allowedGVs.contains("HKSAL"), bankAccount.allowedGVs.contains("HKCCS"))
        }
    }

    open fun mapBankAccountType(bankAccount: Konto): BankAccountType {
        val type = bankAccount.acctype

        return when {
            type.length == 1 -> BankAccountType.Girokonto
            type.startsWith("1") -> BankAccountType.Sparkonto
            type.startsWith("2") -> BankAccountType.Festgeldkonto
            type.startsWith("3") -> BankAccountType.Wertpapierdepot
            type.startsWith("4") -> BankAccountType.Darlehenskonto
            type.startsWith("5") -> BankAccountType.Kreditkartenkonto
            type.startsWith("6") -> BankAccountType.FondsDepot
            type.startsWith("7") -> BankAccountType.Bausparvertrag
            type.startsWith("8") -> BankAccountType.Versicherungsvertrag
            type.startsWith("9") -> BankAccountType.Sonstige
            else -> BankAccountType.Sonstige
        }
    }


    open fun mapTanProcedures(tanProceduresString: String): List<net.dankito.banking.ui.model.tan.TanProcedure> {
        return tanProceduresString.split('|')
            .map { mapTanProcedure(it) }
            .filterNotNull()
    }

    open fun mapTanProcedure(tanProcedureString: String): net.dankito.banking.ui.model.tan.TanProcedure? {
        val parts = tanProcedureString.split(':')

        if (parts.size > 1) {
            val code = parts[0]
            val displayName = parts[1]
            val displayNameLowerCase = displayName.toLowerCase()

            return when {
                // TODO: implement all TAN procedures
                displayNameLowerCase.contains("chiptan") -> {
                    if (displayNameLowerCase.contains("qr")) {
                        net.dankito.banking.ui.model.tan.TanProcedure(displayName, TanProcedureType.ChipTanQrCode, code)
                    }
                    else {
                        net.dankito.banking.ui.model.tan.TanProcedure(displayName, TanProcedureType.ChipTanFlickercode, code)
                    }
                }

                displayNameLowerCase.contains("sms") -> net.dankito.banking.ui.model.tan.TanProcedure(displayName, TanProcedureType.SmsTan, code)
                displayNameLowerCase.contains("push") -> net.dankito.banking.ui.model.tan.TanProcedure(displayName, TanProcedureType.AppTan, code)

                // we filter out iTAN and Einschritt-Verfahren as they are not permitted anymore according to PSD2
                else -> null
            }
        }

        return null
    }

}